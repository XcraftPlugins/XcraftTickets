package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PluginCommand {
	protected XcraftTickets plugin = null;
	protected CommandSender sender = null;
	protected String permnode;

	protected PluginCommand(XcraftTickets instance, String permnode) {
		plugin = instance;
		this.permnode = plugin.getDescription().getName() + "." + permnode;
	}

	protected void init(CommandSender sender) {
		this.sender = sender;
	}

	protected boolean senderHasPermission(String perm) {
		if (sender instanceof Player) {
			return sender.hasPermission(plugin.getName() + "." + perm);
		} else {
			return true;
		}
	}

	protected void reply(String message) {
		sender.sendMessage(plugin.getCName() + message);
	}

	protected void error(String message) {
		sender.sendMessage(ChatColor.RED + "Error: " + message);
	}

	protected String getName() {
		if (sender instanceof Player) {
			return ((Player) sender).getName();
		} else {
			return "Server";
		}
	}

	protected TicketManager getTM() {
		return plugin.ticketManager;
	}

	protected void sendToMods(String owner, String message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (player.hasPermission("XcraftTickets.Mod") && !player.getName().equals(owner)) {
				player.sendMessage(message);
			}
		}
	}

	protected boolean sendToPlayer(String name, String message) {
		Player player = plugin.getServer().getPlayer(name);
		if (player != null) {
			player.sendMessage(message);
			return true;
		}
		return false;
	}

	protected abstract void execute(CommandSender sender, String Command, List<String> args);
}