package me.INemesisI.XcraftTickets;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;


public class XcraftTicketsData {
	private XcraftTickets plugin;
	private File folder;
	private Configuration config = new Configuration(new File("plugins/XcraftTickets/config.yml"));
	private Configuration reminder = new Configuration(new File("plugins/XcraftTickets/reminder.yml"));
	private ArrayList<Integer> ticketIds = new ArrayList<Integer>();
	private HashMap<Integer, Configuration> tickets = new HashMap<Integer, Configuration>();
	private HashMap<Integer, ArrayList<String>> log = new HashMap<Integer, ArrayList<String>>();
	private HashMap<Integer, ArrayList<String>> watched = new HashMap<Integer, ArrayList<String>>();
	private int nextID;
	SimpleDateFormat date = new SimpleDateFormat();
	
	
	public XcraftTicketsData(XcraftTickets instance) {
		plugin = instance;		
	}
	
	@SuppressWarnings("unchecked")
	
	public void load() {
		folder = plugin.getDataFolder();
		if (!folder.exists()) {
			folder.mkdirs();
			folder.setWritable(true);
			folder.setExecutable(true);
		}
		File archive = new File(folder.getPath()+"/archive");
		if (!archive.exists())
			archive.mkdirs();
		String foldername = folder.getPath();
		config.load();
		//load tickets
		File[] files = folder.listFiles();
		for (int i=0;i<files.length;i++) {
			String file = files[i].getAbsoluteFile().getName();
			if (!file.equals("config.yml") && !file.equals("archive") && !file.equals("reminder.yml")) {
			int id = Integer.parseInt(file.replace(".yml", ""));
			Configuration temp = new Configuration(new File(foldername, file));
			temp.load();
			ArrayList<String> templog = (ArrayList<String>) temp.getProperty("Ticket.log");
			if (templog == null) templog = new ArrayList<String>();
			log.put(id, templog);
			ArrayList<String> templist = (ArrayList<String>) temp.getProperty("Ticket.watched");
			if (templist == null) templist = new ArrayList<String>();
			watched.put(id, templist);
			tickets.put(id, temp);
			ticketIds.add(id);
			}
			
		}
		//apply date pattern
		date.applyPattern("E, hh:mm a");
		startScheduler(config.getInt("save.intervall", 5)*1200);
		nextID = config.getInt("next_ticket_ID", 1);
		reminder.load();
	}
	
