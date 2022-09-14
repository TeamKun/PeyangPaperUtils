package net.kunmc.lab.peyangpaperutils;

import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.lib.command.CommandManager;
import net.kunmc.lab.peyangpaperutils.lib.terminal.InputManager;
import net.kunmc.lab.peyangpaperutils.lib.terminal.PlayerTerminal;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
import net.kunmc.lab.peyangpaperutils.plugin.commands.AnswerCommand;
import net.kunmc.lab.peyangpaperutils.plugin.commands.PeyangDebugCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarFile;

public final class PeyangPaperUtils extends JavaPlugin implements Listener
{
    @Getter
    private static PeyangPaperUtils instance;

    @Getter
    private InputManager inputManager;

    @SuppressWarnings("FieldCanBeLocal")
    private CommandManager pluginCommandManager;

    private List<String> classes;

    @Getter
    private final UUID runID;

    public PeyangPaperUtils()
    {
        instance = this;
        this.runID = UUID.randomUUID();
    }

    @Override
    public void onEnable()
    {
        this.inputManager = new InputManager(this);
        this.pluginCommandManager = new CommandManager(this, "peyangutils", "PeyangUtilsDebug", "peyangutils");
        this.pluginCommandManager.registerCommand("debug", new PeyangDebugCommand());
        this.pluginCommandManager.registerCommand("answer", new AnswerCommand());

        this.classes = new ArrayList<>();

        this.getLogger().info("Loading classes...");

        this.loadClasses();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        ((PlayerTerminal) Terminals.of(event.getPlayer())).updatePlayer();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Terminals.purge(event.getPlayer());
    }

    private void loadClasses()
    {
        try (JarFile jarFile = new JarFile(this.getFile()))
        {
            jarFile.stream()
                    .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                    .map(jarEntry -> jarEntry.getName().replaceAll("/", "."))
                    .map(className -> className.substring(0, className.length() - ".class".length()))
                    .forEach(this.classes::add);
        }
        catch (Exception e)
        {
            this.getLogger().severe("Failed to load classes!");
            e.printStackTrace();
        }

        this.getLogger().info("Enumerated " + this.classes.size() + " classes.");

        this.getLogger().info("Loading classes...");

        String urlString = "jar:file:" + this.getFile().getAbsolutePath() + "!/";
        URL url;
        try
        {
            url = new URL(urlString);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalStateException(e);
        }

        try (URLClassLoader ulc = new URLClassLoader(new URL[]{url}, ClassLoader.getSystemClassLoader()))
        {

            AtomicLong count = new AtomicLong(0);

            this.classes.forEach(className ->
            {
                try
                {
                    ulc.loadClass(className);
                    Class.forName(className);
                    count.incrementAndGet();
                }
                catch (Exception e)
                {
                    this.getLogger().severe("Failed to load class " + className + "!");
                    e.printStackTrace();
                }
            });

            this.getLogger().info("Loaded (" + count.get() + "/" + this.classes.size() + ") classes.");

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable()
    {
        this.inputManager.cancelAll();
    }
}
