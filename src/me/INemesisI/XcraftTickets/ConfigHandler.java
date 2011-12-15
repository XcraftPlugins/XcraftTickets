package me.INemesisI.XcraftTickets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {
	XcraftTickets plugin;
	File folder;
	File archive;
	FileConfiguration config;
	File remFile;
	FileConfiguration reminder;
	
	public ConfigHandler(XcraftTickets instance) {
		plugin = instance;
	}
	
	public void load() {
		folder = plugin.getDataFolder();
		archive = new File(folder.getPath()+"/archive");
		if (!archive.exists())
			archive.mkdirs();
		config = plugin.getConfig();
		//load tickets
		File[] files = folder.listFiles();
		List<Ticket> tickets = new ArrayList<Ticket>();
		for (File file : files) {
			Ticket ticket = loadTicket(file);
			if (ticket != null) {
				tickets.add(ticket);
			}
		}
		plugin.ticketHandler.setTickets(tickets);
		plugin.ticketHandler.setNextID(config.getInt("Next_Ticket_ID", 1));
		remFile = new File(folder, "reminder.yml");
		reminder = YamlConfiguration.loadConfiguration(remFile);
	}
	
	public void save() {
		config.set("Next_Ticket_ID", plugin.ticketHandler.getNextID());
		for (Ticket ticket : plugin.ticketHandler.getTickets()) {
			saveTicket(folder, ticket);
		}
	}

	@SuppressWarnings("unchecked")
	public Ticket loadTicket(File ticket) {
		String filename = ticket.getAbsoluteFile().getName();
		if (!filename.equals("config.yml") && !filename.equals("archive") && !filename.equals("reminder.yml")) {
			int id = Integer.parseInt(filename.replace(".yml", ""));
			FileConfiguration temp = YamlConfiguration.loadConfiguration(new File(folder, filename));
			ConfigurationSection cs = temp.getConfigurationSection("Ticket");
			ArrayList<String> log = (ArrayList<String>) cs.getList("log");
			if (log == null) log = new ArrayList<String>();;
			ArrayList<String> watched = (ArrayList<String>) cs.getList("watched");
			if (watched == null) watched = new ArrayList<String>();
			String owner = cs.getString("owner");
			String assignee = cs.getString("assignee");
			String date = cs.getString("date");
			if (assignee.equals("null")) assignee = null;
			cs = temp.getConfigurationSection("Ticket.location");
			World world = plugin.getServer().getWorld(cs.getString("world"));
			Location loc = new Location(world, cs.getLong("x"), cs.getLong("y"), cs.getLong("z"), cs.getLong("pitch"), cs.getLong("yaw"));
			cs = temp.getConfigurationSection("Ticket");
			return new Ticket(id, owner, assignee, date, loc, watched, log);
		}
		return null;
	}
	
	public void saveTicket(File folder, Ticket ticket) {
		FileConfiguration temp = YamlConfiguration.loadConfiguration(new File(folder, ticket.getId()+".yml"));
		temp.set("Ticket.title", ticket.getLog().get(0));
		temp.set("Ticket.owner", ticket.getOwner());
		temp.set("Ticket.date", ticket.getDate());
		temp.set("Ticket.assignee", ticket.getAssignee());
		temp.set("Ticket.log", ticket.getLog());
		temp.set("Ticket.loc", ticket.getLoc());
		temp.set("Ticket.watched", ticket.getWatched());
	}
	
	public void archieveTicket(Ticket ticket) {
		saveTicket(archive, ticket);
		File file = new File(folder, ticket.getId()+".yml");
		file.delete();
	}
}
