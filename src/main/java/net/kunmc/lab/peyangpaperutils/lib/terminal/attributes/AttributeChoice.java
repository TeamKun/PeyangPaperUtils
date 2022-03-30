package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AttributeChoice implements QuestionAttribute
{
    private final Map<String, String> choices;

    public AttributeChoice(Map<String, String> choices)
    {
        this.choices = choices;
    }

    public AttributeChoice(String... choices)
    {
        this.choices = new HashMap<>(choices.length);
        for (String choice : choices)
            this.choices.put(choice.toLowerCase(), choice);
    }

    @Override
    public @NotNull Map<String, String> getChoices(@NotNull Map<String, String> choices)
    {
        choices.putAll(this.choices);
        return choices;
    }

    @Override
    public boolean isMatch(@NotNull String input)
    {
        for (String choice : choices.values())
            if (choice.startsWith(input.toLowerCase()))
                return true;

        return false;
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
