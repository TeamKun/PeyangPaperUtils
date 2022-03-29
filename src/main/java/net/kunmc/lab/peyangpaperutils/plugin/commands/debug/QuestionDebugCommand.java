package net.kunmc.lab.peyangpaperutils.plugin.commands.debug;

import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import net.kunmc.lab.peyangpaperutils.lib.command.CommandBase;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.InputTask;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Terminal;
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

        InputTask in;

        switch (type)
        {
            case "yn":
                in = terminal.getInput().showYNQuestion(question);
                break;
            case "ync":
                in = terminal.getInput().showYNQuestionCancellable(question);
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
                    String value = in.waitAndGetValue();
                    terminal.success("取得した値：%s", value);
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
