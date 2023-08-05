package icu.kevin557.bridginganalyzer.commands;

import icu.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.kevin557.bridginganalyzer.analyzers.Analyzer;
import icu.kevin557.bridginganalyzer.configs.I18n;
import icu.kevin557.bridginganalyzer.utils.ChatUtils;
import icu.kevin557.bridginganalyzer.utils.PermUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PvPCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1)
        {
            PermUtils.check(sender, "bridginganalyzer.pvp.other", () ->
            {
                String name = args[0];

                Player target;

                try
                {
                    UUID uuid = UUID.fromString(name);

                    target = Bukkit.getPlayer(uuid);
                }
                catch (IllegalArgumentException e)
                {
                    target = Bukkit.getPlayer(name);
                }

                if (target == null)
                {
                    ChatUtils.sendMessage(sender, I18n.format("playerNotFound"));
                }
                else
                {
                    Analyzer analyzer = BridgingAnalyzer.getInstance().getAnalyzer(target);

                    boolean flag = !analyzer.isPvPEnabled();
                    analyzer.setPvPEnabled(flag);

                    ChatUtils.sendMessage(sender, I18n.format("target") + I18n.format(flag ? "pvpEnabled" : "pvpDisabled"));
                    ChatUtils.sendMessage(target, I18n.format(flag ? "pvpEnabled" : "pvpDisabled"));
                }
            });

            return true;
        }

        if (!(sender instanceof Player))
        {
            sender.sendMessage("Only player!");
            return true;
        }

        PermUtils.check(sender, "bridginganalyzer.pvp", () ->
        {
            Player player = (Player) sender;

            Analyzer analyzer = BridgingAnalyzer.getInstance().getAnalyzer(player);

            boolean flag = !analyzer.isPvPEnabled();
            analyzer.setPvPEnabled(flag);

            ChatUtils.sendMessage(player, I18n.format(flag ? "pvpEnabled" : "pvpDisabled"));
        });

        return true;
    }
}
