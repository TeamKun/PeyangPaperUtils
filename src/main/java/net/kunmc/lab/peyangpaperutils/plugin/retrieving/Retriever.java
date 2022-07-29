package net.kunmc.lab.peyangpaperutils.plugin.retrieving;

import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Retriever
{
    private static final File FILE_BASE = PeyangPaperUtils.getInstance().getDataFolder();
    private static final File FILE_RETRIEVING = new File(FILE_BASE, "retrieving.json");

    static
    {
        FILE_BASE.mkdirs();
    }

    public static void onEnable()
    {
        if (!FILE_RETRIEVING.exists())
            return;

        RetrievingFile file = RetrievingFile.load(FILE_RETRIEVING);

        if (file == null)
            return;

        PluginManager manager = Bukkit.getPluginManager();

        ArrayList<RetrievingFile.RetrievingMeta> loadOrder = new ArrayList<>(file.getTargetPlugins());
        loadOrder.sort(Comparator.comparingInt(RetrievingFile.RetrievingMeta::getOrder));

        loadOrder.stream()
                .map(RetrievingFile.RetrievingMeta::getPluginName)
                .map(manager::getPlugin)
                .filter(Objects::nonNull)
                .filter(plugin -> !plugin.isEnabled())
                .forEach(plugin -> {
                    try
                    {
                        manager.enablePlugin(plugin);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

        FILE_RETRIEVING.delete();
    }

    public static void onDisable()
    {
        List<RetrievingFile.RetrievingMeta> meta = buildMeta(
                0,
                Arrays.stream(Bukkit.getPluginManager().getPlugins()).parallel()
                        .filter(Plugin::isEnabled)
                        .filter(plugin -> {
                            PluginDescriptionFile description = plugin.getDescription();
                            return description.getDepend().stream().parallel()
                                    .anyMatch(depend -> depend.equalsIgnoreCase("PeyangPaperUtils"))
                                    || description.getSoftDepend().stream().parallel()
                                    .anyMatch(depend -> depend.equalsIgnoreCase("PeyangPaperUtils"));
                        })
                        .collect(Collectors.toList())
        );

        RetrievingFile file = RetrievingFile.builder()
                .targetPlugins(meta)
                .build();

        System.out.printf("%d plugins will be enabled after reloading PeyangPaperUtils: %s%n", meta.size(),
                meta.stream().parallel()
                        .map(RetrievingFile.RetrievingMeta::getPluginName)
                        .collect(Collectors.joining(","))
        );

        file.save(FILE_RETRIEVING);

        ArrayList<RetrievingFile.RetrievingMeta> loadOrder = new ArrayList<>(file.getTargetPlugins());
        loadOrder.sort(Comparator.comparingInt(RetrievingFile.RetrievingMeta::getOrder).reversed());

        loadOrder.stream()
                .map(RetrievingFile.RetrievingMeta::getPluginName)
                .map(Bukkit.getPluginManager()::getPlugin)
                .filter(Objects::nonNull)
                .forEach(plugin -> {
                    try
                    {
                        Bukkit.getPluginManager().disablePlugin(plugin);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });
    }

    private static List<RetrievingFile.RetrievingMeta> buildMeta(int depth, List<? extends Plugin> remaining)
    {
        if (remaining.isEmpty())
            return Collections.emptyList();

        List<Plugin> withDepends = new ArrayList<>();
        List<RetrievingFile.RetrievingMeta> meta = new ArrayList<>();

        for (Plugin plugin : remaining)
        {
            if (hasDepend(plugin))
                withDepends.add(plugin);
            else
                meta.add(new RetrievingFile.RetrievingMeta(plugin.getName(), depth));
        }

        meta.addAll(buildMeta(depth + 1, withDepends));

        return meta;
    }

    private static boolean hasDepend(Plugin plugin)
    {
        PluginManager manager = Bukkit.getPluginManager();
        return Stream.of(plugin.getDescription().getDepend(), plugin.getDescription().getSoftDepend())
                .flatMap(List::stream)
                .parallel()
                .map(manager::getPlugin)
                .filter(Objects::nonNull)
                .anyMatch(Plugin::isEnabled);
    }
}
