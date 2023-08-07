package icu.suc.kevin557.bridginganalyzer;

import icu.suc.kevin557.bridginganalyzer.analyzers.Analyzer;
import icu.suc.kevin557.bridginganalyzer.commands.BridgeCommand;
import icu.suc.kevin557.bridginganalyzer.commands.PvPCommand;
import icu.suc.kevin557.bridginganalyzer.commands.StuckCommand;
import icu.suc.kevin557.bridginganalyzer.configs.Settings;
import icu.suc.kevin557.bridginganalyzer.configs.I18n;
import icu.suc.kevin557.bridginganalyzer.editors.EditorManager;
import icu.suc.kevin557.bridginganalyzer.listeners.AnalyzerListener;
import icu.suc.kevin557.bridginganalyzer.listeners.InventoryListener;
import icu.suc.kevin557.bridginganalyzer.listeners.TriggerBlockListener;
import icu.suc.kevin557.bridginganalyzer.listeners.WorldListener;
import icu.suc.kevin557.bridginganalyzer.nms.INMSHandler;
import icu.suc.kevin557.bridginganalyzer.utils.BlockUtils;
import icu.suc.kevin557.bridginganalyzer.utils.ConfigUtils;
import icu.suc.kevin557.bridginganalyzer.utils.Logger;
import icu.suc.kevin557.bridginganalyzer.utils.data.ParticleRingData;
import icu.suc.kevin557.bridginganalyzer.utils.data.SoundData;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class BridgingAnalyzer extends JavaPlugin
{
    private static BridgingAnalyzer instance;
    private INMSHandler nmsHandler;

    public Settings settings;

    private Set<Block> blocks;
    private Map<Player, Analyzer> analyzerMap;
    private EditorManager editorManager;

    @Override
    public void onEnable()
    {
        super.onEnable();

        instance = this;
        settings = new Settings();

        initNMSHandler();

        I18n.init(new File(getDataFolder(), "messages.properties"));
        saveConfigs();
        loadConfigs();

        blocks = new HashSet<>();
        analyzerMap = new HashMap<>();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        blocks.forEach(block -> block.setType(Material.AIR));
        blocks.clear();
        analyzerMap.clear();
    }

    private void initNMSHandler()
    {
        try
        {
            Class<?> clazz = Class.forName("icu.suc.kevin557.bridginganalyzer.nms." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".NMSHandler");
            if (INMSHandler.class.isAssignableFrom(clazz))
            {
                nmsHandler = (INMSHandler) clazz.getConstructor().newInstance();
            }
        }
        catch (Exception e)
        {
            Logger.warning("Couldn't initialize NMS, check your server version");
            setEnabled(false);
        }
    }

    private void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(new AnalyzerListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new TriggerBlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
    }

    private void registerCommands()
    {
        getCommand("bridge").setExecutor(new BridgeCommand());
        getCommand("stuck").setExecutor(new StuckCommand());
        getCommand("pvp").setExecutor(new PvPCommand());
    }

    private void loadConfigs()
    {
        I18n.load();

        Configuration configuration = getConfig();
        settings.noHunger = configuration.getBoolean("no-hunger", true);
        settings.clearInventory = configuration.getBoolean("clear-inventory", true);
        settings.antiCover = configuration.getBoolean("anti-cover", true);
        settings.antiCoverDelay = configuration.getInt("anti-cover-delay", 10);
        settings.breakDelay = configuration.getInt("break-delay", 10);
        settings.breakMinPeriod = configuration.getInt("break-min-period", 1);
        settings.breakMaxPeriod = configuration.getInt("break-max-period", 3);
        settings.checkBlock = ConfigUtils.loadMaterialData(configuration, "check-block", new MaterialData(133));
        settings.checkNoDamageTicks = configuration.getInt("check-no-damage-ticks", 40);
        settings.checkMessageFadeIn = configuration.getInt("check-message-fade-in", 5);
        settings.checkMessageStay = configuration.getInt("check-message-stay", 10);
        settings.checkMessageFadeOut = configuration.getInt("check-message-fade-out", 5);
        settings.checkParticle = ConfigUtils.loadParticleRingData(configuration, "check-particle", new ParticleRingData(Effect.CLOUD, 1.0D, 20));
        settings.checkSound = ConfigUtils.loadSound(configuration, "check-sound", new SoundData(Sound.ORB_PICKUP, 1.0F, 1.0F));
        settings.endBlock = ConfigUtils.loadMaterialData(configuration, "end-block", new MaterialData(152));
        settings.endNoDamageTicks = configuration.getInt("end-no-damage-ticks", 20);
        settings.endParticle = ConfigUtils.loadParticleRingData(configuration, "end-particle", new ParticleRingData(Effect.WITCH_MAGIC, 1.0D, 20));
        settings.endSound = ConfigUtils.loadSound(configuration, "end-sound", new SoundData(Sound.LEVEL_UP, 1.0F, 1.0F));
        settings.kbBlock = ConfigUtils.loadMaterialData(configuration, "kb-block", new MaterialData(103));
        settings.kbNoDamageTicks = configuration.getInt("kb-no-damage-ticks", 20);
        settings.kbDelay = configuration.getInt("kb-delay", 7);
        settings.spawnBlock = ConfigUtils.loadMaterialData(configuration, "spawn-block", new MaterialData(22));
        settings.spawnProcessingTicks = configuration.getInt("spawn-processing-ticks", 35);
        settings.spawnNoDamageTicks = configuration.getInt("spawn-no-damage-ticks", 40);
        settings.spawnMessageFadeIn = configuration.getInt("spawn-message-fade-in", 5);
        settings.spawnMessageStay = configuration.getInt("spawn-message-stay", 25);
        settings.spawnMessageFadeOut = configuration.getInt("spawn-message-fade-out", 5);
        settings.spawnParticle = ConfigUtils.loadParticleRingData(configuration, "spawn-particle", new ParticleRingData(Effect.FIREWORKS_SPARK, 1.0D, 20));
        settings.spawnSound = ConfigUtils.loadSound(configuration, "spawn-sound", new SoundData(Sound.ORB_PICKUP, 1.0F, 1.0F));
        settings.boostBlock = ConfigUtils.loadMaterialData(configuration, "boost-block", new MaterialData(147));
        settings.boostNoDamageTicks = configuration.getInt("boost-no-damage-ticks", 20);
        settings.boostDuration = configuration.getInt("boost-duration", 100);
        settings.boostAmplifier = configuration.getInt("boost-amplifier", 2);
        settings.liftBlock = ConfigUtils.loadMaterialData(configuration, "lift-block", new MaterialData(138));
        settings.liftNoDamageTicks = configuration.getInt("lift-no-damage-ticks", 20);
        settings.liftSound = ConfigUtils.loadSound(configuration, "lift-sound", new SoundData(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F));
        settings.velocityX = configuration.getDouble("velocity-x", 1.25);
        settings.velocityY = configuration.getDouble("velocity-y", 0.45);
        settings.victoryBlock = ConfigUtils.loadMaterialData(configuration, "victory-block", new MaterialData(169));
        settings.pvpDefault = configuration.getBoolean("pvp-default");
        settings.pvpWarnMessageFadeIn = configuration.getInt("pvp-warn-message-fade-in", 10);
        settings.pvpWarnMessageStay = configuration.getInt("pvp-warn-message-stay", 20);
        settings.pvpWarnMessageFadeOut = configuration.getInt("pvp-warn-message-fade-out", 10);
        settings.damageMessageFadeIn = configuration.getInt("damage-message-fade-in", 10);
        settings.damageMessageStay = configuration.getInt("damage-message-stay", 20);
        settings.damageMessageFadeOut = configuration.getInt("damage-message-fade-out", 10);

        editorManager = new EditorManager(new File(getDataFolder(), "inventories"));
        editorManager.load();
    }

    private void saveConfigs()
    {
        saveDefaultConfig();

        if (I18n.FILE != null && !I18n.FILE.exists())
        {
            saveResource("messages.properties", false);
        }
    }

    public static BridgingAnalyzer getInstance()
    {
        return instance;
    }

    public INMSHandler getNMSHandler()
    {
        return nmsHandler;
    }

    public Set<Block> getBlocks()
    {
        return blocks;
    }

    public Map<Player, Analyzer> getAnalyzerMap()
    {
        return analyzerMap;
    }

    public EditorManager getEditorManager()
    {
        return editorManager;
    }

    public Analyzer getAnalyzer(Player player)
    {
        return getAnalyzerMap().computeIfAbsent(player, k -> new Analyzer(player));
    }

    public void addBlock(Player player, Block block)
    {
        getBlocks().add(block);
        getAnalyzer(player).addBlock(block);
    }

    public void removeBlock(Player player, Block block)
    {
        getBlocks().remove(block);

        Analyzer analyzer = getAnalyzer(player);
        if (analyzer != null)
        {
            analyzer.removeBlock(block);
        }
    }

    public void breakBlock(Player player, Block block, boolean effect)
    {
        if (isPlacedByPlayer(block))
        {
            removeBlock(player, block);

            BlockUtils.breakBlock(block, effect);
        }
    }

    public boolean isPlacedByPlayer(Block block)
    {
        return getBlocks().contains(block);
    }
}
