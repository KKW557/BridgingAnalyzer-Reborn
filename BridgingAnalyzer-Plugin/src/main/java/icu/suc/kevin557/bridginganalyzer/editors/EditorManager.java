package icu.suc.kevin557.bridginganalyzer.editors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import icu.suc.kevin557.bridginganalyzer.utils.Logger;
import icu.suc.kevin557.bridginganalyzer.configs.I18n;
import icu.suc.kevin557.bridginganalyzer.utils.ConfigUtils;
import icu.suc.kevin557.bridginganalyzer.utils.InventoryUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static icu.suc.kevin557.bridginganalyzer.utils.InventoryUtils.*;

public class EditorManager
{
    public final static int[] DISABLED_SLOTS = {27, 28, 29, 30, 31, 32, 33, 34, 35, 45, 46, 52, 53};

    private final File dir;
    private final BiMap<Block, Inventory> inventoryMap;

    public EditorManager(File dir)
    {
        this.dir = dir;
        inventoryMap = HashBiMap.create();
    }

    public void load()
    {
        dir.mkdir();

        for (File file : FileUtils.listFiles(dir, FileFilterUtils.suffixFileFilter("yml"), null))
        {
            try
            {
                String[] location = file.getName().split("\\.");
                Block block = Bukkit.getWorld(location[0]).getBlockAt(Integer.parseInt(location[1]), Integer.parseInt(location[2]), Integer.parseInt(location[3]));

                Configuration configuration = YamlConfiguration.loadConfiguration(file);

                ItemStack[] storage = ConfigUtils.loadItemStack(configuration, "storage", InventoryUtils.PLAYER_STORAGE_LENGTH);
                ItemStack[] hotbar = ConfigUtils.loadItemStack(configuration, "hotbar", InventoryUtils.PLAYER_HOTBAR_LENGTH);
                ItemStack[] armors = ConfigUtils.loadItemStack(configuration, "armors", 4);
                ItemStack offHand = configuration.getItemStack("offHand", null);

                inventoryMap.put(block, newInventory(storage, hotbar, armors, offHand));
            }
            catch (Exception e)
            {
               Logger.warning("Failed to load the file '%s': %s", file.getName(), e.getMessage());
            }
        }
    }

    public void save(Inventory inventory)
    {
        Block block = getBlock(inventory);
        File file = new File(dir, String.format("%s.%s.%s.%s.yml", block.getWorld().getName(), block.getX(), block.getY(), block.getZ()));

        shouldSave:
        {
            ItemStack[] contents = inventory.getContents();
            for (int i = 0; i < contents.length; i++)
            {
                if (!ArrayUtils.contains(DISABLED_SLOTS, i) && !isNullOrAir(contents[i]))
                {
                    break shouldSave;
                }
            }

            if (file.exists())
            {
                file.delete();
            }

            return;
        }

        FileConfiguration configuration = new YamlConfiguration();

        createItemStacksSection(configuration, "storage", getStorage(inventory));
        createItemStacksSection(configuration, "hotbar", getHotbar(inventory));
        createItemStacksSection(configuration, "armors", getArmors(inventory));
        ItemStack offHand = getOffHand(inventory);
        if (!isNullOrAir(offHand))
        {
            configuration.set("offHand", offHand);
        }

        try
        {
            dir.mkdir();
            configuration.save(file);
        }
        catch (IOException e)
        {
            Logger.warning("Failed to save the file '%s': %s", file.getName(), e.getMessage());
        }
    }

    private void createItemStacksSection(FileConfiguration configuration, String path, ItemStack[] itemStacks)
    {
        shouldSave:
        {
            for (int i = 0; i < itemStacks.length; i++)
            {
                if (!isNullOrAir(itemStacks[i]))
                {
                    break shouldSave;
                }
            }
            return;
        }

        Map<Integer, ItemStack> map = new HashMap<>();

        for (int i = 0; i < itemStacks.length; i++)
        {
            ItemStack itemStack = itemStacks[i];

            if (isNullOrAir(itemStack))
            {
                continue;
            }

            map.put(i, itemStack);
        }

        if (!map.isEmpty())
        {
            ConfigurationSection section = configuration.createSection(path);

            map.forEach((slot, itemStack) -> section.set(String.valueOf(slot), itemStack));
        }
    }

