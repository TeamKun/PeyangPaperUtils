package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 質問のタスクです。
 */
@Getter
@EqualsAndHashCode
public class Question
{
    private final UUID uuid;
    private final UUID target;
    private final Input input;
    private final String question;
    private final QuestionAttribute[] attributes;
    private final Map<String, String> choices;
    private String value;

    @Getter(AccessLevel.PRIVATE)
    private final Object locker;
    @Getter(AccessLevel.PRIVATE)
    private boolean valuePresent;
    @Getter(AccessLevel.PRIVATE)
    private QuestionResult result;

    public Question(@NotNull Audience target, @NotNull String question, @NotNull Input input, QuestionAttribute... attributes)
    {
        this.uuid = UUID.randomUUID();
        this.target = target instanceof Player ? ((Player) target).getUniqueId(): null;
        this.input = input;
        this.question = question;
        this.attributes = attributes;

        this.choices = buildChoices(attributes);

        this.locker = new Object();
    }

    private static Map<String, String> buildChoices(QuestionAttribute[] attributes)
    {
        HashMap<String, String> choices = new HashMap<>();
        for (QuestionAttribute attribute : attributes)
            choices.putAll(attribute.getChoices(choices));
        return choices;
    }

    private QuestionAttribute[] detectValidAttributes(String input)
    {
        return Arrays.stream(this.attributes)
                .filter(attribute -> attribute.isMatch(input))
                .toArray(QuestionAttribute[]::new);
    }

    /**
     * ブロッキングして結果を取得します。
     *
     * @return 入力値
     * @throws InterruptedException 入力値が取得できなかった場合/スレッドが殺された場合
     */
    public @NotNull QuestionResult waitAndGetResult() throws InterruptedException
    {
        this.waitForAnswer();
        return this.result;
    }

    /**
     * ブロッキングせずに回答を取得します。
     *
     * @return 回答
     */
    public @Nullable QuestionResult getAnswer()
    {
        return this.result;
    }

    /**
     * 入力値を設定します。
     * ブロッキングされたスレッドは解放されます。
     *
     * @param value 入力値
     */
    public void setAnswer(@NotNull String value)
    {
        if (valuePresent)
            throw new IllegalStateException("value is already set");

        this.result = new QuestionResultIImplement(value, detectValidAttributes(value));
        this.value = value;
        this.valuePresent = true;
        synchronized (locker)
        {
            locker.notifyAll();
        }
    }

    /**
     * 回答を得られるまでブロッキングします。
     *
     * @throws InterruptedException スレッドが殺された場合
     */
    public void waitForAnswer() throws InterruptedException
    {
        if (valuePresent)
            return;

        synchronized (locker)
        {
            locker.wait();
        }
    }

    /**
     * 回答が取得できるかどうかを返します。
     *
     * @return 回答が取得できるかどうか
     */
    public boolean isResultAvailable()
    {
        return valuePresent;
    }

    /**
     * 質問をキャンセルしまします。
     */
    public void cancel()
    {
        if (valuePresent)
            return;
        synchronized (locker)
        {
            locker.notifyAll();
        }
    }

    /**
     * 質問で有効な入力値かどうかを返します。
     *
     * @param input 入力値
     * @return 質問で有効な入力値かどうか
     */
    public boolean checkValidInput(String input)
    {
        return Arrays.stream(attributes)
                .allMatch(attribute -> attribute.isValidInput(input));
    }

    private void printSeparator(Terminal terminal)
    {
        terminal.writeLine(ChatColor.BLUE + ChatColor.STRIKETHROUGH.toString() + "================================================");
    }

    private void printChoices(Terminal terminal, Map<String, String> choices)
    {
        AtomicInteger index = new AtomicInteger(1);
        choices.forEach((value, text) -> terminal.write(
                Component.text(ChatColor.YELLOW.toString() + index.getAndIncrement() +
                                " " + ChatColor.GREEN + text + "( " + value + " )")
                        .clickEvent(ClickEvent.suggestCommand(value))
                        .hoverEvent(HoverEvent.showText(
                                Component.text(ChatColor.YELLOW + "クリックして補完！")))
        ));
    }

    /**
     * 質問を出力します。
     * 実装時は {@link Terminal#info(String, Object...)} を使用することが望ましいです。
     * また、{@link Question#getChoices()} が内部で呼び出されます。
     */
    public void printQuestion()
    {
        Terminal terminal = input.getTerminal();

        printSeparator(terminal);
        terminal.writeLine(ChatColor.GREEN + "    " + question);

        /*if (this instanceof BasicStringInputTask)
        {
            terminal.writeLine("    " + ChatColor.GREEN + "回答をチャットまたはコンソールに入力してください。");
            printSeparator(terminal);
            return;
        }*/

        Map<String, String> choices = getChoices();
        if (choices != null)
            printChoices(terminal, choices);

        printSeparator(terminal);
    }

    @Value
    private static class QuestionResultIImplement implements QuestionResult
    {
        String rawAnswer;
        QuestionAttribute[] validAttributes;

        @Override
        public boolean test(QuestionAttribute attribute)
        {
            return Arrays.stream(validAttributes)
                    .anyMatch(validAttribute -> validAttribute.equals(attribute));
        }
    }

}
