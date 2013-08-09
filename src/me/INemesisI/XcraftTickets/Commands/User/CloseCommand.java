package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "close",
		command = "ticket",
		pattern = "c.*",
		permission = "XcraftTickets.Close",
		usage = "[#] [Nachricht]",
		desc = "Schlieﬂt ein Ticket")
public class CloseCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}

		if (args.length < 2) {
			this.error(sender, "Du hast keine Nachricht eingeben! ");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return true;
		}
		if (!ticket.getOwner().equals(this.getName(sender)) && !sender.hasPermission("XcraftTickets.Close.All")) {
			this.error(sender, "Du hast keine Rechte dieses Ticket zu schliessen!");
			return true;
		}
		String message = "";
		for (int i = 1; i < args.length; i++) {
			message += " " + args[i];
		}
		message = manager.checkPhrases(sender, message);
		ticket.addToLog(new Log(manager.getCurrentDate(), this.getName(sender), Log.Type.CLOSE, message));
		if (ticket.getId() == manager.getLastTicket(sender)) {
			manager.setLastTicket(sender, -1);
		}
		manager.setTicketArchived(ticket);
		manager.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde von "
				+ ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " geschlossen: " + ChatColor.AQUA + message);
		if (getName(sender).equals(ticket.getOwner())) {
			manager.sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY
					+ " wurde von " + ChatColor.YELLOW + "dir" + ChatColor.GRAY + " geschlossen: \n" + ChatColor.AQUA + message);
		} else {
			manager.sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY
					+ " wurde von " + ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " geschlossen: \n" + ChatColor.AQUA + message
					+ ChatColor.GRAY + "\n Nutze bitte /ticket reopen <nr> <nachricht> um es eventuell wieder zu oeffnen!");
		}
		if (!sender.getName().equals(ticket.getOwner())) {
			manager.getPlugin().configManager.addReminder(ticket.getOwner(), id);
		}
		return true;
	}
}