    public Map<Block, Inventory> getInventoryMap()
    {
        return inventoryMap;
    }

    public Inventory getInventory(Block block)
    {
        return inventoryMap.computeIfAbsent(block, k -> newInventory());
    }

    public Block getBlock(Inventory inventory)
    {
        return inventoryMap.inverse().get(inventory);
    }

    public boolean isEditor(Inventory inventory)
    {
        return inventoryMap.containsValue(inventory);
    }

    private Inventory newInventory()
    {
        return newInventory(new ItemStack[InventoryUtils.PLAYER_STORAGE_LENGTH], new ItemStack[InventoryUtils.PLAYER_HOTBAR_LENGTH], new ItemStack[4], null);
    }

    private Inventory newInventory(ItemStack[] storage, ItemStack[] hotbar, ItemStack[] armors, ItemStack offHand)
    {
        Inventory inventory = Bukkit.createInventory(null, 54, I18n.format("editor"));

        for (int slot : DISABLED_SLOTS)
        {
            inventory.setItem(slot, getDisabledIcon(slot));
        }

        for (int i = 0; i < storage.length; i++)
        {
            inventory.setItem(i, storage[i]);
        }

        for (int i = 0; i < hotbar.length; i++)
        {
            inventory.setItem(i + 36, hotbar[i]);
        }

        for (int i = 0; i < armors.length; i++)
        {
            inventory.setItem(i + 47, armors[i]);
        }

        inventory.setItem(51, offHand);

        return inventory;
    }

    private ItemStack getDisabledIcon(int slot)
    {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta itemMeta = itemStack.getItemMeta();

        switch (slot)
        {
            case 46:
            {
                itemMeta.setDisplayName(I18n.format("hotbarUp"));
                itemMeta.setLore(Collections.singletonList(I18n.format("armorsRight")));
                break;
            }
            case 52:
            {
                itemMeta.setDisplayName(I18n.format("hotbarUp"));
                itemMeta.setLore(Collections.singletonList(I18n.format("offHandLeft")));
                break;
            }
            case 45:
            case 53:
            {
                itemMeta.setDisplayName(I18n.format("hotbarUp"));
                break;
            }
            default:
            {
                itemMeta.setDisplayName(I18n.format("storageUp"));
                itemMeta.setLore(Collections.singletonList(I18n.format("hotbarDown")));
                break;
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack[] getContents(Inventory inventory)
    {
        return ArrayUtils.addAll(getHotbar(inventory), getStorage(inventory));
    }

    public ItemStack[] getStorage(Inventory inventory)
    {
        ItemStack[] itemStacks = new ItemStack[InventoryUtils.PLAYER_STORAGE_LENGTH];
        for (int i = 0; i < itemStacks.length; i++)
        {
            itemStacks[i] = inventory.getContents()[i];
        }
        return itemStacks.clone();
    }

    public ItemStack[] getHotbar(Inventory inventory)
    {
        ItemStack[] itemStacks = new ItemStack[InventoryUtils.PLAYER_HOTBAR_LENGTH];
        for (int i = 0; i < itemStacks.length; i++)
        {
            itemStacks[i] = inventory.getContents()[36 + i];
        }
        return itemStacks.clone();
    }

    public ItemStack[] getArmors(Inventory inventory)
    {
        ItemStack[] itemStacks = new ItemStack[4];
        for (int i = 0; i < itemStacks.length; i++)
        {
            itemStacks[i] = inventory.getContents()[47 + i];
        }
        return itemStacks.clone();
    }

    public ItemStack getOffHand(Inventory inventory)
    {
        ItemStack offHand = inventory.getContents()[51];
        return offHand == null ? null : offHand.clone();
    }

    public void applyPlayer(Player player, Inventory inventory)
    {
        PlayerInventory playerInventory = player.getInventory();

        playerInventory.setContents(getContents(inventory).clone());
        ItemStack[] armors = getArmors(inventory);
        ArrayUtils.reverse(armors);
        playerInventory.setArmorContents(armors);
        InventoryUtils.setItemInOffHand(player, getOffHand(inventory));
    }
}
