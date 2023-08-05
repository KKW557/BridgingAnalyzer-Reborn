package icu.kevin557.bridginganalyzer.utils;

import icu.kevin557.bridginganalyzer.configs.I18n;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermUtils
{
    public static void check(CommandSender sender, String permission, Runnable runnable)
    {
        if (sender.hasPermission(permission))
        {
            runnable.run();
        }
        else
        {
            ChatUtils.sendMessage(sender, I18n.format("noPermission"));
        }
    }

    public static boolean canBuild(Player player)
    {
        return player.getGameMode() == GameMode.CREATIVE && player.hasPermission("bridginganalyzer.build");
    }
}
