package net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.inputs;

/**
 * y(Yes,はい), N(No,いいえ), C(Cancel,キャンセル) から選択する形式の入力タスクです。
 */
public interface YesNoCancelInput extends YesNoInput
{
    /**
     * キャンセルされたかどうかを取得します。
     *
     * @return キャンセルされたかどうか
     */
    boolean isCancelled();

    /**
     * スレッドをブロックしてキャンセルされたかどうかを取得します。
     *
     * @return キャンセルされたかどうか
     * @throws InterruptedException スレッドが殺された場合
     */
    boolean waitAndIsCancelled() throws InterruptedException;
}
