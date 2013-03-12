package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "ReOpen",
		command = "ticket|t",
		pattern = "reo.*",
		permission = "XcraftTickets.Reopen",
		usage = "/ticket reopen <#> <Nachricht>",
		desc = "�ffne ein geschlossenes Ticket wieder mit der angegebenen Nachricht")
public class ReOpenCommand extends Command {

	protected ReOpenCommand(XcraftTickets instance) {
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
		Ticket ticket = manager.getArchivedTicket(id);
		if (ticket == null) {
			this.error(sender, "Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED
					+ " konnte nicht gefunden werden");
			return true;
		}
		if (!ticket.getOwner().equals(this.getName(sender)) && sender.hasPermission("XcraftTickets.Close.All")) {
			this.error(sender, "Du hast keine Rechte dieses Ticket zu schliessen!");
			return true;
		}
		String message = "";
		for (String m : args) {
			message += " " + m;
		}

		manager.addTicket(ticket);
		ticket.getLog().add(new Log(manager.getCurrentDate(), this.getName(sender), Log.Type.REOPEN, message));
		ticket.addToWatched(this.getName(sender));
		this.sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id
				+ ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY
				+ " wieder eröffnet: " + ChatColor.AQUA + message);
		this.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY
				+ " wurde von " + ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " wieder eröffnet: "
				+ ChatColor.AQUA + message);
		return true;
	}
}
