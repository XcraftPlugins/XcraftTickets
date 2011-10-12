package me.INemesisI.XcraftTickets.Commands;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OpenCommand implements CommandExecutor{
    public static XcraftTickets plugin;
	
    public OpenCommand(XcraftTickets survival) {
		plugin = survival;
    }

	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Nachricht eingeben! "+ChatColor.GRAY+"(/ticket open <Nachricht>)");
			return true;
		}
		if(args[0].equals("open")){
			String title = "";
			for (int i=1;i<args.length;i++) {
				title = title+args[i];
				if (i < args.length-1) 
					title = title+" ";
			}
			int id = (plugin.data.getNextID());
			plugin.data.newTicket(plugin.data.getSendersName(sender), plugin.data.getSendersLocation(sender), title);
			sender.sendMessage(ChatColor.GRAY+"Vielen Dank! deine Ticketnummer ist "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+". Ein Mod wird sich nun darum kümmern!");
			plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Ein Ticket (Nr. "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+") wurde von " +ChatColor.YELLOW+plugin.data.getSendersName(sender)+ChatColor.GRAY+" eröffnet "+ChatColor.GRAY+": "+ChatColor.AQUA+title);
			plugin.data.setPlayerWatchedTicket(id, plugin.data.getSendersName(sender));
			return true;
		}
		if(args[0].equals("reopen")) {
			if (args.length < 1 || !args[1].matches("\\d*")) {
				sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Ticketnummer angegeben"+ChatColor.GRAY+"(/ticket reopen <Nr> <Nachricht>)");
				return true;
			}
			int id = Integer.parseInt(args[1]);
			String text = "";
			for (int i=2;i<args.length;i++) {
				text = text+args[i];
				if (i < args.length-1) 
					text = text+" ";
			}
			plugin.data.reopenTicket(id);
			plugin.data.addToLog(plugin.data.getSendersName(sender), id, "reopened", text);
			plugin.data.sendMessageToOwner(id, ChatColor.GRAY+"Dein Ticket "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+" wurde wieder eröffnet: "+ChatColor.AQUA+text);
			plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Ticket "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+" wurde wieder eröffnet: "+ChatColor.AQUA+text);
			return true;
		}
		return false;
	}
}