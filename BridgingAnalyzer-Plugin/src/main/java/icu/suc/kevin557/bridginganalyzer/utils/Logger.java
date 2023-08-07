package icu.suc.kevin557.bridginganalyzer.utils;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;

import java.util.logging.Level;

public class Logger
{
    private static final java.util.logging.Logger LOGGER = BridgingAnalyzer.getInstance().getLogger();

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