	public void startScheduler(long intervall) {
		Runnable task = new Runnable() { public void run() {
				save();
				InformPlayers();
			}};
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, task, intervall, intervall);
	}
	
	public void save() {
		for (int i=0;i<ticketIds.size();i++) {
			saveTicket(ticketIds.get(i));
		}
		config.setProperty("next_ticket_ID", nextID);
		config.save();
		reminder.save();
	}
	
	public void saveTicket(int id) {
		Configuration temp = tickets.get(id);
		temp.setProperty("Ticket.watched", watched.get(id));
		temp.setProperty("Ticket.log", log.get(id));
		temp.save();
	}
	
	@SuppressWarnings("unchecked")
	public void InformPlayers(){
		HashMap<Player, Integer> list = new HashMap<Player, Integer>();
		Player[] informants = plugin.getServer().getOnlinePlayers();
		
		for(int i=0;i<tickets.size();i++) {
			for(Player player : informants) {
				if(!hasWatchedTicket(ticketIds.get(i), player.getName()) &&(isMod(player) || isOwner(ticketIds.get(i), player.getName()))) {
					if(list.containsKey(player))
						list.put(player, list.get(player)+1);
					else
						list.put(player, 1);
				}
			}
		}
		for (Player player : informants) {
				if(list.containsKey(player))
					player.sendMessage(ChatColor.GRAY+"Du hast noch "+ChatColor.GOLD+list.get(player)+ChatColor.GRAY+" ungelesene Tickets offen! (/ticket list)");

			ArrayList<Integer> ids = (ArrayList<Integer>) reminder.getProperty(player.getName());
			if (ids != null) {
				if (ids.size() == 1)
					player.sendMessage(ChatColor.GRAY+"Dein Ticket wurde geschlossen! Schau es dir bitte nochmal an. (/ticket view "+ChatColor.GOLD+ids.get(0)+ChatColor.GRAY+")");
				if (ids.size() > 1) {
					String id = ids.toString().replace("[", "").replace("]", "");
					player.sendMessage(ChatColor.GRAY+"Es wurden "+ChatColor.GOLD+ids.size()+ChatColor.GRAY+" deiner Tickets geschlossen! Schau sie dir bitte nochmal an. (/ticket view "+ChatColor.GOLD+id+ChatColor.GRAY+")");
				}
			}
		}
	}
	
	public void newTicket(String owner, Location loc, String title) {
		Configuration ticket = new Configuration(new File(plugin.getDataFolder(),"/"+ nextID+".yml"));
		ticket.load();
		ticket.setProperty("Ticket.owner", owner);
		ticket.setProperty("Ticket.title", title);
		ticket.setProperty("Ticket.date", date.format(new Date()));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("world", loc.getWorld().getName());
		map.put("x", loc.getX());
		map.put("y", loc.getY());
		map.put("z", loc.getZ());
		map.put("yaw", loc.getYaw());
		map.put("pitch", loc.getPitch());
		ticket.setProperty("Ticket.location", map);
		ticket.setProperty("Ticket.assignee", "none");
		ticket.save();
		
		tickets.put(nextID, ticket);
		log.put(nextID, new ArrayList<String>());
		watched.put(nextID, new ArrayList<String>());
		ticketIds.add(nextID);
		nextID++;
	}
	
	@SuppressWarnings("unchecked")
	public void closeTicket(int id) {
		saveTicket(id);
		Configuration ticket = tickets.get(id);
		ticket.removeProperty("Ticket.watched");
		ticket.save();
		String owner = ticket.getString("Ticket.owner");
			ArrayList<Integer> list = (ArrayList<Integer>) reminder.getProperty(owner);
			if (list == null) {
				list = new ArrayList<Integer>();
			}
			list.add(id);
			reminder.setProperty(owner, list);
		
		Configuration archive = new Configuration(new File(folder, "/archive/"+ id+".yml"));
		archive.load();
		archive.setProperty("Archived", ticket.getProperty("Ticket"));
		archive.save();
		new File(folder, id+".yml").delete();
		tickets.remove(id);
		ticketIds.remove((Integer) id);
		log.remove(id);
		watched.remove(id);
	}
	
	@SuppressWarnings("unchecked")
	public void reopenTicket(int id) {
		Configuration archive = new Configuration(new File(folder, "/archive/"+ id+".yml"));
		Configuration ticket = new Configuration(new File(folder, id+".yml"));
		archive.load();
		ticket.load();
		ticket.setProperty("Ticket", archive.getProperty("Archived"));
		ticket.save();
		tickets.put(id, ticket);
		ticketIds.add(id);
		log.put(id, (ArrayList<String>)ticket.getProperty("Ticket.log"));
		watched.put(id, new ArrayList<String>());
		ArrayList<Integer> list = (ArrayList<Integer>) reminder.getProperty(ticket.getString("Ticket.owner"));
		if(list != null) {
		list.remove((Integer) id);
		reminder.setProperty(ticket.getString("Ticket.owner"), list);
		}
	}

	
	public void addToLog(String player, int id, String type, String text) {
		ArrayList<String> idlog = log.get(id);
		String cdate = date.format(new Date());
		if (player.isEmpty())
			idlog.add(cdate+" | "+type+": "+text);
		else
		idlog.add(cdate+" | "+type+" by "+player+": "+text);
		log.put(id, idlog);
	}
	
	public void assignTo(int id, String name) {
		tickets.get(id).setProperty("Ticket.assignee", name);
		tickets.get(id).save();
	}
	
	public Location getTicketLocation(int id) {
		Configuration ticket = tickets.get(id);
		World world = plugin.getServer().getWorld(ticket.getString("Ticket.location.world"));
		double x = ticket.getDouble("Ticket.location.x", 0);
		double y = ticket.getDouble("Ticket.location.y", 0);
		double z = ticket.getDouble("Ticket.location.z", 0);
		float yaw = (float) ticket.getDouble("Ticket.location.yaw", 0);
		float pitch = (float) ticket.getDouble("Ticket.location.pitch", 0);
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public HashMap<String, String> getTicketInfo(int id) {
		Configuration ticket = tickets.get(id);
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("title", ticket.getString("Ticket.title"));
		info.put("owner", ticket.getString("Ticket.owner"));
		info.put("assignee", ticket.getString("Ticket.assignee"));
		info.put("date", ticket.getString("Ticket.date"));
		return info;
	}
	
	public HashMap<String, String> getClosedTicketInfo(int id) {
		Configuration archive = new Configuration(new File(folder, "/archive/"+ id+".yml"));
		archive.load();
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("title", archive.getString("Archived.title"));
		info.put("owner", archive.getString("Archived.owner"));
		info.put("assignee", archive.getString("Archived.assignee"));
		info.put("date", archive.getString("Archived.date"));
		return info;
	}
	
	public Player getTicketOwner(int id) {
		Player[] players = plugin.getServer().getOnlinePlayers();
		String owner = tickets.get(id).getString("Ticket.owner");
		for (int a=0;a<players.length;a++) {
			if (players[a].getName().equals(owner))
				return players[a];
		}
		return null;
	}
	
	public boolean isMod(Player player) {
		return (player.hasPermission("XcraftTickets.Mod"));
	}

	public void sendMessageToMods(int id, String message) {
		String owner = tickets.get(id).getString("Ticket.owner");
		Player[] players = plugin.getServer().getOnlinePlayers();
		ArrayList<Player> mods = new ArrayList<Player>();
		for (Player p : players) {
			if (isMod(p)) {
				mods.add(p);
			}
		}
		for(int i=0;i<mods.size();i++) {
			if (!mods.get(i).getName().equals(owner))
				mods.get(i).sendMessage(message);
		}
	}
	
	public void sendMessageToOwner(int id, String message) {
		Player owner = plugin.getServer().getPlayer(tickets.get(id).getString("Ticket.owner"));
		if (owner != null) 
		owner.sendMessage(message);
	}
	
	public void setPlayerWatchedTicket(int id, String player) {
		if (!watched.get(id).contains(player) && !player.equals("Konsole"))
				watched.get(id).add(player);
	}
	
	public void setTicketUnWatched(int id) {
		watched.get(id).clear();
	}
	
	public boolean hasWatchedTicket( int id, String player) {
		return watched.get(id).contains(player);
	}
	
	public ArrayList<String> getTicketLog(int id) {
		return log.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getClosedTicketLog(int id) {
		Configuration archive = new Configuration(new File(folder, "/archive/"+ id+".yml"));
		archive.load();
		return (ArrayList<String>) archive.getProperty("Archived.log");
	}
	
	public int getTicketLogCounter(int id) {
		return log.get(id).size();
	}
	
	public ArrayList<Integer> getAllTicketIDs() {
		return ticketIds;
	}

	public int getNextID() {
		return nextID;
	}
	
	public boolean isOwner(int id, String player) {
		return tickets.get(id).getString("Ticket.owner").equals(player);
	}
	
	public boolean isTicketAssignee(int id, String player) {
		String ass = tickets.get(id).getString("Ticket.assignee");
		return (ass.equals("none") || ass.equals(player) || ass.contains("G:"));
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getReminderTickets(String player) {
		return (ArrayList<Integer>) reminder.getProperty(player);
	}
	public void setReminderTickets(Player player, ArrayList<Integer> rem) {
		if(rem.size() == 0)
			reminder.removeProperty(player.getName());
		else
			reminder.setProperty(player.getName(), rem); 
	}
	
	public String getSendersName(CommandSender sender) {
		if(sender instanceof Player)
			return ((Player) sender).getName();
		else return "Konsole";
	}
	public Location getSendersLocation(CommandSender sender) {
		if(sender instanceof Player)
			return ((Player) sender).getLocation();
		else return new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0, 0, 0);
	}

}
