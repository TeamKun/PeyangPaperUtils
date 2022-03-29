package net.kunmc.lab.peyangpaperutils.lib.terminal.inputs;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Input;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 候補から選択する形式の入力タスクです。
 */
public class ChoiceInputTask extends AbstractQuestion
{
    private final Map<String, String> choices;

    public ChoiceInputTask(@NotNull Audience target, @NotNull String question, @NotNull Input input, @NotNull Map<String, String> choices)
    {
        super(target, question, input);
        this.choices = choices;
    }

    public ChoiceInputTask(@NotNull Audience target, @NotNull String question, @NotNull Input input, String... choices)
    {
        super(target, question, input);

        Map<String, String> choiceMap = new HashMap<>(choices.length);
        for (String choice : choices)
            choiceMap.put(choice, choice);

        this.choices = choiceMap;
    }

    @Override
    public @Nullable String getRawValue()
    {
        String rawValue = super.getRawValue();
        if (rawValue == null)
            return null;

        if (choices.containsValue(rawValue))
            return choices.entrySet().stream().parallel()
                    .filter(entry -> Objects.equals(entry.getValue(), rawValue))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(null);

        return rawValue;
    }

    @Override
    public boolean checkValidInput(String input)
    {
        return choices.containsKey(input) || choices.containsValue(input);
    }

    @Override
    public Map<String, String> getChoices()
    {
        return choices;
    }
}
