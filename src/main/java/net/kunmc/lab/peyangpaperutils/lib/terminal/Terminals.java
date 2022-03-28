package net.kunmc.lab.peyangpaperutils.lib.terminal;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Terminal;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ターミナル関係のクラスです。
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
     * 指定された {@link Audience} のターミナルを取得します。
     *
     * @param audience {@link Audience}
     * @return ターミナル
     */
    public static @NotNull Terminal of(@NotNull Audience audience)
    {
        if (audience instanceof Player)
            return of((Player) audience);
        return ofConsole();
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
        return of((Audience) sender);
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
}
