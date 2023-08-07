package icu.suc.kevin557.bridginganalyzer.nms;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public interface INMSHandler
{
    Sound getBlockBreakSound(Block block);

    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    void setItemInOffHand(Player player, ItemStack itemStack);
}
