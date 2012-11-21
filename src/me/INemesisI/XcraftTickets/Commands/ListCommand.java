package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends CommandHelper {

	protected ListCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);
		int counter = -1;
		for (Ticket ticket : th.getTickets()) {
			if (ticket.owner.equals(getName()) // tickets from owner
					|| getName().equals("Server") // Command executed from console
					|| (senderHasPermission("View.All") && (ticket.assignee == null)) // player is allowed to see all tickets. exception are assigned ones
					|| ticket.assignee != null && ticket.assignee.equals(getName()) // ticket is assigned to player
					|| (ticket.assignee != null && ticket.assignee.startsWith("G:") && sender instanceof Player // assignee is a group...
					    && plugin.getPermission().playerInGroup((Player) sender, ticket.assignee.replace("G:", ""))) // ...player in group
					|| ((Command.equals("listall") || Command.equals("la")) && senderHasPermission("Listall"))) { // /listall command
				counter++;
				String assignee = "";
				if (ticket.assignee != null) assignee = ChatColor.LIGHT_PURPLE + "->" + ChatColor.DARK_PURPLE + ticket.assignee;
				String id = ChatColor.GOLD + "#" + ticket.id;
				int comments = ticket.log.size() - 1;
				String count;
				if (ticket.watched.contains(getName())) {
					count = ChatColor.GRAY + "[" + comments + "]";
				} else count = ChatColor.DARK_AQUA + "[" + comments + "]";
				String marker = null;
				if (plugin.getServer().getOfflinePlayer(ticket.owner).isOnline()) {
					marker = ChatColor.DARK_GREEN + "+";
				} else {
					marker = ChatColor.DARK_RED + "-";
				}
				String date = ChatColor.DARK_GRAY + ticket.log.get(0).date;
				String name = ChatColor.WHITE + ticket.owner;
				String text = ChatColor.GRAY + ticket.log.get(0).message;

				String output = id + " " + date + " " + marker + name + assignee + ": " + text + " " + count;
				if (comments == 0) ticket.watched.add(getName());
				if (counter == 0) sender.sendMessage(plugin.getCName() + "Ticketliste");
				sender.sendMessage(output);
			}
		}
		if (counter == -1) reply("Es gibt keine offenen Tickets!");
		return;
	}
}
