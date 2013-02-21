package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogCommand extends PluginCommand {

	protected LogCommand(XcraftTickets instance, String permnode) {
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
		if (!ticket.getOwner().equals(this.getName()) && !this.senderHasPermission("View.All") && (ticket.getAssignee() != null) && !ticket
				.getAssignee().equals(this.getName())) {
			this.error("Du hast keine Rechte dieses Ticket kommentieren!");
			return;
		}
		String message = list.subList(1, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		ticket.getLog().add(new Log(this.getTM().getCurrentDate(), this.getName(), Log.Type.COMMENT, message));
		ticket.getWatched().clear();
		ticket.getWatched().add(this.getName());
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (!player.equals(this.getName()) && player.hasPermission(plugin.getDescription().getName() + "." + "Mod")) {
				ticket.getWatched().add(player.getName());
			}
		}
		this.sendToMods(ticket.getOwner(),
				ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this
						.getName() + ChatColor.GRAY + " kommentiert: " + ChatColor.AQUA + message);
		this.sendToPlayer(
				ticket.getOwner(),
				ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this
						.getName() + ChatColor.GRAY + " kommentiert: \n" + ChatColor.AQUA + message + ChatColor.GRAY + "\n Nutze bitte /ticket log <nr> <nachricht> um darauf zu antworten!");
	}
}
