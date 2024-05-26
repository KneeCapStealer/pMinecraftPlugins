package org.pminecraft.copsandcriminals.items.sheriffbadge;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BadgeCommand implements CommandExecutor {
    ItemStack badge;

    public BadgeCommand(ItemStack badge) {
        this.badge = badge;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Incorrect number of args");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        if (!target.isPermissionSet("president") || !target.hasPermission("president")) {
            sender.sendMessage("Det er kun presidenten der må udgive badges");
            return true;
        }

        Bukkit.broadcastMessage(sender.getName() + " sent the command: " + command.getName() + " and gave " + args[0] + " a badge");

        target.getInventory().addItem(badge);

        return true;
    }
}
