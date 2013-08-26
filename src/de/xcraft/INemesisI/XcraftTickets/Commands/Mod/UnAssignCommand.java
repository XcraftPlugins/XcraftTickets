package de.xcraft.INemesisI.XcraftTickets.Commands.Mod;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Msg.Replace;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class UnAssignCommand extends XcraftCommand {

	public UnAssignCommand() {
		super("ticket", "unassign", "una.*", "<ID>", Msg.COMMAND_UNASSIGN.toString(), "XcraftTickets.Unassign");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Replace.ID(id)), true);
			return true;
		}
		ticket.setAssignee(null);
		ticket.clearWatched();
		ticket.addToWatched(sender.getName());
		manager.inform(ticket, Msg.TICKET_BROADCAST_UNASSIGN.toString(Replace.ID(id)), true);
		return true;
	}
}
