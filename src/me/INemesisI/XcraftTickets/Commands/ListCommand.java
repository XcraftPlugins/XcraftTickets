package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListCommand extends CommandHelper {

	protected ListCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		List<Ticket> tickets = th.getTickets();
		sender.sendMessage(plugin.getChatColor() + "---Ticket list---");
		int counter = -1;
		for (Ticket ticket : tickets) {
			if (ticket.getOwner().equals(sender.getName()) || (senderHasPermission("List.Other") && ticket.getAssignee().equals("none") || ticket.getAssignee().equals(sender.getName()) || (ticket.getAssignee().startsWith("g:") && plugin.getPermission().playerInGroup((String) null, sender.getName(), ticket.getAssignee().replace("g:", ""))) || (Command.equals("listall") && senderHasPermission("List.All")))) {
				counter++;
				String assignee = "";
				if (ticket.getAssignee() != null) assignee = ChatColor.LIGHT_PURPLE + "->" + ChatColor.DARK_PURPLE + ticket.getAssignee();
				String id = ChatColor.GOLD + "#" + ticket.getId();
				int comments = ticket.getLog().size() - 1;
				String count;
				if (ticket.getWatched().contains(sender.getName())) {
					if (comments == 1) count = ChatColor.GRAY + "[" + comments + " Kommentar]";
					else
						count = ChatColor.GRAY + "[" + comments + " Kommentare]";
				} else if (comments == 1) count = ChatColor.DARK_AQUA + "[" + comments + " Kommentar]";
				else
					count = ChatColor.DARK_AQUA + "[" + comments + " Kommentare]";

				String marker = null;
				if (plugin.getServer().getOfflinePlayer(ticket.getOwner()).isOnline()) {
					marker = ChatColor.DARK_GREEN + "+";
				} else {
					marker = ChatColor.DARK_RED + "-";
				}
				String name = ChatColor.WHITE + ticket.getOwner();
				String text = ChatColor.GRAY + ticket.getLog().get(0);

				String output = id + " " + marker + name + assignee + ": " + text + " " + count;
				if (comments == 0) ticket.getWatched().add(sender.getName());
				sender.sendMessage(output);
			}
		}
		if (counter == -1) reply("Es gibt keine offenen Tickets!");
		return;
	}
}
