package icu.suc.kevin557.bridginganalyzer.listeners;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.suc.kevin557.bridginganalyzer.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class WorldListener implements Listener
{
    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent e)
    {
        if (BridgingAnalyzer.getInstance().settings.noHunger)
        {
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        if (!BridgingAnalyzer.getInstance().settings.blockDrop)
        {
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        BridgingAnalyzer.getInstance().getAnalyzer(player).breakBlocks();
        BridgingAnalyzer.getInstance().getAnalyzerMap().remove(player);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        if (player.hasPermission("bridginganalyzer.clear") && BridgingAnalyzer.getInstance().settings.clearInventory)
        {
            InventoryUtils.clear(player);
        }
    }
}
