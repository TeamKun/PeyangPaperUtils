package net.kunmc.lab.peyangpaperutils.lib.terminal.inputs;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Input;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.inputs.YesNoInput;
import net.kyori.adventure.audience.Audience;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class YesNoInputTask extends AbstractQuestion implements YesNoInput
{
    protected static final String[] YES = new String[]{"y", "yes", "はい"};
    protected static final String[] NO = new String[]{"n", "no", "いいえ"};

    public YesNoInputTask(@NotNull Audience target, @NotNull String question, @NotNull Input input)
    {
        super(target, question, input);
    }

    @Override
    public boolean checkValidInput(String input)
    {
        return ArrayUtils.contains(YES, input.toLowerCase()) ||
                ArrayUtils.contains(NO, input.toLowerCase());
    }

    @Override
    public Map<String, String> getChoices()
    {
        return new HashMap<String, String>()
        {{
            put("yes", "はい");
            put("no", "いいえ");
        }};
    }

    @Override
    public boolean waitAndGetValueAsBoolean() throws InterruptedException
    {
        return ArrayUtils.contains(YES, waitAndGetValue().toLowerCase());
    }

    @Override
    public boolean getValueAsBoolean()
    {
        return ArrayUtils.contains(YES, getValue().toLowerCase());
    }
}
