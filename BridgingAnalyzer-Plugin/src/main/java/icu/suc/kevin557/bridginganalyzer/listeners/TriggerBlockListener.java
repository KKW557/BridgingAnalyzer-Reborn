package icu.suc.kevin557.bridginganalyzer.listeners;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.suc.kevin557.bridginganalyzer.analyzers.Analyzer;
import icu.suc.kevin557.bridginganalyzer.configs.I18n;
import icu.suc.kevin557.bridginganalyzer.editors.EditorManager;
import icu.suc.kevin557.bridginganalyzer.utils.ParticleUtils;
import icu.suc.kevin557.bridginganalyzer.utils.SoundUtils;
import icu.suc.kevin557.bridginganalyzer.utils.TitleUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static icu.suc.kevin557.bridginganalyzer.utils.BlockUtils.*;

public class TriggerBlockListener implements Listener
{
    @EventHandler
    public void antiCover(BlockPlaceEvent e)
    {
        if (BridgingAnalyzer.getInstance().settings.antiCover)
        {
            Player player = e.getPlayer();

            if (player != null)
            {
                if (player.getGameMode() == GameMode.CREATIVE)
                {
                    return;
                }

                Block block = e.getBlockPlaced();

                if (isTriggerBlock(block.getRelative(BlockFace.DOWN), false) || isTriggerBlock(block.getRelative(BlockFace.DOWN, 2), false))
                {
                    Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> BridgingAnalyzer.getInstance().breakBlock(player, block, true), BridgingAnalyzer.getInstance().settings.antiCoverDelay);
                }
            }
        }
    }

    @EventHandler
    public void triggerCheck(final PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        Block toBlock = e.getTo().getBlock();

        if (e.getFrom().getBlock().equals(toBlock))
        {
            return;
        }

        if (player.getNoDamageTicks() != 0)
        {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR)
        {
            return;
        }

        if (isCheckBlock(toBlock, true))
        {
            player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.checkNoDamageTicks);

            Analyzer analyzer = BridgingAnalyzer.getInstance().getAnalyzer(player);

            World world = player.getWorld();
            Location location = toBlock.getLocation().add(0.5D, 1.0D, 0.5D);
            Location playerLocation = player.getLocation();
            location.setYaw(playerLocation.getYaw());
            location.setPitch(playerLocation.getPitch());
            analyzer.setLocation(location);

            EditorManager manager = BridgingAnalyzer.getInstance().getEditorManager();
            Inventory inventory = manager.getInventory(toBlock.getType() == Material.AIR ?
                    toBlock.getRelative(BlockFace.DOWN) :
                    toBlock);
            manager.applyPlayer(player, inventory);

            ParticleUtils.playRing(location.clone().add(0, 0.5D, 0), BridgingAnalyzer.getInstance().settings.checkParticle);
            TitleUtils.sendTitle(player, "", I18n.format("checked"), BridgingAnalyzer.getInstance().settings.checkMessageFadeIn, BridgingAnalyzer.getInstance().settings.checkMessageStay, BridgingAnalyzer.getInstance().settings.checkMessageFadeOut);
            SoundUtils.playSound(world, toBlock.getLocation(), BridgingAnalyzer.getInstance().settings.checkSound);
        }
    }

    @EventHandler
    public void triggerEnd(final PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        Block toBlock = e.getTo().getBlock();

        if (e.getFrom().getBlock().equals(toBlock))
        {
            return;
        }

        if (player.getNoDamageTicks() != 0)
        {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR)
        {
            return;
        }

        if (isEndBlock(toBlock, true))
        {
            player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.endNoDamageTicks);

            ParticleUtils.playRing(toBlock.getLocation().add(0.5D, 0.1D, 0.5D), BridgingAnalyzer.getInstance().settings.endParticle);
            SoundUtils.playSound(player.getWorld(), toBlock.getLocation(), BridgingAnalyzer.getInstance().settings.endSound);
            BridgingAnalyzer.getInstance().getAnalyzer(player).breakBlocksEnded();
        }
    }

    @EventHandler
    public void triggerKnockback(final PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        Block toBlock = e.getTo().getBlock();

        if (e.getFrom().getBlock().equals(toBlock))
        {
            return;
        }

        if (player.getNoDamageTicks() != 0)
        {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        if (isKbBlock(toBlock, true))
        {
            player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.kbNoDamageTicks);

            Location location = player.getLocation();
            double ran = 90.0D + Math.random() * 30.0D - 15.0D;
            Vector vector = new Vector(
                    location.getX() + 3.0D * Math.cos(Math.toRadians(location.getYaw() + ran)) - location.getX(),
                    0.0D,
                    location.getZ() + 3.0D * Math.sin(Math.toRadians(location.getYaw() + ran)) - location.getZ());

            Location attackFrom = location.add(vector);
            attackFrom.setY(location.getY() + 1.2D);
            Vector normalize = attackFrom.toVector().subtract(player.getLocation().toVector()).normalize();

            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                player.setNoDamageTicks(0);
                player.damage(0.0D);
                player.setVelocity(normalize.multiply(-BridgingAnalyzer.getInstance().settings.velocityX).setY(BridgingAnalyzer.getInstance().settings.velocityY));
            }, BridgingAnalyzer.getInstance().settings.kbDelay);
        }
    }

    @EventHandler
    public void triggerSpawn(final PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        Block toBlock = e.getTo().getBlock();

        if (e.getFrom().getBlock().equals(toBlock))
        {
            return;
        }

        if (player.getNoDamageTicks() != 0)
        {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR)
        {
            return;
        }

        if (isSpawnBlock(toBlock, true))
        {
            player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.spawnNoDamageTicks);

            Analyzer analyzer = BridgingAnalyzer.getInstance().getAnalyzer(player);

            World world = player.getWorld();
            analyzer.setLocation(world.getSpawnLocation().add(0.5D, 0, 0.5D));
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                analyzer.teleport();
                analyzer.reset();
            }, BridgingAnalyzer.getInstance().settings.spawnProcessingTicks);

            ParticleUtils.playRing(toBlock.getLocation().add(0.5D, 1.5D, 0.5D), BridgingAnalyzer.getInstance().settings.spawnParticle);
            TitleUtils.sendTitle(player, "", I18n.format("spawned"), BridgingAnalyzer.getInstance().settings.spawnMessageFadeIn, BridgingAnalyzer.getInstance().settings.spawnMessageStay, BridgingAnalyzer.getInstance().settings.spawnMessageFadeOut);
            SoundUtils.playSound(world, toBlock.getLocation(), BridgingAnalyzer.getInstance().settings.spawnSound);
        }
    }

    @EventHandler
    public void triggerBoost(final PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        Block toBlock = e.getTo().getBlock();

        if (e.getFrom().getBlock().equals(toBlock))
        {
            return;
        }

        if (player.getNoDamageTicks() != 0)
        {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR)
        {
            return;
        }

        if (isBoostBlock(toBlock, true))
        {
            player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.boostNoDamageTicks);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, BridgingAnalyzer.getInstance().settings.boostDuration, BridgingAnalyzer.getInstance().settings.boostAmplifier), true);
        }
    }

    @EventHandler
    public void triggerElevatorUp(final PlayerMoveEvent e)
    {
        Location to = e.getTo();
        Location from = e.getFrom();

        if (to.getY() <= from.getY())
        {
            return;
        }

        Player player = e.getPlayer();

        if (player.getNoDamageTicks() != 0)
        {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR)
        {
            return;
        }

        Block block = from.getBlock();

        if (isLiftBlock(block, true))
        {
            player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.liftNoDamageTicks);

            Block target = block.getRelative(BlockFace.UP, 2);

            while (target.getType() == Material.AIR && target.getY() < 255)
            {
                target = target.getRelative(BlockFace.UP);
            }

            if (isLiftBlock(target, false))
            {
                player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.liftNoDamageTicks);

                Location location = target.getLocation().add(0.5D, 1.0D, 0.5D);
                Location location1 = player.getLocation();
                location.setYaw(location1.getYaw());
                location.setPitch(location1.getPitch());

                player.teleport(location);

                SoundUtils.playSound(target.getWorld(), location, BridgingAnalyzer.getInstance().settings.liftSound);
            }
        }
    }

    @EventHandler
    public void triggerElevatorDown(final PlayerToggleSneakEvent e)
    {
        Player player = e.getPlayer();

        if (player.getNoDamageTicks() != 0)
        {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR)
        {
            return;
        }

        Block block = player.getLocation().getBlock();

        if (isLiftBlock(block, true))
        {
            player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.liftNoDamageTicks);

            Block target = block.getRelative(BlockFace.DOWN, 2);

            while (target.getType() == Material.AIR && target.getY() > 0)
            {
                target = target.getRelative(BlockFace.DOWN);
            }

            if (isLiftBlock(target, false))
            {
                player.setNoDamageTicks(BridgingAnalyzer.getInstance().settings.liftNoDamageTicks);

                Location location = target.getLocation().add(0.5D, 1.0D, 0.5D);
                Location location1 = player.getLocation();
                location.setYaw(location1.getYaw());
                location.setPitch(location1.getPitch());
                player.teleport(location);

                SoundUtils.playSound(target.getWorld(), location, BridgingAnalyzer.getInstance().settings.liftSound);
            }
        }
    }
}
