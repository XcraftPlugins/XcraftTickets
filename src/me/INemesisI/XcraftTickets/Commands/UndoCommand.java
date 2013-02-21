package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class UndoCommand extends PluginCommand {

	protected UndoCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if ((list.size() < 1) || !list.get(0).matches("\\d*")) {
			this.error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = this.getTM().getTicket(id);
		if (ticket == null) {
			ticket = this.getTM().getArchivedTicket(id);
		}
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		} else {
			Log log = ticket.getLog().get(ticket.getLog().size() - 1);
			if (!log.player.equals(this.getName())) {
				this.error("Jemand anders hat die letzte Antwort verfasst. Diese kannst du nicht rückgängig machen");
			} else {
				switch (log.type) {
				case OPEN:
					this.getTM().deleteTicket(ticket);
					this.reply("Dein Ticket wurde wieder gelöscht!");
					return;
				case COMMENT:
					ticket.getLog().remove(log);
					this.reply("Deine letzte Antwort wurde gelöscht!");
					return;
				case REOPEN:
					ticket.getLog().remove(log);
					this.getTM().setTicketArchived(ticket);
					this.reply("Deine letzte Antwort wurde gelöscht und das Ticket wurde wieder geschlossen!");
					return;
				case CLOSE:
					ticket.getLog().remove(log);
					this.getTM().addTicket(ticket);
					this.reply("Deine letzte Antwort wurde gelöscht und das Ticket wurde wieder geöffnet!");
					return;
				}
			}
		}
	}
}
