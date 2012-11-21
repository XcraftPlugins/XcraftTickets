package me.INemesisI.XcraftTickets;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TicketHandler {
	XcraftTickets plugin;
	private int nextID;
	private List<Ticket> tickets = new ArrayList<Ticket>();
	SimpleDateFormat date = new SimpleDateFormat();

	public TicketHandler(XcraftTickets instance) {
		plugin = instance;
		date.applyPattern("E, hh:mm a");
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public Ticket getTicket(int id) {
		for (Ticket ticket : tickets) {
			if (ticket.id == id) return ticket;
		}
		return null;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public void inform(Player player) {
		int a = 0;
		for (Ticket ticket : tickets) {
			if (player.getName().equals(ticket.owner) || player.hasPermission(plugin.getDescription().getName() + "." + "Mod")) {
				if (!ticket.watched.contains(player.getName()) && (ticket.assignee == null || ticket.assignee.equals(player.getName()))) a++;
			}
		}
		if (a == 1) player.sendMessage(plugin.getCName() + "Du hast " + a + " ungelesenes Ticket offen " + ChatColor.GRAY + "/ticket list");
		if (a > 1) player.sendMessage(plugin.getCName() + "Du hast " + a + " ungelesene Tickets offen " + ChatColor.GRAY + "/ticket list");
		
		List<String> list = plugin.configHandler.getReminder(player.getName());
		if (list != null) {
			for (String id : list)
			player.sendMessage(plugin.getCName() + "Dein Ticket #" + id + " wurde geschlossen. Schau es dir bitte an! " + ChatColor.GRAY + "/ticket view " + id);
		}
	}

	public Ticket addTicket(String owner, Location loc, String message) {
		String cdate = date.format(new Date());
		Log log = new Log(cdate, owner, Log.Type.OPEN, message);
		Ticket ticket = new Ticket(getNextID(), owner, loc, log);
		tickets.add(ticket);
		setNextID(getNextID() + 1);
		return ticket;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	public Ticket getArchivedTicket(int id) {
		File archive = new File(plugin.getDataFolder(), "/archive/" + id + ".yml");
		if (!archive.exists()) {
			return null;
		} else {
			return plugin.configHandler.loadTicket(archive);
		}
	}

	public void setTicketArchived(Ticket ticket) {
		tickets.remove(ticket);
		plugin.configHandler.archiveTicket(ticket);
	}

	public void deleteTicket(Ticket ticket) {
		tickets.remove(ticket);
		plugin.configHandler.deleteTicket(ticket);
	}

	public String getCurrentDate() {
		return date.format(new Date());
	}

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
}
