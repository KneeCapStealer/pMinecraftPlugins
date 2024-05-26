package org.pminecraft.copsandcriminals.items.sheriffbadge;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pminecraft.copsandcriminals.CopsAndCriminals;
import org.util.Ref;

public class BadgeListener implements Listener {
    ItemStack badge;
    Ref<Location> cellLocation;


    public BadgeListener(CopsAndCriminals plugin, ItemStack badge, Ref<Location> cellLocation) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.badge = badge;
        this.cellLocation = cellLocation;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent interactEvent) {
        ItemStack item = interactEvent.getItem();
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        if (!new NBTItem(item).getBoolean("SheriffBadge")) {
            return;
        }
        Player player = interactEvent.getPlayer();
        Block block = player.getTargetBlock(null, 15);
        cellLocation.set(block.getLocation());

        String message = "Holding cell location set to x: " + cellLocation.get().getBlockX()
                + ", y: " + cellLocation.get().getBlockY()
                + ", z: " + cellLocation.get().getBlockZ();
        Bukkit.broadcastMessage(message);
    }
}
