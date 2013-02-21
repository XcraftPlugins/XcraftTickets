package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReOpenCommand extends PluginCommand {

	protected ReOpenCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if ((list.size() < 1) || !list.get(0).matches("\\d*")) {
			this.error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr> <Nachricht>)");
			return;
		}
		if (list.size() < 2) {
			this.error("Du hast keine Nachricht eingeben! " + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr> <Nachricht>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = this.getTM().getArchivedTicket(id);
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		String message = list.subList(1, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		this.getTM().addTicket(ticket);
		ticket.getLog().add(new Log(this.getTM().getCurrentDate(), this.getName(), Log.Type.REOPEN, message));
		ticket.getWatched().add(this.getName());
		this.sendToPlayer(
				ticket.getOwner(),
				ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this.getName() + ChatColor.GRAY + " wieder eröffnet: " + ChatColor.AQUA + message);
		this.sendToMods(
				ticket.getOwner(),
				ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this.getName() + ChatColor.GRAY + " wieder eröffnet: " + ChatColor.AQUA + message);
	}
}
