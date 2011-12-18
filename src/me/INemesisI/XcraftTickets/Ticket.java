package me.INemesisI.XcraftTickets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class Ticket {
	private int id;
	private String owner;
	private String assignee;
	private String date;
	private Location loc;
	private List<String> watched = new ArrayList<String>();
	private List<String> log = new ArrayList<String>();

	public Ticket(int id, String owner, String assignee, String date, Location loc, ArrayList<String> watched, ArrayList<String> log) {
		this.setId(id);
		this.setOwner(owner);
		this.setAssignee(assignee);
		this.setDate(date);
		this.setLoc(loc);
		this.setWatched(watched);
		this.setLog(log);
	}

	public Ticket(int id, String owner, String date, Location loc) {
		this.setId(id);
		this.setOwner(owner);
		this.setDate(date);
		this.setLoc(loc);
		this.setAssignee("none");
		this.setLog(new ArrayList<String>());
		this.setWatched(new ArrayList<String>());
	}

	public void log(String message) {
		log.add(message);
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public List<String> getWatched() {
		return watched;
	}

	public void setWatched(List<String> watched) {
		this.watched = watched;
	}

	public List<String> getLog() {
		return log;
	}

	public void setLog(List<String> log) {
		this.log = log;
	}

}
