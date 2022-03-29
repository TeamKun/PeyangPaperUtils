package net.kunmc.lab.peyangpaperutils;

import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.lib.command.CommandManager;
import net.kunmc.lab.peyangpaperutils.lib.terminal.InputManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PeyangPaperUtils extends JavaPlugin
{
    @Getter
    private static PeyangPaperUtils instance;

    @Getter
    private InputManager inputManager;

    private CommandManager pluginCommandManager;

    public PeyangPaperUtils()
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        inputManager = new InputManager(this);
        pluginCommandManager = new CommandManager(this, "peyadebug", "PeyangUtilsDebug", "peyangutils");
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
