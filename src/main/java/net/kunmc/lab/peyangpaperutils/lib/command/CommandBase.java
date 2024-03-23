package net.kunmc.lab.peyangpaperutils.lib.command;

import net.kunmc.lab.peyangpaperutils.lib.components.Text;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminal;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * コマンドの基底クラスです。
 */
public abstract class CommandBase
{
    /**
     * 必須な引数を返します。
     *
     * @param argName  引数名
     * @param typeName 引数の型
     * @return 引数文字列
     */
    protected static @NotNull String required(@NotNull String argName, @NotNull String typeName)
    {
        return ChatColor.AQUA + "<" + argName + ":" + typeName + "> " + ChatColor.RESET;
    }

    /**
     * 必須な、int型の引数を返します。
     *
     * @param argName 引数名
     * @param min     最小値
     * @param max     最大値
     * @return 引数文字列
     */
    protected static @NotNull String required(@NotNull String argName, int min, int max)
    {
        return ChatColor.AQUA + "<" + argName + ":int:" + min + "～" + max + "> " + ChatColor.RESET;
    }

    /**
     * 必須な、int型の引数を最小値と最大値を指定して返します。
     *
     * @param argName 引数名
     * @param min     最小値
     * @param max     最大値
     * @return 引数文字列
     */
    protected static @NotNull String required(@NotNull String argName, double min, double max)
    {
        return ChatColor.AQUA + "<" + argName + ":double:" + min + "～" + max + "> " + ChatColor.RESET;
    }

    /**
     * オプションな引数を返します。
     *
     * @param argName  引数名
     * @param typeName 引数の型
     * @return 引数文字列
     */
    protected static @NotNull String optional(@NotNull String argName, @NotNull String typeName)
    {
        return ChatColor.DARK_AQUA + "[" + argName + ":" + typeName + "] " + ChatColor.RESET;
    }

    /**
     * オプションな、引数をデフォルトの値を指定して返します。
     *
     * @param argName      引数名
     * @param typeName     引数の型
     * @param defaultValue デフォルト値
     * @return 引数文字列
     */
    protected static @NotNull String optional(@NotNull String argName, @NotNull String typeName, @NotNull String defaultValue)
    {
        return ChatColor.DARK_AQUA + "[" + argName + ":" + typeName + "@" + defaultValue + "] " + ChatColor.RESET;
    }

    /**
     * オプションな、int型の引数を最小値と最大値を指定して返します。
     *
     * @param argName 引数名
     * @param min     最小値
     * @param max     最大値
     * @return 引数文字列
     */
    protected static @NotNull String optional(@NotNull String argName, int min, int max)
    {
        return ChatColor.DARK_AQUA + "[" + argName + ":int:" + min + "～" + max + "] " + ChatColor.RESET;
    }

    /**
     * オプションな、int型の引数を最小値と最大値とデフォルト値を指定して返します。
     *
     * @param argName      引数名
     * @param min          最小値
     * @param max          最大値
     * @param defaultValue デフォルト値
     * @return 引数文字列
     */
    protected static @NotNull String optional(@NotNull String argName, int min, int max, @NotNull String defaultValue)
    {
        return ChatColor.DARK_AQUA + "[" + argName + ":int:" + min + "～" + max + "@" + defaultValue + "] " + ChatColor.RESET;
    }

    /**
     * ヘルプ用のコマンドの要約を返します。
     *
     * @return ヘルプ用のコマンドの要約
     */
    public abstract Text getHelpOneLine();

    /**
     * コマンドを提案します。プレイヤはクリックして適用します。
     *
     * @param text    提案する文字列
     * @param command 提案するコマンド
     * @return テキスト
     */
    protected static @NotNull Text suggestCommand(@NotNull String text, @NotNull String command)
    {
        return Text.of(text)
                .suggestCommandOnClick(command)
                .hoverText(ChatColor.YELLOW + "クリックして補完！");
    }

    /**
     * コマンドを提案します。プレイヤはクリックして適用します。
     *
     * @param text    提案する文字列
     * @param command 提案するコマンド
     * @return テキスト
     */
    protected static @NotNull Text suggestCommand(@NotNull Text text, @NotNull String command)
    {
        return text.suggestCommandOnClick(command)
                .hoverText(ChatColor.YELLOW + "クリックして補完！");
    }

    /**
     * 引数の数が正しいかチェックし、正しくなかったら実行者に指摘します。
     *
     * @param terminal ターミナル
     * @param args     引数
     * @param min      最小数(n以上)
     * @param max      最大数(n以下)
     * @return 引数の数が正しければ {@code false}
     */
    protected static boolean indicateArgsLengthInvalid(@NotNull Terminal terminal, String[] args, int min, int max)
    {
        if (min != -1 && args.length < min || (max != -1 && args.length > max))
        {
            terminal.error("引数の数が不正です: 必要: " +
                    (max == -1 ? min: min + "〜" + max) +
                    " 提供: " + args.length);
            return true;
        }

        return false;
    }

    /**
     * 引数の数が正しいかチェックし、正しくなかったら実行者に指摘します。
     *
     * @param terminal ターミナル
     * @param args     引数
     * @param min      最小数(n以上)
     * @return 引数の数が正しければ {@code false}
     */
    public static boolean indicateArgsLengthInvalid(@NotNull Terminal terminal, String[] args, int min)
    {
        return indicateArgsLengthInvalid(terminal, args, min, -1);
    }

