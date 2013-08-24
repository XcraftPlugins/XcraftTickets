package de.xcraft.INemesisI.XcraftTickets.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.xcraft.INemesisI.Utils.Manager.XcraftConfigManager;
import de.xcraft.INemesisI.XcraftTickets.Log;
import de.xcraft.INemesisI.XcraftTickets.Log.EntryType;
import de.xcraft.INemesisI.XcraftTickets.Ticket;
import de.xcraft.INemesisI.XcraftTickets.XcraftTickets;

public class ConfigManager extends XcraftConfigManager {

	TicketManager tmanager;
	File folder;
	File archive;
	File remFile;
	FileConfiguration reminder;

	public ConfigManager(XcraftTickets plugin) {
		super(plugin);
		tmanager = (TicketManager) plugin.pluginManager;
		tmanager.cManager = this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void load() {
		folder = plugin.getDataFolder();
		remFile = new File(folder, "reminder.yml");
		reminder = YamlConfiguration.loadConfiguration(remFile);
		// load tickets
		File[] files = folder.listFiles();
		Arrays.sort(files);
		List<Ticket> tickets = new ArrayList<Ticket>();
		for (File file : files) {
			Ticket ticket = loadTicket(file);
			if (ticket != null) tickets.add(ticket);
		}
		tmanager.setTickets(tickets);
		tmanager.setNextID(config.getInt("Next_Ticket_ID", 1));
		tmanager.getDate().applyPattern(
				config.getString("DateFormat", "dd.MM HH:mm"));
		List<String> assignees = ((List<String>) config.getList("Assignee"));
		tmanager.setAssignees(assignees);
		ConfigurationSection cs = config.getConfigurationSection("Phrases");
		Map<String, String> phrases = new HashMap<String, String>();
		for (String value : cs.getKeys(false)) {
			phrases.put(value, cs.getString(value));
		}
		tmanager.setPhrases(phrases);
	}

	@Override
	public void save() {
		config.set("Next_Ticket_ID", tmanager.getNextID());
		config.set("Assignee", tmanager.getAssignees());
		config.set("Phrases", tmanager.getPhrases());
		plugin.saveConfig();
		try {
			reminder.save(remFile);
		} catch (IOException e) {
		}
		for (Ticket ticket : tmanager.getTickets()) {
			this.saveTicket(folder, ticket);
		}
	}

	@SuppressWarnings("unchecked")
	public Ticket loadTicket(File ticket) {
		String filename = ticket.getAbsoluteFile().getName().replace(".yml", "");
		if (!filename.matches("\\d*"))
			return null;
		int id = Integer.parseInt(filename);
		FileConfiguration temp = YamlConfiguration.loadConfiguration(ticket);
		ConfigurationSection cs = temp.getConfigurationSection("Ticket");
		Log log = new Log(tmanager.getDate());
		List<String> list = (List<String>) cs.getList("log");
		if ((list != null) && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				String split[] = list.get(i).split("; ");
				long time = split[0].matches("\\d*") ? Long.valueOf(split[0]) : 0;
				log.add(time, EntryType.valueOf(split[2]), split[1], split.length >= 4 ? split[3] : "");
			}
		}
		List<String> watched = (ArrayList<String>) cs.getList("watched");
		if (watched == null) {
			watched = new ArrayList<String>();
		}
		String assignee = cs.getString("assignee");
		if ((assignee != null) && assignee.equals("none")) {
			assignee = null;
		}
		long processed = 0;
		if (cs.isLong("processed")) {
			processed = cs.getLong("processed");
		} else {
			processed = new Date().getTime();
		}
		cs = temp.getConfigurationSection("Ticket.location");
		Location loc = null;
		String world = null;
		if (cs != null) {
			world = cs.getString("world");
			World w = plugin.getServer().getWorld(world);
			loc = new Location(w, cs.getLong("x"), cs.getLong("y"),
					cs.getLong("z"), cs.getLong("pitch"), cs.getLong("yaw"));
		}
		return new Ticket(id, assignee, loc, world, processed, watched, log);

	}

	public void saveTicket(File folder, Ticket ticket) {
		File file = new File(folder, ticket.getId() + ".yml");
		FileConfiguration temp = YamlConfiguration.loadConfiguration(file);
		temp.set("Ticket.assignee", ticket.getAssignee());
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < ticket.getLog().size(); i++) {
			list.add(ticket.getLog().getEntry(i).toString());
		}
		temp.set("Ticket.processed", ticket.getProcessed());
		temp.set("Ticket.log", list);
		temp.set("Ticket.watched", ticket.getWatched());
		Location loc = ticket.getLoc();
		if (loc.getWorld() != null) {
			temp.set("Ticket.location.x", loc.getX());
			temp.set("Ticket.location.y", loc.getY());
			temp.set("Ticket.location.z", loc.getZ());
			temp.set("Ticket.location.pitch", loc.getPitch());
			temp.set("Ticket.location.yaw", loc.getYaw());
			temp.set("Ticket.location.world", ticket.getWorld());
		}
		try {
			temp.save(file);
		} catch (IOException e) {
		}
	}

	public void archiveTicket(Ticket ticket) {
		this.saveTicket(archive, ticket);
		this.deleteTicket(ticket);
	}

	public void deleteTicket(Ticket ticket) {
		File file = new File(folder, ticket.getId() + ".yml");
		file.delete();
	}

	@SuppressWarnings("unchecked")
	public void addReminder(String player, int id) {
		List<String> list = (List<String>) reminder.getList(player);
		if (list == null) {
			list = new ArrayList<String>();
		}
		list.add(String.valueOf(id));
		reminder.set(player, list);
	}

	@SuppressWarnings("unchecked")
	public boolean removeReminder(String player, int id) {
		String sid = String.valueOf(id);
		List<String> list = (List<String>) reminder.getList(player);
		if (list != null) {
			if ((list.remove(sid) == true)) {
				if (list.size() == 0) {
					reminder.set(player, null);
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getReminder(String player) {
		return (List<String>) reminder.getList(player);
	}
}
