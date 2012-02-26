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
		int counter = -1;
		for (Ticket ticket : th.getTickets()) {
			if (ticket.getOwner().equals(getName()) 
					|| getName().equals("Server")
					|| (senderHasPermission("List.Other") && (ticket.getAssignee() == null))
					|| ticket.getAssignee().equals(getName())
					|| (ticket.getAssignee().startsWith("G:") && plugin.getPermission().playerInGroup((String) null, getName(), ticket.getAssignee().replace("G:", "")))
					|| (Command.equals("listall") && senderHasPermission("List.All"))) {
				counter++;
				String assignee = "";
				if (ticket.getAssignee() != null) assignee = ChatColor.LIGHT_PURPLE + "->" + ChatColor.DARK_PURPLE + ticket
						.getAssignee();
				String id = ChatColor.GOLD + "#" + ticket.getId();
				int comments = ticket.getLog().size() - 1;
				String count;
				if (ticket.getWatched().contains(getName())) {
					count = ChatColor.GRAY + "[" + comments + "]";
				} else
					count = ChatColor.DARK_AQUA + "[" + comments + "]";
				String marker = null;
				if (plugin.getServer().getOfflinePlayer(ticket.getOwner()).isOnline()) {
					marker = ChatColor.DARK_GREEN + "+";
				} else {
					marker = ChatColor.DARK_RED + "-";
				}
				String name = ChatColor.WHITE + ticket.getOwner();
				String text = ChatColor.GRAY + ticket.getLog().get(0);
				
				String output = id + " " + marker + name + assignee + ": " + text + " " + count;
				if (comments == 0) ticket.getWatched().add(getName());
				if (counter == 0) sender.sendMessage(plugin.getName() + "Ticketliste");
				sender.sendMessage(output);
			}
		}
		if (counter == -1) reply("Es gibt keine offenen Tickets!");
		return;
	}
}
