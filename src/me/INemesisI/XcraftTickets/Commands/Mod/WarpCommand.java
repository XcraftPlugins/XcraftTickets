package me.INemesisI.XcraftTickets.Commands.Mod;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "Warp",
		command = "ticket|t",
		pattern = "w.*",
		permission = "XcraftTickets.Warp",
		usage = "/ticket warp <#>",
		desc = "Teleportiert dich zu dem Ort, an dem das Ticket erstellt wurde!")
public class WarpCommand extends Command {

	protected WarpCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return true;
		}
		Location loc = ticket.getLoc();
		if (loc.getWorld() == null) {
			loc.setWorld(plugin.getServer().createWorld(new WorldCreator(ticket.getWorld())));
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player != null) {
				player.teleport(loc);
				player.performCommand("ticket view " + ticket.getId());
				this.sendToMods(
						ticket.getOwner(),
 ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY
						+ " bearbeitet Ticket " + ChatColor.GOLD + "#" + id);
				this.sendToPlayer(
						ticket.getOwner(),
						"Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wird von " + ChatColor.YELLOW + sender
								.getName() + ChatColor.GRAY + " bearbeitet!");
				if (ticket.getAssignee() == null) {
					ticket.setAssignee(player.getName());
				}
			}
		} else {
			this.error(sender, "Wie soll ich den Server teleportieren???!? :)");
		}
		return false;
	}
}
