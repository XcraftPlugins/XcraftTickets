package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

public class OpenCommand extends CommandHelper{

	protected OpenCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);
		
		if (list.size() < 1) {
			sender.sendMessage(ChatColor.BLUE + plugin.getName() + ChatColor.RED + "Du hast keine Nachricht eingeben! " + "\n" + ChatColor.GRAY + "(/ticket open <Nachricht>)");
			return;
		}
		
		String message = list.subList(0, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		System.out.println("openmessage: "   +   message);
		Location loc = null;
		if (player != null)
			loc = player.getLocation();
		Ticket ticket = th.addTicket(sender.getName(), loc);
		ticket.getLog().add(message);
		ticket.getWatched().add(ticket.getOwner());
		reply("Vielen dank! Dein Ticket wurde erstellt. Deine Ticketnummer ist " + ChatColor.GOLD + "#" + ticket.getId());
		sendToMods(ChatColor.GRAY + "Ein Ticket (Nr. " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + ") wurde von "  + ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " eröffnet " + ChatColor.GRAY + ": " + ChatColor.AQUA + message);
	}
}
