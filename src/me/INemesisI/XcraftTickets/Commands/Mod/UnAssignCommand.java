package me.INemesisI.XcraftTickets.Commands.Mod;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "unassign",
		command = "ticket",
		pattern = "una.*",
		permission = "XcraftTickets.Unassign",
		usage = "<#>",
		desc = "Entfernt die Weiterleitung")
public class UnAssignCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return true;
		}
		ticket.setAssignee(null);
		ticket.clearWatched();
		ticket.addToWatched(this.getName(sender));
		manager.sendToPlayer(ticket.getOwner(), //
				ChatColor.GRAY + "Die Zuweisung fuer dein Ticket " + ChatColor.GOLD + "#" + ticket.getId()
						+ ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender.getName() + ChatColor.GRAY
						+ " entfernt!");
		manager.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Die Zuweisung für Ticket " + ChatColor.GOLD + "#"
				+ ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender.getName()
				+ ChatColor.GRAY + " entfernt!");
		return true;
	}
}
