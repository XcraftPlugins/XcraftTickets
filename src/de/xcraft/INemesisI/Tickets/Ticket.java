package de.xcraft.INemesisI.Tickets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class Ticket {
	private int id;
	private String owner;
	private String assignee;
	private Location loc;
	private String world;
	private long processed;
	private List<String> watched = new ArrayList<String>();
	private Log log;

	public Ticket(int id, String assignee, Location loc, String world, long processed, List<String> watched, Log log) {
		this.id = id;
		this.owner = log.getEntry(0).player;
		this.assignee = assignee;
		this.loc = loc;
		this.world = world;
		this.processed = processed;
		this.watched = watched;
		this.log = log;
	}

	public Ticket(int id, String owner, Location loc, Log log) {
		this.id = id;
		this.owner = owner;
		this.loc = loc;
		this.world = loc.getWorld().getName();
		this.processed = new Date().getTime();
		this.watched = new ArrayList<String>();
		this.log = log;
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

	public boolean isAssignee(CommandSender player, TicketManager manager) {
		if (this.assignee != null
				&& (this.assignee.equals(player.getName()) || manager.getPlugin().getPermission().playerInGroup((Player) player, this.assignee)))
			return true;
		else
			return false;
	}

	public boolean isAssigned() {
		return assignee != null;
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

	public long getProcessed() {
		return processed;
	}

	public void setProcessed(long processed) {
		this.processed = processed;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
