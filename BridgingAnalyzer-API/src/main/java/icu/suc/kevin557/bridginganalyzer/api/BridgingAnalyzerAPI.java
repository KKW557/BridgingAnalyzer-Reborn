package icu.suc.kevin557.bridginganalyzer.api;

import icu.suc.kevin557.bridginganalyzer.BridgingAnalyzer;
import icu.suc.kevin557.bridginganalyzer.analyzers.Analyzer;
import icu.suc.kevin557.bridginganalyzer.configs.Settings;
import icu.suc.kevin557.bridginganalyzer.editors.EditorManager;
import icu.suc.kevin557.bridginganalyzer.nms.INMSHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public class BridgingAnalyzerAPI
{
    public final static BridgingAnalyzer INSTANCE = BridgingAnalyzer.getInstance();
    public final static INMSHandler NMS = INSTANCE.getNMSHandler();

    public static Set<Block> getBlocks()
    {
        return INSTANCE.getBlocks();
    }

    public static Map<Player, Analyzer> getAnalyzerMap()
    {
        return INSTANCE.getAnalyzerMap();
    }

    public static EditorManager getEditorManager()
    {
        return INSTANCE.getEditorManager();
    }

    public static Analyzer getAnalyzer(Player player)
    {
        return INSTANCE.getAnalyzer(player);
    }

    public static void addBlock(Player player, Block block)
    {
        INSTANCE.addBlock(player, block);
    }

    public static void removeBlock(Player player, Block block)
    {
        INSTANCE.removeBlock(player, block);
    }

    public static void breakBlock(Player player, Block block, boolean effect)
    {
        INSTANCE.breakBlock(player, block, effect);
    }

    public static boolean isPlacedByPlayer(Block block)
    {
        return INSTANCE.isPlacedByPlayer(block);
    }

    public static Settings getSettings()
    {
        return INSTANCE.settings;
    }
}
