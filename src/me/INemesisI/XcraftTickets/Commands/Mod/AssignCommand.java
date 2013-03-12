package me.INemesisI.XcraftTickets.Commands.Mod;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "Assign",
		command = "ticket|t",
		pattern = "a.*",
		permission = "XcraftTickets.Assign",
		usage = "/ticket assign <#> <Name/Gruppe>",
		desc = "Weiterleiten eines Tickets an eine(n) Gruppe/Spieler")
public class AssignCommand extends Command {
	private Permission permission;

	protected AssignCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		permission = plugin.getPermission();
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}
		if (args.length < 2) {
			this.error(sender, "Du hast keine(n) Namen/Gruppe eingeben!");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return true;
		}

		String assignee = null;
		for (String group : permission.getGroups()) {
			if (group.equals(args[1])) {
				assignee = group;
			}
		}
		if (assignee != null) { // its a group!
			if (!permission.groupHas(ticket.getWorld(), assignee, "XcraftTickets.Assign")) {
				this.error(sender, "Die Gruppe " + assignee + " hat nicht die noetigen Rechte!");
				return true;
			}
		} else { // its not a group!
			OfflinePlayer p = plugin.getServer().getOfflinePlayer(args[1]);
			if (!p.hasPlayedBefore()) {
				this.error(sender, "Der Spieler " + assignee + " war noch nie auf diesem Server!");
				return true;
			}
			assignee = p.getName();
			if (!permission.has(ticket.getWorld(), assignee, "XcraftTickets.Assign")) {
				this.error(sender, "Der Spieler " + assignee + " hat nicht die noetigen Rechte!");
				return true;
			}
		}
		ticket.setAssignee(assignee);
		this.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + ticket.getId()
				+ ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " an "
				+ ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
		this.sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId()
				+ ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " an "
				+ ChatColor.DARK_PURPLE + assignee + ChatColor.GRAY + " zugewiesen!");
		return false;
	}
}
