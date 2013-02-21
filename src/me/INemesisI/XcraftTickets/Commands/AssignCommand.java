package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class AssignCommand extends PluginCommand {
	private Permission permission;

	protected AssignCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);
		permission = plugin.getPermission();
		if ((list.size() < 1) || !list.get(0).matches("\\d*")) {
			this.error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket" + Command + " <Nr> <Name|Gruppe>)");
			return;
		}
		if (list.size() < 2) {
			this.error("Du hast keine(n) Namen/Gruppe eingeben! " + "\n" + ChatColor.GRAY + "(/ticket" + Command + " <Nr> <Name|Gruppe>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = this.getTM().getTicket(id);
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}

		String assignee = null;
		for (String group : permission.getGroups()) {
			if (group.equals(list.get(1))) {
				assignee = group;
			}
		}
		// its a group!
		if (assignee != null) {
			if (!permission.groupHas(ticket.getWorld(), assignee, "XcraftTickets.Assign")) {
				this.error("Die Gruppe " + assignee + " hat nicht die nötigen Rechte!");
				return;
			}
		}
		// its not a group!
		else {
			OfflinePlayer p = plugin.getServer().getOfflinePlayer(list.get(1));
			if (!p.hasPlayedBefore()) {
				this.error("Der Spieler " + assignee + " war noch nie auf diesem Server!");
				return;
			}
			assignee = p.getName();
			if (!permission.has(ticket.getWorld(), assignee, "XcraftTickets.Assign")) {
				this.error("Der Spieler " + assignee + " hat nicht die nötigen Rechte!");
				return;
			}
		}
		ticket.setAssignee(assignee);
		this.sendToMods(ticket.getOwner(),
				ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender
						.getName() + ChatColor.GRAY + " an " + ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
		this.sendToPlayer(ticket.getOwner(),
				ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender
						.getName() + ChatColor.GRAY + " an " + ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
	}
}
