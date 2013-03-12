package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "List",
		command = "ticket|t",
		pattern = "li.*",
		permission = "XcraftTickets.List",
		usage = "/ticket list",
		desc = "Listet alle deine Tickets auf")
public class ListCommand extends Command {

	protected ListCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		int counter = -1;
		for (Ticket ticket : manager.getTickets()) {
			//@formatter:off
				// tickets from owner
			if (ticket.getOwner().equals(this.getName(sender))
				// Command executed from console
			|| (sender instanceof ConsoleCommandSender)
				// player is allowed to see all tickets. except of assigned ones
			|| (sender.hasPermission("XcraftTickets.View.All") && (ticket.getAssignee() == null))
				// ticket is assigned to the player
			|| (((ticket.getAssignee() != null) && ticket.getAssignee().equals(this.getName(sender)))
				// or assignee is a group and player is in group
			|| plugin.getPermission().playerInGroup((Player) sender, ticket.getAssignee()))) {
			//@formatter:on
				if (counter == -1) {
					sender.sendMessage(plugin.getCName() + "Ticketliste");
				}
				counter++;
				int c = ticket.getLog().size() - 1;
				String comments;
				if (ticket.hasWatched(this.getName(sender))) {
					comments = ChatColor.GRAY + "[" + c + "]";
				} else {
					comments = ChatColor.DARK_AQUA + "[" + c + "]";
				}
				if (c == 0) {
					if (!ticket.hasWatched(this.getName(sender)))
						ticket.addToWatched(this.getName(sender));
				}
				sender.sendMessage(manager.getTicketInfo(ticket) + " " + comments);
			}
		}
		if (counter == -1) {
			this.reply(sender, "Es gibt keine offenen Tickets!");
		}
		return true;
	}
}
