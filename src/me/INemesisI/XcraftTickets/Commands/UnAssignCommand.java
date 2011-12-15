package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

public class UnAssignCommand extends CommandHelper{

	protected UnAssignCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.setSender(sender);
		
		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + ChatColor.GRAY + "(/ticket unassign <Nr> )");
			return;
		}
		Ticket ticket = th.getTicket(Integer.parseInt(list.get(0)));
		th.LogTicket(ticket, sender.getName(), "unassigned", "");
		ticket.setAssignee(null);
		ticket.getWatched().clear();
		ticket.getWatched().add(sender.getName());
		sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Die Zuweisung für dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() +  ChatColor.GRAY + " wurde entfernt!");
		sendToMods(ChatColor.GRAY + "Die Zuweisung für Ticket " + ChatColor.GOLD + "#" + ticket.getId() +  ChatColor.GRAY + " wurde entfernt!");
	}
}
