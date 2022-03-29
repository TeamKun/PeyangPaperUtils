package net.kunmc.lab.peyangpaperutils.lib.terminal;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 質問の属性を表します。
 */
public interface QuestionAttribute
{
    /**
     * 質問で有効な回答の一覧を取得します。
     * 値に {@code null} を含む場合、自由入力となります。
     *
     * @param choices 既存の有効な回答の一覧
     * @return 回答一覧
     */
    @NotNull Map<String, String> getChoices(@NotNull Map<String, String> choices);

    /**
     * 属性に合っているかどうかを判定します。
     *
     * @return 回答
     */
    boolean isMatch(@NotNull String input);

    /**
     * おなじ属性かどうかを判定します。
     *
     * @param other 判定対象
     * @return 同一属性の場合 {@code true}
     */
    boolean equals(QuestionAttribute other);

    /**
     * 回答が正しいかどうかをテストします。
     * 選択式などで、根幹に関わるような不正な入力が合った場合のみ {@code false} を返します。
     *
     * @param input 回答
     * @return 回答が正しい場合 {@code true}
     */
    boolean isValidInput(String input);
}
