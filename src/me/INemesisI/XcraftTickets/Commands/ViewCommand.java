package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ViewCommand extends PluginCommand {

	protected ViewCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if ((list.size() < 1) || !list.get(0).matches("\\d*")) {
			this.error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command
					+ " <Nr>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = this.getTM().getTicket(id);
		if (ticket == null) {
			ticket = this.getTM().getArchivedTicket(Integer.parseInt(list.get(0)));
		}
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return;
		}
		if (!ticket.getOwner().equals(this.getName()) && !this.senderHasPermission("View.All")) {
			this.error("Du hast keine Rechte dieses Ticket zu sehen!");
			return;
		}
		this.reply(ChatColor.GREEN + "info für Ticket " + ChatColor.GOLD + "#" + ticket.getId());
		if (ticket.getAssignee() != null) {
			sender.sendMessage(ChatColor.GOLD + "Zugewiesen an: " + ChatColor.RED + ticket.getAssignee());
		}
		for (int i = 0; i < ticket.getLog().size(); i++) {
			sender.sendMessage(ticket.getLog().get(i).format());
		}
		if (!ticket.getWatched().contains(this.getName()))
			ticket.getWatched().add(this.getName());
		plugin.configManager.removeReminder(ticket.getOwner(), ticket.getId());
	}
}
