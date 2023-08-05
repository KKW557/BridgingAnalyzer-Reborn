package icu.kevin557.bridginganalyzer.commands;

import icu.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.kevin557.bridginganalyzer.configs.I18n;
import icu.kevin557.bridginganalyzer.utils.BlockUtils;
import icu.kevin557.bridginganalyzer.utils.ChatUtils;
import icu.kevin557.bridginganalyzer.utils.PermUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StuckCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("Only player!");
            return true;
        }

        PermUtils.check(sender, "bridginganalyzer.stuck", () ->
        {
            Player player = (Player) sender;

            if (BlockUtils.isCheckBlock(player.getLocation().getBlock(), true))
            {
                for (int x = -3; x < 3; x++)
                {
                    for (int y = -3; y < 3; y++)
                    {
                        for (int z = -3; z < 3; z++)
                        {
                            final Block block = player.getLocation().add(x, y, z).getBlock();
                            BridgingAnalyzer.getInstance().breakBlock(player, block, false);
                        }
                    }
                }

                ChatUtils.sendMessage(player, I18n.format("destroyedNear"));
            }
            else
            {
                ItemStack pickaxe = new ItemStack(Material.GOLD_PICKAXE, 1);
                pickaxe.setDurability((short) 27);
                player.getInventory().addItem(pickaxe);

                ChatUtils.sendMessage(player, I18n.format("givingPickaxe"));
            }
        });

        return true;
    }
}
