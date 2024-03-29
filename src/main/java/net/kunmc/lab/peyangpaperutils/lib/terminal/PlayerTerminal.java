package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * プレイヤのターミナルです。
 * コンストラクタからインスタンスを生成せずに、 {@link Terminals#of(Player)} で取得してください。
 */
public class PlayerTerminal extends AbstractBukkitTerminal
{
    @Getter
    private Player player;
    private final HashMap<String, Progressbar> progressbars;

    PlayerTerminal(Player player)
    {
        super(player);
        this.player = player;
        this.progressbars = new HashMap<>();
    }

    @Override
    public @NotNull CommandSender getSender()
    {
        return this.player;
    }

    @Override
    public void write(@NotNull BaseComponent[] component)
    {
        // noinspection deprecation
        this.player.spigot().sendMessage(component);
    }

    @Override
    public boolean isPlayer()
    {
        return true;
    }

    /**
     * プログレスバーを作成します。
     *
     * @param name プログレスバーの名前
     * @param type プログレスバーのタイプ
     * @return 作成したプログレスバー
     * @throws IllegalStateException 既に同じ名前のプログレスバーが存在する場合
     */
    public @NotNull Progressbar createProgressbar(@NotNull String name, @NotNull PlayerProgressbar.ProgressbarType type) throws IllegalStateException
    {
        if (this.progressbars.containsKey(name))
            throw new IllegalStateException("Progressbar with name " + name + " already exists!");

        Progressbar progressbar = new PlayerProgressbar(this.player, type);
        this.progressbars.put(name, progressbar);

        return progressbar;
    }

    @Override
    public @NotNull Progressbar createProgressbar(@NotNull String name) throws IllegalStateException
    {
        return this.createProgressbar(name, PlayerProgressbar.ProgressbarType.BOSS_BAR);
    }

    @Override
    public boolean removeProgressbar(@NotNull String name)
    {
        Progressbar progressbar = this.progressbars.remove(name);
        if (progressbar == null)
            return false;

        progressbar.hide();
        return true;
    }

    @Override
    public @Nullable Progressbar getProgressbar(@NotNull String name)
    {
        return this.progressbars.get(name);
    }

    @Override
    public void showNotification(@NotNull String title, @NotNull String message, int showTimeMS)
    {
        int ticks = showTimeMS / 50;

        this.player.sendTitle(title, message, 5, (int) (ticks * 0.5F), 10);
    }

    @Override
    public void clearNotification()
    {
        this.player.resetTitle();
    }

    /**
     * プレイヤーインスタンスを取得し直します。
     */
    public void updatePlayer()
    {
        this.player = Bukkit.getPlayer(this.player.getUniqueId());
        this.setSender(this.player);
    }
}
