package net.kunmc.lab.peyangpaperutils.lib.command;

import net.kunmc.lab.peyangpaperutils.lib.components.Text;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminal;
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
            terminal.error("不正なページ番号です。");
            return;
        }

        if (page < 1 || page > pageLengthMax)
        {
            terminal.error("ページ番号が範囲外です。");
            return;
        }

        buildHelpPage(terminal, this.pluginName, page, pageLengthMax, commands, subCommand ? args[0]: null);
    }

    private void send(Terminal terminal, Text helpMessage, String commandName, int space_size, String subCommand)
    {
        terminal.write(suggestCommand(
                of(ChatColor.AQUA + "/" + this.baseCommandName + " " + (subCommand != null ? subCommand + " ": "")
                        + commandName + " " + StringUtils.repeat(" ", space_size - commandName.length()) + " - ")
                        .append(helpMessage.color(ChatColor.DARK_AQUA)),
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
                .filter(entry -> !entry.getKey().startsWith(CommandManager.ALIAS_PREFIX))
                .skip(start)
                .limit(end - start)
                .forEach(entry -> send(terminal, entry.getValue().getHelpOneLine().append(
                                        of("\n    " + String.join(" ", entry.getValue().getArguments()))),
                                entry.getKey(), maxLengthOfLine, subcommandName
                        )
                );

        Text footer = of(ChatColor.GOLD + "-----=====");

        if (page > 1)
            footer.append(of(ChatColor.GOLD + " [" + ChatColor.RED + "<<" + ChatColor.GOLD + "]")
                    .runCommandOnClick("/" + this.baseCommandName + " help " +
                            (subcommandName == null ? "": subcommandName + " ") + (page - 1))
                    .hoverText(ChatColor.AQUA + "クリックして前のページに戻る"));
        else
            footer.append(of("     "));

        footer.append(of(ChatColor.GOLD + " " + pluginName + " "));

        if (page < pageLengthMax)
            footer.append(of(ChatColor.GOLD + "[" + ChatColor.GREEN + ">>" + ChatColor.GOLD + "] ")
                    .runCommandOnClick("/" + this.baseCommandName + " help " +
                            (subcommandName == null ? "": subcommandName + " ") + (page + 1))
                    .hoverText(of(ChatColor.AQUA + "クリックして次のページに進む")));
        else
            footer.append(of("    "));

        terminal.write(footer.append(of(ChatColor.GOLD + "=====-----")));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        List<String> result = new ArrayList<>(this.subCommands);

        if (args.length == 1)
            return result;
        else if (args.length == 2 && !this.commands.containsKey(args[0]))
            return Collections.singletonList("存在しないサブコマンドです。ページ番号を指定している場合は第二引数は必要ありません。");

        return null;
    }

    @Override
    public @Nullable String getPermission()
    {
        return this.permission;
    }

    @Override
    public Text getHelpOneLine()
    {
        return of("ヘルプを表示します。");
    }

    @Override
    public String[] getArguments()
    {
        return new String[]{
                optional("ページ番号|サブコマンド", "integer/string"),
                optional("ページ番号", "integer"),
        };
    }
}
