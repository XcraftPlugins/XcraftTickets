package de.xcraft.INemesisI.XcraftTickets.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftUsage;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class IDUsage extends XcraftUsage {
	TicketManager tManager;

	public IDUsage() {
		super("ID", Msg.USAGE_ID.toString());
	}

	@Override
	public List<String> onTabComplete(XcraftPluginManager pManager, List<String> list, CommandSender sender, String token) {
		TicketManager tManager = (TicketManager) pManager;
		int lastTicket = tManager.getLastTicket(sender);
		List<Ticket> ticketsUnassigned = new ArrayList<Ticket>();
		List<Ticket> ticketsAssigned = new ArrayList<Ticket>();
		List<Ticket> ticketsOwned = new ArrayList<Ticket>();
		for (Ticket ticket : tManager.getTickets()) {
			if (!String.valueOf(ticket.getId()).startsWith(token)) {
				continue;
			}
			if (lastTicket != -1 && lastTicket == ticket.getId()) continue;
			if ((ticket.isAssigned() && ticket.isAssignee(sender, tManager))) {
				ticketsAssigned.add(ticket);
			} else if (!ticket.isAssigned()) {
				ticketsUnassigned.add(ticket);
			} else if (ticket.getOwner().equals(sender.getName())) {
				ticketsOwned.add(ticket);
			}
		}
		sort(ticketsAssigned);
		sort(ticketsUnassigned);
		sort(ticketsOwned);
		// add to the actual Tab-List
		if (lastTicket != -1) list.add(String.valueOf(lastTicket));
		for (Ticket ticket : ticketsAssigned) {
			list.add(String.valueOf(ticket.getId()));
		}
		for (Ticket ticket : ticketsUnassigned) {
			list.add(String.valueOf(ticket.getId()));
		}
		for (Ticket ticket : ticketsOwned) {
			list.add(String.valueOf(ticket.getId()));
		}
		return list;
	}

	private void sort(List<Ticket> ticketsAssigned) {
		for (int n = ticketsAssigned.size(); n > 1; n--) {
			for (int i = 0; i < n - 1; i++) {
				if (ticketsAssigned.get(i).getProcessed() > ticketsAssigned.get(i + 1).getProcessed()) {
					Ticket temp = ticketsAssigned.get(i);
					ticketsAssigned.set(i, ticketsAssigned.get(i + 1));
					ticketsAssigned.set(i + 1, temp);
				}
			}
		}
	}

}
