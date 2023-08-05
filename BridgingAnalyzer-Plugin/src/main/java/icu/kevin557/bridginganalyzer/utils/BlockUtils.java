package icu.kevin557.bridginganalyzer.utils;

import icu.kevin557.bridginganalyzer.BridgingAnalyzer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public class BlockUtils
{
    public static boolean equalsMaterialData(Block block, MaterialData materialData)
    {
        if (materialData == null)
        {
            return false;
        }

        return block.getTypeId() == materialData.getItemTypeId() && block.getData() == materialData.getData();
    }

    public static void breakBlock(Block block, boolean effect)
    {
        Material type = block.getType();

        if (type == Material.AIR)
        {
            return;
        }

        byte data = block.getData();

        if (!block.getChunk().isLoaded())
        {
            block.getChunk().load(false);
        }

        if (effect)
        {

            if (data == 0)
            {
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, type);
            }
            else
            {
                for (int i = 0; i < 4; ++i)
                {
                    for (int j = 0; j < 4; ++j)
                    {
                        for (int k = 0; k < 4; ++k)
                        {
                            block.getWorld().spigot().playEffect(block.getLocation().add((i + 0.5D) / 4, (j + 0.5D) / 4, (k + 0.5D) / 4), Effect.TILE_BREAK, type.getId(), data, 0, 0, 0, 1.0F, 1, 64);
                        }
                    }
                }

                Sound blockBreakSound = SoundUtils.getBlockBreakSound(block);
                if (blockBreakSound != null)
                {
                    block.getWorld().playSound(block.getLocation(), blockBreakSound, 1.0F, 1.0F);
                }
            }
        }

        block.setType(Material.AIR);
    }

    public static boolean isTriggerBlock(Block block, boolean down)
    {
        return isCheckBlock(block, down) || isEndBlock(block, down) || isKbBlock(block, down) || isSpawnBlock(block, down) || isBoostBlock(block, down) || isLiftBlock(block, down);
    }

    public static boolean isCheckBlock(Block block, boolean down)
    {
        return isBlock(block, BridgingAnalyzer.getInstance().settings.checkBlock, down);
    }

    public static boolean isEndBlock(Block block, boolean down)
    {
        return isBlock(block, BridgingAnalyzer.getInstance().settings.endBlock, down);
    }

    public static boolean isKbBlock(Block block, boolean down)
    {
        return isBlock(block, BridgingAnalyzer.getInstance().settings.kbBlock, down);
    }

    public static boolean isSpawnBlock(Block block, boolean down)
    {
        return isBlock(block, BridgingAnalyzer.getInstance().settings.spawnBlock, down);
    }

    public static boolean isBoostBlock(Block block, boolean down)
    {
        return isBlock(block, BridgingAnalyzer.getInstance().settings.boostBlock, down);
    }

    public static boolean isLiftBlock(Block block, boolean down)
    {
        return isBlock(block, BridgingAnalyzer.getInstance().settings.liftBlock, down);
    }

    private static boolean isBlock(Block block, MaterialData material, boolean down)
    {
        if (down && block.getType() == Material.AIR)
        {
            return BlockUtils.equalsMaterialData(block.getRelative(BlockFace.DOWN), material);
        }
        else
        {
            return BlockUtils.equalsMaterialData(block, material);
        }
    }
}
