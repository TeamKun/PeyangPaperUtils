package net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.inputs;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Question;

/**
 * y(Yes,はい), N(No,いいえ) から選択する形式の入力タスクです。
 */
public interface YesNoInput extends Question
{
    /**
     * スレッドをブロックして真偽値として値を取得します。
     *
     * @return 真偽値
     * @throws InterruptedException キャンセルされた場合
     */
    boolean waitAndGetValueAsBoolean() throws InterruptedException;

    /**
     * 真偽値として値を取得します。
     *
     * @return 真偽値
     */
    boolean getValueAsBoolean();
}
