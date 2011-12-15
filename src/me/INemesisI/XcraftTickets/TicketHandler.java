package me.INemesisI.XcraftTickets;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;

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
			if (ticket.getId() == id )
				return ticket;
		}
		return null;
	}
	
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Ticket addTicket(String owner, Location loc) {
		Ticket ticket = new Ticket(getNextID(), owner, date.format(new Date()), loc);
		tickets.add(ticket);
		return ticket;
	}
	
	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	public Ticket getArchievedTicket(int id) {
		File archive = new File(plugin.getDataFolder(), "/archive/" + id + ".yml");
		if (!archive.exists()) {
			return null;
		} else {
			return plugin.configHandler.loadTicket(archive);
		}
	}
	
	public void setArchievedTicket(Ticket ticket) {
		tickets.remove(ticket);
		plugin.configHandler.archieveTicket(ticket);
	}
	
	public void LogTicket(Ticket ticket, String player, String type, String message) {
		String cdate = date.format(new Date());
		ticket.log(cdate+" | "+type+" by "+player+": "+message);
	}
	
	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
}
