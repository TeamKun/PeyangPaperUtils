package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class AttributeApplyForAll implements QuestionAttribute
{
    @Override
    public @NotNull LinkedHashMap<String, String> getChoices(@NotNull LinkedHashMap<String, String> choices)
    {
        Map<String, String> result = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : choices.entrySet())
            result.put(
                    entry.getKey() + "_a",
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "すべて " + entry.getValue()
            );

        choices.putAll(result);
        return choices;
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
