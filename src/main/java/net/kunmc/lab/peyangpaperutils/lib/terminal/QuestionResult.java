package net.kunmc.lab.peyangpaperutils.lib.terminal;

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
     * <p>
     * 使用法：
     * <pre>
     *     if (result.test(QuestionAttribute.YES)) {
     *         if (result.test(QuestionAttribute.APPLY_FOR_ALL)) {
     *             System.out.println("Yes for all!");
     *         } else {
     *             System.out.println("Yes!");
     *         }
     *     }
     * </pre>
     *
     * @see QuestionAttribute
     * @param attribute 属性
     * @return 属性が回答に含まれている場合はtrue
     */
    boolean test(QuestionAttribute attribute);
}
