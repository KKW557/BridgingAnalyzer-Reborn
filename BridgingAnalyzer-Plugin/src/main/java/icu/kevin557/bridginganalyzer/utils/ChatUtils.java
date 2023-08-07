package icu.kevin557.bridginganalyzer.utils;

import icu.kevin557.bridginganalyzer.configs.I18n;
import org.bukkit.command.CommandSender;

public class ChatUtils
{
    public static void sendMessage(CommandSender receiver, String message)
    {
        receiver.sendMessage(I18n.format("prefix") + message);
    }
}
