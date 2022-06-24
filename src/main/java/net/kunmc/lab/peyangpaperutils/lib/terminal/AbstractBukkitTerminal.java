package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBukkitTerminal implements Terminal
{
    @Getter
    private final Audience audience;
    @Getter
    private final Input input;

    public AbstractBukkitTerminal(Audience audience)
    {
        this.audience = audience;
        this.input = new Input(this);
    }

    /**
     * メッセージをフォーマットする.
     * <p>
     * String.format()の第二引数は可変数のため, Object... argsで受け取った値を与えると, [args] となる.
     * そのため, このようなメソッドで明示的にする.
     * これを使用しない方法は, String.format時に明示的に (Object[]) args をする必要があるが,
     * とりあえずこちらを推奨する.
     * </p>
     *
     * @param format フォーマット文字列
     * @param args   引数
     * @return フォーマットされた文字列
     */
    private static @NotNull String safeFormat(@NotNull String format, @NotNull Object[] args)
    {
        if (args.length == 0)
            return format;

        return String.format(format, args);
    }

    @Override
    public void info(@NotNull String message, Object... args)
    {
        writeLine(safeFormat(ChatColor.BLUE + "I: " + message, args));
    }

    @Override
    public void error(@NotNull String message, Object... args)
    {
        writeLine(safeFormat(ChatColor.RED + "E: " + message, args));
    }

    @Override
    public void success(@NotNull String message, Object... args)
    {
        writeLine(safeFormat(ChatColor.GREEN + "S: " + message, args));
    }

    @Override
    public void warn(@NotNull String message, Object... args)
    {
        writeLine(safeFormat(ChatColor.YELLOW + "W: " + message, args));
    }

    @Override
    public void writeLine(@NotNull String message)
    {
        write(Component.text(message));
    }

    @Override
    public void write(@NotNull Component component)
    {
        this.audience.sendMessage(component);
    }
}
