package net.kunmc.lab.peyangpaperutils.lib.terminal.attributes;

import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AttributeOK implements QuestionAttribute
{
    @Override
    public @NotNull Map<String, String> getChoices(@NotNull Map<String, String> choices)
    {
        choices.put("ok", ChatColor.GREEN + ChatColor.BOLD.toString() + "OK");
        return choices;
    }

    @Override
    public boolean isMatch(@NotNull String input)
    {
        return input.toLowerCase().startsWith("ok");
    }

    @Override
    public String getName()
    {
        return "ok";
    }

    @Override
    public boolean isValidInput(String input)
    {
        return input.toLowerCase().startsWith("ok");
    }

    @Override
    public int getPriority()
    {
        return PRIORITY_NORMAL;
    }
}
