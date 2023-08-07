package icu.suc.kevin557.bridginganalyzer.utils;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.suc.kevin557.bridginganalyzer.utils.data.SoundData;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SoundUtils
{
    public static void playSound(World world, Location location, SoundData data)
    {
        if (data == null)
        {
            return;
        }

        world.playSound(location, data.getSound(), data.getVolume(), data.getPitch());
    }

    public static Sound getBlockBreakSound(Block block)
    {
        return BridgingAnalyzer.getInstance().getNMSHandler().getBlockBreakSound(block);
    }
}
