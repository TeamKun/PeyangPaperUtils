package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import net.kunmc.lab.peyangpaperutils.lib.terminal.attributes.AttributeChoice;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

/**
 * プレイヤの入力です。
 */
public class Input
{
    @Getter
    private final Terminal terminal;

    Input(Terminal terminal)
    {
        this.terminal = terminal;
    }

    private UUID getUUID()
    {
        if (terminal.getAudience() instanceof Player)
            return ((Player) terminal.getAudience()).getUniqueId();

        return null;
    }

    private Question registerInputTask(@NotNull Question task)
    {
        PeyangPaperUtils.getInstance().getInputManager().addInputTask(getUUID(), task);
        return task;
    }

    /**
     * Y/N(yes/no)で回答できる質問を表示します。
     *
     * @param question 質問内容
     * @return 回答
     */
    public @NotNull Question showYNQuestion(@NotNull String question, @NotNull QuestionAttribute... attributes)
    {
        QuestionAttribute[] attrs = new QuestionAttribute[attributes.length + 2];
        System.arraycopy(attributes, 0, attrs, 0, attributes.length);
        attrs[attrs.length - 2] = QuestionAttribute.YES;
        attrs[attrs.length - 1] = QuestionAttribute.NO;

        return registerInputTask(new Question(terminal.getAudience(), question, this,
                attrs
        ));
    }

    /**
     * キャンセル可能な、Y/N(yes/no)で回答できる質問を表示します。
     * また、キャンセルされた場合は値に{@code null}が収納されます。
     *
     * @param question 質問内容
     * @return 回答
     */
    public @NotNull Question showYNQuestionCancellable(@NotNull String question, @NotNull QuestionAttribute... attributes)
    {
        QuestionAttribute[] attrs = new QuestionAttribute[attributes.length + 3];
        System.arraycopy(attributes, 0, attrs, 0, attributes.length);
        attrs[attrs.length - 3] = QuestionAttribute.CANCELLABLE;
        attrs[attrs.length - 2] = QuestionAttribute.YES;
        attrs[attrs.length - 1] = QuestionAttribute.NO;

        return registerInputTask(new Question(terminal.getAudience(), question, this, attrs));
    }

    /**
     * 自由入力で回答できる質問を表示します。
     *
     * @param question 質問内容
     * @return 回答
     */
    public @NotNull Question showInputQuestion(@NotNull String question)
    {
        return registerInputTask(new Question(terminal.getAudience(), question, this));
    }

    /**
     * 選択式の質問を表示します。
     *
     * @param question 質問
     * @param choices  選択肢
     * @return 回答
     */
    public @NotNull Question showChoiceQuestion(@NotNull String question, String... choices)
    {
        return registerInputTask(new Question(terminal.getAudience(), question, this, new AttributeChoice(choices)));
    }

    /**
     * 選択式の質問を表示します。
     *
     * @param question 質問
     * @param choices  選択肢
     * @return 回答
     */
    public @NotNull Question showChoiceQuestion(@NotNull String question, @NotNull HashMap<String, String> choices)
    {
        return registerInputTask(new Question(terminal.getAudience(), question, this, new AttributeChoice(choices)));
    }

    /**
     * 質問を表示します。
     *
     * @param question   質問
     * @param attributes 属性
     * @return 質問
     */
    public @NotNull Question showQuestion(@NotNull String question, @NotNull QuestionAttribute... attributes)
    {
        return registerInputTask(new Question(terminal.getAudience(), question, this, attributes));
    }

    /**
     * 質問募集をキャンセルします。
     *
     * @param task 質問のタスク
     */
    public void cancelQuestion(Question task)
    {
        PeyangPaperUtils.getInstance().getInputManager().cancelInputTask(getUUID(), task);
    }
}

