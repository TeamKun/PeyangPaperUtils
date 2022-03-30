package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class AttributeCancellable implements QuestionAttribute
{
    @Override
    public @NotNull LinkedHashMap<String, String> getChoices(@NotNull LinkedHashMap<String, String> choices)
    {
        choices.put("c", ChatColor.GRAY + ChatColor.BOLD.toString() + "キャンセル");
        return choices;
    }

    @Override
    public boolean isMatch(@NotNull String input)
    {
        return input.toLowerCase().startsWith("c");
    }

    @Override
    public String getName()
    {
        return "cancel";
    }

    @Override
    public boolean isValidInput(String input)
    {
        return input.toLowerCase().startsWith("c");
    }

    @Override
    public int getPriority()
    {
        return PRIORITY_NORMAL;
    }
}
