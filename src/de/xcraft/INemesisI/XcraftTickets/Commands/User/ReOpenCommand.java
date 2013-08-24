package de.xcraft.INemesisI.XcraftTickets.Commands.User;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Utils.Command.XcraftCommand;
import de.xcraft.INemesisI.Utils.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Log.EntryType;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Msg.Replace;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class ReOpenCommand extends XcraftCommand {

	public ReOpenCommand() {
		super("ticket", "reopen", "reo.*|ro", "<ID> <MESAGE> ...", Msg.COMMAND_REOPEN.toString(), "XcraftTickets.Reopen");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_NO_TICKET_ID.toString(), true);
			return false;
		}

		if (args.length < 2) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_NO_MESSAGE.toString(), true);
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getArchivedTicket(id);
		if (ticket == null) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Replace.ID(id)), true);
			return true;
		}
		if (!ticket.getOwner().equals(sender.getName()) && !sender.hasPermission("XcraftTickets.Reopen.All")) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(), true);
			return true;
		}
		String message = "";
		for (int i = 1; i < args.length; i++) {
			message += " " + args[i];
		}
		message = manager.checkPhrases(sender, message);
		manager.addTicket(ticket);
		ticket.getLog().add(EntryType.REOPEN, sender.getName(), message);
		ticket.addToWatched(sender.getName());
		Replace[] replace = {Replace.ID(ticket.getId()), Replace.NAME(sender.getName()), Replace.MESSAGE(message)};
		manager.inform(ticket, Msg.TICKET_BROADCAST_REOPEN.toString(replace), true);
		return true;
	}
}