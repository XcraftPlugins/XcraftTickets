package me.INemesisI.XcraftTickets.Commands.Mod;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "undo",
		command = "ticket",
		pattern = "und.*",
		permission = "XcraftTickets.Undo",
		usage = "<#>",
		desc = "Entfernt die letzte Aktion eines Tickets")
public class UndoCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			ticket = manager.getArchivedTicket(id);
		}
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return true;
		} else {
			Log log = ticket.getLog().get(ticket.getLog().size() - 1);
			if (!log.player.equals(this.getName(sender))) {
				this.error(sender,
						"Jemand anders hat die letzte Antwort verfasst. Diese kannst du nicht ruekgaengig machen");
			} else {
				switch (log.type) {
					case OPEN :
						manager.deleteTicket(ticket);
						this.reply(sender, "Dein Ticket wurde wieder geloescht!");
						return true;
					case COMMENT :
						ticket.getLog().remove(log);
						this.reply(sender, "Deine letzte Antwort wurde geloescht!");
						return true;
					case REOPEN :
						ticket.getLog().remove(log);
						manager.setTicketArchived(ticket);
						this.reply(sender,
								"Deine letzte Antwort wurde geloescht und das Ticket wurde wieder geschlossen!");
						return true;
					case CLOSE :
						ticket.getLog().remove(log);
						manager.addTicket(ticket);
						this.reply(sender,
								"Deine letzte Antwort wurde geloescht und das Ticket wurde wieder geoeffnet!");
						return true;
					case ASSIGN :
						ticket.getLog().remove(log);
						ticket.setAssignee(null);
						this.reply(sender, "Die Zuweisung wurde entfernt!");
						return true;
				}
			}
		}
		return true;
	}
}
