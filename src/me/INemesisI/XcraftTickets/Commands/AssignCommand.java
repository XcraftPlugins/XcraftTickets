package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class AssignCommand extends CommandHelper {
	Permission permission = null;

	protected AssignCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);
		permission = plugin.getPermission();

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket" + Command + " <Nr> <Name|G:Gruppe>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = th.getTicket(id);
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		if (list.size() < 2) {
			error("Du hast keine(n) Namen/Gruppe eingeben! " + "\n" + ChatColor.GRAY + "(/ticket" + Command + " <Nr> <Name|G:Gruppe>)");
			return;
		}
		String assignee = null;
		if (list.get(1).startsWith("g:")) {
			String g = list.get(1).replace("g:", "").replace("G:", "");
			for (String group : permission.getGroups()) {
				if (group.equals(g)) assignee = group;
			}
			if (assignee != null && permission.groupHas(ticket.loc.getWorld().getName(), assignee, "XcraftTickets.Mod")) {
				assignee = "G:" + assignee;
				ticket.assignee = assignee;
			} else {
				error("Die Gruppe " + assignee + " hat keine Rechte oder existiert nicht!");
				return;
			}
		} else {
			OfflinePlayer p = plugin.getServer().getOfflinePlayer(list.get(1));
			if (p == null) {
				error("Ein Spieler mit diesem Namen existiert nicht!");
				return;
			} else {
				assignee = p.getName();
				ticket.assignee = assignee;
			}
		}
		sendToMods(ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + ticket.id + ChatColor.GRAY + " wurde an " + ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
		sendToPlayer(
				ticket.owner,
				ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.id + ChatColor.GRAY + " wurde an " + ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
	}
}
