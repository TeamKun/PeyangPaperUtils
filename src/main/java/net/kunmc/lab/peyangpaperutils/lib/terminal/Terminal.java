package net.kunmc.lab.peyangpaperutils.lib.terminal;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Input/Outputのターミナルを定義します。
 */
public interface Terminal
{
    /**
     * 通知のデフォルトの表示時間です。
     */
    int DEFAULT_NOTIFICATION_TIME = 3000;

    /**
     * ターミナルの使用者を取得します。
     *
     * @return {@link Audience}
     */
    @NotNull Audience getAudience();

    /**
     * 情報メッセージを出力します。
     *
     * @param message 出力するメッセージ
     * @param args    引数
     * @see String#format(String, Object...)
     */
    void info(@NotNull String message, Object... args);

    /**
     * エラーメッセージを出力します。
     *
     * @param message 出力するメッセージ
     * @param args    引数
     * @see String#format(String, Object...)
     */
    void error(@NotNull String message, Object... args);

    /**
     * 成功メッセージを出力します。
     *
     * @param message 出力するメッセージ
     * @param args    引数
     * @see String#format(String, Object...)
     */
    void success(@NotNull String message, Object... args);

    /**
     * 警告メッセージを出力します。
     *
     * @param message 出力するメッセージ
     * @param args    引数
     * @see String#format(String, Object...)
     */
    void warn(@NotNull String message, Object... args);

    /**
     * メッセージを改行して出力します。
     *
     * @param message 出力するメッセージ
     */
    void writeLine(@NotNull String message);

    /**
     * コンポーネントを出力します。
     *
     * @param component 出力するコンポーネント
     */
    void write(@NotNull Component component);

    /**
     * プレイヤかどうかを返します。
     * @return プレイヤかどうか
     */
    boolean isPlayer();

    /**
     * プログレスバーを新規作成します。
     *
     * @param name プログレスバーの名前
     * @return プログレスバー
     * @throws IllegalStateException プログレスバーが既に存在する場合
     */
    @NotNull Progressbar createProgressbar(@NotNull String name) throws IllegalStateException;

    /**
     * 作成したプログレスバーを削除します。
     *
     * @param name 削除するプログレスバーの名前
     * @return 削除できたかどうか
     */
    boolean removeProgressbar(@NotNull String name);

    /**
     * プログレスバーを取得します。
     *
     * @param name 取得するプログレスバーの名前
     * @return プログレスバー
     */
    @Nullable Progressbar getProgressbar(@NotNull String name);

    /**
     * 通知を表示します。
     *
     * @param title    タイトル
     * @param message  メッセージ
     * @param showTime 表示時間(ミリ秒)
     */
    void showNotification(@NotNull String title, @NotNull String message, int showTime);

    /**
     * 通知を表示します。
     * 表示時間はデフォルトでは {@link #DEFAULT_NOTIFICATION_TIME} ミリ秒です。
     *
     * @param title   タイトル
     * @param message メッセージ
     */
    default void showNotification(@NotNull String title, @NotNull String message)
    {
        showNotification(title, message, DEFAULT_NOTIFICATION_TIME);
    }

    /**
     * 通知を表示します。
     *
     * @param message メッセージ
     */
    default void showNotification(@NotNull String message)
    {
        showNotification("", message);
    }

    /**
     * 通知を非表示にします。
     * 通知時間中でも強制的に非表示にされます。
     */
    void clearNotification();

    /**
     * 静かなターミナルかどうかを返します。
     *
     * @return 静か
     */
    default boolean isQuiet()
    {
        return false;
    }

    /**
     * 入力用のインタフェースを取得します。
     *
     * @return 入力用のインタフェース
     */
    Input getInput();

    /**
     * 静かなモードにします。
     * このモードを有効にしたターミナルでは、{@link #info(String message, Object... args)} をはじめとする出力系メソッドは文字列を出力しないようにしてください。
     *
     * @return IOInterfaceのコピー
     */
    default Terminal quiet()
    {
        return new Terminal()
        {
            @Override
            public @NotNull Audience getAudience()
            {
                return Terminal.this.getAudience();
            }

            @Override
            public void info(@NotNull String message, Object... args)
            {

            }

            @Override
            public void error(@NotNull String message, Object... args)
            {

            }

            @Override
            public void success(@NotNull String message, Object... args)
            {

            }

            @Override
            public void warn(@NotNull String message, Object... args)
            {

            }

            @Override
            public void writeLine(@NotNull String message)
            {

            }

            @Override
            public void write(@NotNull Component component)
            {

            }

            @Override
            public boolean isPlayer()
            {
                return Terminal.this.isPlayer();
            }

            @Override
            public @NotNull Progressbar createProgressbar(@NotNull String name) throws IllegalStateException
            {
                return Terminal.this.createProgressbar(name);
            }

            @Override
            public boolean removeProgressbar(@NotNull String name)
            {
                return Terminal.this.removeProgressbar(name);
            }

            @Override
            public @Nullable Progressbar getProgressbar(@NotNull String name)
            {
                return Terminal.this.getProgressbar(name);
            }

            @Override
            public void showNotification(@NotNull String title, @NotNull String message, int showTime)
            {
            }

            @Override
            public void showNotification(@NotNull String message)
            {
            }

            @Override
            public Input getInput()
            {
                return Terminal.this.getInput();
            }

            @Override
            public void clearNotification()
            {
            }

            @Override
            public boolean isQuiet()
            {
                return true;
            }

        };
    }
}

