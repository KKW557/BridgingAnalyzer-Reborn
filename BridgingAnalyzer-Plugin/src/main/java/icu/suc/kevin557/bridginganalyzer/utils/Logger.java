package icu.suc.kevin557.bridginganalyzer.utils;

import java.util.logging.Level;

public class Logger
{
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("BridgingAnalyzer-Reborn");

    public static void info(String msg, Object... args)
    {
        log(Level.INFO, msg, args);
    }

    public static void warning(String msg, Object... args)
    {
        log(Level.WARNING, msg, args);
    }

    public static void log(Level level, String msg, Object... args)
    {
        LOGGER.log(level, String.format(msg, args));
    }
}
