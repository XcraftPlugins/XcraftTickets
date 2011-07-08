package me.INemesisI.XcraftTickets;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class XcraftTicketsCommandHandler implements CommandExecutor {
	public static XcraftTickets plugin;

	private HashMap<String, CommandExecutor> executors = new HashMap<String, CommandExecutor>();
	private HashMap<String, String> permission = new HashMap<String, String>();

	public XcraftTicketsCommandHandler(XcraftTickets instance) {
		plugin = instance;
	}
	public void registerExecutor(String subcmd, CommandExecutor cmd, String permNode) {
	executors.put(subcmd.toLowerCase(), cmd);
	permission.put(subcmd.toLowerCase(), permNode);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
			if (args.length == 0 || args[0].equals("help")){
            	printUsage(sender);
            	return true;
            }
            String subcommandName = args[0].toLowerCase();
                //if argument NOT known
            if (!executors.containsKey(subcommandName)) {
            	printUsage(sender);
             	return true;
            }
            //if argument known: check if player has permission or isOp
            if (sender instanceof ConsoleCommandSender || sender instanceof Player && ( permission.get(subcommandName).isEmpty() || plugin.hasPermission((Player) sender, permission.get(subcommandName)))) 
            return executors.get(subcommandName).onCommand(sender, cmd, commandLabel, args);
            else {
              	sender.sendMessage(ChatColor.BLUE +plugin.getName()+ChatColor.RED +" You do not have access to that command!");
               	return true;
            }
	}
	public void printUsage(CommandSender sender) {
		sender.sendMessage(ChatColor.BLUE+"["+plugin.getDescription().getFullName()+"] by INemesisI" );
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket open <Nachricht>"+ChatColor.GRAY+"  Öffnet ein neues Ticket");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket comment|log <#> <Nachricht>"+ChatColor.GRAY+"  Kommentiert ein Ticket");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket close <#> (<Nachricht>)"+ChatColor.GRAY+"  Schliesst ein Ticket");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket list"+ChatColor.GRAY+"  Listet alle offenen Tickets auf");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket view|info <#>"+ChatColor.GRAY+"  Zeigt alle Informationen des Tickets");
		if(sender instanceof Player && plugin.data.isMod((Player) sender)) {
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket warp|goto <#>");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket assign <#> G:<Group>|<Mod>");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket unassign <#>");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket reopen <#> <Nachricht>");
		sender.sendMessage(ChatColor.BLUE+"-> "+ChatColor.DARK_AQUA+"/ticket listall");
		}
	}
}

