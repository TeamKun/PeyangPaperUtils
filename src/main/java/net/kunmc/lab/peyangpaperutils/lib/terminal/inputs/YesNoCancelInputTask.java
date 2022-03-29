package net.kunmc.lab.peyangpaperutils.lib.terminal.inputs;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Input;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.inputs.YesNoCancelInput;
import net.kyori.adventure.audience.Audience;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class YesNoCancelInputTask extends YesNoInputTask implements YesNoCancelInput
{
    private static final String[] CANCEL = new String[]{"c", "cancel", "キャンセル"};

    public YesNoCancelInputTask(@NotNull Audience target, @NotNull String question, @NotNull Input input)
    {
        super(target, question, input);
    }

    @Override
    public boolean checkValidInput(String input)
    {
        return super.checkValidInput(input) || ArrayUtils.contains(CANCEL, input);
    }

    @Override
    public boolean isCancelled()
    {
        return ArrayUtils.contains(CANCEL, getValue());
    }

    @Override
    public boolean waitAndIsCancelled() throws InterruptedException
    {
        waitForAnswer();
        return isCancelled();
    }

    @Override
    public Map<String, String> getChoices()
    {
        Map<String, String> choices = super.getChoices();
        choices.put("c", "キャンセル");
        return choices;
    }
}
