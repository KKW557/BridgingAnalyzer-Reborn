package icu.suc.kevin557.bridginganalyzer.nms.v1_8_R3;

import icu.suc.kevin557.bridginganalyzer.utils.Logger;
import icu.suc.kevin557.bridginganalyzer.nms.INMSHandler;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NMSHandler implements INMSHandler
{
    private final Map<Block, Sound> soundCache = new HashMap<>();

    @Override
    public Sound getBlockBreakSound(Block block)
    {
        if (soundCache.containsKey(block))
        {
            return soundCache.get(block);
        }

        try
        {
            Field field = CraftSound.class.getDeclaredField("sounds");
            field.setAccessible(true);

            String[] sounds = (String[]) field.get(null);

            Method getNMSBlock = CraftBlock.class.getDeclaredMethod("getNMSBlock");
            getNMSBlock.setAccessible(true);
            net.minecraft.server.v1_8_R3.Block nmsBlock = (net.minecraft.server.v1_8_R3.Block) getNMSBlock.invoke(block);

            for (Sound sound : Sound.values())
            {
                if (nmsBlock.stepSound.getBreakSound().equals(sounds[sound.ordinal()]))
                {
                    soundCache.put(block, sound);
                    return sound;
                }
            }
        }
        catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            Logger.info("Couldn't found sound of \"%s\"", block.getType());
        }

        return null;
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut)
    {
        sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(title)));
        sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatComponentText(subtitle)));
        sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut));
    }

    @Override
    public void setItemInOffHand(Player player, ItemStack itemStack)
    {
        // NO-OP
    }

    private void sendPacket(Player player, Packet<?> packet)
    {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
