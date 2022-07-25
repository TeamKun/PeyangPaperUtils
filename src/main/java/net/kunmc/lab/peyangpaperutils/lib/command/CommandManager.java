package net.kunmc.lab.peyangpaperutils.lib.command;

import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminal;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
import net.kunmc.lab.peyangpaperutils.lib.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * コマンドを管理するクラスです。
 */
public class CommandManager implements CommandExecutor, TabCompleter
{
    public static final String ALIAS_PREFIX = "\0alias\0";

    private final HashMap<String, CommandBase> commands;
    private final String permission;
    private final CommandBase helpCommand;

    /**
     * コマンドマネジャのコンストラクタです。
     *
     * @param plugin      プラグイン
     * @param commandName コマンド名
     * @param pluginName  プラグイン名(ヘルプのヘッダーとフッターに使用します。実際のプラグイン名と同一である必要はありません。)
     * @param permission  このコマンドを実行するのに必要な権限
     */
    @SuppressWarnings("ConstantConditions")
    public CommandManager(@NotNull JavaPlugin plugin, @NotNull String commandName, @NotNull String pluginName, @NotNull String permission)
    {
        this.commands = new HashMap<>();
        this.permission = permission;
        this.helpCommand = new CommandHelp(pluginName, commandName, permission + ".help", this.commands);

        plugin.getCommand(commandName).setExecutor(this);
        plugin.getCommand(commandName).setTabCompleter(this);

        registerCommand("help", this.helpCommand);
    }

    public static List<String> handleTabComplete(@NotNull CommandSender sender, @NotNull String[] args,
                                                 @NotNull Terminal terminal, @NotNull Map<String, ? extends CommandBase> commands)
    {
        List<String> completes = new ArrayList<>();

        if (args.length == 1)
            commands.keySet().stream().filter(key -> !key.startsWith(ALIAS_PREFIX)).forEach(completes::add);

        else if (commands.containsKey(args[0]) || commands.containsKey(ALIAS_PREFIX + args[0]))
        {
            CommandBase commandBase = commands.get(args[0]);
            if (commandBase == null)
                commandBase = commands.get(ALIAS_PREFIX + args[0]);

            if (commandBase.getPermission() != null && !sender.hasPermission(commandBase.getPermission()))
                return completes;

            String[] commandArguments = commandBase.getArguments();
            List<String> commandCompletes = commandBase.onTabComplete(sender, terminal, Utils.removeFirstElement(args));
            if (commandCompletes != null)
                completes.addAll(commandCompletes);
            if (commandArguments.length >= args.length - 1)
                completes.add(ChatColor.stripColor(commandArguments[args.length - 2]));
        }

        return completes;
    }

    /**
     * コマンドを登録します。
     *
     * @param commandName コマンド名
     * @param command     コマンド
     * @param alias       エイリアス
     */
    public void registerCommand(@NotNull String commandName, @NotNull CommandBase command, String... alias)
    {
        if (commandName.startsWith(ALIAS_PREFIX))
            throw new IllegalArgumentException("コマンド名に予約語が含まれています.");

        this.commands.put(commandName, command);

        if (alias != null)
            for (String aliasName : alias)
                this.commands.put(ALIAS_PREFIX + aliasName, command);

        if (command instanceof SubCommandWith)
            ((SubCommandWith) command).setCommandName(commandName);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {

        Terminal terminal = Terminals.of(sender);

        if (!sender.hasPermission(this.permission))
            return true;

        if (CommandBase.indicateArgsLengthInvalid(terminal, args, 1))
        {
            this.helpCommand.onCommand(sender, terminal, new String[0]);
            return true;
        }

        if (!(this.commands.containsKey(args[0]) || this.commands.containsKey(ALIAS_PREFIX + args[0])))
        {
            terminal.error("サブコマンドが見つかりませんでした:  " + args[0]);
            this.helpCommand.onCommand(sender, terminal, new String[0]);
            return true;
        }

        CommandBase commandBase = this.commands.get(args[0]);
        if (commandBase == null)
            commandBase = this.commands.get(ALIAS_PREFIX + args[0]);

        if (commandBase.getPermission() != null && !sender.hasPermission(commandBase.getPermission()))
        {
            terminal.error("このコマンドを使用するには権限が必要です！");
            return true;
        }
        commandBase.onCommand(sender, terminal, Utils.removeFirstElement(args));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
    {
        Terminal terminal = Terminals.of(sender);

        if (!sender.hasPermission(this.permission))
            return null;

        List<String> completes = handleTabComplete(sender, args, terminal, this.commands);

        ArrayList<String> result = new ArrayList<>();

        for (String complete : completes)
        {
            if (StringUtils.containsIgnoreCase(complete, args[args.length - 1]))
                result.add(complete);
        }
        Collections.sort(result);
        return result;
    }


}
