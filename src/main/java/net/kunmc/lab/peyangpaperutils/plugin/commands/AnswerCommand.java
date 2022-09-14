package net.kunmc.lab.peyangpaperutils.plugin.commands;

import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import net.kunmc.lab.peyangpaperutils.lib.command.CommandBase;
import net.kunmc.lab.peyangpaperutils.lib.terminal.InputManager;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Question;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminal;
import net.kunmc.lab.peyangpaperutils.lib.utils.Utils;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class AnswerCommand extends CommandBase
{
    private static UUID parseUUID(Terminal terminal, String uuidString)
    {
        try
        {
            return UUID.fromString(uuidString);
        }
        catch (IllegalArgumentException e)
        {
            terminal.error("IDの形式が不正です");
            return null;
        }
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        if (indicateArgsLengthInvalid(terminal, args, 3))
            return;

        String questionID = args[0];
        String answerEncoded = args[1];
        String validation = args[2];

        Integer validationInteger;
        UUID questionUUID;
        if ((validationInteger = parseInteger(terminal, validation, -1)) == null ||
                (questionUUID = parseUUID(terminal, questionID)) == null)
            return;

        int exceptedValidation = Utils.generateQuestionAnswerValidation(questionID, answerEncoded);

        if (validationInteger != exceptedValidation)
        {
            terminal.error("Validation failed.");
            return;
        }

        String answerDecoded;
        try
        {
            answerDecoded = URLDecoder.decode(answerEncoded, StandardCharsets.UTF_8.name());
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);  // Never happen
        }

        InputManager manager = PeyangPaperUtils.getInstance().getInputManager();

        Question question = manager.getQuestionByID(questionUUID);

        if (question == null)
        {
            terminal.error("質問が既に回答されているか、存在しません。");
            return;
        }

        manager.doAnswer(question, answerDecoded);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        return null;
    }

    @Override
    public @Nullable String getPermission()
    {
        return null;
    }

    @Override
    public TextComponent getHelpOneLine()
    {
        return of("質問の答えを入力します。内部で使用されるコマンドです。");
    }

    @Override
    public String[] getArguments()
    {
        return new String[]{
                required("questionID", "string"),
                required("answer", "enum|any"),
                required("validation", "int")
        };
    }
}
