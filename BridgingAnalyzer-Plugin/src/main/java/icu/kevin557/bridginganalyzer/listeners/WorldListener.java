package icu.kevin557.bridginganalyzer.listeners;

import icu.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.kevin557.bridginganalyzer.utils.InventoryUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked().getType() == EntityType.VILLAGER)
        {
            e.setCancelled(true);
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
