package icu.suc.kevin557.bridginganalyzer.utils;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;
import org.bukkit.entity.Player;

public class TitleUtils
{
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut)
    {
        BridgingAnalyzer.getInstance().getNMSHandler().sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }
}