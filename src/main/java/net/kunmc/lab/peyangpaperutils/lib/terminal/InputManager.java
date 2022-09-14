package net.kunmc.lab.peyangpaperutils.lib.terminal;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
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
 *
 * @see Terminals#of(Player)
 * @see Terminals#ofConsole()
 */
public class InputManager implements Listener
{
    private final Map<UUID, ArrayList<Question>> inputTasks;

    public InputManager(PeyangPaperUtils plugin)
    {
        this.inputTasks = new HashMap<>();
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
        if (this.isInputTaskAvailable(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);

        String message = ((TextComponent) event.message().asComponent()).content();

        doAnswer(event.getPlayer().getUniqueId(), message);
    }

    /**
     * 入力タスクが存在するかどうかを返します。
     *
     * @param id プレイヤーのUUID
     * @return 入力タスクが存在するかどうか
     */
    public boolean isInputTaskAvailable(@Nullable UUID id)
    {
        ArrayList<Question> inputTasks = this.inputTasks.get(id);
        return inputTasks != null && !inputTasks.isEmpty();
    }

    /**
     * 入力タスクを返します。
     *
     * @param id 入力タスクのUUID
     * @return 入力タスク
     */
    public @Nullable Question getQuestionByID(@NotNull UUID id)
    {
        return this.inputTasks.values().stream().parallel()
                .flatMap(ArrayList::stream)
                .filter(q -> q.getUuid().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * 入力タスクに答えます。
     *
     * @param question InputTask
     * @param answer   答え
     */
    public void doAnswer(@NotNull Question question, @NotNull String answer)
    {
        if (!question.checkValidInput(answer))
        {
            question.getInput().getTerminal().error("入力が無効です：" + answer);
            return;
        }

        question.setAnswer(answer);
        ArrayList<Question> inputTasks = this.inputTasks.get(question.getTarget());
        inputTasks.remove(0);
        if (!inputTasks.isEmpty())
            inputTasks.get(0).printQuestion();
    }

    private void doAnswer(@Nullable UUID uuid, @NotNull String answer)
    {
        ArrayList<Question> inputTasks = this.inputTasks.get(uuid);
        if (inputTasks == null || inputTasks.isEmpty())
            return;

        doAnswer(inputTasks.get(0), answer);
    }

    @EventHandler
    public void onConsoleSay(ServerCommandEvent e)
    {
        if (this.isInputTaskAvailable(null))
            return;

        e.setCancelled(true);

        doAnswer((UUID) null, e.getCommand());
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
