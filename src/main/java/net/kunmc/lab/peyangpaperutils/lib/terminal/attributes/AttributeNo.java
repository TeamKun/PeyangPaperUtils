package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AttributeNo implements QuestionAttribute
{
    @Override
    public @NotNull Map<String, String> getChoices(@NotNull Map<String, String> choices)
    {
        choices.put("n", "いいえ");
        return choices;
    }

    @Override
    public boolean isMatch(@NotNull String input)
    {
        return input.toLowerCase().startsWith("y");
    }

    @Override
    public String getName()
    {
        return "no";
    }

    @Override
    public boolean isValidInput(String input)
    {
        return input.toLowerCase().startsWith("n");
    }

    @Override
    public int getPriority()
    {
        return PRIORITY_NORMAL;
    }
}
