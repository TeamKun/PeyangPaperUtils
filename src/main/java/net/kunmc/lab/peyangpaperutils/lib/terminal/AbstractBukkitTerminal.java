package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kunmc.lab.peyangpaperutils.lib.components.Text;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

abstract class AbstractBukkitTerminal implements Terminal
{
    @Nullable
    private static final Method mOf;
    private static final ChatColor INFO_COLOR = ofColor("#36C0E3", ChatColor.AQUA);
    private static final ChatColor WARN_COLOR = ofColor("#E3BB36", ChatColor.YELLOW);
    private static final ChatColor ERR_COLOR = ofColor("#E34736", ChatColor.RED);
    private static final ChatColor SCSS_COLOR = ofColor("#4CB52B", ChatColor.DARK_GREEN);
    private static final ChatColor HINT_COLOR = ofColor("#CCD4DB", ChatColor.GRAY);
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private CommandSender sender;

    static
    {
        Method mOf$ = null;
        try
        {
            mOf$ = ChatColor.class.getDeclaredMethod("of", String.class);
            mOf$.setAccessible(true);
        }
        catch (NoSuchMethodException ignored)
        {
        }

        mOf = mOf$;
    }

    public AbstractBukkitTerminal(CommandSender sender)
    {
        this.sender = sender;
        this.input = new Input(this);
    }
    @Getter
    private final Input input;

    @Override
    public void writeLine(@NotNull String message)
    {
        write(Text.of(message));
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
        if (prefix == null)
            return TextComponent.fromLegacyText(color + message, color);
        else
            return TextComponent.fromLegacyText(color + prefix + ": " + message, color);
    }

    private static BaseComponent[] buildText(String message, ChatColor color)
    {
        return buildText(null, message, color);
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
    public void infoImplicit(@NotNull String message, Object... args)
    {
        this.write(buildText(safeFormat(message, args), INFO_COLOR));
    }

    @Override
    public void errorImplicit(@NotNull String message, Object... args)
    {
        this.write(buildText(safeFormat(message, args), ERR_COLOR));
    }

    @Override
    public void successImplicit(@NotNull String message, Object... args)
    {
        this.write(buildText(safeFormat(message, args), SCSS_COLOR));
    }

    @Override
    public void warnImplicit(@NotNull String message, Object... args)
    {
        this.write(buildText(safeFormat(message, args), WARN_COLOR));
    }

    @Override
    public void hintImplicit(@NotNull String message, Object... args)
    {
        this.write(buildText(safeFormat(message, args), HINT_COLOR));
    }

    @Override
    public void write(@NotNull Text component)
    {
        this.sender.spigot().sendMessage(component.asComponents());
    }

    private static ChatColor ofColor(String hex, ChatColor def)
    {
        if (mOf == null)
            return def;

        try
        {
            return (ChatColor) mOf.invoke(null, hex);
        }
        catch (Exception e)
        {
            return def;
        }
    }
}
