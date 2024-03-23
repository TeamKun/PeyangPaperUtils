package net.kunmc.lab.peyangpaperutils.lib.terminal;

import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ConsoleTerminal extends AbstractBukkitTerminal
{
    ConsoleTerminal()
    {
        super(Bukkit.getConsoleSender());
    }

    @Override
    public void write(@NotNull BaseComponent[] component)
    {
        Bukkit.getConsoleSender().spigot().sendMessage(component);
    }

    @Override
    public boolean isPlayer()
    {
        return false;
    }

    /**
     * コンソールではサポートしていません。
     */
    @Override
    public @NotNull Progressbar createProgressbar(@NotNull String name) throws IllegalStateException
    {
        throw new UnsupportedOperationException("Console does not support progressbar.");
    }

    /**
     * コンソールではサポートしていません。
     */
    @Override
    public boolean removeProgressbar(@NotNull String name)
    {
        throw new UnsupportedOperationException("Console does not support progressbar.");
    }

    @Override
    public @Nullable Progressbar getProgressbar(@NotNull String name)
    {
        throw new UnsupportedOperationException("Console does not support progressbar.");
    }

    @Override
    public void showNotification(@NotNull String title, @NotNull String message, int showTimeMS)
    {
        String header = ChatColor.GREEN + "+" + StringUtils.repeat("-", 30) + "+\n";

        this.writeLine(
                header +
                        ChatColor.GREEN + "|     \n" +
                        ChatColor.WHITE + title + StringUtils.repeat(" ", 25 - title.length()) +
                        ChatColor.GREEN + "     |\n" +
                        header
        );
    }

    /**
     * サーバのコンソールでは文字を消せないため、コンソールではサポートしていません。
     */
    @Override
    public void clearNotification()
    {
    }
}
