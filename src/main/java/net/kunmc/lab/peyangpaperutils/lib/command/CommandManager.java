package net.kunmc.lab.peyangpaperutils.lib.command;

import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Terminal;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
        commands = new HashMap<>();
        this.permission = permission;
        this.helpCommand = new CommandHelp(pluginName, commandName, permission + ".help", commands);

        plugin.getCommand(commandName).setExecutor(this);
        plugin.getCommand(commandName).setTabCompleter(this);

        registerCommand("help", helpCommand);
    }

    private static void injectSubcommand(String name, SubCommandWith command)
    {
        try
        {
            Field field = command.getClass().getDeclaredField("commandName");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(command, name);
        }
        catch (NoSuchFieldException | IllegalAccessException ignored)
        {
        }
    }

    public static List<String> handleTabComplete(@NotNull CommandSender sender, @NotNull String[] args,
                                                 @NotNull Terminal terminal, @NotNull Map<String, CommandBase> commands)
    {
        List<String> completes = new ArrayList<>();

        if (args.length == 1)
            completes.addAll(commands.keySet());

        else if (commands.containsKey(args[0]))
        {
            CommandBase commandBase = commands.get(args[0]);

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
     * @param command コマンド
     */
    public void registerCommand(@NotNull String commandName, @NotNull CommandBase command)
    {
        commands.put(commandName, command);

        if (command instanceof SubCommandWith)
            injectSubcommand(commandName, (SubCommandWith) command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {

        Terminal terminal = Terminals.of(sender);

        if (!sender.hasPermission(this.permission))
            return true;

        if (CommandBase.indicateArgsLengthInvalid(terminal, args, 1))
        {
            helpCommand.onCommand(sender, terminal, Utils.removeFirstElement(new String[0]));
            return true;
        }

        if (!commands.containsKey(args[0]))
        {
            terminal.error("サブコマンドが見つかりませんでした:  " + args[0]);
            helpCommand.onCommand(sender, terminal, Utils.removeFirstElement(new String[0]));
            return true;
        }

        CommandBase commandBase = commands.get(args[0]);
        commandBase.onCommand(sender, terminal, Utils.removeFirstElement(args));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
    {
        Terminal terminal = Terminals.of(sender);

        if (!sender.hasPermission(this.permission))
            return null;

        List<String> completes = handleTabComplete(sender, args, terminal, commands);

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
