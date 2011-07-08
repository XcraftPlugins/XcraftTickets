package me.INemesisI.XcraftTickets.Commands;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommentCommand implements CommandExecutor{
    public static XcraftTickets plugin;
	
    public CommentCommand(XcraftTickets survival) {
		plugin = survival;
    }

	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean isplayer = (sender instanceof Player);
		if (args.length < 1 || !args[1].matches("\\d*")) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Ticketnummer angegeben"+ChatColor.GRAY+"(/ticket comment <Nr> <Nachricht>)");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Nachricht eingeben! "+ChatColor.GRAY+"(/ticket comment <Nr> <Nachricht>)");
			return true;
		}
		int id = Integer.parseInt(args[1]);
		if(!plugin.data.getAllTicketIDs().contains(id)) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Ein Ticket mit dieser Nummer existier nicht!");
			return true;
		}
		if (isplayer && (!plugin.data.isOwner(id, plugin.data.getSendersName(sender)) && !plugin.data.isMod((Player) sender))) {
			sender.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Rechte dieses Ticket kommentieren!"+ChatColor.GRAY+" \nEs ist nicht dein Ticket...");
			return true;
		}
		String text = "";
		for (int i=2;i<args.length;i++) {
			text = text+args[i];
			if (i < args.length-1) 
				text = text+" ";
		}
		plugin.data.addToLog(plugin.data.getSendersName(sender), id,"comment", text);
		plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Ticket "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+" wurde von "+ChatColor.YELLOW+plugin.data.getSendersName(sender)+ChatColor.GRAY+" kommentiert: "+ChatColor.AQUA+text);
		plugin.data.sendMessageToOwner(id, ChatColor.GRAY+"Dein Ticket "+ChatColor.GOLD+"#"+id+ChatColor.GRAY+" wurde von "+ChatColor.YELLOW+plugin.data.getSendersName(sender)+ChatColor.GRAY+" kommentiert: "+ChatColor.AQUA+text);
		plugin.data.setTicketUnWatched(id);
		plugin.data.setPlayerWatchedTicket(id, plugin.data.getSendersName(sender));
		
		return true;
	}

}
