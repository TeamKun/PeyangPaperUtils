package net.kunmc.lab.peyangpaperutils.lib.utils;

import net.kunmc.lab.peyangpaperutils.PeyangPaperUtils;

public class Utils
{
    public static String[] removeFirstElement(String[] args)
    {
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return newArgs;
    }

    public static int generateQuestionAnswerValidation(String questionID, String answer)
    {
        String runID = PeyangPaperUtils.getInstance().getRunID().toString();
        String param = String.format("%s;%s;%s", runID, questionID, answer);

        return param.hashCode();
    }
}
