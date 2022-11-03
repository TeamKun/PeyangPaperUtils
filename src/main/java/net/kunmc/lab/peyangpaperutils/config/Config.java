package net.kunmc.lab.peyangpaperutils.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * コンフィグのフィールドに付けるアノテーションです。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config
{
    /**
     * ヘルプに表示するヘルプメッセージです。
     *
     * @return ヘルプメッセージ
     */
    String value();

    /**
     * 受け入れる値の最小値です。
     *
     * @return 最小値
     */
    double min() default -1;

    /**
     * 受け入れる値の最大値です。
     *
     * @return 最大値
     */
    double max() default -1;

    String[] enums() default {};

    /**
     * コンフィグが範囲値であるかどうかを示します。
     * 範囲値は, {@code minConfigName} と {@code maxName} で指定されたフィールドの値を参照します。
     *
     * @return 範囲値かどうか
     */
    boolean ranged() default false;
}
