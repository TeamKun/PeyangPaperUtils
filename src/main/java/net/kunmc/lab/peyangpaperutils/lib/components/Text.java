package net.kunmc.lab.peyangpaperutils.lib.components;

import net.kunmc.lab.peyangpaperutils.lang.LangProvider;
import net.kunmc.lab.peyangpaperutils.lang.MsgArgs;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Bungeecord のコンポネント系のより使いやすいラッパークラスです。
 */
public class Text
{
    private final ComponentBuilder builder;

    private Text(@NotNull ComponentBuilder builder)
    {
        this.builder = builder;
    }

    /**
     * テキストを追加します。
     *
     * @param text 追加するテキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text append(@NotNull String text)
    {
        this.builder.append(text);
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param text 追加するテキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text append(@NotNull Text text)
    {
        this.builder.append(text.builder.create());
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param text 追加するテキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text append(@NotNull TextComponent text)
    {
        this.builder.append(text);
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param texts 追加するテキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text append(@NotNull TextComponent... texts)
    {
        this.builder.append(texts);
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param texts 追加するテキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text append(@NotNull BaseComponent... texts)
    {
        this.builder.append(texts);
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param text                  追加するテキスト
     * @param formatRetentionPolicy フォーマットの保持方法
     * @return このインスタンス
     */
    @Contract("_, _ -> this")
    public Text append(@NotNull Text text, @NotNull ComponentBuilder.FormatRetention formatRetentionPolicy)
    {
        this.builder.append(text.builder.create(), formatRetentionPolicy);
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param text                  追加するテキスト
     * @param formatRetentionPolicy フォーマットの保持方法
     * @return このインスタンス
     */
    @Contract("_, _ -> this")
    public Text append(@NotNull TextComponent text, @NotNull ComponentBuilder.FormatRetention formatRetentionPolicy)
    {
        this.builder.append(text, formatRetentionPolicy);
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param texts                 追加するテキスト
     * @param formatRetentionPolicy フォーマットの保持方法
     * @return このインスタンス
     */
    @Contract("_, _ -> this")
    public Text append(@NotNull TextComponent[] texts, @NotNull ComponentBuilder.FormatRetention formatRetentionPolicy)
    {
        this.builder.append(texts, formatRetentionPolicy);
        return this;
    }

    /**
     * テキストを追加します。
     *
     * @param texts                 追加するテキスト
     * @param formatRetentionPolicy フォーマットの保持方法
     * @return このインスタンス
     */
    @Contract("_, _ -> this")
    public Text append(@NotNull BaseComponent[] texts, @NotNull ComponentBuilder.FormatRetention formatRetentionPolicy)
    {
        this.builder.append(texts, formatRetentionPolicy);
        return this;
    }

    /**
     * テキストの色を変更します。
     *
     * @param color 色
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text color(@Nullable ChatColor color)
    {
        this.builder.color(color);
        return this;
    }

    /**
     * テキストの色を変更します。
     *
     * @param color 色
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text color(@Nullable org.bukkit.ChatColor color)
    {
        if (color == null)
            return this.color((ChatColor) null);
        else
            return this.color(color.asBungee());
    }

    /**
     * テキストを太字にします。
     *
     * @return このインスタンス
     */
    @Contract(" -> this")
    public Text bold()
    {
        return this.bold(true);
    }

    /**
     * テキストの太字状態を変更します。
     *
     * @param bold 太字かどうか
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text bold(boolean bold)
    {
        this.builder.bold(bold);
        return this;
    }

    /**
     * テキストを斜体にします。
     *
     * @return このインスタンス
     */
    @Contract(" -> this")
    public Text italic()
    {
        return this.italic(true);
    }

    /**
     * テキストの斜体状態を変更します。
     *
     * @param italic 斜体かどうか
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text italic(boolean italic)
    {
        this.builder.italic(italic);
        return this;
    }

    /**
     * テキストを下線付きにします。
     *
     * @return このインスタンス
     */
    @Contract(" -> this")
    public Text underline()
    {
        return this.underline(true);
    }

    /**
     * テキストの下線状態を変更します。
     *
     * @param underlined 下線かどうか
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text underline(boolean underlined)
    {
        this.builder.underlined(underlined);
        return this;
    }

    /**
     * テキストを取り消し線付きにします。
     *
     * @return このインスタンス
     */
    @Contract(" -> this")
    public Text strikethrough()
    {
        return this.strikethrough(true);
    }

    /**
     * テキストの取り消し線状態を変更します。
     *
     * @param strikethrough 取り消し線かどうか
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text strikethrough(boolean strikethrough)
    {
        this.builder.strikethrough(strikethrough);
        return this;
    }

    /**
     * テキストを難読化します。
     *
     * @return このインスタンス
     */
    @Contract(" -> this")
    public Text obfuscated()
    {
        return this.obfuscated(true);
    }

    /**
     * テキストの難読化状態を変更します。
     *
     * @param obfuscated 難読化かどうか
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text obfuscated(boolean obfuscated)
    {
        this.builder.obfuscated(obfuscated);
        return this;
    }

    /**
     * クリック時に実行されるコマンドを設定します。
     *
     * @param command コマンド
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text runCommandOnClick(@NotNull String command)
    {
        // コマンドが / で始まっていない場合は追加する
        String cmd = command.startsWith("/") ? command: "/" + command;

        return this.clickAction(ClickEvent.Action.RUN_COMMAND, cmd);
    }

    /**
     * クリック時に提案されるコマンドを設定します。
     *
     * @param command コマンド
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text suggestCommandOnClick(@NotNull String command)
    {
        return this.clickAction(ClickEvent.Action.SUGGEST_COMMAND, command);
    }

    /**
     * クリック時に URL にアクセスするように設定します。
     *
     * @param url URL
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text openURLOnClick(@NotNull String url)
    {
        return this.clickAction(ClickEvent.Action.OPEN_URL, url);
    }

    /**
     * クリック時にファイルを開くように設定します。
     *
     * @param file ファイル
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text openFileOnClick(@NotNull String file)
    {
        return this.clickAction(ClickEvent.Action.OPEN_FILE, file);
    }

    /**
     * クリック時にページを変更するように設定します。
     *
     * @param page ページ
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text changePageOnClick(int page)
    {
        return this.clickAction(ClickEvent.Action.CHANGE_PAGE, String.valueOf(page));
    }

    /**
     * クリック時にアクションを実行するように設定します。
     *
     * @param action アクション
     * @param value  値
     * @return このインスタンス
     */
    @Contract("_, _ -> this")
    public Text clickAction(@NotNull ClickEvent.Action action, @NotNull String value)
    {
        this.builder.event(new ClickEvent(action, value));
        return this;
    }

    /**
     * ホバー時にアクションを実行するように設定します。
     *
     * @param action アクション
     * @param value  値
     * @return このインスタンス
     */
    @Contract("_, _ -> this")
    public Text hoverAction(@NotNull HoverEvent.Action action, @NotNull BaseComponent... value)
    {
        this.builder.event(new HoverEvent(action, value));
        return this;
    }

    /**
     * ホバー時にアクションを実行するように設定します。
     *
     * @param action アクション
     * @param value  値
     * @return このインスタンス
     */
    @Contract("_, _ -> this")
    public Text hoverAction(@NotNull HoverEvent.Action action, @NotNull Text value)
    {
        this.builder.event(new HoverEvent(action, value.builder.create()));
        return this;
    }

    /**
     * ホバー時にテキストを表示するように設定します。
     *
     * @param text テキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverText(@NotNull String text)
    {
        return this.hoverText(Text.of(text));
    }

    /**
     * ホバー時にテキストを表示するように設定します。
     *
     * @param text テキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverText(@NotNull Text text)
    {
        return this.hoverAction(HoverEvent.Action.SHOW_TEXT, text);
    }

    /**
     * ホバー時にテキストを表示するように設定します。
     *
     * @param text テキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverText(@NotNull TextComponent text)
    {
        return this.hoverAction(HoverEvent.Action.SHOW_TEXT, text);
    }

    /**
     * ホバー時にテキストを表示するように設定します。
     *
     * @param text テキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverText(@NotNull BaseComponent... text)
    {
        return this.hoverAction(HoverEvent.Action.SHOW_TEXT, text);
    }

    /**
     * ホバー時にテキストを表示するように設定します。
     *
     * @param text テキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverText(@NotNull Text... text)
    {
        return this.hoverText(Text.of(text));
    }

    /**
     * ホバー時にテキストを表示するように設定します。
     *
     * @param text テキスト
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverText(@NotNull TextComponent... text)
    {
        return this.hoverText(Text.of(text));
    }

    /**
     * ホバー時に実績を表示するように設定します。
     *
     * @param achievement 実績
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverAchievement(@NotNull String achievement)
    {
        return this.hoverAction(HoverEvent.Action.SHOW_ACHIEVEMENT, Text.of(achievement));
    }

    /**
     * ホバー時にアイテムを表示するように設定します。
     *
     * @param item アイテム
     * @return このインスタンス
     */
    @Contract("_ -> this")
    public Text hoverItem(@NotNull ItemStack item)
    {
        return this.hoverAction(HoverEvent.Action.SHOW_ITEM, new TextComponent(ItemSerializer.serializeItemStack(item)));
    }

    /**
     * 翻訳可能なテキストを生成します。
     * 注：このメソッドは、LangProvider が設定されていることを前提としています。
     *
     * @param key キー
     * @return 生成されたテキスト
     */
    public Text translatable(@NotNull String key)
    {
        return this.append(LangProvider.get(key));
    }

    /**
     * 翻訳可能なテキストを生成します。
     * 注：このメソッドは、LangProvider が設定されていることを前提としています。
     *
     * @param key  キー
     * @param args 引数
     * @return 生成されたテキスト
     */
    public Text translatable(@NotNull String key, @NotNull MsgArgs args)
    {
        return this.append(LangProvider.get(key, args));
    }

    /**
     * 生成されたテキストを取得します。
     *
     * @return 生成されたテキスト
     */
    public BaseComponent[] asComponents()
    {
        return this.builder.create();
    }

    /**
     * 生成に使用するビルダを取得します。
     *
     * @return 生成に使用するビルダー
     */
    public ComponentBuilder getBuilder()
    {
        return this.builder;
    }

    /**
     * からのテキストを生成します。
     *
     * @return 生成されたテキスト
     */
    public static Text empty()
    {
        return new Text(new ComponentBuilder(""));
    }

    /**
     * テキストを生成します。
     *
     * @param text テキスト
     * @return 生成されたテキスト
     */
    public static Text of(@NotNull String text)
    {
        return new Text(new ComponentBuilder(text));
    }

    /**
     * テキストを生成します。
     *
     * @param text テキスト
     * @return 生成されたテキスト
     */
    public static Text of(@NotNull TextComponent text)
    {
        return new Text(new ComponentBuilder(text));
    }

    /**
     * テキストを生成します。
     *
     * @param texts テキスト
     * @return 生成されたテキスト
     */
    public static Text of(@NotNull TextComponent... texts)
    {
        if (texts.length == 0)
            return empty();
        else
        {
            TextComponent first = texts[0];
            TextComponent[] rest = new TextComponent[texts.length - 1];
            System.arraycopy(texts, 1, rest, 0, rest.length);

            return new Text(new ComponentBuilder(first).append(rest));
        }
    }

    /**
     * テキストを生成します。
     *
     * @param texts テキスト
     * @return 生成されたテキスト
     */
    public static Text of(@NotNull BaseComponent... texts)
    {
        if (texts.length == 0)
            return empty();
        else
        {
            BaseComponent first = texts[0];
            BaseComponent[] rest = new BaseComponent[texts.length - 1];
            System.arraycopy(texts, 1, rest, 0, rest.length);

            return new Text(new ComponentBuilder(first).append(rest));
        }
    }

    /**
     * テキストを生成します。
     *
     * @param texts テキスト
     * @return 生成されたテキスト
     */
    public static Text of(@NotNull Text... texts)
    {
        if (texts.length == 0)
            return empty();
        else
        {
            Text txt = of(texts[0]);
            for (int i = 1; i < texts.length; i++)
                txt.append(texts[i]);

            return txt;
        }
    }

    /**
     * テキストを生成します。
     *
     * @param text テキスト
     * @return 生成されたテキスト
     */
    public static Text of(@NotNull Text text)
    {
        return of(text.builder.create());
    }

    /**
     * 翻訳されたテキストを生成します。
     *
     * @param key キー
     * @return 生成されたテキスト
     */
    public static Text ofTranslatable(@NotNull String key)
    {
        return of(LangProvider.get(key));
    }

    /**
     * 翻訳されたテキストを生成します。
     *
     * @param key  キー
     * @param args 引数
     * @return 生成されたテキスト
     */
    public static Text ofTranslatable(@NotNull String key, @NotNull MsgArgs args)
    {
        return of(LangProvider.get(key, args));
    }

    /**
     * テキストが同じかどうかを判定します。
     *
     * @param content 比較するテキスト
     * @return 同じかどうか
     */
    @Contract(value = "null -> false", pure = true)
    public boolean isSameContent(@Nullable String content)
    {
        if (content == null)
            return false;

        return this.toPlainText().equals(content);
    }

    /**
     * テキストをプレーンテキストに変換します。
     *
     * @return プレーンテキスト
     */
    @NotNull
    public String toPlainText()
    {
        BaseComponent[] components = this.builder.create();
        StringBuilder builder = new StringBuilder();
        for (BaseComponent component : components)
            builder.append(component.toPlainText());

        return builder.toString();
    }
}
