package me.INemesisI.XcraftTickets.Commands;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor{
    public static XcraftTickets plugin;
	
    public WarpCommand(XcraftTickets survival) {
		plugin = survival;
    }

	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (args.length < 1 || !args[1].matches("\\d*")) {
			player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Du hast keine Ticketnummer angegeben"+ChatColor.GRAY+"(/ticket warp <Nr>)");
			return true;
		}
		int id = Integer.parseInt(args[1]);
		if(!plugin.data.getAllTicketIDs().contains(id)) {
			player.sendMessage(ChatColor.BLUE+plugin.getName()+ChatColor.RED+"Ein Ticket mit dieser Nummer existier nicht!");
			return true;
		}
		Location loc = plugin.data.getTicketLocation(id);
		player.teleport(loc);
		return true;
	}
}