    /**
     * プレイヤが実行したかチェックし、プレイヤ以外の実行なら指摘します。
     *
     * @param terminal ターミナル
     * @return プレイヤからの実行ならば {@code false}
     */
    protected static boolean indicatePlayer(@NotNull Terminal terminal)
    {
        if (!terminal.isPlayer())
        {
            terminal.error("このコマンドはプレイヤーからのみ実行できます！");
            return true;
        }

        return false;
    }

    /**
     * {@link Integer} 型か確認し、範囲の確認もして {@link Integer} 型に変換して返します。
     * また、これらの条件に合致していなければ指摘します。
     *
     * @param terminal ターミナル
     * @param arg      引数
     * @param min      最小値(n以上)
     * @param max      最大値(n以下)
     * @return 変換した結果
     */
    protected static @Nullable Integer parseInteger(@NotNull Terminal terminal, @NotNull String arg, int min, int max)
    {
        try
        {
            int num = Integer.parseInt(arg);
            if (checkValidNumber(terminal, num, min, max))
                return null;
            return num;
        }
        catch (NumberFormatException e)
        {
            terminal.error("引数が数値ではありません: 必要: " +
                    (max == -1 ? min: min + "〜" + max) +
                    " 提供: " + arg);
            return null;
        }
    }

    /**
     * {@link Integer} 型か確認し、最小以上かの確認もして {@link Integer} 型に変換して返します。
     * また、これらの条件に合致していなければ指摘します。
     *
     * @param terminal ターミナル
     * @param arg      引数
     * @param min      最小値(n以上)
     * @return 変換した結果
     */
    public static @Nullable Integer parseInteger(@NotNull Terminal terminal, @NotNull String arg, int min)
    {
        return parseInteger(terminal, arg, min, -1);
    }

    /**
     * {@link Double} 型か確認し、範囲の確認もして {@link Double} 型に変換して返します。
     * また、これらの条件に合致していなければ指摘します。
     *
     * @param terminal ターミナル
     * @param arg      引数
     * @param min      最小値(n以上)
     * @param max      最大値(n以下)
     * @return 変換した結果
     */
    public static Double parseDouble(@NotNull Terminal terminal, String arg, double min, double max)
    {
        try
        {
            double num = Double.parseDouble(arg);
            if (checkValidNumber(terminal, num, min, max))
                return null;
            return num;
        }
        catch (NumberFormatException e)
        {
            terminal.error("引数が数値ではありません: 必要: " +
                    (max == -1 ? min: min + "〜" + max) +
                    " 提供: " + arg);
            return null;
        }
    }

    /**
     * {@link Double} 型か確認し、最小以上かの確認もして {@link Double} 型に変換して返します。
     * また、これらの条件に合致していなければ指摘します。
     *
     * @param terminal ターミナル
     * @param arg      引数
     * @param min      最小値(n以上)
     * @return 変換した結果
     */
    public static @Nullable Double parseDouble(@NotNull Terminal terminal, @NotNull String arg, double min)
    {
        return parseDouble(terminal, arg, min, -1d);
    }

    private static boolean checkValidNumber(@NotNull Terminal terminal, Number number, Number min, Number max)
    {

        if ((min.doubleValue() != -1d &&
                number.doubleValue() < min.doubleValue()) ||
                (max.doubleValue() != -1d && number.doubleValue() > max.doubleValue()))
        {
            terminal.error("引数の値が不正です: 必要: " +
                    (max == null ? min: min + "〜" + max) +
                    " 提供: " + number);
            return true;
        }

        return false;
    }

    /**
     * 適切な権限があるかチェックし、ない場合は指摘します。
     *
     * @param sender     コマンド実行者
     * @param terminal   ターミナル
     * @param permission 権限
     * @return 適切な権限がある場合は {@code false} を返します。
     */
    protected static boolean checkPermission(@NotNull CommandSender sender, @NotNull Terminal terminal, @NotNull String permission)
    {
        if (sender.hasPermission(permission))
            return false;

        terminal.error("このコマンドを使用するには権限が必要です！");
        return true;
    }

    /**
     * コマンドのハンドラです。
     *
     * @param sender   コマンド実行者
     * @param terminal ターミナル
     * @param args     コマンド引数
     */
    public abstract void onCommand(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args);

    /**
     * タブ補完のハンドラです。
     *
     * @param sender コマンド実行者
     * @param terminal ターミナル
     * @param args   コマンド引数
     * @return タブ補完結果
     */
    public abstract @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args);

    /**
     * このコマンドの実行に必要な権限を返します。
     * 返された権限はハンドラの呼び出し前に自動でチェックされます。
     *
     * @return このコマンドの実行に必要な権限
     */
    public abstract @Nullable String getPermission();

    /**
     * String型の文字列をTextComponentにします。
     *
     * @param text 文字列
     * @return TextComponent
     */
    protected static @NotNull Text of(@NotNull String text)
    {
        return Text.of(text);
    }

    /**
     * コマンドの引数を以下の書式で返します。
     * <pre>
     *     String[] args = {
     *         required("arg1", "string"),
     *         optional("arg3", "int"),
     *         optional("arg2", "string", "default")
     *     };
     * </pre>
     *
     * @return コマンドの引数
     */
    public abstract String[] getArguments();
}
