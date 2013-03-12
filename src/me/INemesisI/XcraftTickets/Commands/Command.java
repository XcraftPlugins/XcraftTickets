package me.INemesisI.XcraftTickets.Commands;

import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command {
	protected XcraftTickets plugin = null;

	protected Command(XcraftTickets instance) {
		plugin = instance;
	}

	public abstract boolean execute(TicketManager manager, CommandSender sender, String[] args);

	protected void reply(CommandSender sender, String message) {
		sender.sendMessage(plugin.getCName() + message);
	}

	protected void error(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.RED + "Error: " + message);
	}

	protected String getName(CommandSender sender) {
		if (sender instanceof Player) {
			return ((Player) sender).getName();
		} else {
			return "Server";
		}
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
}