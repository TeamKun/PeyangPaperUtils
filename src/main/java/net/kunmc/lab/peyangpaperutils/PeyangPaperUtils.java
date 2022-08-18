package net.kunmc.lab.peyangpaperutils;

import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.lib.command.CommandManager;
import net.kunmc.lab.peyangpaperutils.lib.terminal.InputManager;
import net.kunmc.lab.peyangpaperutils.plugin.commands.PeyangDebugCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarFile;

public final class PeyangPaperUtils extends JavaPlugin
{
    @Getter
    private static PeyangPaperUtils instance;

    @Getter
    private InputManager inputManager;

    private CommandManager pluginCommandManager;

    private List<String> classes;

    public PeyangPaperUtils()
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        this.inputManager = new InputManager(this);
        this.pluginCommandManager = new CommandManager(this, "peyangutils", "PeyangUtilsDebug", "peyangutils");
        this.pluginCommandManager.registerCommand("debug", new PeyangDebugCommand());

        this.classes = new ArrayList<>();

        this.getLogger().info("Loading classes...");

        this.loadClasses();
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
        URL url = null;
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
        // Plugin shutdown logic
    }
}
