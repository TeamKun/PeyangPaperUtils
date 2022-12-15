package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBukkitTerminal implements Terminal
{
    private static final ChatColor INFO_COLOR = ChatColor.of("#36C0E3");
    private static final ChatColor WARN_COLOR = ChatColor.of("#E3BB36");
    private static final ChatColor ERR_COLOR = ChatColor.of("#E34736");
    private static final ChatColor SCSS_COLOR = ChatColor.of("#4CB52B");
    private static final ChatColor HINT_COLOR = ChatColor.of("#CCD4DB");

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Audience audience;
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

    private static BaseComponent[] buildText(String prefix, String message, ChatColor color)
    {
        return TextComponent.fromLegacyText(color + prefix + ": " + message);
    }

    @Override
    public void info(@NotNull String message, Object... args)
    {
        this.write(buildText("I", safeFormat(message, args), INFO_COLOR));
    }

    @Override
    public void error(@NotNull String message, Object... args)
    {
        this.write(buildText("E", safeFormat(message, args), ERR_COLOR));
    }

    @Override
    public void success(@NotNull String message, Object... args)
    {
        this.write(buildText("S", safeFormat(message, args), SCSS_COLOR));
    }

    @Override
    public void warn(@NotNull String message, Object... args)
    {
        this.write(buildText("W", safeFormat(message, args), WARN_COLOR));
    }

    @Override
    public void hint(@NotNull String message, Object... args)
    {
        this.write(buildText("H", safeFormat(message, args), HINT_COLOR));
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
