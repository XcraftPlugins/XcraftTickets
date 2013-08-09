package de.xcraft.INemesisI.XcraftTickets.Commands.User;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "view",
		command = "ticket",
		pattern = "v.*",
		permission = "XcraftTickets.View",
		usage = "[#]",
		desc = "Zeigt alle Informationen eines Tickets")
public class ViewCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			ticket = manager.getArchivedTicket(id);
			if (ticket == null) {
				this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
				return true;
			}
		}
		if (!ticket.getOwner().equals(this.getName(sender)) && !sender.hasPermission("XcraftTickets.View.All")) {
			this.error(sender, "Du hast keine Rechte dieses Ticket zu sehen!");
			return true;
		}
		this.reply(sender, ChatColor.GREEN + "info fuer Ticket " + ChatColor.GOLD + "#" + ticket.getId());
		if (ticket.getAssignee() != null) {
			sender.sendMessage(ChatColor.GOLD + "Zugewiesen an: " + ChatColor.RED + ticket.getAssignee());
		}
		for (int i = 0; i < ticket.getLog().size(); i++) {
			sender.sendMessage(ticket.getLog().get(i).format());
		}
		ticket.addToWatched(this.getName(sender));
		manager.getPlugin().configManager.removeReminder(ticket.getOwner(), ticket.getId());
		return true;
	}
}
