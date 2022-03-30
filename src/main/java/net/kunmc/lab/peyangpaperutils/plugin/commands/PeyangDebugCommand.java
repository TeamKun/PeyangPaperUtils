package net.kunmc.lab.peyangpaperutils.plugin.commands;

import net.kunmc.lab.peyangpaperutils.lib.command.CommandBase;
import net.kunmc.lab.peyangpaperutils.lib.command.SubCommandWith;
import net.kunmc.lab.peyangpaperutils.plugin.commands.debug.QuestionDebugCommand;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PeyangDebugCommand extends SubCommandWith
{
    private final Map<String, CommandBase> subCommands;

    public PeyangDebugCommand()
    {
        this.subCommands = new HashMap<>();
        this.subCommands.put("question", new QuestionDebugCommand());
    }

    @Override
    public @NotNull String getPermission()
    {
        return "peyangutils.debug";
    }

    @Override
    public TextComponent getHelpOneLine()
    {
        return of("PeyangUtilsのデバッグコマンドです。");
    }

    @Override
    protected String getName()
    {
        return "debug";
    }

    @Override
    protected Map<String, CommandBase> getSubCommands(@NotNull CommandSender sender)
    {
        return this.subCommands;
    }

}
