package me.INemesisI.XcraftTickets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class Ticket {
	private int id;
	private String owner;
	private String assignee;
	private Location loc;
	private String world;
	private List<String> watched = new ArrayList<String>();
	private List<Log> log = new ArrayList<Log>();

	public Ticket(int id, String assignee, Location loc, String world, List<String> watched, List<Log> log) {
		this.id = id;
		owner = log.get(0).player;
		this.assignee = assignee;
		this.loc = loc;
		this.world = world;
		this.watched = watched;
		this.log = log;
	}

	public Ticket(int id, String owner, Location loc, Log log) {
		this.id = id;
		this.owner = owner;
		this.loc = loc;
		this.world = loc.getWorld().getName();
		this.watched = new ArrayList<String>();
		this.log.add(log);
	}

	public boolean hasWatched(String player) {
		return watched.contains(player);
	}

	public void clearWatched() {
		watched.clear();
	}

	public boolean addToWatched(String player) {
		if (!watched.contains(player)) {
			watched.add(player);
			return true;
		}
		return false;
	}

	public List<String> getWatched() {
		return watched;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public List<Log> getLog() {
		return log;
	}

	public void setLog(List<Log> log) {
		this.log = log;
	}
}
