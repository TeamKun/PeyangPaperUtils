package net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * 入力(質問)のタスクです。
 */
public interface Question
{
    /**
     * ブロッキングして結果を取得します。
     *
     * @return 入力値
     * @throws InterruptedException 入力値が取得できなかった場合/スレッドが殺された場合
     */
    @NotNull QuestionResult waitAndGetResult() throws InterruptedException;

    /**
     * ブロッキングせずに回答を取得します。
     *
     * @return 回答
     */
    @Nullable QuestionResult getAnswer();

    /**
     * 回答を得られるまでブロッキングします。
     *
     * @throws InterruptedException スレッドが殺された場合
     */
    void waitForAnswer() throws InterruptedException;

    /**
     * 入力値を設定します。
     * ブロッキングされたスレッドは解放されます。
     *
     * @param value 入力値
     */
    void setAnswer(@NotNull String value);

    /**
     * 回答が取得できるかどうかを返します。
     *
     * @return 回答が取得できるかどうか
     */
    boolean isResultAvailable();

    /**
     * 質問をキャンセルしまします。
     */
    default void cancel()
    {
        this.getInput().cancelQuestion(this);
    }

    /**
     * 質問のUUIDを返します。
     *
     * @return 質問のUUID
     */
    @NotNull UUID getUuid();

    /**
     * 質問のターゲットを返します。
     *
     * @return 質問のターゲット
     */
    @Nullable UUID getTarget();

    /**
     * 質問で有効な入力値かどうかを返します。
     *
     * @param input 入力値
     * @return 質問で有効な入力値かどうか
     */
    boolean checkValidInput(String input);

    /**
     * このタスクを作成したInputを返します。
     *
     * @return このタスクを作成したInput
     */
    @NotNull Input getInput();

    /**
     * 質問を出力します。
     * 実装時は {@link Terminal#info(String, Object...)} を使用することが望ましいです。
     * また、{@link Question#getChoices()} が内部で呼び出されます。
     */
    void printQuestion();

    /**
     * 質問の選択肢を取得します。
     * Mapの鍵はクリック時に自動入力される値で、値は表示される値です。
     * デフォルトでは {@link org.bukkit.ChatColor#GREEN} 色で表示されます。
     *
     * @return 質問の選択肢
     */
    Map<String, String> getChoices();

    /**
     * 質問の属性を取得します。
     *
     * @return 質問の属性
     */
    QuestionAttribute[] getAttributes();
}
