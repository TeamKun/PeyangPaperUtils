package net.kunmc.lab.peyangpaperutils.lang;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kunmc.lab.peyangpaperutils.lib.utils.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * シングルトンな言語プロバイダです。
 * {@link #init(Plugin, Path)} で初期化します。第ニ引数には言語ファイルディレクtロイの相対パスを指定します(例： {@code Paths.get("lang")}。
 * {@link #init(Plugin)} で初期化すると、{@code lang/} フォルダを使用します。
 * <br><br>
 * 言語ファイルのフォーマットは, Java の {@link Properties} と同じで, {@code key=value} という形式で記述します。
 * また、{@code key} には {@code .} を含めることができます。
 * <br><br>
 * 変数を使用する場合は、{@code %%test%%} のように記述します。
 * 同じ言語ファイル内で記述されているエントリも置き換えられます。例：<br>
 * <pre>
 *     path.to.file=ファイルへのパス
 *     path.to.file.desc=%%path.to.file%% にファイルを保存します。
 * </pre><br>
 * Java コード内で変数を置換する場合は, {@link #get(String, MsgArgs)} を使用します。
 * <br><br>
 * 色や装飾コードは, 英語名を {@code %%} で囲んで記述します。
 * 例： {@code %%red%%} は赤色になり, {@code %%bold%%} は太字になります。
 */
public class LangProvider
{
    private static LangProvider INSTANCE;

    private final LangLoader loader;
    @Getter(AccessLevel.PACKAGE)
    private final Plugin plugin;
    @Getter
    @Setter
    private String currentLanguage;
    private Properties currentLanguageMessages;

    private LangProvider(Plugin plugin, Path langFilePath) throws IOException
    {
        this.plugin = plugin;
        this.loader = new LangLoader(this, langFilePath);

        LangProvider.INSTANCE = this;
    }

    /**
     * 言語プロバイダを初期化します。
     *
     * @param plugin       プラグイン
     * @param langFilePath 言語ファイルの相対パスです。
     * @throws IOException           書記言語を読み取れなかった場合にスローされます。
     * @throws IllegalStateException 既に初期化されている場合にスローされます。
     */
    public static void init(Plugin plugin, Path langFilePath) throws IOException
    {
        if (INSTANCE != null)
            throw new IllegalStateException("LanguageProvider has already been initialized.");
        new LangProvider(plugin, langFilePath);
    }

    /**
     * 言語プロバイダを初期化します。
     * {@code lang/} フォルダを使用します。
     *
     * @param plugin プラグイン
     * @throws IOException           書記言語を読み取れなかった場合にスローされます。
     * @throws IllegalStateException 既に初期化されている場合にスローされます。
     */
    public static void init(Plugin plugin) throws IOException
    {
        init(plugin, Paths.get("lang"));
    }

    /**
     * 言語を設定します。
     *
     * @param languageName 言語名
     * @throws IOException 言語ファイルを読み取れなかった場合にスローされます。
     */
    public static void setLanguage(String languageName) throws IOException
    {
        LangProvider provider = INSTANCE;
        provider.currentLanguage = languageName;
        provider.currentLanguageMessages = INSTANCE.loader.loadLanguage(languageName);

        buildCache();
    }

    /**
     * 言語キャッシュを構築します。
     */
    public static void buildCache()
    {
        Map<Object, Object> current = INSTANCE.currentLanguageMessages;
        Properties cache = new Properties();
        current.keySet().stream().parallel()
                .map(Object::toString)
                .map(key -> Pair.of(key, get(key)))
                .forEach(pair -> cache.setProperty(pair.getLeft(), pair.getRight()));

        INSTANCE.currentLanguageMessages = cache;
    }

    /**
     * メッセージを取得します。
     *
     * @param key  キー
     * @param args 引数
     * @return メッセージ
     */
    public static String get(String key, MsgArgs args)
    {
        String msg = INSTANCE.currentLanguageMessages.getProperty(key);
        if (msg == null)
            return "%%" + key + "%%";
        return args.format(msg);
    }

    /**
     * メッセージを取得します。
     *
     * @param key キー
     * @return メッセージ
     */
    public static String get(String key)
    {
        return get(key, MsgArgs.ofEmpty());
    }

    /**
     * メッセージを {@link Component} として取得します。
     *
     * @param key  キー
     * @param args 引数
     * @return メッセージ
     */
    public static Component getComponent(String key, MsgArgs args)
    {
        return Component.text(get(key, args));
    }

    /**
     * メッセージを {@link Component} として取得します。
     *
     * @param key キー
     * @return メッセージ
     */
    public static Component getComponent(String key)
    {
        return getComponent(key, MsgArgs.ofEmpty());
    }
}
