package de.xcraft.INemesisI.XcraftTickets.Commands.User;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.Utils.Command.XcraftCommand;
import de.xcraft.INemesisI.Utils.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Msg.Replace;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class OpenCommand extends XcraftCommand {

	public OpenCommand() {
		super("ticket", "open", "o.*", "<MESSAGE> ...", Msg.COMMAND_OPEN.toString(), "XcraftTickets.Open");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		if (args.length == 0) {
			pManager.plugin.messenger.sendInfo(sender, Msg.ERR_NO_MESSAGE.toString(), true);
			return false;
		}
		String message = "";
		for (int i = 0; i < args.length; i++) {
			message += " " + args[i];
		}
		Location loc = null;
		if (sender instanceof Player) {
			loc = ((Player) sender).getLocation();
		}
		Ticket ticket = manager.addTicket(sender.getName(), loc, message);
		ticket.addToWatched(ticket.getOwner());
		Replace[] replace = {Replace.ID(ticket.getId()), Replace.NAME(sender.getName()), Replace.MESSAGE(message)};
		pManager.plugin.messenger.sendInfo(sender, Msg.COMMAND_OPEN_SUCCESSFUL.toString(replace), true);
		manager.inform(ticket, Msg.TICKET_BROADCAST_OPEN.toString(replace), true);
		return true;
	}
}
