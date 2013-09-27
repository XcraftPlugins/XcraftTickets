package de.xcraft.INemesisI.Tickets.Commands.User;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Log;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class ListCommand extends XcraftCommand {

	public ListCommand() {
		super("ticket", "list", "li.*", "", Msg.COMMAND_LIST.toString(), "XcraftTickets.List");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		int counter = -1;
		for (Ticket ticket : manager.getTickets()) {
			//@formatter:off
			// tickets from owner
			if (ticket.getOwner().equals(sender.getName())
					|| (args.length > 0 && args[0].matches("a.*") && sender.hasPermission("XcraftTickets.List.All"))
					// sender is console...
					|| (sender instanceof ConsoleCommandSender)
					// player is allowed to see all tickets. except of assigned ones
					|| (ticket.getAssignee() == null && sender.hasPermission("XcraftTickets.View.All"))
					// ticket is assigned to the player
					|| (ticket.getAssignee() != null && (ticket.getAssignee().equals(sender.getName())
							// or assignee is a group and player is in group
							|| manager.getPlugin().getPermission().playerInGroup((Player) sender, ticket.getAssignee())))) {
				//@formatter:on
				if (counter == -1) {
					pManager.plugin.getMessenger().sendInfo(sender, "", false);
					pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_LIST_HEAD.toString(), true);
				}
				counter++;
				// Ticket comments counter
				int c = ticket.getLog().size() - 1;
				if (c == 0 && !ticket.hasWatched(sender.getName())) {
					ticket.addToWatched(sender.getName());
				}
				String comments;
				if (ticket.hasWatched(sender.getName())) {
					comments = Msg.TICKET_LIST_COMMENT_READ.toString(Replace.COMMENTS(String.valueOf(c)));
				} else {
					comments = Msg.TICKET_LIST_COMMENT_UNREAD.toString(Replace.COMMENTS(String.valueOf(c)));
				}
				//
				Log log = ticket.getLog();
				String misc = "";
				if (manager.getPlugin().getServer().getPlayerExact(ticket.getOwner()) != null) {
					misc = Msg.TICKET_LIST_MISC_ONLINE.toString();
				} else {
					misc = Msg.TICKET_LIST_MISC_OFFLINE.toString();
				}
				String assignee = "";
				if (ticket.isAssigned()) {
					assignee = Msg.TICKET_LIST_ASSIGNEE.toString(Replace.NAME(ticket.getAssignee()));
				}
				Replace[] replace = { Replace.ID(ticket.getId()), Replace.TIME(log.getDate()), Replace.MISC(misc), Replace.NAME(ticket.getOwner()),
						Replace.ASSIGNEE(assignee), Replace.COMMENTS(comments), Replace.MESSAGE(log.getEntry(0).message) };
				pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_LIST.toString(replace), false);
			}
		}
		if (counter == -1) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.TICKET_LIST_EMTPY.toString(), true);
		}
		return true;
	}
}
