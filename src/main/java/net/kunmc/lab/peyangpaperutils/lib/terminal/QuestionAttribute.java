package net.kunmc.lab.peyangpaperutils.lib.terminal;

import net.kunmc.lab.peyangpaperutils.lib.terminal.attributes.AttributeApplyForAll;
import net.kunmc.lab.peyangpaperutils.lib.terminal.attributes.AttributeCancellable;
import net.kunmc.lab.peyangpaperutils.lib.terminal.attributes.AttributeNo;
import net.kunmc.lab.peyangpaperutils.lib.terminal.attributes.AttributeOK;
import net.kunmc.lab.peyangpaperutils.lib.terminal.attributes.AttributeYes;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

/**
 * 質問の属性を表します。
 */
public interface QuestionAttribute
{
    /**
     * 最も低い優先度を表します。
     *
     * @see #getPriority()
     */
    int PRIORITY_LOWEST = -5000;
    /**
     * 低い優先度を表します。
     *
     * @see #getPriority()
     */
    int PRIORITY_LOW = -1000;
    /**
     * 普通の優先度を表します。
     *
     * @see #getPriority()
     */
    int PRIORITY_NORMAL = 0;
    /**
     * 高い優先度を表します。
     *
     * @see #getPriority()
     */
    int PRIORITY_HIGH = 1000;
    /**
     * 最も高い優先度を表します。
     *
     * @see #getPriority()
     */
    int PRIORITY_HIGHEST = 5000;

    /**
     * はい/yで回答できる質問です。
     */
    QuestionAttribute YES = new AttributeYes();
    /**
     * いいえ/nで回答できる質問です。
     */
    QuestionAttribute NO = new AttributeNo();
    /**
     * OK/okで回答できる質問です。
     */
    QuestionAttribute OK = new AttributeOK();
    /**
     * キャンセル可能な質問です。
     */
    QuestionAttribute CANCELLABLE = new AttributeCancellable();
    /**
     * すべてにおいて、この選択を適用するという意を表します。
     * ほかの属性と適用することを前提としています。
     * 例えば、 {@link QuestionAttribute#YES} と併用した場合、新たに "すべて はい" の質問が追加されます。
     */
    QuestionAttribute APPLY_FOR_ALL = new AttributeApplyForAll();

    /**
     * 質問で有効な回答の一覧を取得します。
     * 値に {@code null} を含む場合、自由入力となります。
     *
     * @param choices 既存の有効な回答の一覧
     * @return 回答一覧
     */
    @NotNull LinkedHashMap<String, String> getChoices(@NotNull LinkedHashMap<String, String> choices);

    /**
     * 属性に合っているかどうかを判定します。
     *
     * @param input 回答
     * @return 回答
     */
    boolean isMatch(@NotNull String input);

    /**
     * 属性の名前返します。
     *
     * @return 属性名
     */
    String getName();

    /**
     * 回答が正しいかどうかをテストします。
     *
     * @param input 回答
     * @return 回答が正しい場合 {@code true}
     */
    boolean isValidInput(String input);

    /**
     * この属性の優先度を返します。
     *
     * @return 優先度
     */
    int getPriority();

}
