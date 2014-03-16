package de.xcraft.INemesisI.Tickets.Commands.User;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class OpenCommand extends XcraftCommand {


	public OpenCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
		super(cManager, command, name, pattern, usage, desc, permission);
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		if (args.length == 1 && args[0].matches("\\d.*")) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.USAGE_NO_MESSAGE.toString(), true);
			return false;
		}
		String message = manager.getMessage(sender, args);
		Location loc = null;
		if (sender instanceof Player) {
			loc = ((Player) sender).getLocation();
		} else {
			loc = manager.plugin.getServer().getWorlds().get(0).getSpawnLocation();
		}
		Ticket ticket = manager.addTicket(sender.getName(), loc, message);
		ticket.addToWatched(ticket.getOwner());
		Replace[] replace = { Replace.ID(ticket.getId()), Replace.NAME(sender.getName()), Replace.MESSAGE(message) };
		pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_OPEN_SUCCESSFUL.toString(replace), true);
		manager.inform(ticket, Msg.TICKET_BROADCAST_OPEN.toString(replace), true);
		return true;
	}
}
