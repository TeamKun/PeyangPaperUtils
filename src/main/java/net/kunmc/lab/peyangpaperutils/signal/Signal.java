package net.kunmc.lab.peyangpaperutils.signal;

import lombok.Getter;
import lombok.Setter;

/**
 * フロントエンドにスローできるシグナルです。
 * このシグナルは、フロントエンドとバックエンドの隔離のために使用します。
 */
@Getter
@Setter
public abstract class Signal
{
    /**
     * シグナルがハンドルされたかどうかを示すフラグです。
     * このフラグが {@code true} の場合, ハンドルマネージャはこれ以降のハンドラを呼び出しません。
     */
    private boolean handled;
}
