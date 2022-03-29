package net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * 質問の回答を表します。
 */
public interface QuestionResult
{
    /**
     * 回答を取得します。
     *
     * @return 回答
     */
    @NotNull String getRawAnswer();

    /**
     * 属性が回答に含まれているかどうかをテストします。
     *
     * @param attribute 属性
     * @return 属性が回答に含まれている場合はtrue
     */
    boolean test(QuestionAttribute attribute);
}
