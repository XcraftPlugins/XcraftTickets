package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends CommandHelper {

	protected WarpCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = th.getTicket(id);
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		Location loc = ticket.loc;
		if (loc != null) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player != null) {
					player.teleport(loc);
					player.performCommand("ticket view " + ticket.id);
				}
			} else error("Wie soll ich den Server teleportieren???!? :)");
		} else {
			error("Für dieses Ticket gibt es keinen Warp");
		}

	}

}
