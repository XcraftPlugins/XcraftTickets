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
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/" + Command + list.get(0) + " <Nr> )");
			return;
		}
		Ticket ticket = th.getTicket(Integer.parseInt(list.get(0)));
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + Integer.parseInt(list.get(0)) + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		ticket.assignee = null;
		ticket.watched.clear();
		ticket.watched.add(getName());
		sendToPlayer(ticket.owner,
				ChatColor.GRAY + "Die Zuweisung für dein Ticket " + ChatColor.GOLD + "#" + ticket.id + ChatColor.GRAY + " wurde entfernt!");
		sendToMods(ChatColor.GRAY + "Die Zuweisung für Ticket " + ChatColor.GOLD + "#" + ticket.id + ChatColor.GRAY + " wurde entfernt!");
	}
}
