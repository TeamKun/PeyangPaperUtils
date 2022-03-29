package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Input;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Question;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.QuestionAttribute;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.QuestionResult;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Terminal;
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
 * 入力のタスクです。
 */
@Getter
@EqualsAndHashCode
public class QuestionImplement implements Question
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

    public QuestionImplement(@NotNull Audience target, @NotNull String question, @NotNull Input input, QuestionAttribute... attributes)
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

    @Override
    public @NotNull QuestionResult waitAndGetResult() throws InterruptedException
    {
        this.waitForAnswer();
        return this.result;
    }

    @Override
    public @Nullable QuestionResult getAnswer()
    {
        return this.result;
    }


    @Override
    public void waitForAnswer() throws InterruptedException
    {
        if (valuePresent)
            return;

        synchronized (locker)
        {
            locker.wait();
        }
    }

    @Override
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

    @Override
    public boolean isResultAvailable()
    {
        return valuePresent;
    }

    @Override
    public void cancel()
    {
        if (valuePresent)
            return;
        synchronized (locker)
        {
            locker.notifyAll();
        }
    }

    @Override
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

    @Override
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
