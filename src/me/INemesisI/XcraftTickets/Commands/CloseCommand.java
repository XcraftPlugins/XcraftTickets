package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CloseCommand extends PluginCommand {

	protected CloseCommand(XcraftTickets instance, String permnode) {
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
		Ticket ticket = this.getTM().getTicket(id);
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		if (!ticket.getOwner().equals(this.getName()) && !this.senderHasPermission("Close.All")) {
			this.error("Du hast keine Rechte dieses Ticket schliessen! (Nr: " + id + ")");
			return;
		}
		String message = list.subList(1, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		ticket.getLog().add(new Log(this.getTM().getCurrentDate(), this.getName(), Log.Type.CLOSE, message));
		this.getTM().setTicketArchived(ticket);
		this.sendToMods(
				ticket.getOwner(),
				ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this.getName() + ChatColor.GRAY + " geschlossen: " + ChatColor.AQUA + message);
		this.sendToPlayer(
				ticket.getOwner(),
				ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this.getName() + ChatColor.GRAY + " geschlossen: " + ChatColor.AQUA + message);
		plugin.configManager.addReminder(ticket.getOwner(), id);
	}

}
