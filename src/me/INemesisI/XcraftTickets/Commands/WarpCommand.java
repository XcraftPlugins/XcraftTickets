package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class WarpCommand extends CommandHelper {

	protected WarpCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket warp <Nr>)");
			return;
		}
		Ticket ticket = th.getTicket(Integer.parseInt(list.get(0)));
		if (ticket == null) {
			error("Ein Ticket mit dieser Nummer existier nicht!");
			return;
		}
		Location loc = ticket.getLoc();
		if (loc != null) {
			player.teleport(loc);
			if (player != null) {
				player.performCommand("/ticket view " + ticket.getId());
			}
		} else {
			error("Für dieses Ticket gibt es keinen Warp");
		}

	}

}
