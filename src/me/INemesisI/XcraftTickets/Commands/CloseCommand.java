package me.INemesisI.XcraftTickets.Commands;


import java.util.ArrayList;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CloseCommand implements CommandExecutor{
    public static XcraftTickets plugin;
	
    public CloseCommand(XcraftTickets survival) {
		plugin = survival;
    }

	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean isplayer = (sender instanceof Player);
		if (args.length < 3) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Nachricht eingeben! "+ChatColor.GRAY+"(/ticket close <Nr> <Nachricht>)");
			return true;
		}
		String text = "";
		for (int i=2;i<args.length;i++) {
			text = text+args[i];
			if (i < args.length-1) 
				text = text+" ";
		}
		if (args.length < 1 || !args[1].matches("\\d*")) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Ticketnummer angegeben"+ChatColor.GRAY+"(/ticket close <Nr> <Nachricht>)");
			return true;
		}
		int id = Integer.parseInt(args[1]);
		if(!plugin.data.getAllTicketIDs().contains(id)) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Ein Ticket mit dieser Nummer existier nicht!");
			return true;
		}
		if (isplayer && (!plugin.data.isOwner(id, ((Player) sender).getName()) && !plugin.data.isMod((Player) sender))) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Rechte dieses Ticket schliessen!"+ChatColor.GRAY+" \nEs ist nicht dein Ticket...");
			return true;
		}
		plugin.data.addToLog(plugin.data.getSendersName(sender), id, "closed", text);
		String dot;
		if (text.length() == 0)
			 dot = ". ";
		else  dot = ": ";
		if (!isplayer)
			sender.sendMessage(ChatColor.GRAY+"Ticket "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+" wurde geschlossen"+dot+ChatColor.AQUA+text);
		plugin.data.sendMessageToOwner(id, ChatColor.GRAY+"Dein Ticket "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+" wurde geschlossen"+dot+ChatColor.AQUA+text);
		plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Ticket "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+" wurde geschlossen"+dot+ChatColor.AQUA+text);
		plugin.data.closeTicket(id);
		if (sender instanceof Player) {
				ArrayList<Integer> reminder = plugin.data.getReminderTickets(((Player) sender).getName());
			if (reminder != null && reminder.contains(id)) {
				reminder.remove((Integer) id);
				plugin.data.setReminderTickets((Player) sender, reminder);
			}	
		}
		
		
		return true;
	}
}