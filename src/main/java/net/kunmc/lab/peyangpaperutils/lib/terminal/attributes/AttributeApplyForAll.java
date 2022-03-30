package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AttributeApplyForAll implements QuestionAttribute
{
    @Override
    public @NotNull Map<String, String> getChoices(@NotNull Map<String, String> choices)
    {
        return choices.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue() + "_a"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public boolean isMatch(@NotNull String input)
    {
        return input.toLowerCase().endsWith("_a");
    }

    @Override
    public String getName()
    {
        return "apply_for_all";
    }

    @Override
    public boolean isValidInput(String input)
    {
        return false;
    }

    @Override
    public int getPriority()
    {
        return PRIORITY_LOWEST;
    }
}
