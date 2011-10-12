package me.INemesisI.XcraftTickets.Commands;


import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AssignCommand implements CommandExecutor{
    public static XcraftTickets plugin;
	
    public AssignCommand(XcraftTickets survival) {
		plugin = survival;
    }

	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (args.length < 1 || !args[1].matches("\\d*")) {
			player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Ticketnummer angegeben"+ChatColor.GRAY+"(/ticket assign <Nr> <Name|G:Gruppe>)");
		return true;
	}
	int id = Integer.parseInt(args[1]);
	if(!plugin.data.getAllTicketIDs().contains(id)) {
		player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Ein Ticket mit dieser nummer existier nicht!");
		return true;
	}
		if(args[0].equals("assign")) {
			String name = args[2];
			String mod = "";
			Player p = plugin.getServer().getPlayer(args[2]);
			if (p == null) {
				OfflinePlayer op = plugin.getServer().getOfflinePlayer(name);
				if(op != null)
				mod = op.getName();
				else
					player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Spieler mit dem Namen existiert nicht!");
			}
			else
				if (p.hasPermission("XcraftTickets.Mod")) {
				mod = p.getDisplayName();
				}
				else
					player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Konnte Mod "+ChatColor.DARK_PURPLE+args[2]+ChatColor.RED+" nicht finden!");
			if (!mod.isEmpty()) {
				plugin.data.assignTo(id, mod);
				plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde "+ChatColor.DARK_PURPLE+mod+ChatColor.GRAY +" zugewiesen!");
				plugin.data.sendMessageToOwner(id, ChatColor.GRAY+"Dein Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde "+ChatColor.DARK_PURPLE+mod+ChatColor.GRAY +" zugewiesen!");
				return true;
			}
		}
		if(args[0].equals("unassign")){
			plugin.data.addToLog(player.getName(), id, "unassigned", "");
			plugin.data.assignTo(id, "none");
			plugin.data.sendMessageToOwner(id, ChatColor.GRAY+"Die Zuweisung für dein Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde entfernt!");
			plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Die Zuweisung für Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde entfernt!");
			plugin.data.setTicketUnWatched(id);
			return true;
		}
		return false;
	}
}