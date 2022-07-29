package net.kunmc.lab.peyangpaperutils.plugin.retrieving;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

@Value
@Builder
public class RetrievingFile
{

    @Singular("target")
    List<RetrievingMeta> targetPlugins;

    public static RetrievingFile load(File file)
    {
        Gson gson = new Gson();

        System.out.println("Loading retrieving file...");

        try (FileReader reader = new FileReader(file))
        {
            return gson.fromJson(reader, RetrievingFile.class);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void save(File file)
    {
        Gson gson = new Gson();

        System.out.println("Saving retrieving file...");

        try (PrintWriter writer = new PrintWriter(file))
        {
            writer.println(gson.toJson(this));
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Value
    public static class RetrievingMeta
    {
        String pluginName;
        int order;
    }
}

