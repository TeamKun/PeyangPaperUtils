package net.kunmc.lab.peyangpaperutils.lang;

import net.kunmc.lab.peyangpaperutils.lib.utils.Pair;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * メッセージの引数を格納します。
 * {@code MsgArgs.of("path.to.var", "value").add("path.to.var2", "value2")...} のように使用します。
 */
public class MsgArgs
{
    private static final Pattern ARG_PATTERN = Pattern.compile("%%([\\w._-]+)%%");
    private final List<Pair<String, String>> args;

    private MsgArgs(List<Pair<String, String>> args)
    {
        this.args = args;
    }

    public static MsgArgs ofEmpty()
    {
        return new MsgArgs(new ArrayList<>());
    }

    /**
     * 引数を設定し, あたらしい  を返します。
     *
     * @param key   引数のキー
     * @param value 引数の値
     * @return あたらしい
     */
    public static MsgArgs of(String key, Object value)
    {
        return MsgArgs.ofEmpty().add(key, String.valueOf(value));
    }

    private static String formatColors(String msg)
    {
        // This part is hideous, but I don't know how to make it better without any performance loss.
        // Your contribution is welcome, so please make a pull request if you have any idea.
        // @formatter:off
        return StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
        StringUtils.replace(
            msg,
            "%%black%%", ChatColor.BLACK.toString()),
            "%%dark_blue%%", ChatColor.DARK_BLUE.toString()),
            "%%dark_green%%", ChatColor.DARK_GREEN.toString()),
            "%%dark_aqua%%", ChatColor.DARK_AQUA.toString()),
            "%%dark_red%%", ChatColor.DARK_RED.toString()),
            "%%dark_purple%%", ChatColor.DARK_PURPLE.toString()),
            "%%gold%%", ChatColor.GOLD.toString()),
            "%%gray%%", ChatColor.GRAY.toString()),
            "%%dark_gray%%", ChatColor.DARK_GRAY.toString()),
            "%%blue%%", ChatColor.BLUE.toString()),
            "%%green%%", ChatColor.GREEN.toString()),
            "%%aqua%%", ChatColor.AQUA.toString()),
            "%%red%%", ChatColor.RED.toString()),
            "%%light_purple%%", ChatColor.LIGHT_PURPLE.toString()),
            "%%yellow%%", ChatColor.YELLOW.toString()),
            "%%white%%", ChatColor.WHITE.toString()),
            "%%reset%%", ChatColor.RESET.toString()),
            "%%bold%%", ChatColor.BOLD.toString()),
            "%%strikethrough%%", ChatColor.STRIKETHROUGH.toString()),
            "%%underline%%", ChatColor.UNDERLINE.toString()),
            "%%italic%%", ChatColor.ITALIC.toString()),
            "%%magic%%", ChatColor.MAGIC.toString()),
            "%%obfuscated%%", ChatColor.MAGIC.toString()
        );
        // @formatter:on
    }

    private String formatDeep(String msg)
    {
        Map<String, String> argMap = new HashMap<>();
        Matcher matcher = ARG_PATTERN.matcher(msg);
        String replacedMessage = msg;
        while (matcher.find())
        {
            String key = matcher.group(1);
            String value = argMap.get(key);
            if (value == null)
            {
                value = LangProvider.get(key, this);
                if (value == null)
                    value = "%%" + key + "%%";
                argMap.put(key, value);
            }
            replacedMessage = StringUtils.replace(msg, "%%" + key + "%%", value);
        }
        return replacedMessage;
    }

    /**
     * 引数を追加します。
     *
     * @param key   引数のキー
     *              例えば、{@code "path.to.var"} とすると、 {@code %%path.to.var%%} が置換されます。
     * @param value 引数の値
     * @return このオブジェクト
     */
    public MsgArgs add(String key, Object value)
    {
        this.args.add(Pair.of(key, String.valueOf(value)));
        return this;
    }

    /**
     * 引数を追加します。
     *
     * @param args 追加する引数
     * @return このオブジェクト
     * @see #add(String, Object)
     */
    public MsgArgs add(MsgArgs args)
    {
        this.args.addAll(args.args);
        return this;
    }

    /* non-public */ String format(String msg)
    {
        String result = msg;
        for (Pair<String, String> arg : this.args)
            result = result.replace("%%" + arg.getLeft() + "%%", arg.getRight());

        if (!result.contains("%%"))
            return result;

        result = formatColors(result);

        try
        {
            return this.formatDeep(result);
        }
        catch (StackOverflowError e)
        {
            return result;
        }
    }
}
