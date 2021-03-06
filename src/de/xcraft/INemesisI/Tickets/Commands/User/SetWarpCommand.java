package de.xcraft.INemesisI.Tickets.Commands.User;

import org.bukkit.Location;
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

public class SetWarpCommand extends XcraftCommand {


	public SetWarpCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
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
		if (!sender.getName().equals(ticket.getOwner())) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NO_PERMISSION.toString(), true);
			return true;
		}
		if (!(sender instanceof Player)) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_NOT_FROM_CONSOLE.toString(), true);
		} else {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ticket.setLoc(loc);

			String message = manager.getMessage(sender, args);
			ticket.getLog().add(EntryType.SETWARP, sender.getName(), message);
			Replace[] replace = { Replace.ID(ticket.getId()), Replace.NAME(sender.getName()), Replace.MESSAGE(message) };
			manager.inform(ticket, Msg.TICKET_BROADCAST_SETWARP.toString(replace), true);
			ticket.clearWatched();
		}
		return true;
	}
}
