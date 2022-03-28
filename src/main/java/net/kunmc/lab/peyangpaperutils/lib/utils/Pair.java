package net.kunmc.lab.peyangpaperutils.lib.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<L, R>
{
    private L left;
    private R right;

    public static <L, R> Pair<L, R> of(L left, R right)
    {
        return new Pair<>(left, right);
    }
}
