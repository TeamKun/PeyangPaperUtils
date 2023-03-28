package net.kunmc.lab.peyangpaperutils.versioning;

/**
 * バージョンの構文が不正な場合に投げられる例外です。
 */
public class InvalidVersionSyntaxException extends IllegalArgumentException
{
    public InvalidVersionSyntaxException(String message)
    {
        super(message);
    }
}
