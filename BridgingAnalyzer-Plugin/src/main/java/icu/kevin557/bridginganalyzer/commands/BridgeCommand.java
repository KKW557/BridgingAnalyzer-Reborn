package icu.kevin557.bridginganalyzer.commands;

import icu.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.kevin557.bridginganalyzer.configs.I18n;
import icu.kevin557.bridginganalyzer.utils.ChatUtils;
import icu.kevin557.bridginganalyzer.utils.PermUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class BridgeCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            PermUtils.check(sender, "bridginganalyzer", () -> ChatUtils.sendMessage(sender, I18n.format("version", BridgingAnalyzer.getInstance().getDescription().getVersion())));

            return true;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH))
        {
            case "reload":
            {
                PermUtils.check(sender, "bridginganalyzer.reload", () ->
                {
                    ChatUtils.sendMessage(sender, I18n.format("reloadStart"));
                    BridgingAnalyzer.getInstance().reloadConfig();
                    BridgingAnalyzer.getInstance().onLoad();
                    ChatUtils.sendMessage(sender, I18n.format("reloadEnd"));
                });
                break;
            }
            default:
            {
                PermUtils.check(sender, "bridginganalyzer", () -> ChatUtils.sendMessage(sender, I18n.format("version")));
                break;
            }
        }

        return true;
    }
}
