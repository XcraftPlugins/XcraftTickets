package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class UnAssignCommand extends CommandHelper {

	protected UnAssignCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket unassign <Nr> )");
			return;
		}
		Ticket ticket = th.getTicket(Integer.parseInt(list.get(0)));
		ticket.setAssignee(null);
		ticket.getWatched().clear();
		ticket.getWatched().add(getName());
		sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Die Zuweisung für dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde entfernt!");
		sendToMods(ChatColor.GRAY + "Die Zuweisung für Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde entfernt!");
	}
}
