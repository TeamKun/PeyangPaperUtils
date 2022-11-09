package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
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
    public @NotNull Audience getAudience()
    {
        return this.player;
    }

    @Override
    public boolean isPlayer()
    {
        return true;
    }

    @Override
    public @NotNull Progressbar createProgressbar(@NotNull String name) throws IllegalStateException
    {
        if (this.progressbars.containsKey(name))
            throw new IllegalStateException("Progressbar with name " + name + " already exists!");

        Progressbar progressbar = new PlayerProgressbar(this.player, PlayerProgressbar.ProgressbarType.BOSS_BAR);
        this.progressbars.put(name, progressbar);

        return progressbar;
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
        this.player.clearTitle();
    }

    /**
     * プレイヤーインスタンスを取得し直します。
     */
    public void updatePlayer()
    {
        this.player = Bukkit.getPlayer(this.player.getUniqueId());
        this.setAudience(this.player);
    }
}
