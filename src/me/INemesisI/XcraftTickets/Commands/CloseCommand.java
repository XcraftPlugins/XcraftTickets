package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CloseCommand extends CommandHelper {

	protected CloseCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr> <Nachricht>)");
			return;
		}
		if (list.size() < 2) {
			error("Du hast keine Nachricht eingeben! " + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr> <Nachricht>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = th.getTicket(id);
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		if (!ticket.owner.equals(getName()) && !senderHasPermission("Close.All")) {
			error("Du hast keine Rechte dieses Ticket schliessen! (Nr: " + id + ")");
			return;
		}
		String message = list.subList(1, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		ticket.log.add(new Log(th.getCurrentDate(), getName(), Log.Type.CLOSE, message));
		th.setTicketArchived(ticket);
		sendToMods(ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde geschlossen: " + ChatColor.AQUA + message);
		sendToPlayer(ticket.owner,
				ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde geschlossen: " + ChatColor.AQUA + message);
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (player.getName().equals(ticket.owner)) {
				return;
			}
		}
		plugin.configHandler.addReminder(ticket.owner, id);
	}

}
