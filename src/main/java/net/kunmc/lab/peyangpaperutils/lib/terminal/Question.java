package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private final List<QuestionAttribute> attributes;
    private final LinkedHashMap<String, String> choices;
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
        this.attributes = Arrays.asList(attributes);
        this.attributes.sort(Comparator.comparingInt(QuestionAttribute::getPriority).reversed());


        this.choices = buildChoices(attributes);

        this.locker = new Object();
    }

    private static LinkedHashMap<String, String> buildChoices(QuestionAttribute[] attributes)
    {
        LinkedHashMap<String, String> choices = new LinkedHashMap<>();
        for (QuestionAttribute attribute : attributes)
            choices.putAll(attribute.getChoices(choices));
        return choices;
    }

    private QuestionAttribute[] detectValidAttributes(String input)
    {
        return this.attributes.stream()
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
        if (this.valuePresent || this.attributes.isEmpty())
            return true;

        boolean result = this.attributes.stream()
                .anyMatch(attribute -> attribute.isValidInput(input));

        if (!result)
        {
            this.choices.clear();
            this.choices.putAll(buildChoices(this.attributes.toArray(new QuestionAttribute[0])));
        }

        return result;
    }

    private void printSeparator(Terminal terminal)
    {
        terminal.writeLine(ChatColor.BLUE + ChatColor.STRIKETHROUGH.toString() +
                "================================================================================");
    }

    private void printChoices(Terminal terminal, Map<String, String> choices)
    {
        choices.forEach((value, text) -> terminal.write(
                Component.text(ChatColor.YELLOW + value +
                                " - " + ChatColor.GREEN + text)
                        .clickEvent(ClickEvent.runCommand(value))
                        .hoverEvent(HoverEvent.showText(
                                Component.text(ChatColor.YELLOW + "クリックして送信： " + text)))
        ));
    }

    /**
     * 質問を出力します。
     * 実装時は {@link Terminal#info(String, Object...)} を使用することが望ましいです。
     */
    public void printQuestion()
    {
        Terminal terminal = input.getTerminal();

        printSeparator(terminal);
        terminal.writeLine(ChatColor.GREEN + StringUtils.repeat(" ", 40 - (question.length() / 2)) + question);

        if (this.attributes.isEmpty())
        {
            terminal.writeLine("        ---- " + ChatColor.GREEN +
                    "回答をチャットまたはコンソールに入力してください。" + ChatColor.WHITE + " ----");
            printSeparator(terminal);
            return;
        }
        else
            terminal.writeLine("        ---- " +
                    ChatColor.GREEN + "回答を入力" +
                    (terminal.isPlayer() ? "するか、回答をクリック": "") + "してください。" + ChatColor.WHITE + " ----");

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
                    .anyMatch(validAttribute -> validAttribute.getName().equals(attribute.getName()));
        }
    }
}
