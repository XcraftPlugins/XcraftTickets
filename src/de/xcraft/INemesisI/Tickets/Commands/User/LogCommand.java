package de.xcraft.INemesisI.Tickets.Commands.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Log.EntryType;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class LogCommand extends XcraftCommand {

	public LogCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
		super(cManager, command, name, pattern, usage, desc, permission);
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Replace.ID(id)), true);
			return true;
		}
		if (!ticket.getOwner().equals(sender.getName()) && !sender.hasPermission("XcraftTickets.Log.All")) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(), true);
			return true;
		}
		String message = manager.getMessage(sender, args);
		ticket.getLog().add(EntryType.COMMENT, sender.getName(), message);
		if (ticket.getId() == manager.getLastTicket(sender)) {
			manager.setLastTicket(sender, -1);
		}
		ticket.clearWatched();
		ticket.addToWatched(sender.getName());
		for (Player player : manager.getPlugin().getServer().getOnlinePlayers()) {
			if (!player.equals(sender.getName()) && player.hasPermission(manager.getPlugin().getDescription().getName() + "." + "Mod")) {
				ticket.addToWatched(player.getName());
			}
		}
		Replace[] replace = { Replace.ID(id), Replace.NAME(sender.getName()), Replace.MESSAGE(message) };
		manager.inform(ticket, Msg.TICKET_BROADCAST_COMMENT.toString(replace), true);
		return true;
	}
}
