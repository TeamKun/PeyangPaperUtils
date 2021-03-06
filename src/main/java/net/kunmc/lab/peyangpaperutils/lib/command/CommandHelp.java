package net.kunmc.lab.peyangpaperutils.lib.command;

import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class CommandHelp extends CommandBase
{
    private final String pluginName;
    private final String baseCommandName;
    private final String permission;
    private final HashMap<String, CommandBase> commands;
    private final List<String> subCommands;

    public CommandHelp(String pluginName, String baseCommandName, String permission, HashMap<String, CommandBase> commands)
    {
        this.pluginName = pluginName;
        this.baseCommandName = baseCommandName;
        this.permission = permission;
        this.commands = commands;
        this.subCommands = commands.entrySet().stream().parallel()
                .filter(entry -> entry.getValue() instanceof SubCommandWith)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        Map<String, CommandBase> commands = this.commands;
        boolean subCommand = false;

        if (args.length > 0 && commands.containsKey(args[0]))
        {
            CommandBase command = commands.get(args[0]);
            if (command instanceof SubCommandWith)
            {
                SubCommandWith subCommandable = (SubCommandWith) command;
                commands = subCommandable.getSubCommands(sender);
                subCommand = true;
            }
        }

        int pageLengthMax = commands.size() / 5 + 1;

        int page = 1;
        try
        {
            if (args.length > 1)
                page = Integer.parseInt(args[1]);
            else if (args.length > 0)
                if (args[0].matches("\\d+"))
                    page = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e)
        {
            terminal.error("?????????????????????????????????");
            return;
        }

        if (page < 1 || page > pageLengthMax)
        {
            terminal.error("????????????????????????????????????");
            return;
        }

        buildHelpPage(terminal, this.pluginName, page, pageLengthMax, commands, subCommand ? args[0]: null);
    }

    private void send(Terminal terminal, TextComponent helpMessage, String commandName, int space_size, String subCommand)
    {
        terminal.write(suggestCommand(
                of(ChatColor.AQUA + "/" + this.baseCommandName + " " + (subCommand != null ? subCommand + " ": "")
                        + commandName + " " + StringUtils.repeat(" ", space_size - commandName.length()) + " - ")
                        .append(Component.text(ChatColor.DARK_AQUA.toString()))
                        .append(helpMessage.color(NamedTextColor.DARK_AQUA)),
                "/" + this.baseCommandName + " " + (subCommand != null ? subCommand + " ": "") + commandName
        ));
    }

    private void buildHelpPage(Terminal terminal, String pluginName, int page, int pageLengthMax,
                               Map<String, ? extends CommandBase> commands, String subcommandName)
    {
        terminal.writeLine(ChatColor.GOLD + "-----=====    " + pluginName + " (" + page + "/" + pageLengthMax + ")  =====-----");

        int start = (page - 1) * 5;
        int end = Math.min(start + 5, commands.size());
        int maxLengthOfLine = commands.keySet().stream().mapToInt(String::length).max().orElse(0);

        commands.entrySet().stream()
                .skip(start)
                .limit(end - start)
                .forEach(entry -> send(terminal, entry.getValue().getHelpOneLine().append(
                                        of("\n    " + String.join(" ", entry.getValue().getArguments()))),
                                entry.getKey(), maxLengthOfLine, subcommandName
                        )
                );

        TextComponent footer = of(ChatColor.GOLD + "-----=====");

        if (page > 1)
            footer = footer.append(of(ChatColor.GOLD + " [" + ChatColor.RED + "<<" + ChatColor.GOLD + "]")
                    .clickEvent(ClickEvent.runCommand("/" + this.baseCommandName + " help " +
                            (subcommandName == null ? "": subcommandName + " ") + (page - 1)))
                    .hoverEvent(HoverEvent.showText(of(ChatColor.AQUA + "??????????????????????????????????????????"))));
        else
            footer = footer.append(of("     "));

        footer = footer.append(of(ChatColor.GOLD + " " + pluginName + " "));

        if (page < pageLengthMax)
            footer = footer.append(of(ChatColor.GOLD + "[" + ChatColor.GREEN + ">>" + ChatColor.GOLD + "] ")
                    .clickEvent(ClickEvent.runCommand("/" + this.baseCommandName + " help " +
                            (subcommandName == null ? "": subcommandName + " ") + (page + 1)))
                    .hoverEvent(HoverEvent.showText(of(ChatColor.AQUA + "??????????????????????????????????????????"))));
        else
            footer = footer.append(of("    "));

        terminal.write(footer.append(of(ChatColor.GOLD + "=====-----")));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        List<String> result = new ArrayList<>(this.subCommands);

        if (args.length == 1)
            return result;
        else if (args.length == 2 && !this.commands.containsKey(args[0]))
            return Collections.singletonList("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");

        return null;
    }

    @Override
    public @Nullable String getPermission()
    {
        return this.permission;
    }

    @Override
    public TextComponent getHelpOneLine()
    {
        return of("??????????????????????????????");
    }

    @Override
    public String[] getArguments()
    {
        return new String[]{
                optional("???????????????|??????????????????", "integer/string"),
                optional("???????????????", "integer"),
        };
    }
}
