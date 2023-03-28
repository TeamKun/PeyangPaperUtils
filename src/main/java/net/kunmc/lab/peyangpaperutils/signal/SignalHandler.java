package net.kunmc.lab.peyangpaperutils.signal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * シグナルを受け取るメソッドに付与するアノテーションです。
 * このアノテーションが付与されたメソッドは、シグナルを受け取れます。
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SignalHandler
{
}
