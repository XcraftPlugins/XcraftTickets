package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class UnAssignCommand extends PluginCommand {

	protected UnAssignCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if ((list.size() < 1) || !list.get(0).matches("\\d*")) {
			this.error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/" + Command + list.get(0) + " <Nr> )");
			return;
		}
		Ticket ticket = this.getTM().getTicket(Integer.parseInt(list.get(0)));
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + Integer.parseInt(list.get(0)) + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		ticket.setAssignee(null);
		ticket.getWatched().clear();
		ticket.getWatched().add(this.getName());
		this.sendToPlayer(
				ticket.getOwner(),
				ChatColor.GRAY + "Die Zuweisung für dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender
						.getName() + ChatColor.GRAY + " entfernt!");
		this.sendToMods(
				ticket.getOwner(),
				ChatColor.GRAY + "Die Zuweisung für Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender
						.getName() + ChatColor.GRAY + " entfernt!");
	}
}
