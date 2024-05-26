package org.pminecraft.copsandcriminals;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.pminecraft.copsandcriminals.commands.PresidentCommand;
import org.pminecraft.copsandcriminals.items.sheriffbadge.BadgeCommand;
import org.pminecraft.copsandcriminals.items.sheriffbadge.BadgeListener;

import java.util.Vector;

public final class CopsAndCriminals extends JavaPlugin {
    private ItemStack badge;
    public ItemStack getBadge() { return badge; };

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Plugin started");

        badge = createSheriffBadge();

        new BadgeListener(this);
        getCommand("spawnsheriffbadge").setExecutor(new BadgeCommand(badge));
        getCommand("togglepresident").setExecutor(new PresidentCommand(this));
        Bukkit.getLogger().info("CopsAndCriminals plugin finished loading");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("CopsAndCriminals plugin finished shutting down");
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
        nbtItem.setByte("CustomRoleplayData", (byte) 1);

        return nbtItem.getItem();
    }
}