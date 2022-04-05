package net.kunmc.lab.peyangpaperutils.lib.utils;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
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
    public static @NotNull BukkitTask run(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException)
    {
        return SCHEDULER.runTask(plugin, new PrivateRunnable(runnable, onException));
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
    public static @NotNull BukkitTask runAsync(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException)
    {
        return SCHEDULER.runTaskAsynchronously(plugin, new PrivateRunnable(runnable, onException));
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
    public static @NotNull BukkitTask runLater(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable,
                                               @Nullable Consumer<Exception> onException, long delay)
    {
        return SCHEDULER.runTaskLater(plugin, new PrivateRunnable(runnable, onException), delay);
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
    public static @NotNull BukkitTask runLaterAsync(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable,
                                                    @Nullable Consumer<Exception> onException, long delay)
    {
        return SCHEDULER.runTaskLaterAsynchronously(plugin, new PrivateRunnable(runnable, onException), delay);
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
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @Nullable ExceptableRunner runnable,
                                               @Nullable Consumer<Exception> onException, long delay, long period)
    {
        return SCHEDULER.runTaskTimer(plugin, new PrivateRunnable(runnable, onException), delay, period);
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
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @Nullable ExceptableRunner runnable,
                                                    @Nullable Consumer<Exception> onException, long delay, long period)
    {
        return SCHEDULER.runTaskTimerAsynchronously(plugin, new PrivateRunnable(runnable, onException), delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link  Runner#runTimer(Plugin, ExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable,
                                               @Nullable Consumer<Exception> onException, long period)
    {
        return SCHEDULER.runTaskTimer(plugin, new PrivateRunnable(runnable, onException), 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link  Runner#runTimerAsync(Plugin, ExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin      Plugin
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable,
                                                    @Nullable Consumer<Exception> onException, long period)
    {
        return SCHEDULER.runTaskTimerAsynchronously(plugin, new PrivateRunnable(runnable, onException), 0L, period);
    }

    /**
     * Runnableを実行します。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @return BukkitTask
     * @see BukkitScheduler#runTask(Plugin, Runnable)
     */
    public static @NotNull BukkitTask run(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable)
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
    public static @NotNull BukkitTask runAsync(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable)
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
    public static @NotNull BukkitTask runLater(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, long delay)
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
    public static @NotNull BukkitTask runLaterAsync(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, long delay)
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
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, long delay, long period)
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
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, long delay, long period)
    {
        return runTimerAsync(plugin, runnable, null, delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link  Runner#runTimer(Plugin, ExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param plugin   Plugin
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, long period)
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
    public static @NotNull BukkitTask runTimerAsync(@NotNull Plugin plugin, @NotNull ExceptableRunner runnable, long period)
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
     * @see #run(Plugin, ExceptableRunner, Consumer)
     */
    public static @NotNull BukkitTask run(@NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException)
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
     * @see #runAsync(Plugin, ExceptableRunner, Consumer)
     */
    public static @NotNull BukkitTask runAsync(@NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException)
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
     * @see #runLater(Plugin, ExceptableRunner, Consumer, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException, long delay)
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
     * @see #runLaterAsync(Plugin, ExceptableRunner, Consumer, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException, long delay)
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
     * @see #runTimer(Plugin, ExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException, long period, long delay)
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
     * @see #runTimerAsync(Plugin, ExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException, long period, long delay)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, onException, period, delay);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(Plugin, ExceptableRunner, Consumer, long, long)} の第四引数には 0L が指定されます。
     *
     * @param runnable    Runnable
     * @param onException 実行中に例外が発生した場合に呼び出されるRunnable
     * @param period      定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, ExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull ExceptableRunner runnable, @Nullable Consumer<Exception> onException, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, onException, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(Plugin, ExceptableRunner, Consumer, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, ExceptableRunner, Consumer, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(ExceptableRunner runnable, @Nullable Consumer<Exception> onException, long period)
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
     * @see #run(Plugin, ExceptableRunner)
     */
    public static @NotNull BukkitTask run(@NotNull ExceptableRunner runnable)
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
     * @see #runAsync(Plugin, ExceptableRunner)
     */
    public static @NotNull BukkitTask runAsync(@NotNull ExceptableRunner runnable)
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
     * @see #runLater(Plugin, ExceptableRunner, long)
     */
    public static @NotNull BukkitTask runLater(@NotNull ExceptableRunner runnable, long delay)
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
     * @see #runLaterAsync(Plugin, ExceptableRunner, long)
     */
    public static @NotNull BukkitTask runLaterAsync(@NotNull ExceptableRunner runnable, long delay)
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
     * @see #runTimer(Plugin, ExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull ExceptableRunner runnable, long delay, long period)
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
     * @see #runTimerAsync(Plugin, ExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull ExceptableRunner runnable, long delay, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, delay, period);
    }

    /**
     * Runnableを定期的に実行します。
     * {@link #runTimer(ExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimer(Plugin, ExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimer(@NotNull ExceptableRunner runnable, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimer(plugin, runnable, 0L, period);
    }

    /**
     * Runnableを定期的に非同期で実行します。
     * {@link #runTimerAsync(ExceptableRunner, long, long)} の第二引数には 0L が指定されます。
     *
     * @param runnable Runnable
     * @param period   定期チック数
     * @return BukkitTask
     * @throws IllegalArgumentException プラグインが特定できない場合
     * @see #runTimerAsync(Plugin, ExceptableRunner, long, long)
     */
    public static @NotNull BukkitTask runTimerAsync(@NotNull ExceptableRunner runnable, long period)
    {
        Plugin plugin = getPlugin(MethodHandles.lookup().lookupClass());

        if (plugin == null)
            throw new IllegalStateException("Can't specify your plugin.");

        return runTimerAsync(plugin, runnable, 0L, period);
    }

    /**
     * Exceptionのスローを許容します。
     */
    @FunctionalInterface
    public interface ExceptableRunner
    {
        /**
         * 実行します。
         *
         * @throws Exception 例外
         */
        void run() throws Exception;
    }

    @AllArgsConstructor
    private static class PrivateRunnable implements Runnable
    {
        private final ExceptableRunner runner;
        private final Consumer<Exception> onException;

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
