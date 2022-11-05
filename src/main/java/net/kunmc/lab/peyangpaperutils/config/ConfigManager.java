package net.kunmc.lab.peyangpaperutils.config;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * コンフィグの管理を行うクラスです。
 *
 * @param <T> コンフィグの型です。
 */
public class ConfigManager<T>
{
    private final Path path;
    private final Class<T> configClass;
    private final HashMap<String, ConfigEntry> map;
    @Getter
    private T config;

    public ConfigManager(@NotNull Path path, @NotNull T config)
    {
        this.path = path;
        this.config = config;
        // noinspection unchecked
        this.configClass = (Class<T>) config.getClass();
        this.map = new HashMap<>();
        this.generateMap();
    }

    /**
     * コンフィグを読み込みます。
     *
     * @throws UncheckedIOException     読み込みに失敗した場合
     * @throws IllegalArgumentException ファイルが存在しない場合
     * @throws ClassCastException       ファイルの内容がコンフィグの型と一致しない場合
     */
    public void loadConfig(Path path) throws UncheckedIOException, IllegalArgumentException, ClassCastException
    {
        if (!Files.exists(path))
            throw new IllegalArgumentException("Config file not found.");

        Gson gson = new Gson();

        try
        {
            this.config = gson.fromJson(new String(Files.readAllBytes(path)), this.configClass);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }

    }

