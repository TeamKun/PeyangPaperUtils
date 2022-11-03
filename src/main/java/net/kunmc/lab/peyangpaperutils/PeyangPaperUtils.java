package net.kunmc.lab.peyangpaperutils;

import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.lib.terminal.InputManager;
import net.kunmc.lab.peyangpaperutils.lib.terminal.PlayerTerminal;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public final class PeyangPaperUtils implements Listener
{
    @Getter
    private static PeyangPaperUtils instance;

    @Getter
    private final Plugin plugin;

    @Getter
    private final InputManager inputManager;

    @Getter
    private final UUID runID;

    private PeyangPaperUtils(Plugin plugin)
    {
        this.plugin = plugin;
        this.inputManager = new InputManager(plugin);
        this.runID = UUID.randomUUID();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static void init(Plugin plugin)
    {
        if (instance != null)
        {
            if (PeyangPaperUtils.class.getPackage().getName().equals("net.kunmc.lab" + "peyangpaperutils"))
                throw new IllegalStateException("PeyangPaperUtils is used without package relocation.");
            throw new IllegalStateException("PeyangPaperUtils is already initialized.");
        }


        instance = new PeyangPaperUtils(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        ((PlayerTerminal) Terminals.of(event.getPlayer())).updatePlayer();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Terminals.purge(event.getPlayer());
    }

    public static void dispose()
    {
        if (instance == null)
            throw new IllegalStateException("PeyangPaperUtils is not initialized.");

        instance.inputManager.cancelAll();
    }
}
