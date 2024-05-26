package org.pminecraft.copsandcriminals.items.sheriffbadge;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pminecraft.copsandcriminals.CopsAndCriminals;

public class BadgeListener implements Listener {
    ItemStack badge;

    public BadgeListener(CopsAndCriminals plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        badge = plugin.getBadge();
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

        String message = interactEvent.getPlayer().getName() + " used badge";
        Bukkit.broadcastMessage(message);
    }
}
