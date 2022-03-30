package net.kunmc.lab.peyangpaperutils.plugin.commands.debug;

import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import net.kunmc.lab.peyangpaperutils.lib.command.CommandBase;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Question;
import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionAttribute;
import net.kunmc.lab.peyangpaperutils.lib.terminal.QuestionResult;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminal;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class QuestionDebugCommand extends CommandBase
{
    private static final String[] QUESTION_TYPES = {
            "yn",
            "ync",
            "input"
    };

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        if (indicateArgsLengthInvalid(terminal, args, 2, 2))
            return;

        String type = args[0];
        String question = args[1];

        Question in;

        switch (type)
        {
            case "yn":
                in = terminal.getInput().showYNQuestion(question, QuestionAttribute.APPLY_FOR_ALL, QuestionAttribute.CANCELLABLE);
                break;
            case "ync":
                in = terminal.getInput().showYNQuestionCancellable(question, QuestionAttribute.APPLY_FOR_ALL, QuestionAttribute.CANCELLABLE);
                break;
            case "input":
                in = terminal.getInput().showInputQuestion(question);
                break;
            default:
                terminal.error("無効な値です：" + type);
                return;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    QuestionResult result = in.waitAndGetResult();
                    terminal.success("取得した値：%s", result.getRawAnswer());

                    if (result.test(QuestionAttribute.OK))
                        terminal.success("OK");
                    if (result.test(QuestionAttribute.CANCELLABLE))
                        terminal.success("キャンセル");
                    if (result.test(QuestionAttribute.NO))
                        terminal.success("NO");
                    if (result.test(QuestionAttribute.YES))
                        terminal.success("YES");
                    if (result.test(QuestionAttribute.APPLY_FOR_ALL))
                        terminal.success("全てに適用");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(PeyangPaperUtils.getInstance());

        terminal.success("質問を作成しました。");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args)
    {
        if (args.length == 1)
            return Arrays.asList(QUESTION_TYPES);
        return null;
    }

    @Override
    public @NotNull String getPermission()
    {
        return "peyangpaperutils.debug.question";
    }

    @Override
    public TextComponent getHelpOneLine()
    {
        return of("Question debug");
    }

    @Override
    public String[] getArguments()
    {
        return new String[]{
                required("type", "string"),
                required("question", "string")
        };
    }
}
