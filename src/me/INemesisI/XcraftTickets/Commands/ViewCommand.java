package me.INemesisI.XcraftTickets.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewCommand implements CommandExecutor{
    public static XcraftTickets plugin;
	
    public ViewCommand(XcraftTickets survival) {
		plugin = survival;
    }

	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean isplayer = (sender instanceof Player);
		if (args.length < 2 && !args[1].matches("\\d*")) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Ticketnummer angegeben"+ChatColor.GRAY+"(/ticket view <Nr>)");
			return true;
		}
		int id = Integer.parseInt(args[1]);
		HashMap<String, String> info = new HashMap<String, String>();
		
		if(plugin.data.getAllTicketIDs().contains(id) && isplayer)
			plugin.data.setPlayerWatchedTicket(id, plugin.data.getSendersName(sender));
		
		if(!plugin.data.getAllTicketIDs().contains(id) && isplayer) {
			info = plugin.data.getClosedTicketInfo(id);
			if(info == null || info.isEmpty() || info.get("owner") == null) {
				sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Ein Ticket mit dieser Nummer existier nicht!");
				return true;
			}
			ArrayList<Integer> reminder = plugin.data.getReminderTickets(((Player) sender).getName());
			if (reminder != null && reminder.contains(id)) {
				reminder.remove((Integer) id);
				plugin.data.setReminderTickets((Player) sender, reminder);
			}
		}
		// Ticket view stuff
		if(info.isEmpty())
		info = plugin.data.getTicketInfo(id);
		if (isplayer && !plugin.data.getSendersName(sender).equals(info.get("owner")) && !plugin.data.isMod((Player) sender)) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Rechte dieses Ticket zu sehen!"+ChatColor.GRAY+"  Es ist nicht dein Ticket...");
			return true;
		}
		sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.GREEN+"info für Ticket " +ChatColor.GOLD+"#"+id +"  "+ChatColor.GRAY+info.get("date"));
		String marker = null;
		Player[] players = plugin.getServer().getOnlinePlayers();
		for (int i=0;i<players.length;i++) {
			if (players[i].getName().equals(info.get("owner")))
				marker = ChatColor.DARK_GREEN+"+";
		}
		if(marker == null)
			 marker = ChatColor.DARK_RED+"-";
		String name = ChatColor.YELLOW+info.get("owner");
		String text = ChatColor.GRAY+info.get("title");
		String output = ChatColor.GOLD+"Ticket opened "+marker+name+": "+text;
		sender.sendMessage(output);
		if (!info.get("assignee").equals("none"))
			sender.sendMessage(ChatColor.GOLD+"Assigned to: "+ChatColor.RED+info.get("assignee"));
		ArrayList<String> log = new ArrayList<String>();
		if(plugin.data.getAllTicketIDs().contains(id))
			log = plugin.data.getTicketLog(id);
		else 
			log = plugin.data.getClosedTicketLog(id);
		for(int i=0;i<log.size();i++) {
			text = log.get(i);
			text = text.replace("comment by ", "");
			String[] split = text.split("\\|", 2);
			String time = split[0];
			split = split[1].split(":", 2);
			String inf0 = split[0];
			String out = split[1];
			output = ChatColor.BLUE+"-> "+ChatColor.DARK_GRAY+time+ChatColor.WHITE+"|"+ChatColor.YELLOW+inf0+ChatColor.WHITE+out;
			sender.sendMessage(output);
		}
		return true;
	}
}