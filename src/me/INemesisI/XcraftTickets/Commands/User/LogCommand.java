package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "Log",
		command = "ticket|t",
		pattern = "l|lo.*",
		permission = "XcraftTickets.Log",
		usage = "/ticket log <#> <Nachricht>",
		desc = "Kommentiert ein Ticket mit der angegebenen Nummer und der Nachricht")
public class LogCommand extends Command {

	protected LogCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		if ((args.length < 1) || !args[0].matches("\\d*")) {
			this.error(sender, "Du hast keine Ticketnummer angegeben");
			return false;
		}

		if (args.length < 2) {
			this.error(sender, "Du hast keine Nachricht eingeben! ");
			return false;
		}
		int id = Integer.parseInt(args[0]);
		Ticket ticket = manager.getTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return true;
		}
		if (!ticket.getOwner().equals(this.getName(sender)) && sender.hasPermission("XcraftTickets.Log.All")) {
			this.error(sender, "Du hast keine Rechte dieses Ticket zu kommentieren!");
			return true;
		}
		String message = "";
		for (String m : args)
			message += " " + m;
		ticket.getLog().add(new Log(manager.getCurrentDate(), this.getName(sender), Log.Type.COMMENT, message));
		ticket.clearWatched();
		ticket.addToWatched(getName(sender));
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (!player.equals(this.getName(sender))
					&& player.hasPermission(plugin.getDescription().getName() + "." + "Mod")) {
				ticket.addToWatched(player.getName());
			}
		}
		this.sendToMods(ticket.getOwner(),
				ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this
.getName(sender) + ChatColor.GRAY
				+ " kommentiert: " + ChatColor.AQUA + message);
		this.sendToPlayer(
				ticket.getOwner(),
				ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + ticket.getId() + ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this
.getName(sender) + ChatColor.GRAY
				+ " kommentiert: \n" + ChatColor.AQUA + message + ChatColor.GRAY
				+ "\n Nutze bitte /ticket log <nr> <nachricht> um darauf zu antworten!");
		return false;
	}
}
