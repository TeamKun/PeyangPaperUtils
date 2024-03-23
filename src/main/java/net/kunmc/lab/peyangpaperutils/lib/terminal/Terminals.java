package net.kunmc.lab.peyangpaperutils.lib.terminal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ターミナルを取得するためのクラスです。
 */
public class Terminals
{
    private static final Map<UUID, Terminal> terminals;

    static
    {
        terminals = new HashMap<>();
    }

    private Terminals()
    {
    }

    /**
     * 指定されたプレイヤーのターミナルを取得します。
     *
     * @param player プレイヤー
     * @return ターミナル
     */
    public static @NotNull Terminal of(@NotNull Player player)
    {
        Terminal terminal = terminals.get(player.getUniqueId());
        if (terminal != null)
            if (player.isOnline())
                return terminal;

        terminal = new PlayerTerminal(player);
        terminals.put(player.getUniqueId(), terminal);
        return terminal;
    }

    /**
     * 指定された {@link CommandSender} のターミナルを取得します。
     *
     * @param sender {@link CommandSender}
     * @return ターミナル
     */
    public static @NotNull Terminal of(@NotNull CommandSender sender)
    {
        if (sender instanceof Player)
            return of((Player) sender);
        return ofConsole();
    }

    /**
     * コンソールのターミナルを取得します。
     *
     * @return ターミナル
     */
    public static @NotNull Terminal ofConsole()
    {
        Terminal terminal = terminals.get(null);
        if (terminal != null)
            return terminal;

        terminal = new ConsoleTerminal();
        terminals.put(null, terminal);
        return terminal;
    }

    /**
     * 指定されたターミナルを破棄します。
     *
     * @param terminal ターミナル
     */
    public static void purge(@NotNull Terminal terminal)
    {
        if (terminal instanceof PlayerTerminal)
            terminals.remove(((PlayerTerminal) terminal).getPlayer().getUniqueId());
        else if (terminal instanceof ConsoleTerminal)
            terminals.remove(null);
    }

    /**
     * 指定されたターミナルを破棄します。
     *
     * @param player プレイヤー
     */
    public static void purge(@NotNull Player player)
    {
        terminals.remove(player.getUniqueId());
    }
}
