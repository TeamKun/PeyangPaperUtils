package net.kunmc.lab.peyangpaperutils.lib.utils;

import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/**
 * BukkitRunnableを簡単に実行するためのラッパーです。
 */
public class Runner
{
    /**
     * Runnableを実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @return BukkitTask
     * @see BukkitScheduler#runTask(Plugin, Runnable)
     */
    public static @NotNull BukkitTask run(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTask(plugin);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを非同期で実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @return BukkitTask
     * @see BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)
     */
    public static @NotNull BukkitTask runAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTaskAsynchronously(plugin);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskLater(Plugin, Runnable, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                               @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTaskLater(plugin, delay);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskLaterAsynchronously(Plugin, Runnable, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                                    @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTaskLaterAsynchronously(plugin, delay);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @Nullable GeneralExceptableRunner runnable,
                                               @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay, long period)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTaskTimer(plugin, delay, period);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に非同期で定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @Nullable GeneralExceptableRunner runnable,
                                                    @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay, long period)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTaskTimerAsynchronously(plugin, delay, period);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを定期的に実行します。
     * {@link  Runner#runTimer(Plugin, GeneralExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                               @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link  Runner#runTimerAsync(Plugin, GeneralExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                                    @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        return runTimerAsync(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @Nullable CountExceptableRunner runnable,
                                               @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay, long period)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTaskTimer(plugin, delay, period);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に非同期で定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @Nullable CountExceptableRunner runnable,
                                                    @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay, long period)
    {
        RunnableWrapper runnableWrapper = new RunnableWrapper(runnable, onException);
        BukkitTask task = runnableWrapper.runTaskTimerAsynchronously(plugin, delay, period);
        runnableWrapper.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     * {@link  Runner#runTimer(Plugin, CountExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable,
                                               @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを指定チック経過後に非同期で定期的に実行します。
     * {@link  Runner#runTimerAsync(Plugin, CountExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable,
                                                    @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        return runTimerAsync(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @return BukkitTask
     * @see BukkitScheduler#runTask(Plugin, Runnable)
     */
    public static @NotNull BukkitTask run(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable)
    {
        return run(plugin, runnable, null);
    }

    /**
     * Runnableを非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @return BukkitTask
     * @see BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)
     */
    public static @NotNull BukkitTask runAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable)
    {
        return runAsync(plugin, runnable, null);
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskLater(Plugin, Runnable, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, long delay)
    {
        return runLater(plugin, runnable, null, delay);
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskLaterAsynchronously(Plugin, Runnable, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, long delay)
    {
        return runLaterAsync(plugin, runnable, null, delay);
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, long delay, long period)
    {
        return runTimer(plugin, runnable, null, delay, period);
    }

    /**
     * Runnableを指定チック経過後に定期的に非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, long delay, long period)
    {
        return runTimerAsync(plugin, runnable, null, delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link  Runner#runTimer(Plugin, GeneralExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, long period)
    {
        return runTimer(plugin, runnable, null, 0L, period);
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
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, long period)
    {
        return runTimerAsync(plugin, runnable, null, 0L, period);
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable, long delay, long period)
    {
        return runTimer(plugin, runnable, null, delay, period);
    }

    /**
     * Runnableを指定チック経過後に定期的に非同期で実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable, long delay, long period)
    {
        return runTimerAsync(plugin, runnable, null, delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link  Runner#runTimer(Plugin, CountExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable, long period)
    {
        return runTimer(plugin, runnable, null, 0L, period);
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
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable, long period)
    {
        return runTimerAsync(plugin, runnable, null, 0L, period);
    }

    private static Plugin getPlugin(Class<?> clazz)
    {
        ClassLoader classLoader = clazz.getClassLoader();
        if (!(classLoader instanceof PluginClassLoader))
            throw new IllegalArgumentException("ClassLoader is not PluginClassLoader");

        PluginClassLoader pluginClassLoader = (PluginClassLoader) classLoader;
        Plugin plugin = pluginClassLoader.getPlugin();
        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");
        return plugin;
    }

    /**
     * Runnableを実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #run(Plugin, GeneralExceptableRunner, BiConsumer)
     */
    public static @NotNull BukkitTask run(@NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return run(plugin, runnable, onException);
    }

    /**
     * Runnableを非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runAsync(Plugin, GeneralExceptableRunner, BiConsumer)
     */
    public static @NotNull BukkitTask runAsync(@NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runAsync(plugin, runnable, onException);
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLater(Plugin, GeneralExceptableRunner, BiConsumer, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runLater(plugin, runnable, onException, delay);
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLaterAsync(Plugin, GeneralExceptableRunner, BiConsumer, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runLaterAsync(plugin, runnable, onException, delay);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, GeneralExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runTimer(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, GeneralExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runTimerAsync(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(Plugin, GeneralExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, GeneralExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(Plugin, GeneralExceptableRunner, BiConsumer, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるBiConsumer
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, GeneralExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(GeneralExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runTimerAsync(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runTimer(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @param delay       遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runTimerAsync(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(Plugin, CountExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(Plugin, CountExceptableRunner, BiConsumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, @Nullable BiConsumer<? super Exception, ? super BukkitTask> onException, long period)
    {
        return runTimerAsync(getPlugin(MethodHandles.lookup().lookupClass()), runnable, onException, 0L, period);
    }

    /**
     * Runnableを実行します。
     *
     * @param runnable Runnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #run(Plugin, GeneralExceptableRunner)
     */
    public static @NotNull BukkitTask run(@NotNull GeneralExceptableRunner runnable)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());
        return run(plugin, runnable);
    }

    /**
     * Runnableを非同期で実行します。
     *
     * @param runnable Runnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runAsync(Plugin, GeneralExceptableRunner)
     */
    public static @NotNull BukkitTask runAsync(@NotNull GeneralExceptableRunner runnable)
    {
        return runAsync(getPlugin(MethodHandles.lookup().lookupClass()), runnable);
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLater(Plugin, GeneralExceptableRunner, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull GeneralExceptableRunner runnable, long delay)
    {
        return runLater(getPlugin(MethodHandles.lookup().lookupClass()), runnable, delay);
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLaterAsync(Plugin, GeneralExceptableRunner, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull GeneralExceptableRunner runnable, long delay)
    {
        return runLaterAsync(getPlugin(MethodHandles.lookup().lookupClass()), runnable, delay);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, GeneralExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull GeneralExceptableRunner runnable, long delay, long period)
    {
        return runTimer(getPlugin(MethodHandles.lookup().lookupClass()), runnable, delay, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable Runnable
     * @param delay    遅らせるチック数
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, GeneralExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull GeneralExceptableRunner runnable, long delay, long period)
    {
        return runTimerAsync(getPlugin(MethodHandles.lookup().lookupClass()), runnable, delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(GeneralExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, GeneralExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull GeneralExceptableRunner runnable, long period)
    {
        return runTimer(getPlugin(MethodHandles.lookup().lookupClass()), runnable, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(GeneralExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, GeneralExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull GeneralExceptableRunner runnable, long period)
    {
        return runTimerAsync(getPlugin(MethodHandles.lookup().lookupClass()), runnable, 0L, period);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @param delay    遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, long period, long delay)
    {
        return runTimer(getPlugin(MethodHandles.lookup().lookupClass()), runnable, period, delay);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @param delay    遅らせるチック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, long period, long delay)
    {
        return runTimerAsync(getPlugin(MethodHandles.lookup().lookupClass()), runnable, period, delay);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(CountExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, long period)
    {
        return runTimer(getPlugin(MethodHandles.lookup().lookupClass()), runnable, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(CountExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, BiConsumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, long period)
    {
        return runTimerAsync(getPlugin(MethodHandles.lookup().lookupClass()), runnable, 0L, period);
    }

    private interface RunnableRunner
    {
    }

    /**
     * Exceptionのスローを許容します。 {@link CountExceptableRunner#run(long)} の実行時に {@code 0} からカウントされます。
     */
    @FunctionalInterface
    public interface CountExceptableRunner extends RunnableRunner
    {
        /**
         * 実行します。
         * 初期値は0です。
         * @param count 実行回数
         *
         * @throws Exception 例外
         */
        void run(long count) throws Exception;
    }

    /**
     * Exceptionのスローを許容します。
     */
    @FunctionalInterface
    public interface GeneralExceptableRunner extends RunnableRunner
    {
        /**
         * 実行します。
         *
         * @throws Exception 例外
         */
        void run() throws Exception;
    }

    private static class RunnableWrapper extends BukkitRunnable
    {
        private final RunnableRunner runner;
        private final BiConsumer<? super Exception, ? super BukkitTask> onException;
        private final AtomicLong count;

        @Setter
        private BukkitTask task;

        RunnableWrapper(RunnableRunner runner, BiConsumer<? super Exception, ? super BukkitTask> onException)
        {
            this.runner = runner;
            this.onException = onException;
            this.task = null;
            this.count = new AtomicLong(0L);
        }

        @Override
        public void run()
        {
            try
            {
                if (this.runner instanceof CountExceptableRunner)
                    ((CountExceptableRunner) this.runner).run(this.count.getAndIncrement());
                else
                    ((GeneralExceptableRunner) this.runner).run();
            }
            catch (Exception e)
            {
                if (this.onException != null)
                    this.onException.accept(e, this.task);
                else
                    e.printStackTrace();
            }
        }
    }
}
