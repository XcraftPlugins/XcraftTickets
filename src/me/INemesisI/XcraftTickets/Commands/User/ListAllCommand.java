package me.INemesisI.XcraftTickets.Commands.User;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.command.CommandSender;

@CommandInfo(name = "ListAll",
		command = "ticket|t",
		pattern = "la|lista.*",
		permission = "XcraftTickets.Listall",
		usage = "/ticket listall",
		desc = "Listet alle offenen Tickets auf (auch zugewiesene)")
public class ListAllCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		int counter = -1;
		for (Ticket ticket : manager.getTickets()) {
			if (counter == -1) {
				sender.sendMessage(manager.getPlugin().getCName() + "Ticketliste");
			}
			counter++;
			sender.sendMessage(manager.getTicketInfo(ticket));
		}
		if (counter == -1) {
			this.reply(sender, "Es gibt keine offenen Tickets!");
		}
		return true;
	}
}
