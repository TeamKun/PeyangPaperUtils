package net.kunmc.lab.peyangpaperutils.collectors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * つよつよなコレクターを提供します。
 */
public class ExCollectors
{
    /**
     * 逆順のリストを生成するコレクターを返します。
     *
     * @param <T> 要素の型
     * @return コレクター
     */
    public static <T> ReversingCollector<T> toReversedList()
    {
        return new ReversingCollector<>();
    }

    /**
     * {@link net.kunmc.lab.peyangpaperutils.lib.utils.Pair} の {@link Map} を生成するコレクターを返します。
     *
     * @param mapSupplier {@link Map} のコンストラクター
     * @param <L>         左側の型
     * @param <R>         右側の型
     * @param <M>         {@link Map} の型
     * @return コレクター
     */
    public static <L, R, M extends Map<L, R>> MappingPairCollector<L, R, M> toPairMap(Supplier<M> mapSupplier)
    {
        return new MappingPairCollector<>(mapSupplier);
    }

    /**
     * {@link net.kunmc.lab.peyangpaperutils.lib.utils.Pair} の {@link HashMap} を生成するコレクターを返します。
     *
     * @param <L> 左側の型
     * @param <R> 右側の型
     * @return コレクター
     */
    public static <L, R> MappingPairCollector<L, R, HashMap<L, R>> toPairHashMap()
    {
        return new MappingPairCollector<>(HashMap::new);
    }

    /**
     * {@link Map.Entry} を {@link Map} として生成するコレクターを返します。
     *
     * @param mapSupplier {@link Map} のコンストラクター
     * @param <L>         左側の型
     * @param <R>         右側の型
     * @param <M>         {@link Map} の型
     * @return コレクター
     */
    public static <L, R, M extends Map<L, R>> MappingMapElementCollector<L, R, M> toMap(Supplier<M> mapSupplier)
    {
        return new MappingMapElementCollector<>(mapSupplier);
    }

    /**
     * {@link Map.Entry} を {@link HashMap} として生成するコレクターを返します。
     *
     * @param <L> 左側の型
     * @param <R> 右側の型
     * @return コレクター
     */
    public static <L, R> MappingMapElementCollector<L, R, HashMap<L, R>> toHashMap()
    {
        return new MappingMapElementCollector<>(HashMap::new);
    }
}
