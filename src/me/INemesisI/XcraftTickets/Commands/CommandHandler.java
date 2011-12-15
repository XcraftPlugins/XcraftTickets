package me.INemesisI.XcraftTickets.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler extends CommandHelper implements CommandExecutor {
	private static Map<String, CommandHelper> subcommands = new HashMap<String, CommandHelper>();
	private static Map<String, String> permNodes = new HashMap<String, String>();

	public CommandHandler(XcraftTickets instance) {
		super(instance);
		CommandHelper ch = new PluginCommand(plugin);
		addCommand("save", "Save", ch);
		addCommand("reload", "Reload", ch);
		ch = new OpenCommand(plugin);
		addCommand("open", "Open", ch);
		addCommand("o", "Open", ch);
		ch = new ReOpenCommand(plugin);
		addCommand("reopen", "Reopen", ch);
		addCommand("r", "Reopen", ch);
		ch = new CloseCommand(plugin);
		addCommand("close", "Close", ch);
		addCommand("c", "Close", ch);
		ch = new LogCommand(plugin);
		addCommand("comment", "Log", ch);
		addCommand("log", "Log", ch);
		addCommand("l", "Log", ch);
		ch = new ListCommand(plugin);
		addCommand("list", "List", ch);
		ch = new ViewCommand(plugin);
		addCommand("view", "View", ch);
		addCommand("v", "View", ch);
		ch = new WarpCommand(plugin);
		addCommand("warp", "Warp", ch);
		addCommand("w", "Warp", ch);
		ch = new AssignCommand(plugin);
		addCommand("assign", "Assign", ch);
		addCommand("a", "Assign", ch);
		ch = new UnAssignCommand(plugin);
		addCommand("unassign", "Unassign", ch);
		addCommand("u", "Unassign", ch);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		this.sender = sender;
		player = (sender instanceof Player) ? (Player) sender : null;

		if (player == null) {
			// TODO: Console Stuff
		} else if (args.length == 0 || args[0].equals("help")) {
			PrintHelp();
			return true;
		} else if (subcommands.get(args[0].toLowerCase()) == null) {
			error("Unkown command: " + args[0].toLowerCase());
		} else if (!(permNodes.get(args[0]).isEmpty() || player.hasPermission(plugin.getDescription().getName() + "." + permNodes.get(args[0])) || player.isOp())) {
			error("You do not have access to that command!");
			return true;
		}

		else {
			List<String> largs = Arrays.asList(args);
			String Command = largs.get(0);
			largs = largs.subList(1, largs.size());
			(subcommands.get(args[0].toLowerCase())).execute(sender, Command, (largs.size() > 0 ? largs.subList(0, largs.size()) : new ArrayList<String>()));
		}
		return true;
	}
	
	private void addCommand(String command, String permode, CommandHelper commandclass) {
		permNodes.put(command, permode);
		subcommands.put(command, commandclass);
	}

	protected void print(String cmd, String values, String message) {
		if (player.hasPermission("XcraftRegionMarket." + permNodes.get(cmd)))
			sender.sendMessage(ChatColor.DARK_GRAY + "-->" + ChatColor.GREEN + "/ticket " + cmd + " " + values + ChatColor.DARK_AQUA + "- " + message);
	}

	public void PrintHelp() {
		sender.sendMessage(ChatColor.BLUE + "[" + plugin.getDescription().getFullName() + "] by INemesisI");
		print("open|o", "<Nachricht>", "Öffnet ein neues Ticket");
		print("comment|log", "<#>", "Kommentiert ein Ticket");
		print("close|c", "<#>", "Schliesst ein Ticket");
		print("reopen", "<Typ>", "Ändert den Typ");
		print("list", "", "Listet alle Tickets auf");
		print("view|v", "<#>", "Zeigt alle Informationen eines Tickets");
		print("warp", "<#>", "");	
		print("assign", "<#>", "");
		print("unassign", "<#>", "");
		print("listall", "", "");
	}
	

	@Override
	public void execute(CommandSender sender, String Command, List<String> list) {
	}
}
