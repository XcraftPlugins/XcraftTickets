package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class AssignCommand extends CommandHelper {

	protected AssignCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);
		permission = plugin.getPermission();

		if (list.size() == 0 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket assign <#> <Name|G:Gruppe>)");
			return;
		}
		Ticket ticket = plugin.ticketHandler.getTicket(Integer.parseInt(list.get(0)));
		if (ticket == null) {
			error("Ein Ticket mit dieser nummer existier nicht!");
			return;
		}
		String assignee = null;
		if (list.get(1).startsWith("g:")) {
			String g = list.get(1).replace("g:", "");
			for (String group : permission.getGroups()) {
				if (group.equals(g)) assignee = group;
			}
			if (assignee != null && permission.groupHas((String) null, assignee, "XcraftTickets.Mod")) ticket.setAssignee("G:" + assignee);
		} else {
			OfflinePlayer p = plugin.getServer().getOfflinePlayer(list.get(1));
			if (p == null) {
				error("Ein Spieler mit diesem Namen existiert nicht!");
				return;
			} else {
				System.out.print(permission.playerHas(player, "XcraftTickets.Mod"));
				assignee = p.getName();
				ticket.setAssignee(assignee);
			}
		}
		sendToMods(ChatColor.GRAY + "Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde " + ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
		sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde " + ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
	}
}
