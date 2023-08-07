package icu.suc.kevin557.bridginganalyzer.utils;

import icu.suc.kevin557.bridginganalyzer.utils.data.ParticleRingData;
import icu.suc.kevin557.bridginganalyzer.utils.data.SoundData;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public class ConfigUtils
{
    public static SoundData loadSound(Configuration configuration, String path, SoundData def)
    {
        ConfigurationSection section = configuration.getConfigurationSection(path);

        if (section.getKeys(false).isEmpty())
        {
            return null;
        }

        Sound sound;
        try
        {
            sound = Sound.valueOf(section.getString("type"));
        }
        catch (IllegalArgumentException | NullPointerException e)
        {
            sound = def.getSound();
        }

        try
        {
            return new SoundData(sound, (float) section.getDouble("volume", def.getVolume()), (float) section.getDouble("pitch", def.getPitch()));
        }
        catch (NullPointerException e)
        {
            return def;
        }
    }

    public static MaterialData loadMaterialData(Configuration configuration, String path, MaterialData def)
    {
        ConfigurationSection section = configuration.getConfigurationSection(path);

        if (section.getKeys(false).isEmpty())
        {
            return null;
        }

        try
        {
            return new MaterialData(section.getInt("type", def.getItemTypeId()), (byte) section.getInt("data", def.getData()));
        }
        catch (NullPointerException e)
        {
            return def;
        }
    }

    public static ParticleRingData loadParticleRingData(Configuration configuration, String path, ParticleRingData def)
    {
        ConfigurationSection section = configuration.getConfigurationSection(path);

        if (section.getKeys(false).isEmpty())
        {
            return null;
        }

        Effect effect;
        try
        {
            effect = Effect.valueOf(section.getString("effect"));

            if (effect.getType() != Effect.Type.PARTICLE)
            {
                effect = def.getEffect();
            }
        }
        catch (NullPointerException | IllegalArgumentException e)
        {
            effect = def.getEffect();
        }

        try
        {
            return new ParticleRingData(effect, section.getDouble("radius", def.getRadius()), section.getInt("count", def.getCount()));
        }
        catch (NullPointerException e)
        {
            return def;
        }
    }

    public static ItemStack[] loadItemStack(Configuration configuration, String path, int length)
    {
        ConfigurationSection section = configuration.getConfigurationSection(path);

        ItemStack[] itemStacks = new ItemStack[length];

        if (section == null)
        {
            return itemStacks;
        }

        for (String s : section.getKeys(false))
        {
            itemStacks[Integer.parseInt(s)] = section.getItemStack(s, null);
        }

        return itemStacks;
    }
}
