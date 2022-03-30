package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class AttributeNo implements QuestionAttribute
{
    @Override
    public @NotNull LinkedHashMap<String, String> getChoices(@NotNull LinkedHashMap<String, String> choices)
    {
        choices.put("n", ChatColor.RED + ChatColor.BOLD.toString() + "いいえ");
        return choices;
    }

    @Override
    public boolean isMatch(@NotNull String input)
    {
        return input.toLowerCase().startsWith("n");
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
