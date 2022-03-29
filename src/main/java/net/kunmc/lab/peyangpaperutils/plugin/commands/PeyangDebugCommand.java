package net.kunmc.lab.peyangpaperutils.plugin.commands;

import net.kunmc.lab.peyangpaperutils.lib.command.CommandBase;
import net.kunmc.lab.peyangpaperutils.lib.command.SubCommandWith;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PeyangDebugCommand extends SubCommandWith
{
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
        return "peyadebug";
    }

    @Override
    protected Map<String, CommandBase> getSubCommands(@NotNull CommandSender sender)
    {
        return null;
    }
}
