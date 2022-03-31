package net.kunmc.lab.peyangpaperutils.lib.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.lang.invoke.MethodHandles;

/**
 * BukkitRunnableを簡単に実行するためのラッパーです。
 */
public class Runner
{
    private static final BukkitScheduler SCHEDULER;

    static
    {
        SCHEDULER = Bukkit.getScheduler();
    }

    /**
     * Runnableを実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @return BukkitTask
     * @see BukkitScheduler#runTask(Plugin, Runnable)
     */
    public static BukkitTask run(Plugin plugin, Runnable runnable)
    {
        return SCHEDULER.runTask(plugin, runnable);
    }

    /**
     * Runnableを非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @return BukkitTask
     * @see BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)
     */
    public static BukkitTask runAsync(Plugin plugin, Runnable runnable)
    {
        return SCHEDULER.runTaskAsynchronously(plugin, runnable);
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskLater(Plugin, Runnable, long)
     */
    public static BukkitTask runLater(Plugin plugin, Runnable runnable, long delay)
    {
        return SCHEDULER.runTaskLater(plugin, runnable, delay);
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)
     */
    public static BukkitTask runLaterAsync(Plugin plugin, Runnable runnable, long delay)
    {
        return SCHEDULER.runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    チック数
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimer(Plugin plugin, Runnable runnable, long delay, long period)
    {
        return SCHEDULER.runTaskTimer(plugin, runnable, delay, period);
    }

    /**
     * Runnableを指定チック経過後に定期的に非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    チック数
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimerAsync(Plugin plugin, Runnable runnable, long delay, long period)
    {
        return SCHEDULER.runTaskTimerAsynchronously(plugin, runnable, delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link  Runner#runTimer(Plugin, Runnable, long, long)} の第三引数には 0L が指定されます。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimer(Plugin plugin, Runnable runnable, long period)
    {
        return SCHEDULER.runTaskTimer(plugin, runnable, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimerAsync(Plugin plugin, Runnable runnable, long period)
    {
        return SCHEDULER.runTaskTimerAsynchronously(plugin, runnable, 0L, period);
    }

    private static Plugin getPlugin(Class<?> clazz)
    {
        ClassLoader classLoader = clazz.getClassLoader();
        if (!(classLoader instanceof PluginClassLoader))
            return null;
        PluginClassLoader pluginClassLoader = (PluginClassLoader) classLoader;
        return pluginClassLoader.getPlugin();
    }

    /**
     * Runnableを実行します。
     *
     * @param runnable Runnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #run(Plugin, Runnable)
     */
    public static BukkitTask run(Runnable runnable)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return run(plugin, runnable);
    }

    /**
     * Runnableを非同期で実行します。
     *
     * @param runnable Runnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runAsync(Plugin, Runnable)
     */
    public static BukkitTask runAsync(Runnable runnable)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runAsync(plugin, runnable);
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param runnable Runnable
     * @param delay    チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLater(Plugin, Runnable, long)
     */
    public static BukkitTask runLater(Runnable runnable, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runLater(plugin, runnable, delay);
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param runnable Runnable
     * @param delay    チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLaterAsync(Plugin, Runnable, long)
     */
    public static BukkitTask runLaterAsync(Runnable runnable, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runLaterAsync(plugin, runnable, delay);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable Runnable
     * @param delay    チック数
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimer(Runnable runnable, long delay, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, delay, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable Runnable
     * @param delay    チック数
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimerAsync(Runnable runnable, long delay, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(Runnable, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimer(Runnable runnable, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(Runnable, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, Runnable, long, long)
     */
    public static BukkitTask runTimerAsync(Runnable runnable, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, 0L, period);
    }
}
