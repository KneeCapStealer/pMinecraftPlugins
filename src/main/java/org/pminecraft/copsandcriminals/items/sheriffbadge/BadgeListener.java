package org.pminecraft.copsandcriminals.items.sheriffbadge;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pminecraft.copsandcriminals.CopsAndCriminals;
import org.util.Ref;

public class BadgeListener implements Listener {
    ItemStack badge;
    ItemStack baton;
    Ref<Location> cellLocation;


    public BadgeListener(CopsAndCriminals plugin, ItemStack badge, ItemStack baton, Ref<Location> cellLocation) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.badge = badge;
        this.baton = baton;
        this.cellLocation = cellLocation;
    }

    @EventHandler
    public void setCellLocation(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
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

        Location loc = event.getClickedBlock().getLocation();
        if (loc.distance(event.getPlayer().getLocation()) > 10) {
            return;
        }

        cellLocation.set(loc);

        String message = "Holding cell location set to x: " + cellLocation.get().getBlockX()
                + ", y: " + cellLocation.get().getBlockY()
                + ", z: " + cellLocation.get().getBlockZ();
        event.getPlayer().sendMessage(message);
    }

    @EventHandler
    public void giveBaton(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            return;
        }

        Entity damaged = event.getEntity();
        if (!(damaged instanceof Player)) {
            return;
        }
        Player officer = (Player) damaged;
        Player sheriff = (Player) damager;

        ItemStack weapon = sheriff.getInventory().getItemInMainHand();
        if (!new NBTItem(weapon).getBoolean("SheriffBadge")) {
            return;
        }

        event.setCancelled(true);
        officer.getInventory().addItem(baton);

        sheriff.sendMessage("You gave " + officer.getName() + " a baton!");
        officer.sendMessage("You received a baton from " + sheriff.getName()+ "!!");
    }
}
