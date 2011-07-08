package me.INemesisI.XcraftTickets.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor{
    public static XcraftTickets plugin;
	
    public ListCommand(XcraftTickets survival) {
		plugin = survival;
    }

	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean isplayer = (sender instanceof Player);
		ArrayList<Integer> IDs = plugin.data.getAllTicketIDs();
		Collections.sort(IDs);
		sender.sendMessage(ChatColor.YELLOW+plugin.getName()+ChatColor.GREEN+"Ticket list");
		int counter = -1;
		for(int i=0;i<IDs.size();i++) {
			counter = i;
			HashMap<String, String> info = plugin.data.getTicketInfo(IDs.get(i));
			String assignee = info.get("assignee");
			Player player = null;
			if (isplayer)
				player = (Player) sender;
			if (player != null && (player.getName().equals(info.get("owner")) || (assignee.equals("none") || args[0].equals("listall") || assignee.toLowerCase().equals(player.getName().toLowerCase()) || 
			(assignee.startsWith("G:") && plugin.permissionHandler.inGroup(player.getWorld().getName(), player.getName(), assignee.replace("G:", "")))) && plugin.data.isMod(player)) || !isplayer) {
				String assign = "";
				if(!assignee.equals("none"))
				assign = ChatColor.LIGHT_PURPLE+"->"+ChatColor.DARK_PURPLE+assignee;
				String id = ChatColor.GOLD+"#"+IDs.get(i);
				int comments = plugin.data.getTicketLogCounter(IDs.get(i));
				String count;
				if (plugin.data.hasWatchedTicket(IDs.get(i), plugin.data.getSendersName(sender))) {
					if (comments == 1) count = ChatColor.GRAY+"["+comments+" Kommentar]";
					else count = ChatColor.GRAY+"["+comments+" Kommentare]";
				}
				else
				if (comments == 1) count = ChatColor.DARK_AQUA+"["+comments+" Kommentar]";
				else count = ChatColor.DARK_AQUA+"["+comments+" Kommentare]";
				
				String marker = null;
				Player[] players = plugin.getServer().getOnlinePlayers();
				for (int a=0;a<players.length;a++) {
					if (players[a].getName().equals(info.get("owner")))
						marker = ChatColor.DARK_GREEN+"+";
				}
				if(marker == null)
					 marker = ChatColor.DARK_RED+"-";
				String name = ChatColor.WHITE+info.get("owner");
				String text = ChatColor.GRAY+info.get("title");
				
				String output = id+" "+marker+name+assign+": "+text+" "+count;
				if(comments == 0)
					plugin.data.setPlayerWatchedTicket(IDs.get(i), plugin.data.getSendersName(sender));
				sender.sendMessage(output);
			}
			else {
				plugin.data.setPlayerWatchedTicket(IDs.get(i), plugin.data.getSendersName(sender));
			}
		}
		if (counter==-1)
			sender.sendMessage(ChatColor.GRAY+"Es gibt keine offenen Tickets!");
		return true;
	}
}