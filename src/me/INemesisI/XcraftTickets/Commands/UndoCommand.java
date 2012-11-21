package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class UndoCommand extends CommandHelper {

	protected UndoCommand(XcraftTickets instance) {
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
		if (ticket == null) ticket = th.getArchivedTicket(id);
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		} else {
			Log log = ticket.log.get(ticket.log.size()-1);
			if (!log.player.equals(getName())) error("Jemand anders hat die letzte Antwort verfasst. Diese kannst du nicht rückgängig machen");
			else switch (log.type) {
			case OPEN:
				th.deleteTicket(ticket);
				reply("Dein Ticket wurde wieder gelöscht!");
				return;
			case COMMENT:
				ticket.log.remove(log);
				reply("Deine letzte Antwort wurde gelöscht!");
				return;
			case REOPEN:
				ticket.log.remove(log);
				th.setTicketArchived(ticket);
				reply("Deine letzte Antwort wurde gelöscht und das Ticket wurde wieder geschlossen!");
				return;
			case CLOSE:
				ticket.log.remove(log);
				th.addTicket(ticket);
				reply("Deine letzte Antwort wurde gelöscht und das Ticket wurde wieder geöffnet!");
				return;
			}
		}
	}
}
