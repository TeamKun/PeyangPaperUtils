package net.kunmc.lab.peyangpaperutils.lib.terminal;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Question;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 入力のマネージャです。
 */
public class InputManager implements Listener
{
    private final Map<UUID, ArrayList<Question>> inputTasks;

    public InputManager(PeyangPaperUtils plugin)
    {
        inputTasks = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * 入力タスクを追加します。
     *
     * @param uuid      登録するプレイヤーのUUID。Nullの場合はコンソールからの入力を受け付け
     * @param inputTask 入力タスク
     */
    public void addInputTask(@Nullable UUID uuid, Question inputTask)
    {
        ArrayList<Question> inputTasks = this.inputTasks.get(uuid);
        if (inputTasks == null)
            inputTasks = new ArrayList<>();
        inputTasks.add(inputTask);

        if (inputTasks.size() == 1)
            inputTask.printQuestion();
        this.inputTasks.put(uuid, inputTasks);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSay(AsyncChatEvent event)
    {
        ArrayList<Question> inputTasks = this.inputTasks.get(event.getPlayer().getUniqueId());
        String message = ((TextComponent) event.originalMessage().asComponent()).content();

        if (inputTasks == null || inputTasks.isEmpty())
            return;

        event.setCancelled(true);

        Question inputTask = inputTasks.get(0);

        if (!inputTask.checkValidInput(message))
        {
            inputTask.getInput().getTerminal().error("入力が無効です：" + message);
            return;
        }

        inputTask.setAnswer(message);
        inputTasks.remove(0);
        if (!inputTasks.isEmpty())
            inputTasks.get(0).printQuestion();
    }

    @EventHandler
    public void onConsoleSay(ServerCommandEvent e)
    {
        ArrayList<Question> inputTasks = this.inputTasks.get(null);

        if (inputTasks == null || inputTasks.isEmpty())
            return;

        e.setCancelled(true);

        Question inputTask = inputTasks.get(0);
        String message = e.getCommand();

        if (!inputTask.checkValidInput(message))
        {
            inputTask.getInput().getTerminal().error("入力が無効です：" + message);
            return;
        }

        inputTask.setAnswer(message);
        inputTasks.remove(0);
        if (!inputTasks.isEmpty())
            inputTasks.get(0).printQuestion();
    }

    /**
     * 指定したプレイヤの入力タスクをすべて削除します。
     *
     * @param uuid 削除するプレイヤのUUID
     */
    public void cancelInputTask(@Nullable UUID uuid)
    {
        ArrayList<Question> inputTasks = this.inputTasks.get(uuid);
        if (inputTasks == null)
            return;
        inputTasks.clear();
    }

    /**
     * 指定したプレイヤの入力タスクを削除します。
     *
     * @param uuid     削除するプレイヤのUUID
     * @param question 削除する入力タスク
     */
    public void cancelInputTask(@Nullable UUID uuid, @NotNull Question question)
    {
        ArrayList<Question> inputTasks = this.inputTasks.get(uuid);
        if (inputTasks == null)
            return;
        inputTasks.remove(question);
    }
}
