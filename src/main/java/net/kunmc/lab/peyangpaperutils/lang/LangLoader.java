package net.kunmc.lab.peyangpaperutils.lang;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/* non-public */ class LangLoader
{
    private List<String> findDataFolderLanguages(File languagesFolder, Logger logger)
    {
        List<String> result = new ArrayList<>();

        if (!languagesFolder.exists())
            return result;

        File[] files = languagesFolder.listFiles();
        if (files == null)
            return result;

        for (File file : files)
        {
            if (file.isDirectory())
                continue;

            String fileName = file.getName();
            if (!fileName.endsWith(EXT_LANG))
            {
                logger.warning("Invalid language file name: " + fileName);
                continue;
            }

            result.add(fileName.substring(0, fileName.length() - EXT_LANG.length()));
        }

        if (result.isEmpty())
            logger.warning("No language files found in the data folder.");
        else
            logger.info("Found " + result.size() + " language files in the data folder.");

        return result;
    }    private static final String EXT_LANG = EXT_LANG;
    
    private static final Method pluginGetFile;

    static
    {
        try
        {
            pluginGetFile = JavaPlugin.class.getDeclaredMethod("getFile");
            pluginGetFile.setAccessible(true);
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalStateException(e);
        }
    }

    private final Plugin plugin;
    private final Path langFilesPath;
    private final Path dataFolderLangPath;
    private final List<String> jarLanguages;
    private final List<String> dataFolderLanguages;

    LangLoader(LangProvider provider, Path langFilesPath) throws IOException
    {
        this.plugin = provider.getPlugin();
        this.langFilesPath = langFilesPath;
        this.dataFolderLangPath = this.plugin.getDataFolder().toPath().resolve(langFilesPath);

        this.dataFolderLanguages = this.findDataFolderLanguages(
                this.dataFolderLangPath.toFile(),
                this.plugin.getLogger()
        );
        this.jarLanguages = this.findJarLanguages(langFilesPath);

        for (String lang : this.jarLanguages)
            if (this.dataFolderLanguages.contains(lang))
            {
                this.plugin.getLogger().info("Duplicated language file \"" + lang + "\" in data folder " +
                        "will be overlapped by the one in jar file.");
                this.dataFolderLanguages.remove(lang);
            }
    }

    private static Properties loadLangIS(InputStream inputStream) throws IOException
    {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        {
            Properties properties = new Properties();
            properties.load(reader);
            return properties;
        }
    }

    public static File getPluginFile(Plugin plugin)
    {
        try
        {
            return (File) pluginGetFile.invoke(plugin);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            throw new IllegalStateException(e);
        }
    }

    private Properties loadLanguageFromJar(String name) throws IOException
    {
        File pluginFile = getPluginFile(this.plugin);
        String languagePath = this.langFilesPath.resolve(name).toString();

        try (ZipInputStream zipInputStream = new ZipInputStream(pluginFile.toURI().toURL().openStream()))
        {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null)
            {
                if (entry.isDirectory())
                    continue;

                String entryName = entry.getName();
                if (!(entryName.startsWith(languagePath) && entryName.endsWith(EXT_LANG)))
                    continue;

                break;
            }

            if (entry == null)
                throw new IllegalStateException("Language file \"" + name + "\" was not found in the jar file.");

            return loadLangIS(zipInputStream);
        }
    }

    public Properties loadLanguage(@NotNull String langName) throws IOException
    {
        if (!(this.jarLanguages.contains(langName) || this.dataFolderLanguages.contains(langName)))
            throw new IllegalArgumentException("No such language \"" + langName + "\".");

        boolean isInJar = this.jarLanguages.contains(langName);
        if (isInJar)
            return this.loadLanguageFromJar(langName);
        else
            return this.loadLanguageFromDataFolder(langName);
    }

    private Properties loadLanguageFromDataFolder(String name) throws IOException
    {
        File languageFile = this.dataFolderLangPath.resolve(name + EXT_LANG).toFile();

        try (InputStream inputStream = languageFile.toURI().toURL().openStream())
        {
            return loadLangIS(inputStream);
        }
    }

    private List<String> findJarLanguages(Path langFilesPath) throws IOException
    {
        List<String> result = new ArrayList<>();

        File jarFile = getPluginFile(this.plugin);
        try (ZipFile zipFile = new ZipFile(jarFile))
        {
            Iterator<? extends ZipEntry> iterator = zipFile.stream().iterator();

            boolean anyLanguageFound = false;
            while (iterator.hasNext())
            {
                ZipEntry entry = iterator.next();
                if (entry.isDirectory())
                    continue;

                String entryName = entry.getName();

                if (!entryName.startsWith(langFilesPath.toString()))
                    if (anyLanguageFound)
                        break;  // We have found all languages
                    else
                        continue;

                anyLanguageFound = true;

                String[] parts = entryName.split("/");
                if (parts.length != 2)
                {
                    this.plugin.getLogger().warning("Invalid language file path: " + entryName);
                    continue;
                }

                String langName = parts[1];
                if (!langName.endsWith(EXT_LANG))
                {
                    this.plugin.getLogger().warning("Invalid language file name: " + langName);
                    continue;
                }

                result.add(langName.substring(0, langName.length() - EXT_LANG.length()));
            }

        }

        if (result.isEmpty())
            this.plugin.getLogger().warning("No language files found in the jar file.");
        else
            this.plugin.getLogger().info("Found " + result.size() + " language files in the jar file.");

        return result;
    }



}
