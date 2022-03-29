package net.kunmc.lab.peyangpaperutils.lib.terminal.inputs;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Input;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 標準的なテキストの入力タスクです。
 */
public class BasicStringInputTask extends AbstractInputTask
{
    public BasicStringInputTask(@NotNull Audience target, @NotNull String question, @NotNull Input input)
    {
        super(target, question, input);
    }

    @Override
    public boolean checkValidInput(String input)
    {
        return true;
    }

    @Override
    public @Nullable Map<String, String> getChoices()
    {
        return null;
    }
}
