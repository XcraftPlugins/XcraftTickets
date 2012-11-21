package me.INemesisI.XcraftTickets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class Ticket {
	public int id;
	public String owner;
	public String assignee;
	public Location loc;
	public List<String> watched = new ArrayList<String>();
	public List<Log> log = new ArrayList<Log>();

	public Ticket(int id, String assignee, Location loc, List<String> watched, List<Log> log) {
		this.id = id;
		this.owner = log.get(0).player;
		this.assignee = assignee;
		this.loc = loc;
		this.watched = watched;
		this.log = log;
	}

	public Ticket(int id, String owner, Location loc, Log log) {
		this.id = id;
		this.owner = owner;
		this.loc = loc;
		this.watched = new ArrayList<String>();
		this.log.add(log);
	}
}
