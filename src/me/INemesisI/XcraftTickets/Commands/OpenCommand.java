package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends PluginCommand {

	protected OpenCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1) {
			this.error("Du hast keine Nachricht eingeben! " + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nachricht>)");
			return;
		}

		String message = list.subList(0, list.size()).toString().replace(",", "").replace("[", "").replace("]", "");
		Location loc = null;
		if (sender instanceof Player) {
			loc = ((Player) sender).getLocation();
		}
		Ticket ticket = this.getTM().addTicket(this.getName(), loc, message);
		ticket.getWatched().add(ticket.getOwner());
		this.reply("Vielen dank! Dein Ticket wurde erstellt. Deine Ticketnummer ist " + ChatColor.GOLD + "#" + ticket.getId());
		this.sendToMods(ticket.getOwner(),
				ChatColor.GRAY + "Ein Ticket (" + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + ") wurde von " + ChatColor.YELLOW + this
						.getName() + ChatColor.GRAY + " eröffnet " + ChatColor.GRAY + ": " + ChatColor.AQUA + message);
	}
}
