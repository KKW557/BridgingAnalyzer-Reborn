package icu.suc.kevin557.bridginganalyzer.listeners;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.suc.kevin557.bridginganalyzer.analyzers.Analyzer;
import icu.suc.kevin557.bridginganalyzer.configs.I18n;
import icu.suc.kevin557.bridginganalyzer.utils.PermUtils;
import icu.suc.kevin557.bridginganalyzer.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class AnalyzerListener implements Listener
{
    @EventHandler
    public void onPvP(final EntityDamageByEntityEvent e)
    {
        Entity entity = e.getEntity();
        Entity damager = e.getDamager();

        if (e.isCancelled())
        {
            return;
        }

        if (entity == null)
        {
            return;
        }

        if (damager == null)
        {
            return;
        }

        if (entity.getType() != EntityType.PLAYER)
        {
            return;
        }

        if (damager.getType() == EntityType.PLAYER)
        {
            Analyzer entityAnalyzer = BridgingAnalyzer.getInstance().getAnalyzer(((Player) entity));
            Analyzer damagerAnalyzer = BridgingAnalyzer.getInstance().getAnalyzer(((Player) damager));

            if (!entityAnalyzer.isPvPEnabled())
            {
                e.setCancelled(true);
            }
            else if (!damagerAnalyzer.isPvPEnabled())
            {
                e.setCancelled(true);
                damagerAnalyzer.setPvPEnabled(true);
                TitleUtils.sendTitle(((Player) damager), "", I18n.format("pvpWarn"), BridgingAnalyzer.getInstance().settings.pvpWarnMessageFadeIn, BridgingAnalyzer.getInstance().settings.pvpWarnMessageStay, BridgingAnalyzer.getInstance().settings.pvpWarnMessageFadeOut);
                ((Player) entity).damage(0.0D);
//                ((Player) entity).setNoDamageTicks(60);
//                ((Player) damager).setNoDamageTicks(60);
            }
        }
        else if (damager instanceof Projectile)
        {
            ProjectileSource shooter = ((Projectile) damager).getShooter();

            if (shooter instanceof Player)
            {
                Analyzer entityAnalyzer = BridgingAnalyzer.getInstance().getAnalyzer(((Player) entity));
                Analyzer shooterAnalyzer = BridgingAnalyzer.getInstance().getAnalyzer(((Player) shooter));

                if (!entityAnalyzer.isPvPEnabled())
                {
                    e.setCancelled(true);
                }
                else if (!shooterAnalyzer.isPvPEnabled())
                {
                    e.setCancelled(true);
                    shooterAnalyzer.setPvPEnabled(true);
                    TitleUtils.sendTitle((Player) shooter, "", I18n.format("pvpWarn"), BridgingAnalyzer.getInstance().settings.pvpWarnMessageFadeIn, BridgingAnalyzer.getInstance().settings.pvpWarnMessageStay, BridgingAnalyzer.getInstance().settings.pvpWarnMessageFadeOut);
                    ((Player) entity).damage(0.0D);
//                    ((Player) entity).setNoDamageTicks(60);
//                    ((Player) shooter).setNoDamageTicks(60);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Entity entity = e.getEntity();

        if (entity.getType() == EntityType.PLAYER)
        {
            Analyzer analyzer = BridgingAnalyzer.getInstance().getAnalyzer((Player) entity);

            double damage = e.getFinalDamage();
            double maxHealth = ((Player) entity).getMaxHealth();

            if (damage > maxHealth)
            {
                analyzer.teleport();
                analyzer.reset();

                TitleUtils.sendTitle((Player) entity, "", I18n.format("deadlyDamage", damage / 2), BridgingAnalyzer.getInstance().settings.damageMessageFadeIn, BridgingAnalyzer.getInstance().settings.damageMessageStay, BridgingAnalyzer.getInstance().settings.damageMessageFadeOut);
            }
            else if (damage > maxHealth / 2)
            {
                TitleUtils.sendTitle((Player) entity, "", I18n.format("hugeDamage", damage / 2), BridgingAnalyzer.getInstance().settings.damageMessageFadeIn, BridgingAnalyzer.getInstance().settings.damageMessageStay, BridgingAnalyzer.getInstance().settings.damageMessageFadeOut);
            }

            e.setDamage(0.0);
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Player player = e.getPlayer();
        Block block = e.getBlockPlaced();

        if (PermUtils.canBuild(player))
        {
            BridgingAnalyzer.getInstance().getBlocks().remove(block);

            return;
        }

        BridgingAnalyzer.getInstance().addBlock(player, block);

        Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () ->
        {
            ItemStack item = e.getItemInHand().clone();
            item.setAmount(1);
            player.getInventory().addItem(item);
        }, 1);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e)
    {
        if (PermUtils.canBuild(e.getPlayer()))
        {
            return;
        }

        if (!BridgingAnalyzer.getInstance().isPlacedByPlayer(e.getBlock()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onVoiding(final PlayerMoveEvent e)
    {
        if (e.getTo().getY() < 0)
        {
            Analyzer analyzer = BridgingAnalyzer.getInstance().getAnalyzer(e.getPlayer());
            analyzer.teleport();
            analyzer.reset();
        }
    }

    /**
     * Cancel Liquid Flow
     */
    @EventHandler
    public void onBlockFromTo(final BlockFromToEvent e)
    {
        e.setCancelled(true);
    }
}
