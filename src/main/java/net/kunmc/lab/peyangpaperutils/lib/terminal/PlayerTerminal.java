package net.kunmc.lab.peyangpaperutils.lib.terminal;

import net.kunmc.lab.peyangpaperutils.lib.terminal.interfaces.Progressbar;
import net.kyori.adventure.audience.Audience;
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
    private final Player player;
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
        return player;
    }

    @Override
    public boolean isPlayer()
    {
        return true;
    }

    @Override
    public @NotNull Progressbar createProgressbar(@NotNull String name) throws IllegalStateException
    {
        if (progressbars.containsKey(name))
            throw new IllegalStateException("Progressbar with name " + name + " already exists!");

        Progressbar progressbar = new PlayerProgressbar(player, PlayerProgressbar.ProgressbarType.BOSS_BAR);
        progressbars.put(name, progressbar);

        return progressbar;
    }

    @Override
    public boolean removeProgressbar(@NotNull String name)
    {
        return progressbars.remove(name) != null;
    }

    @Override
    public @Nullable Progressbar getProgressbar(@NotNull String name)
    {
        return progressbars.get(name);
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
}
