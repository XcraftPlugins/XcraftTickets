package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

public class CloseCommand extends CommandHelper{

	protected CloseCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.setSender(sender);
		
		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			sender.sendMessage(ChatColor.BLUE + plugin.getName() + ChatColor.RED + "Du hast keine Ticketnummer angegeben" + ChatColor.GRAY + "(/ticket close <Nr> <Nachricht>)");
			return;
		}
		if (list.size() < 2) {
			error("Du hast keine Nachricht eingeben! " + ChatColor.GRAY + "(/ticket close <#> <Nachricht>)");
			return;
		}
		String message = list.subList(1, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		System.out.println("closemessage: " + message);
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = plugin.ticketHandler.getTicket(id);
		if(ticket == null) {
			sender.sendMessage(ChatColor.BLUE + plugin.getName() + ChatColor.RED + "Ein Ticket mit dieser Nummer existier nicht!");
			return;
		}
		if (!ticket.getOwner().equals(player.getName()) && !player.hasPermission("XcraftTickets.Close.All")) {
			error("Du hast keine Rechte dieses Ticket schliessen! (Nr: " + id + ")");
			return;
		}
		th.LogTicket(ticket, player.getName(), "closed", message);
		th.setArchievedTicket(ticket);
		sendToMods(ChatColor.GRAY + "Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde geschlossen: " + ChatColor.AQUA + message);
		sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY + " wurde geschlossen: " + ChatColor.AQUA + message);
	}

}
