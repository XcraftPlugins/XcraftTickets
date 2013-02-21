package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class WarpCommand extends PluginCommand {

	protected WarpCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);
		if ((list.size() < 1) || !list.get(0).matches("\\d*")) {
			this.error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = this.getTM().getTicket(id);
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
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
						ChatColor.YELLOW + this.getName() + ChatColor.GRAY + " bearbeitet Ticket " + ChatColor.GOLD + "#" + id);
				this.sendToPlayer(
						ticket.getOwner(),
						"Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wird von " + ChatColor.YELLOW + sender
								.getName() + ChatColor.GRAY + " bearbeitet!");
				if (ticket.getAssignee() == null) {
					ticket.setAssignee(player.getName());
				}
			}
		} else {
			this.error("Wie soll ich den Server teleportieren???!? :)");
		}
	}
}
