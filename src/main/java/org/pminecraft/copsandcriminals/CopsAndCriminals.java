package org.pminecraft.copsandcriminals;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.pminecraft.copsandcriminals.commands.PresidentCommand;
import org.pminecraft.copsandcriminals.items.policebaton.BatonListener;
import org.pminecraft.copsandcriminals.items.sheriffbadge.BadgeCommand;
import org.pminecraft.copsandcriminals.items.sheriffbadge.BadgeListener;
import org.util.Ref;

import java.util.Vector;

public final class CopsAndCriminals extends JavaPlugin {
    ItemStack badge;
    ItemStack baton;
    Ref<Location> cellLocation = new Ref<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Plugin started");

        badge = createSheriffBadge();
        baton = createBaton();

        new BadgeListener(this, badge, baton, cellLocation);
        new BatonListener(this, baton, cellLocation);
        getCommand("spawnsheriffbadge").setExecutor(new BadgeCommand(badge));
        getCommand("togglepresident").setExecutor(new PresidentCommand(this));
        Bukkit.getLogger().info("CopsAndCriminals plugin finished loading");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("CopsAndCriminals plugin finished shutting down");
        String message = "Holding cell location set to x: " + cellLocation.get().getBlockX()
                + ", y: " + cellLocation.get().getBlockY()
                + ", z: " + cellLocation.get().getBlockZ();

        Bukkit.getLogger().info("The cell location was: " + message);
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
