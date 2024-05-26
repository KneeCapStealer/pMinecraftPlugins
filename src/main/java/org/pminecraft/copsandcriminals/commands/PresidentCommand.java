package org.pminecraft.copsandcriminals.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.pminecraft.copsandcriminals.CopsAndCriminals;

import java.util.HashMap;
import java.util.UUID;

public class PresidentCommand implements CommandExecutor {
    HashMap<UUID, PermissionAttachment> playerAttachments = new HashMap<>();
    CopsAndCriminals plugin;

    public PresidentCommand(CopsAndCriminals plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Incorrect number of args");
            return true;
        }

        if (!sender.isOp()) {
            sender.sendMessage("Need op permission");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        playerAttachments.putIfAbsent(target.getUniqueId(), target.addAttachment(plugin));
        PermissionAttachment attachment = playerAttachments.get(target.getUniqueId());
        attachment.setPermission("president", !attachment.getPermissions().getOrDefault("president", false));
        target.updateCommands();

        if (attachment.getPermissions().get("president")) {
            Bukkit.broadcastMessage(target.getName() + " is now president!!!");
            return true;
        }
        Bukkit.broadcastMessage(target.getName() + " is no longer president!");

        return true;
    }
}
