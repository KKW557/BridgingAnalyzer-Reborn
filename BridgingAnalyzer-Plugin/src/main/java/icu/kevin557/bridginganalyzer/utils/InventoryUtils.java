package icu.kevin557.bridginganalyzer.utils;

import icu.kevin557.bridginganalyzer.BridgingAnalyzer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils
{
    public static final int PLAYER_HOTBAR_LENGTH = 9;
    public static final int PLAYER_STORAGE_LENGTH = 27;

    public static void setItemInOffHand(Player player, ItemStack itemStack)
    {
        BridgingAnalyzer.getInstance().getNMSHandler().setItemInOffHand(player, itemStack);
    }

    public static boolean isNullOrAir(ItemStack itemStack)
    {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public static void clear(Player player)
    {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
    }
}
