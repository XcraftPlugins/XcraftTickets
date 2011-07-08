package me.INemesisI.XcraftTickets.Commands;

import java.io.File;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nijiko.permissions.Group;
import com.nijiko.permissions.User;

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
			if (args[2].toLowerCase().startsWith("g:")) {
				if (!plugin.assignSupport)
					player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Gruppen werden vom permission Plugin nicht unterstützt!");
				name = name.replace("g:", "").replace("G:", "");
				Group group = plugin.permissionHandler.getGroupObject(player.getWorld().getName(), name);
				if (group != null) {
					if (group.hasPermission("XcraftTickets.mod")) {
					plugin.data.assignTo(id, "G:"+group.getName());
					plugin.data.sendMessageToOwner(id, ChatColor.GRAY+"Dein Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde der Gruppe "+ChatColor.DARK_PURPLE+group.getName()+ChatColor.GRAY +" zugewiesen!");
					plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde der Gruppe "+ChatColor.DARK_PURPLE+group.getName()+ChatColor.GRAY +" zugewiesen!");
					return true;
					}
					else
						player.sendMessage(ChatColor.RED+"Gruppe "+ChatColor.DARK_PURPLE+group.getName()+" hat keine Mod permission!");
				}
				else
					player.sendMessage(ChatColor.RED+"Konnte Gruppe "+ChatColor.DARK_PURPLE+args[2] +ChatColor.RED+" nicht finden!");
			}
			else {
				String mod = "";
				if(plugin.assignSupport == false) {
					System.out.println(name);
					File file = new File(player.getWorld().getName()+"/players/"+name+".dat");
					if (!file.exists()) {
						player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"konnte Diesen Spieler nicht finden!");
						return true;
					}
					mod = file.getAbsoluteFile().getName().replace(".dat", "");
				}
				else {
				User user = plugin.permissionHandler.getUserObject(player.getWorld().getName(), args[2]);
				if (user.hasPermission("XcraftTickets.mod")) {
					mod = user.getName();
				}
				else
					player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Konnte Mod "+ChatColor.DARK_PURPLE+args[2]+ChatColor.RED+" nicht finden!");
				return true;
				}
				System.out.println(mod);
				if (!mod.isEmpty()) {
					plugin.data.assignTo(id, mod);
					plugin.data.sendMessageToMods(id, ChatColor.GRAY+"Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde "+ChatColor.DARK_PURPLE+mod+ChatColor.GRAY +" zugewiesen!");
					plugin.data.sendMessageToOwner(id, ChatColor.GRAY+"Dein Ticket "+ChatColor.GOLD+"#"+id+ ChatColor.GRAY+" wurde "+ChatColor.DARK_PURPLE+mod+ChatColor.GRAY +" zugewiesen!");
				}
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