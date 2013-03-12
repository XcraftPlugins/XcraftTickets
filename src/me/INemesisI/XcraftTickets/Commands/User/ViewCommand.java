package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "View",
		command = "ticket|t",
		pattern = "v.*",
		permission = "XcraftTickets.View",
		usage = "/ticket view <#>",
		desc = "Zeigt alle Informationen eines Tickets")
public class ViewCommand extends Command {

	protected ViewCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getArchivedTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return true;
		}
		if (!ticket.getOwner().equals(this.getName(sender)) && !sender.hasPermission("XcraftTickets.View.All")) {
			this.error(sender, "Du hast keine Rechte dieses Ticket zu sehen!");
			return true;
		}
		this.reply(sender, ChatColor.GREEN + "info für Ticket " + ChatColor.GOLD + "#" + ticket.getId());
		if (ticket.getAssignee() != null) {
			sender.sendMessage(ChatColor.GOLD + "Zugewiesen an: " + ChatColor.RED + ticket.getAssignee());
		}
		for (int i = 0; i < ticket.getLog().size(); i++) {
			sender.sendMessage(ticket.getLog().get(i).format());
		}
		ticket.addToWatched(getName(sender));
		plugin.configManager.removeReminder(ticket.getOwner(), ticket.getId());
		return true;
	}
}
