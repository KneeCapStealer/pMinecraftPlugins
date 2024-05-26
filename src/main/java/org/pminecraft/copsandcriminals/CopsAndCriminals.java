package org.pminecraft.copsandcriminals;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.pminecraft.copsandcriminals.commands.PresidentCommand;
import org.pminecraft.copsandcriminals.items.policebaton.BatonListener;
import org.pminecraft.copsandcriminals.items.sheriffbadge.BadgeCommand;
import org.pminecraft.copsandcriminals.items.sheriffbadge.BadgeListener;
import org.util.Ref;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

public final class CopsAndCriminals extends JavaPlugin {
    ItemStack badge;
    ItemStack baton;
    Ref<Location> cellLocation;

    PresidentCommand presidentCommand;

    @Override
    public void onEnable() {
        // Plugin startup logic

        badge = createSheriffBadge();
        baton = createBaton();
        cellLocation = loadCellLocation();

        presidentCommand = new PresidentCommand(this);

        new BadgeListener(this, badge, baton, cellLocation);
        new BatonListener(this, baton, cellLocation);
        getCommand("spawnsheriffbadge").setExecutor(new BadgeCommand(badge));
        getCommand("togglepresident").setExecutor(presidentCommand);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        String message = "[CopsAndCriminals] Holding cell location set to x: " + cellLocation.get().getBlockX()
                + ", y: " + cellLocation.get().getBlockY()
                + ", z: " + cellLocation.get().getBlockZ();

        Bukkit.getLogger().info("[CopsAndCriminals] Saving president permission attachments");
        presidentCommand.saveAttachments();
        Bukkit.getLogger().info("[CopsAndCriminals] Saving cell location");
        saveCellLocation();
    }

    void saveCellLocation() {
        Gson gson = new Gson();
        File file = new File(getDataFolder(), "cellLocation.json");

        final Type type = new TypeToken<Ref<Location>>() {}.getType();
        try {
            if (!file.createNewFile()) {
                Bukkit.getLogger().info("[CopsAndCriminals] cellLocation file already exists, overriding file");
            } else {
                Bukkit.getLogger().info("[CopsAndCriminals] cellLocation file created, saving to file");
            }

            JsonWriter writer = gson.newJsonWriter(new FileWriter(file));
            gson.toJson(cellLocation, type, writer);
            writer.close();

            Bukkit.getLogger().info("[CopsAndCriminals] successfully saved the cell location");
        } catch (IOException e) {
            Bukkit.getLogger().severe("[CopsAndCriminals] Error saving cell location: " + e.getMessage());
        }
    }

    Ref<Location> loadCellLocation() {
        File file = new File(getDataFolder(), "cellLocation.json");
        if (!file.exists()) {
            Bukkit.getLogger().warning("No cellLocation file found.");
            return new Ref<>();
        }

        Gson gson = new Gson();
        final Type type = new TypeToken<Ref<Location>>() {}.getType();

        Ref<Location> out = null;
        try {
            JsonReader reader = gson.newJsonReader(new FileReader(file));
            out = gson.fromJson(reader, type);
            reader.close();
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().severe("Couldn't find cellLocation file: " + e.getMessage());
        } catch (IOException e) {
            Bukkit.getLogger().severe("Error reading cellLocation file: " + e.getMessage());
        }

        return out == null ? new Ref<>() : out;

    }

    ItemStack createSheriffBadge() {
        ItemStack item = new ItemStack(Material.IRON_NUGGET, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        // Change name and lore
        meta.setDisplayName("Sheriff Badge");
        Vector<String> lore = new Vector<String>(2);
        lore.add("This item is the Sheriffs badge.");
        lore.add("It can be used to set the holding cell location");
        lore.add("and give police-batons.");
        meta.setLore(lore);

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("SheriffBadge", true);
        nbtItem.setByte("CustomRoleplayData", (byte) 2);

        return nbtItem.getItem();
    }

    ItemStack createBaton() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setDisplayName("Baton");
        Vector<String> lore = new Vector<>();
        lore.add("This is a baton and is used by officers");
        lore.add("When attacking a player with the item they are sent to the holding cell");
        meta.setLore(lore);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("Baton", true);
        nbtItem.setByte("CustomRoleplayData", (byte) 3);

        return nbtItem.getItem();
    }
}
