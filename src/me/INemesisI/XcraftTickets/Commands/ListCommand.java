package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends PluginCommand {

	protected ListCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);
		int counter = -1;
		for (Ticket ticket : this.getTM().getTickets()) {
			// tickets from owner
			if (ticket.getOwner().equals(this.getName())
			// Command executed from console
					|| (sender instanceof ConsoleCommandSender)
					// /listall command
					|| (Command.matches("listall|la") && this.senderHasPermission("Listall"))
					// player is allowed to see all tickets. except of assigned
					// ones
					|| (this.senderHasPermission("View.All") && (ticket.getAssignee() == null))
					// ticket is assigned to the player
					|| (((ticket.getAssignee() != null) && ticket.getAssignee().equals(this.getName()))
					// or assignee is a group and player is in group
					|| plugin.getPermission().playerInGroup((Player) sender, ticket.getAssignee()))) {
				counter++;
				String assignee = "";
				if (ticket.getAssignee() != null) {
					assignee = ChatColor.LIGHT_PURPLE + "->" + ChatColor.DARK_PURPLE + ticket.getAssignee();
				}
				String id = ChatColor.GOLD + "#" + ticket.getId();
				int comments = ticket.getLog().size() - 1;
				String count;
				if (ticket.getWatched().contains(this.getName())) {
					count = ChatColor.GRAY + "[" + comments + "]";
				} else {
					count = ChatColor.DARK_AQUA + "[" + comments + "]";
				}
				String marker = null;
				if (plugin.getServer().getOfflinePlayer(ticket.getOwner()).isOnline()) {
					marker = ChatColor.DARK_GREEN + "+";
				} else {
					marker = ChatColor.DARK_RED + "-";
				}
				String date = ChatColor.DARK_GRAY + ticket.getLog().get(0).date;
				String name = ChatColor.WHITE + ticket.getOwner();
				String text = ChatColor.GRAY + ticket.getLog().get(0).message;

				String output = id + " " + date + " " + marker + name + assignee + ": " + text + " " + count;
				if (comments == 0) {
					if (!ticket.getWatched().contains(this.getName()))
						ticket.getWatched().add(this.getName());
				}
				if (counter == 0) {
					sender.sendMessage(plugin.getCName() + "Ticketliste");
				}
				sender.sendMessage(output);
			}
		}
		if (counter == -1) {
			this.reply("Es gibt keine offenen Tickets!");
		}
		return;
	}
}