    /**
     * コンフィグを保存します。
     */
    public void saveConfig()
    {
        Gson gson = new Gson();
        try
        {
            Files.write(this.path, gson.toJson(this.config).getBytes());
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private void generateMap()
    {
        List<ConfigEntry> configs = getConfigs();
        configs.forEach(config -> this.map.put(config.getField().getName(), config));
    }

    /**
     * コンフィグが存在するかどうかを返します。
     *
     * @param name コンフィグの名前
     * @return コンフィグが存在するかどうか
     */
    public boolean isConfigExist(String name)
    {
        if (this.map.containsKey(name))
            return true;

        if (name.length() < 4)
            return false;
        String n = "min" + name.substring(0, 1).toUpperCase() + name.substring(1);
        ConfigEntry config = this.map.get(n);
        if (config == null)
            return false;

        return config.define.ranged();
    }

    /**
     * コンフィグをすべて取得します。
     *
     * @return コンフィグのリスト
     */
    public List<ConfigEntry> getConfigs()
    {
        List<ConfigEntry> configs = new ArrayList<>();
        for (Field declaredField : this.configClass.getDeclaredFields())
        {
            if (!declaredField.isAnnotationPresent(Config.class))
                continue;
            declaredField.setAccessible(true);

            configs.add(new ConfigEntry(
                    this.config,
                    declaredField,
                    declaredField.getAnnotation(Config.class)
            ));
        }

        return configs;
    }

    /**
     * コンフィグの値として正しいかどうかを返します。
     *
     * @param name  コンフィグの名前
     * @param value コンフィグの値
     * @throws IllegalArgumentException コンフィグの値が正しくない場合
     */
    public void checkValid(String name, String value)
            throws IllegalArgumentException
    {
        if (!this.map.containsKey(name))
            throw new IllegalArgumentException("No config has found with name: " + name);

        ConfigEntry config = this.map.get(name);

        if (config.getDefine().enums().length != 0)
        {
            List<String> enums = new ArrayList<>(Arrays.asList(config.getDefine().enums()));
            if (!enums.contains(value))
                throw new IllegalArgumentException("The value is not in the enum list");
        }

        double min = config.getDefine().min();
        double max = config.getDefine().max();

        if (config.getField().getType() == Long.class || config.getField().getType() == long.class)
        {
            long longValue = Long.parseLong(value);
            if (min != -1 && longValue < min)
                throw new IllegalArgumentException("The value is less than the min value: " + name);
            if (max != -1 && longValue > max)
                throw new IllegalArgumentException("The value is greater than the max value: " + name);
        }
        else if (config.getField().getType() == Integer.class || config.getField().getType() == int.class)
        {
            int intValue = Integer.parseInt(value);
            if (min != -1 && intValue < min)
                throw new IllegalArgumentException("The value is less than the min value: " + name);
            if (max != -1 && intValue > max)
                throw new IllegalArgumentException("The value is greater than the max value: " + name);
        }
        else if (config.getField().getType() == Double.class || config.getField().getType() == double.class)
        {
            double doubleValue = Double.parseDouble(value);
            if (min != -1 && doubleValue < min)
                throw new IllegalArgumentException("The value is less than the min value: " + name);
            if (max != -1 && doubleValue > max)
                throw new IllegalArgumentException("The value is greater than the max value: " + name);
        }
        else if (config.getField().getType() == String.class)
        {
            if (min != -1 && value.length() < min)
                throw new IllegalArgumentException("The value is less than the min value: " + name);
            if (max != -1 && value.length() > max)
                throw new IllegalArgumentException("The value is greater than the max value: " + name);
        }
        else if (config.getField().getType() == Boolean.class || config.getField().getType() == boolean.class)
        {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                throw new IllegalArgumentException("The value is not a boolean: " + name);
        }
        else if (config.getField().getType().isEnum())
        {
            if (Arrays.stream(config.getField().getType().getEnumConstants())
                    .map(o -> (Enum<?>) o)
                    .map(Enum::name)
                    .noneMatch(s -> s.equalsIgnoreCase(value)))
                throw new IllegalArgumentException("The value is not in the enum list");
        }
    }

    /**
     * コンフィグの値を設定します。
     *
     * @param name  コンフィグの名前
     * @param value コンフィグの値
     * @return コンフィグの値
     * @throws IllegalArgumentException コンフィグの値が正しくない場合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean setValue(String name, String value)
            throws IllegalArgumentException
    {
        checkValid(name, value);

        ConfigEntry config = this.map.get(name);
        if (config.getField().getType() == Long.class || config.getField().getType() == long.class)
            config.setValue(Long.parseLong(value));
        else if (config.getField().getType() == Integer.class || config.getField().getType() == int.class)
            config.setValue(Integer.parseInt(value));
        else if (config.getField().getType() == Double.class || config.getField().getType() == double.class)
            config.setValue(Double.parseDouble(value));
        else if (config.getField().getType() == String.class)
            config.setValue(value);
        else if (config.getField().getType() == Boolean.class || config.getField().getType() == boolean.class)
            config.setValue(Boolean.parseBoolean(value));
        else if (config.getField().getType().isEnum())
            config.setValue(Enum.valueOf((Class<Enum>) config.getField().getType(), value));
        else
            return false;
        return true;
    }

    /**
     * コンフィグの値を取得します。
     *
     * @param name コンフィグの名前
     * @return コンフィグの値
     */
    public ConfigEntry getConfig(String name)
    {
        if (!this.map.containsKey(name))
            return null;
        return this.map.get(name);
    }

    public ConfigEntry getConfigAllowRanged(String name, boolean isMax)
    {
        if (this.map.containsKey(name))
            return this.map.get(name);

        if (name.length() < 1)
            return null;

        String prefix = isMax ? "max": "min";
        name = prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
        if (this.map.containsKey(name))
            return this.map.get(name);
        return null;
    }

    public ConfigEntry getConfigAllowRangedAny(String name)
    {
        ConfigEntry config = getConfigAllowRanged(name, false);
        if (config != null)
            return config;
        return getConfigAllowRanged(name, true);
    }

    @AllArgsConstructor
    public static class ConfigEntry
    {
        private final Object clazz;
        @Getter
        private final Field field;

        @Getter
        Config define;

        public Object getValue()
        {
            try
            {
                return this.field.get(this.clazz);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        public boolean setValue(Object value)
        {
            try
            {
                this.field.set(this.clazz, value);
                return true;
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
                return false;
            }
        }

        public Integer intValue()
        {
            return (Integer) getValue();
        }

        public Boolean booleanValue()
        {
            return (Boolean) getValue();
        }

        public String stringValue()
        {
            return (String) getValue();
        }

        public Double doubleValue()
        {
            return (Double) getValue();
        }

        public Long longValue()
        {
            return (Long) getValue();
        }

        public <T extends Enum<T>> T enumValue()
        {
            return (T) getValue();
        }

        public boolean isRanged()
        {
            return this.define.min() != -1 || this.define.max() != -1;
        }
    }
}
