package de.xcraft.INemesisI.Tickets.Manager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Tickets.Log;
import de.xcraft.INemesisI.Tickets.Log.EntryType;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Msg.Replace;
import de.xcraft.INemesisI.Tickets.Ticket;
import de.xcraft.INemesisI.Tickets.XcraftTickets;

public class TicketManager extends XcraftPluginManager {
	public ConfigManager cManager = null;
	private int nextID;
	private List<Ticket> tickets = new ArrayList<Ticket>();
	private Map<String, String> phrases = new HashMap<String, String>();
	private List<String> assignees = new ArrayList<String>();
	private final Map<CommandSender, Integer> lastTicket = new HashMap<CommandSender, Integer>();
	private final SimpleDateFormat date = new SimpleDateFormat();

	public TicketManager(XcraftTickets plugin) {
		super(plugin);
		date.applyPattern("dd.MM HH:mm");
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public Ticket getTicket(int id) {
		for (Ticket ticket : tickets) {
			if (ticket.getId() == id)
				return ticket;
		}
		return null;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public void onJoinInform(Player player) {
		if (player.hasPermission("XcraftTickets.Mod")) {
			int x = 0;
			for (Ticket ticket : tickets) {
				if (!ticket.hasWatched(player.getName()) && (!ticket.isAssigned() || ticket.isAssignee(player, this))) {
					x++;
				}
			}
			if (x > 0) {
				plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_UNREAD_LIST.toString(Replace.MISC(String.valueOf(x))), true);
			}
		} else {
			for (Ticket ticket : tickets) {
				if (ticket.getOwner().equals(player.getName()) && !ticket.hasWatched(player.getName())) {
					plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_UNREAD.toString(Replace.ID(ticket.getId())), true);
				}
			}
		}

	}

	public void informPlayers(Server server) {
		Map<Player, Integer> mods = new HashMap<Player, Integer>();
		// unread-reminder
		for (Player player : server.getOnlinePlayers()) {
			if (player.hasPermission(plugin.getDescription().getName() + "." + "Mod")) {
				mods.put(player, 0);
			}
		}
		for (Ticket ticket : tickets) {
			OfflinePlayer owner = server.getOfflinePlayer(ticket.getOwner());
			if (owner.isOnline() && !ticket.hasWatched(ticket.getOwner())) {
				for (Player mod : mods.keySet()) {
					if (!ticket.hasWatched(mod.getName())) {
						mods.put(mod, mods.get(mod) + 1);
					}
				}
			}
		}

		for (Player mod : mods.keySet()) {
			if (mods.get(mod) > 0) {

				plugin.getMessenger().sendInfo(mod, Msg.TICKET_REMIND_UNREAD_LIST.toString(Replace.MISC(String.valueOf(mods.get(mod)))), true);
			}
		}
		// closed-reminder
		for (Player player : server.getOnlinePlayers()) {
			List<String> list = cManager.getReminder(player.getName());
			if (list != null) {
				if (list.size() > 1) {
					plugin.getMessenger().sendInfo(player,
							Msg.TICKET_REMIND_CLOSE_LIST.toString(Replace.ID(list.size()), Replace.MISC(list.toString())), true);

				}
				else {
					plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_CLOSE.toString(Replace.ID(Integer.parseInt(list.get(0)))), true);
				}
			}
		}
	}

	public void inform(Ticket ticket, String message, boolean sendToOwner) {
		Player owner = plugin.getServer().getPlayer(ticket.getOwner());
		if (owner != null) {
			owner.sendMessage(message);
		}
		for (Player Mod : plugin.getServer().getOnlinePlayers()) {
			if (Mod.hasPermission("XcraftTickets.Mod") && !Mod.equals(owner)) {
				Mod.sendMessage(message);
			}
		}
	}

	public String getMessage(CommandSender sender, String[] args) {
		String message = " ";
		int start = 0;
		if (args[0].matches("\\d.*")) {
			start = 1;
		}
		for (int i = start; i < args.length; i++) {
			message += args[i] + " ";
		}
		if (sender.hasPermission("XcraftTickets.Phrases")) {
			for (String s : this.getPhrases().keySet()) {
				if (message.contains(" " + s + " ")) {
					message = message.replace(s, "$[" + s + "]$");
				}
			}
			message = message.trim();
			for (String s : this.getPhrases().keySet()) {
				if (message.contains("$[" + s + "]$")) {
					message = message.replace("$[" + s + "]$", this.getPhrases().get(s));
				}
			}
		}
		return message;
	}

	public Ticket addTicket(String owner, Location loc, String message) {
		Log log = new Log(date);
		log.add(EntryType.OPEN, owner, message);
		Ticket ticket = new Ticket(this.nextID, owner, loc, log);
		tickets.add(ticket);
		cManager.saveTicket(cManager.ticketFolder, ticket);
		this.nextID++;
		cManager.save();
		return ticket;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	public Ticket getArchivedTicket(int id) {
		File archive = new File(plugin.getDataFolder(), "/archive/" + id + ".yml");
		if (!archive.exists())
			return null;
		else
			return cManager.loadTicket(archive);
	}

	public void setTicketArchived(Ticket ticket) {
		tickets.remove(ticket);
		cManager.archiveTicket(ticket);
	}

	public void deleteTicket(Ticket ticket) {
		tickets.remove(ticket);
		cManager.deleteTicket(ticket);
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

	public Map<String, String> getPhrases() {
		return phrases;
	}

	public void setPhrases(Map<String, String> phrases) {
		this.phrases = phrases;
	}

	public List<String> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<String> assignees) {
		this.assignees = assignees;
	}

	public int getLastTicket(CommandSender sender) {
		if (lastTicket.containsKey(sender))
			return lastTicket.get(sender);
		else
			return -1;
	}

	public void setLastTicket(CommandSender sender, int id) {
		this.lastTicket.put(sender, id);
	}

	public SimpleDateFormat getDate() {
		return date;
	}

	@Override
	public XcraftTickets getPlugin() {
		return (XcraftTickets) plugin;
	}
}
