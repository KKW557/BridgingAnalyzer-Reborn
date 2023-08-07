package icu.suc.kevin557.bridginganalyzer.analyzers;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.suc.kevin557.bridginganalyzer.utils.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Analyzer
{
    private final Player player;

    private final List<Block> blocks;

    private Location location;

    private boolean pvpEnabled;

    public Analyzer(Player player)
    {
        this.player = player;

        this.blocks = new LinkedList<>();

        this.location = player.getWorld().getSpawnLocation().add(0.5D, 0, 0.5D);

        this.pvpEnabled = BridgingAnalyzer.getInstance().settings.pvpDefault;
    }

    public void breakBlocks()
    {
        new BreakRunnable(this);
    }

    public void breakBlocksEnded()
    {
        for (final Block block : blocks)
        {
            if (block.getType() != Material.AIR)
            {
                block.setType(BridgingAnalyzer.getInstance().settings.victoryBlock.getItemType());
                block.setData(BridgingAnalyzer.getInstance().settings.victoryBlock.getData());
            }
        }
        teleport();
        breakBlocks();
    }

    public void reset()
    {
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        breakBlocks();
    }

    public void teleport()
    {
        player.setFallDistance(0);
        player.teleport(location);
    }

    public void addBlock(Block block)
    {
        blocks.add(block);
    }

    public void removeBlock(Block block)
    {
        blocks.remove(block);
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public boolean isPvPEnabled()
    {
        return pvpEnabled;
    }

    public void setPvPEnabled(boolean pvpEnabled)
    {
        this.pvpEnabled = pvpEnabled;
    }

    private static class BreakRunnable implements Runnable
    {
        Analyzer analyzer;
        BukkitTask task;

        public BreakRunnable(Analyzer analyzer)
        {
            if (analyzer.blocks.isEmpty())
            {
                return;
            }

            this.analyzer = analyzer;

            task = Bukkit.getScheduler().runTaskTimer(BridgingAnalyzer.getInstance(), this, BridgingAnalyzer.getInstance().settings.breakDelay, Math.min(BridgingAnalyzer.getInstance().settings.breakMinPeriod + 60 / analyzer.blocks.size(), BridgingAnalyzer.getInstance().settings.breakMaxPeriod));
        }

        @Override
        public void run()
        {
            if (analyzer.blocks.isEmpty())
            {
                task.cancel();
            }

            Block block = null;

            while (!analyzer.blocks.isEmpty() && block == null)
            {
                block = analyzer.blocks.get(0);
                analyzer.blocks.remove(0);
                BridgingAnalyzer.getInstance().getBlocks().remove(block);
            }

            if (block != null && !BridgingAnalyzer.getInstance().isPlacedByPlayer(block))
            {
                BlockUtils.breakBlock(block, true);
            }
        }
    }
}
