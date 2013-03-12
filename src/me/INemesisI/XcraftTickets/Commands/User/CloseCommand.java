package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "Close",
		command = "ticket|t",
		pattern = "c.*",
		permission = "XcraftTickets.Close",
		usage = "/ticket close <#> <Nachricht>",
		desc = "Schlieﬂt ein Ticket mit der angegebenen Nachricht")
public class CloseCommand extends Command {

	protected CloseCommand(XcraftTickets instance) {
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
		if (!ticket.getOwner().equals(this.getName(sender)) && sender.hasPermission("XcraftTickets.Close.All")) {
			this.error(sender, "Du hast keine Rechte dieses Ticket zu schliessen!");
			return true;
		}
		String message = "";
		for (String m : args) {
			message += " " + m;
		}
		ticket.getLog().add(new Log(manager.getCurrentDate(), this.getName(sender), Log.Type.CLOSE, message));
		manager.setTicketArchived(ticket);
		this.sendToMods(ticket.getOwner(), ChatColor.GRAY + "Das Ticket " + ChatColor.GOLD + "#" + id + ChatColor.GRAY
				+ " wurde von " + ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY + " geschlossen: "
				+ ChatColor.AQUA + message);
		this.sendToPlayer(ticket.getOwner(), ChatColor.GRAY + "Dein Ticket " + ChatColor.GOLD + "#" + id
				+ ChatColor.GRAY + " wurde von " + ChatColor.YELLOW + this.getName(sender) + ChatColor.GRAY
				+ " geschlossen: " + ChatColor.AQUA + message);
		plugin.configManager.addReminder(ticket.getOwner(), id);
		return true;
	}

}
