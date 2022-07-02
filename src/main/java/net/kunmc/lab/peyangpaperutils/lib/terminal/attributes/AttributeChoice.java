package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class AttributeChoice implements QuestionAttribute
{
    private final Map<String, String> choices;

    public AttributeChoice(Map<String, String> choices)
    {
        this.choices = new LinkedHashMap<>(choices);
    }

    public AttributeChoice(String... choices)
    {
        int count = 0;
        this.choices = new LinkedHashMap<>(choices.length);
        for (String choice : choices)
            this.choices.put(String.valueOf(++count), choice);
    }

    @Override
    public @NotNull LinkedHashMap<String, String> getChoices(@NotNull LinkedHashMap<String, String> choices)
    {
        choices.putAll(this.choices);
        return choices;
    }

    @Override
    public boolean isMatch(@NotNull String input)
    {
        return this.choices.entrySet().stream().parallel()
                .anyMatch(entry -> entry.getKey().equalsIgnoreCase(input));
    }

    @Override
    public String getName()
    {
        return "choice";
    }

    @Override
    public boolean isValidInput(String input)
    {
        return isMatch(input);
    }

    @Override
    public int getPriority()
    {
        return PRIORITY_NORMAL;
    }
}
