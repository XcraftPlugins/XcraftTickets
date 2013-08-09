package de.xcraft.INemesisI.XcraftTickets.Commands.User;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.XcraftTickets.Log;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "reopen",
		command = "ticket",
		pattern = "reo.*|ro",
		permission = "XcraftTickets.Reopen",
		usage = "[#] [Nachricht]",
		desc = "Eröffnet ein Ticket wieder")
public class ReOpenCommand extends Command {

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
		Ticket ticket = manager.getArchivedTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return true;
		}
		if (!ticket.getOwner().equals(this.getName(sender)) && !sender.hasPermission("XcraftTickets.Reopen.All")) {
			this.error(sender, "Du hast keine Rechte dieses Ticket wieder zu öffnen!");
			return true;
		}
		String message = "";
		for (int i = 1; i < args.length; i++) {
			message += " " + args[i];
		}
		message = manager.checkPhrases(sender, message);
		manager.addTicket(ticket);
		ticket.addToLog(new Log(manager.getCurrentDate(), this.getName(sender), Log.Type.REOPEN, message));
		ticket.addToWatched(this.getName(sender));
		manager.sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde von "
				+ ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " wieder eroeffnet: " + ChatColor.AQUA + message);
		manager.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde von "
				+ ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " wieder eroeffnet: " + ChatColor.AQUA + message);
		return true;
	}
}
