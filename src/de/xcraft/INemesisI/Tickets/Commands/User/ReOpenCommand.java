package de.xcraft.INemesisI.Tickets.Commands.User;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Log.EntryType;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class ReOpenCommand extends XcraftCommand {

	public ReOpenCommand() {
		super("ticket", "reopen", "reo.*|ro", "<ID> <MESSAGE> ...", Msg.COMMAND_REOPEN.toString(), "XcraftTickets.Reopen");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getArchivedTicket(id);
		if (ticket == null) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Replace.ID(id)), true);
			return true;
		}
		if (!ticket.getOwner().equals(sender.getName()) && !sender.hasPermission("XcraftTickets.Reopen.All")) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(), true);
			return true;
		}
		String message = manager.getMessage(sender, args);
		manager.addTicket(ticket);
		ticket.getLog().add(EntryType.REOPEN, sender.getName(), message);
		ticket.addToWatched(sender.getName());
		Replace[] replace = { Replace.ID(ticket.getId()), Replace.NAME(sender.getName()), Replace.MESSAGE(message) };
		manager.inform(ticket, Msg.TICKET_BROADCAST_REOPEN.toString(replace), true);
		return true;
	}
}
