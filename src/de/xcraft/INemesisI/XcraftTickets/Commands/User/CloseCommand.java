package de.xcraft.INemesisI.XcraftTickets.Commands.User;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Log.EntryType;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Msg.Replace;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Manager.ConfigManager;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class CloseCommand extends XcraftCommand {

	public CloseCommand() {
		super("ticket", "close", "c.*", "<ID> <MESSAGE> ...", Msg.COMMAND_CLOSE.toString(), "XcraftTickets.Close");
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
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Replace.ID(id)), true);
			return true;
		}
		if (!ticket.getOwner().equals(sender.getName()) && !sender.hasPermission("XcraftTickets.Close.All")) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(), true);
			return true;
		}
		String message = manager.getMessage(sender, args);
		ticket.getLog().add(EntryType.CLOSE, sender.getName(), message);
		if (ticket.getId() == manager.getLastTicket(sender)) {
			manager.setLastTicket(sender, -1);
		}
		manager.setTicketArchived(ticket);
		Replace[] replace = {Replace.NAME(sender.getName()), Replace.ID(id), Replace.MESSAGE(message)};
		manager.inform(ticket, Msg.TICKET_BROADCAST_CLOSE.toString(replace), true);
		if (!sender.getName().equals(ticket.getOwner())) {
			((ConfigManager) manager.getPlugin().configManager).addReminder(ticket.getOwner(), id);
		}
		return true;
	}
}
