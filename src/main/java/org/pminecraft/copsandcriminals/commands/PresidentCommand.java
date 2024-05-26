package org.pminecraft.copsandcriminals.commands;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.pminecraft.copsandcriminals.CopsAndCriminals;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class PresidentCommand implements CommandExecutor {
    HashMap<UUID, PermissionAttachment> playerAttachments = new HashMap<>();
    CopsAndCriminals plugin;

    public PresidentCommand(CopsAndCriminals plugin) {
        this.plugin = plugin;
        playerAttachments = loadAttachments();
    }

    public void saveAttachments() {
        File data = plugin.getDataFolder();
        Gson gson = new Gson();
        final Type type = new TypeToken<HashMap<UUID, PermissionAttachment>>() {}.getType();

        File file = new File(data, "attachments.json");
        try {
            if (!file.createNewFile()) {
                Bukkit.getLogger().info("[CopsAndCriminals] attachments file exists. Overriding prev file");
            }
            else {
                Bukkit.getLogger().info("[CopsAndCriminals] No attachments file. Creating save file");
            }

            JsonWriter writer = gson.newJsonWriter(new FileWriter(file));
            gson.toJson(playerAttachments, type, writer);
            writer.close();

            Bukkit.getLogger().info("[CopsAndCriminals] Successfully saved attachments");
        } catch (IOException e) {
            Bukkit.getLogger().severe("[CopsAndCriminals] Couldn't save attachments file: " + e.getMessage());
        }
    }

    HashMap<UUID, PermissionAttachment> loadAttachments() {
        File data = plugin.getDataFolder();
        data = new File(data, "attachments.json");
        if (!data.exists()) {
            Bukkit.getLogger().warning("No attachments file found.");
            return new HashMap<>();
        }

        Gson gson = new Gson();
        final Type type = new TypeToken<HashMap<UUID, PermissionAttachment>>() {}.getType();

        HashMap<UUID, PermissionAttachment> out = null;
        try {
            JsonReader reader = gson.newJsonReader(new FileReader(data));
            out = gson.fromJson(reader, type);
            reader.close();
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().severe("Couldn't find attachments file: " + e.getMessage());
        } catch (IOException e) {
            Bukkit.getLogger().severe("Error reading attachments file: " + e.getMessage());
        }

        return out == null ? new HashMap<>() : out;
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
