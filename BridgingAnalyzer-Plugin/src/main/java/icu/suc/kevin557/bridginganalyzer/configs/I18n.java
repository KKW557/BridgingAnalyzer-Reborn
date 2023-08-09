package icu.suc.kevin557.bridginganalyzer.configs;

import icu.suc.kevin557.bridginganalyzer.utils.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class I18n
{
    public static File FILE;

    public static final Map<String, String> MESSAGES = new HashMap<>();

    public static void init(File file)
    {
        FILE = file;
    }

    public static void load()
    {
        try (Reader reader = new InputStreamReader(new FileInputStream(FILE), StandardCharsets.UTF_8))
        {
            Properties properties = new Properties();
            properties.load(reader);

            for (String key : properties.stringPropertyNames())
            {
                MESSAGES.put(key, properties.getProperty(key));
            }
        }
        catch (IOException e)
        {
            Logger.warning("Failed to load the translation file: %s", e.getMessage());
        }
    }

    public static String format(String key, Object... args)
    {
        String message = MESSAGES.get(key);

        if (message == null)
        {
            Logger.warning("Missing translation key \"%s\" in translation file", key);
            return key;
        }

        return String.format(MESSAGES.get(key), args);
    }
}
