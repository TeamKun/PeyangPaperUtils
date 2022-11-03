package net.kunmc.lab.peyangpaperutils.sound;

/**
 * 音を再生するエリアを表すインターフェースです。
 */
public enum PlayArea
{
    /**
     * プレイヤのみに再生します。
     */
    SELF,
    /**
     * プレイヤの周囲5ブロックに再生します。
     */
    NEARBY_5,
    /**
     * プレイヤの周囲10ブロックに再生します。
     */
    NEARBY_10,
    /**
     * プレイヤの周囲20ブロックに再生します。
     */
    NEARBY_20,
    /**
     * サーバ全体に再生します。
     */
    SERVER_ALL
}
