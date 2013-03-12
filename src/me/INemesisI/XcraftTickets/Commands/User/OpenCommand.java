package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "Open",
		command = "ticket|t",
		pattern = "o.*",
		permission = "XcraftTickets.Open",
		usage = "/ticket open <Nachricht>",
		desc = "÷ffnet ein neues Ticket mit der angegebenen Bitte")
public class OpenCommand extends Command {

	protected OpenCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if (args.length == 0) {
			this.error(sender, "Du hast keine Nachricht eingeben! ");
			return false;
		}
		String message = "";
		for (String m : args) {
			message += " " + m;
		}
		Location loc = null;
		if (sender instanceof Player) {
			loc = ((Player) sender).getLocation();
		}
		Ticket ticket = manager.addTicket(this.getName(sender), loc, message);
		ticket.addToWatched(ticket.getOwner());
		this.reply(sender, "Vielen dank! Dein Ticket wurde erstellt. Deine Ticketnummer ist " + ChatColor.GOLD + "#"
				+ ticket.getId());
		this.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Ein Ticket (" + ChatColor.GOLD + "#" + ticket.getId()
				+ ChatColor.GRAY + ") wurde von " + ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY
				+ " er√∂ffnet " + ChatColor.GRAY + ": " + ChatColor.AQUA + message);
		return true;
	}
}
