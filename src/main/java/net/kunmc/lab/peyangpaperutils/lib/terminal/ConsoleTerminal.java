package net.kunmc.lab.peyangpaperutils.lib.terminal;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Progressbar;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConsoleTerminal extends AbstractBukkitTerminal
{
    private static final DummyProgressbar DUMMY_PROGRESSBAR;

    static
    {
        DUMMY_PROGRESSBAR = new DummyProgressbar();
    }

    ConsoleTerminal()
    {
        super(Bukkit.getConsoleSender());
    }

    /**
     * コンソールではサポートしていません。
     */
    @Override
    public @NotNull Progressbar createProgressbar(@NotNull String name) throws IllegalStateException
    {
        return DUMMY_PROGRESSBAR;
    }

    /**
     * コンソールではサポートしていません。
     */
    @Override
    public boolean removeProgressbar(@NotNull String name)
    {
        return true;
    }

    @Override
    public @Nullable Progressbar getProgressbar(@NotNull String name)
    {
        return DUMMY_PROGRESSBAR;
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

    private static class DummyProgressbar implements Progressbar
    {

        @Override
        public void setProgressMax(int max)
        {

        }

        @Override
        public void setProgress(int progress)
        {

        }

        @Override
        public void setPrefix(@Nullable String prefix)
        {

        }

        @Override
        public void setSuffix(@Nullable String suffix)
        {

        }

        @Override
        public void setSize(int size)
        {

        }

        @Override
        public void show()
        {

        }

        @Override
        public void hide()
        {

        }

        @Override
        public void update()
        {

        }
    }
}
