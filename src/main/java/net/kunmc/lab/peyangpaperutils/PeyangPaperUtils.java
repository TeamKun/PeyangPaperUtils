package net.kunmc.lab.peyangpaperutils;

import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.lib.command.CommandManager;
import net.kunmc.lab.peyangpaperutils.lib.terminal.InputManager;
import net.kunmc.lab.peyangpaperutils.plugin.commands.PeyangDebugCommand;
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
        this.inputManager = new InputManager(this);
        this.pluginCommandManager = new CommandManager(this, "peyangutils", "PeyangUtilsDebug", "peyangutils");
        this.pluginCommandManager.registerCommand("debug", new PeyangDebugCommand());
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
