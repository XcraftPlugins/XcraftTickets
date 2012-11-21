package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends CommandHelper {

	protected OpenCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1) {
			error("Du hast keine Nachricht eingeben! " + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nachricht>)");
			return;
		}

		String message = list.subList(0, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		Location loc = null;
		if (sender instanceof Player) loc = ((Player) sender).getLocation();
		Ticket ticket = th.addTicket(getName(), loc, message);
		ticket.watched.add(ticket.owner);
		reply("Vielen dank! Dein Ticket wurde erstellt. Deine Ticketnummer ist " + ChatColor.GOLD + "#" + ticket.id);
		sendToMods(ChatColor.GRAY + "Ein Ticket (" + ChatColor.GOLD + "#" + ticket.id + ChatColor.GRAY + ") wurde von " + ChatColor.YELLOW + getName() + ChatColor.GRAY + " eröffnet " + ChatColor.GRAY + ": " + ChatColor.AQUA + message);
	}
}
