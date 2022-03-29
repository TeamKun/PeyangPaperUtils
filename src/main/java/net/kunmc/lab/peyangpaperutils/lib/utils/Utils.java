package net.kunmc.lab.peyangpaperutils.lib.utils;

public class Utils
{
    public static String[] removeFirstElement(String[] args)
    {
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return newArgs;
    }
}
