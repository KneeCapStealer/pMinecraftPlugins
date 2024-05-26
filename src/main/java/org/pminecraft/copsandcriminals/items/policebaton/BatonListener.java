package org.pminecraft.copsandcriminals.items.policebaton;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.pminecraft.copsandcriminals.CopsAndCriminals;
import org.util.Ref;

public class BatonListener implements Listener {
    ItemStack baton;
    Ref<Location> cellLocation;

    public BatonListener(CopsAndCriminals plugin, ItemStack baton, Ref<Location> cellLocation) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.baton = baton;
        this.cellLocation = cellLocation;
    }

    @EventHandler
    public void onBatonAttack(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();

        if (!(attacker instanceof Player)) {
            return;
        }

        Entity criminal = event.getEntity();
        if (!(criminal instanceof Player)) {
            return;
        }

        ItemStack weapon = ((Player) attacker).getInventory().getItemInMainHand();
        NBTItem nbt = new NBTItem(weapon);

        if (!nbt.getBoolean("Baton")) {
            return;
        }

        event.setCancelled(true);
        criminal.teleport(cellLocation.get());
    }
}
