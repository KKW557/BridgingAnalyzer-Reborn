package icu.kevin557.bridginganalyzer.listeners;

import icu.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.kevin557.bridginganalyzer.editors.EditorManager;
import icu.kevin557.bridginganalyzer.utils.BlockUtils;
import icu.kevin557.bridginganalyzer.utils.PermUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e)
    {
        Player player = e.getPlayer();

        if (!PermUtils.canBuild(player))
        {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        if (e.hasItem())
        {
            return;
        }

        Block block = e.getClickedBlock();
        if (BlockUtils.isCheckBlock(block, false))
        {
            player.openInventory(BridgingAnalyzer.getInstance().getEditorManager().getInventory(block));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(final InventoryClickEvent e)
    {
        if (BridgingAnalyzer.getInstance().getEditorManager().isEditor(e.getClickedInventory()))
        {
            if (ArrayUtils.contains(EditorManager.DISABLED_SLOTS, e.getRawSlot()))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e)
    {
        Inventory inventory = e.getInventory();

        EditorManager manager = BridgingAnalyzer.getInstance().getEditorManager();
        if (manager.isEditor(inventory))
        {
            manager.save(inventory);
        }
    }
}
