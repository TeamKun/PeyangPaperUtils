package net.kunmc.lab.peyangpaperutils.lib.command;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Terminal;
import net.kunmc.lab.peyangpaperutils.lib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * サブコマンドがあるコマンドに適用します。
 */
public abstract class SubCommandWith extends CommandBase
{
    // This field will rewrite by the reflection.
    private final String commandName = null;

    /**
     * この名前を返します。
     * {@code /<command> <subcommand> <args>} というコマンドを受け付ける場合、 {@code <subcommand>} の部分を返してください。
     *
     * @return このコマンドの名前
     */
    protected abstract String getName();

    /**
     * サブコマンドを返します。
     *
     * @return サブコマンド
     */
    protected abstract Map<String, CommandBase> getSubCommands(@NotNull CommandSender sender);

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        if (indicateArgsLengthInvalid(terminal, args, 1))
        {
            Bukkit.dispatchCommand(sender, commandName + " help " + getName());
            return;
        }

        Map<String, CommandBase> commands = getSubCommands(sender);

        if (!commands.containsKey(args[0]))
        {
            terminal.error("サブコマンドが見つかりませんでした: " + args[0]);
            Bukkit.dispatchCommand(sender, commandName + " help " + getName());
            return;
        }

        CommandBase commandBase = commands.get(args[0]);
        commandBase.onCommand(sender, terminal, Utils.removeFirstElement(args));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        Map<String, CommandBase> commands = getSubCommands(sender);


        return CommandManager.handleTabComplete(sender, args, terminal, commands);
    }

    @Override
    public String[] getArguments()
    {
        return new String[]{
                required("subcommand", "string"),
                optional("args", "string[]")
        };
    }
}
