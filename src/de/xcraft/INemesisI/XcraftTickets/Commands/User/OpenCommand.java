package de.xcraft.INemesisI.XcraftTickets.Commands.User;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "open",
		command = "ticket",
		pattern = "o.*",
		permission = "XcraftTickets.Open",
		usage = "[Nachricht]",
		desc = "Öffnet ein neues Ticket")
public class OpenCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if (args.length == 0) {
			this.error(sender, "Du hast keine Nachricht eingeben! ");
			return false;
		}
		String message = "";
		for (int i = 0; i < args.length; i++) {
			message += " " + args[i];
		}
		Location loc = null;
		if (sender instanceof Player) {
			loc = ((Player) sender).getLocation();
		}
		Ticket ticket = manager.addTicket(this.getName(sender), loc, message);
		ticket.addToWatched(ticket.getOwner());
		this.reply(sender, " Vielen Dank! Dein Ticket wurde erstellt. Deine Ticketnummer ist " + ChatColor.GOLD + "#" + ticket.getId());
		manager.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Ein Ticket (" + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY
				+ ") wurde von " + ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " eroeffnet " + ChatColor.GRAY + ": " + ChatColor.AQUA
				+ message);
		return true;
	}
}
