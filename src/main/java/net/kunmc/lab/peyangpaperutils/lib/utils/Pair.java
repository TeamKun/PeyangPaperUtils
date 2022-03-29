package net.kunmc.lab.peyangpaperutils.lib.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ペアを表すクラス
 */
@Data
@AllArgsConstructor
public class Pair<L, R>
{
    private L left;
    private R right;

    /**
     * ペアを生成する
     *
     * @param left  左側の値
     * @param right 右側の値
     * @param <L>   左側の型
     * @param <R>   右側の型
     * @return ペア
     */
    public static <L, R> Pair<L, R> of(L left, R right)
    {
        return new Pair<>(left, right);
    }
}
