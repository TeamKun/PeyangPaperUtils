package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import net.kunmc.lab.peyangpaperutils.lib.terminal.inputs.BasicStringInputTask;
import net.kunmc.lab.peyangpaperutils.lib.terminal.inputs.ChoiceInputTask;
import net.kunmc.lab.peyangpaperutils.lib.terminal.inputs.YesNoCancelInputTask;
import net.kunmc.lab.peyangpaperutils.lib.terminal.inputs.YesNoInputTask;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Input;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.InputTask;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Terminal;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

/**
 * プレイヤの入力です。
 */
class InputImplement implements Input
{
    @Getter
    private final Terminal terminal;

    InputImplement(Terminal terminal)
    {
        this.terminal = terminal;
    }

    private UUID getUUID()
    {
        if (terminal.getAudience() instanceof Player)
            return ((Player) terminal.getAudience()).getUniqueId();

        return null;
    }

    private InputTask registerInputTask(@NotNull InputTask task)
    {
        PeyangPaperUtils.getInstance().getInputManager().addInputTask(getUUID(), task);
        return task;
    }

    @Override
    public @NotNull InputTask showYNQuestion(@NotNull String question)
    {
        return registerInputTask(new YesNoInputTask(terminal.getAudience(), question, this));
    }

    @Override
    public @NotNull InputTask showYNQuestionCancellable(@NotNull String question)
    {
        return registerInputTask(new YesNoCancelInputTask(terminal.getAudience(), question, this));
    }

    @Override
    public @NotNull InputTask showInputQuestion(@NotNull String question)
    {
        return registerInputTask(new BasicStringInputTask(terminal.getAudience(), question, this));
    }

    @Override
    public @NotNull InputTask showChoiceQuestion(@NotNull String question, String... choices)
    {
        return registerInputTask(new ChoiceInputTask(terminal.getAudience(), question, this, choices));
    }

    @Override
    public @NotNull InputTask showChoiceQuestion(@NotNull String question, @NotNull HashMap<String, String> choices)
    {
        return registerInputTask(new ChoiceInputTask(terminal.getAudience(), question, this, choices));
    }

    @Override
    public void cancelQuestion(InputTask task)
    {
        PeyangPaperUtils.getInstance().getInputManager().cancelInputTask(getUUID(), task);
    }
}

