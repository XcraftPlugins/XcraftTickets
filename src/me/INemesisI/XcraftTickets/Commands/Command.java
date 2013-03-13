package me.INemesisI.XcraftTickets.Commands;

import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command {

	public abstract boolean execute(TicketManager manager, CommandSender sender, String[] args);

	protected void reply(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GRAY + "[XcraftTickets] " + ChatColor.DARK_AQUA + message);
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
}