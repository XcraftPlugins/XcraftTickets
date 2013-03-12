package me.INemesisI.XcraftTickets.Manager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class TicketManager {
	private final XcraftTickets plugin;
	private int nextID;
	private List<Ticket> tickets = new ArrayList<Ticket>();
	SimpleDateFormat date = new SimpleDateFormat();

	public TicketManager(XcraftTickets instance) {
		plugin = instance;
		date.applyPattern("E, hh:mm a");
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public Ticket getTicket(int id) {
		for (Ticket ticket : tickets) {
			if (ticket.getId() == id) {
				return ticket;
			}
		}
		return null;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public void informPlayer(Player player) {

		if (player.hasPermission(plugin.getDescription().getName() + "." + "Mod")) {
			int x = 0;
			for (Ticket ticket : tickets) {
				if (!ticket.hasWatched(player.getName())) {
					x++;
				}
			}
			player.sendMessage(plugin.getCName() + "Du hast noch " + ChatColor.YELLOW + x + plugin.getChatColor()
					+ " ungelesene Tickets offen!");
		} else {
			for (Ticket ticket : tickets) {
				if (ticket.getOwner().equals(player.getName()) && !ticket.hasWatched(player.getName())) {
					player.sendMessage(plugin.getCName() + "Du hast noch ungelesene Nachrichten in deinem Ticket "
							+ ChatColor.GOLD + "#" + ticket.getId());
				}
			}
		}

	}

	public void informPlayers(Server server) {
		Map<Player, Integer> mods = new HashMap<Player, Integer>();
		for (Player player : server.getOnlinePlayers()) {
			if (player.hasPermission(plugin.getDescription().getName() + "." + "Mod")) {
				mods.put(player, 0);
			}
		}
		for (Ticket ticket : tickets) {
			OfflinePlayer owner = server.getOfflinePlayer(ticket.getOwner());
			if (owner.isOnline() && !ticket.hasWatched(ticket.getOwner())) {
				Player player = (Player) owner;
				player.sendMessage(plugin.getCName() + "Du hast noch ungelesene Nachrichten in deinem Ticket "
						+ ChatColor.GOLD + "#" + ticket.getId());
				for (Player mod : mods.keySet()) {
					if (!ticket.hasWatched(mod.getName())) {
						mods.put(mod, mods.get(mod) + 1);
					}
				}
			}
		}
		for (Player mod : mods.keySet()) {
			if (mods.get(mod) > 0) {
				mod.sendMessage(plugin.getCName() + "Du hast noch " + ChatColor.YELLOW + mods.get(mod)
						+ plugin.getChatColor() + " ungelesene Tickets offen!");
			}
		}
		for (Player player : server.getOnlinePlayers()) {
			List<String> list = plugin.configManager.getReminder(player.getName());
			if (list != null) {
				for (String id : list) {
					player.sendMessage(plugin.getCName() + "Dein Ticket #" + id
							+ " wurde geschlossen. Schau es dir bitte an! " + ChatColor.GRAY + "/ticket view " + id);
				}
			}
		}
	}

	public void sendToMods(String owner, String message) {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (player.hasPermission("XcraftTickets.Mod") && !player.getName().equals(owner)) {
				player.sendMessage(message);
			}
		}
	}

	public boolean sendToPlayer(String name, String message) {
		Player player = plugin.getServer().getPlayer(name);
		if (player != null) {
			player.sendMessage(message);
			return true;
		}
		return false;
	}

	public Ticket addTicket(String owner, Location loc, String message) {
		String cdate = date.format(new Date());
		Log log = new Log(cdate, owner, Log.Type.OPEN, message);
		Ticket ticket = new Ticket(this.getNextID(), owner, loc, log);
		tickets.add(ticket);
		this.setNextID(this.getNextID() + 1);
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
			return plugin.configManager.loadTicket(archive);
		}
	}

	public void setTicketArchived(Ticket ticket) {
		tickets.remove(ticket);
		plugin.configManager.archiveTicket(ticket);
	}

	public void deleteTicket(Ticket ticket) {
		tickets.remove(ticket);
		plugin.configManager.deleteTicket(ticket);
	}

	public String getTicketInfo(Ticket ticket) {
		String assignee = "";
		if (ticket.getAssignee() != null) {
			assignee = ChatColor.LIGHT_PURPLE + "->" + ChatColor.DARK_PURPLE + ticket.getAssignee();
		}
		String id = ChatColor.GOLD + "#" + ticket.getId();
		String marker = null;
		if (plugin.getServer().getOfflinePlayer(ticket.getOwner()).isOnline()) {
			marker = ChatColor.DARK_GREEN + "+";
		} else {
			marker = ChatColor.DARK_RED + "-";
		}
		String date = ChatColor.DARK_GRAY + ticket.getLog().get(0).date;
		String name = ChatColor.WHITE + ticket.getOwner();
		String text = ChatColor.GRAY + ticket.getLog().get(0).message;

		return id + " " + date + " " + marker + name + assignee + ": " + text;

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

	public XcraftTickets getPlugin() {
		return plugin;
	}
}
