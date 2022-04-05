package net.kunmc.lab.peyangpaperutils.lib.utils;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

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
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @return BukkitTask
     * @see BukkitScheduler#runTask(Plugin, Runnable)
     */
    public static @NotNull BukkitTask run(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException)
    {
        GeneralExceptableRunnerWrapper runner = new GeneralExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTask(plugin, new PrivateRunnable(runner, onException));
        runner.setTask(task);
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
    public static @NotNull BukkitTask runAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException)
    {
        GeneralExceptableRunnerWrapper runner = new GeneralExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTaskAsynchronously(plugin, new PrivateRunnable(runner, onException));
        runner.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskLater(Plugin, Runnable, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                               @Nullable Consumer<? super Exception> onException, long delay)
    {
        GeneralExceptableRunnerWrapper runner = new GeneralExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTaskLater(plugin, new PrivateRunnable(runner, onException), delay);
        runner.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskLaterAsynchronously(Plugin, Runnable, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                                    @Nullable Consumer<? super Exception> onException, long delay)
    {
        GeneralExceptableRunnerWrapper runner = new GeneralExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTaskLaterAsynchronously(plugin, new PrivateRunnable(runner, onException), delay);
        runner.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @Nullable GeneralExceptableRunner runnable,
                                               @Nullable Consumer<? super Exception> onException, long delay, long period)
    {
        GeneralExceptableRunnerWrapper runner = new GeneralExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTaskTimer(plugin, new PrivateRunnable(runner, onException), delay, period);
        runner.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に非同期で定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @Nullable GeneralExceptableRunner runnable,
                                                    @Nullable Consumer<? super Exception> onException, long delay, long period)
    {
        GeneralExceptableRunnerWrapper runner = new GeneralExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTaskTimerAsynchronously(plugin, new PrivateRunnable(runner, onException), delay, period);
        runner.setTask(task);
        return task;
    }

    /**
     * Runnableを定期的に実行します。
     * {@link  Runner#runTimer(Plugin, GeneralExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                               @Nullable Consumer<? super Exception> onException, long period)
    {
        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link  Runner#runTimerAsync(Plugin, GeneralExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull GeneralExceptableRunner runnable,
                                                    @Nullable Consumer<? super Exception> onException, long period)
    {
        return runTimerAsync(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @Nullable CountExceptableRunner runnable,
                                               @Nullable Consumer<? super Exception> onException, long delay, long period)
    {
        CountExceptableRunnerWrapper runner = new CountExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTaskTimer(plugin, new PrivateRunnable(runner, onException), delay, period);
        runner.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に非同期で定期的に実行します。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @Nullable CountExceptableRunner runnable,
                                                    @Nullable Consumer<? super Exception> onException, long delay, long period)
    {
        CountExceptableRunnerWrapper runner = new CountExceptableRunnerWrapper(runnable);
        BukkitTask task = SCHEDULER.runTaskTimerAsynchronously(plugin, new PrivateRunnable(runner, onException), delay, period);
        runner.setTask(task);
        return task;
    }

    /**
     * Runnableを指定チック経過後に定期的に実行します。
     * {@link  Runner#runTimer(Plugin, CountExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable,
                                               @Nullable Consumer<? super Exception> onException, long period)
    {
        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを指定チック経過後に非同期で定期的に実行します。
     * {@link  Runner#runTimerAsync(Plugin, CountExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull CountExceptableRunner runnable,
                                                    @Nullable Consumer<? super Exception> onException, long period)
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
     * @param delay    チック数
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
     * @param delay    チック数
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
     * @param delay    チック数
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
     * @param delay    チック数
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
     * {@link  Runner#runTimer(Plugin, GeneralExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
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
     * @param delay    チック数
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
     * @param delay    チック数
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
     * {@link  Runner#runTimer(Plugin, CountExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
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
            return null;
        PluginClassLoader pluginClassLoader = (PluginClassLoader) classLoader;
        return pluginClassLoader.getPlugin();
    }

    /**
     * Runnableを実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #run(Plugin, GeneralExceptableRunner, Consumer)
     */
    public static @NotNull BukkitTask run(@NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return run(plugin, runnable, onException);
    }

    /**
     * Runnableを非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runAsync(Plugin, GeneralExceptableRunner, Consumer)
     */
    public static @NotNull BukkitTask runAsync(@NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runAsync(plugin, runnable, onException);
    }

    /**
     * Runnableを指定チック経過後に実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLater(Plugin, GeneralExceptableRunner, Consumer, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runLater(plugin, runnable, onException, delay);
    }

    /**
     * Runnableを指定チック経過後に非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param delay       チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runLaterAsync(Plugin, GeneralExceptableRunner, Consumer, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runLaterAsync(plugin, runnable, onException, delay);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @param delay       初回実行チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, GeneralExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @param delay       初回実行チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, GeneralExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(Plugin, GeneralExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, GeneralExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(Plugin, GeneralExceptableRunner, Consumer, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, GeneralExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(GeneralExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @param delay       初回実行チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(Plugin, CountExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(Plugin, CountExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, @Nullable Consumer<? super Exception> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, onException, 0L, period);
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
     * @see #runAsync(Plugin, GeneralExceptableRunner)
     */
    public static @NotNull BukkitTask runAsync(@NotNull GeneralExceptableRunner runnable)
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
     * @see #runLater(Plugin, GeneralExceptableRunner, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull GeneralExceptableRunner runnable, long delay)
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
     * @see #runLaterAsync(Plugin, GeneralExceptableRunner, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull GeneralExceptableRunner runnable, long delay)
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
     * @see #runTimer(Plugin, GeneralExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull GeneralExceptableRunner runnable, long delay, long period)
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
     * @see #runTimerAsync(Plugin, GeneralExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull GeneralExceptableRunner runnable, long delay, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, delay, period);
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
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, 0L, period);
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
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, 0L, period);
    }

    /**
     * Runnableを定期的に実行します。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, period, delay);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, period, delay);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(CountExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull CountExceptableRunner runnable, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(CountExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, CountExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull CountExceptableRunner runnable, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, 0L, period);
    }

    /**
     * キャンセル可能なランナー
     */
    interface CancellableRunner
    {
        /**
         * 実行をキャンセルします。
         */
        default void cancel()
        {
        } // It will be overridden.
    }

    /**
     * Exceptionのスローを許容します。 {@link CountExceptableRunner#run(long)} の実行時に {@code 0} からカウントされます。
     */
    @FunctionalInterface
    public interface CountExceptableRunner extends CancellableRunner
    {
        /**
         * 実行します。
         * 初期値は0です。
         *
         * @throws Exception 例外
         */
        void run(long count) throws Exception;
    }

    /**
     * Exceptionのスローを許容します。
     */
    @FunctionalInterface
    public interface GeneralExceptableRunner extends CancellableRunner
    {
        /**
         * 実行します。
         *
         * @throws Exception 例外
         */
        void run() throws Exception;
    }

    private static class GeneralExceptableRunnerWrapper implements GeneralExceptableRunner
    {
        protected final GeneralExceptableRunner runner;

        @Setter
        private BukkitTask task;

        GeneralExceptableRunnerWrapper(GeneralExceptableRunner runner)
        {
            this.runner = runner;
        }

        @Override
        public void run() throws Exception
        {
            this.runner.run();
        }

        @Override
        public void cancel()
        {
            try
            {
                if (this.task != null)
                    this.task.cancel();
            }
            catch (RuntimeException ignored)
            {
            }
        }
    }

    private static class CountExceptableRunnerWrapper extends GeneralExceptableRunnerWrapper implements CountExceptableRunner
    {
        private final AtomicLong count;

        CountExceptableRunnerWrapper(CountExceptableRunner runner)
        {
            super((GeneralExceptableRunner) runner);
            this.count = new AtomicLong();
        }

        @Override
        public void run() throws Exception
        {
            this.run(this.count.getAndIncrement());
        }

        @Override
        public void run(long count) throws Exception
        {
            ((CountExceptableRunner) this.runner).run(count);
        }
    }

    @AllArgsConstructor
    private static class PrivateRunnable implements Runnable
    {
        private final GeneralExceptableRunnerWrapper runner;
        private final Consumer<? super Exception> onException;

        public PrivateRunnable(CountExceptableRunnerWrapper runner, Consumer<? super Exception> onException)
        {
            this.runner = runner;
            this.onException = onException;
        }

        @Override
        public void run()
        {
            try
            {
                this.runner.run();
            }
            catch (Exception e)
            {
                if (this.onException != null)
                    this.onException.accept(e);
                else
                    e.printStackTrace();
            }
        }
    }
}
