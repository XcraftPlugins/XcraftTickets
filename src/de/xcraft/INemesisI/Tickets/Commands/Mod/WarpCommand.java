package de.xcraft.INemesisI.Tickets.Commands.Mod;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class WarpCommand extends XcraftCommand {

	public WarpCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
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
		Location loc = ticket.getLoc();
		if (loc.getWorld() == null) {
			loc.setWorld(manager.getPlugin().getServer().createWorld(new WorldCreator(ticket.getWorld())));
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player != null) {
				player.teleport(loc);
				player.performCommand("ticket view " + ticket.getId() + " all");
				manager.inform(ticket, Msg.TICKET_BROADCAST_WARP.toString(Replace.NAME(player.getName()), Replace.ID(id)), true);
				if (ticket.getAssignee() == null) {
					ticket.setAssignee(player.getName());
				}
				manager.setLastTicket(sender, ticket.getId());
				ticket.setProcessed(new Date().getTime());
				// invulnerability for 10 secs
				player.setNoDamageTicks(200);
			}
		} else {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_NOT_FROM_CONSOLE.toString(), true);
		}
		return true;
	}
}
