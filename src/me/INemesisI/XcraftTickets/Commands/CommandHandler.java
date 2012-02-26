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

public class CommandHandler extends CommandHelper implements CommandExecutor {
	private static Map<String, CommandHelper> subcommands = new HashMap<String, CommandHelper>();
	private static Map<String, String> permNodes = new HashMap<String, String>();

	public CommandHandler(XcraftTickets instance) {
		super(instance);
		CommandHelper commandhelper = new SaveCommand(plugin);
		addCommand("save", "Save", commandhelper);
		
		commandhelper = new ReloadCommand(plugin);
		addCommand("reload", "Reload", commandhelper);
		
		commandhelper = new OpenCommand(plugin);
		addCommand("open", "Open", commandhelper);
		addCommand("o", "Open", commandhelper);
		
		commandhelper = new ReOpenCommand(plugin);
		addCommand("reopen", "Reopen", commandhelper);
		addCommand("r", "Reopen", commandhelper);
		
		commandhelper = new CloseCommand(plugin);
		addCommand("close", "Close", commandhelper);
		addCommand("c", "Close", commandhelper);
		
		commandhelper = new LogCommand(plugin);
		addCommand("comment", "Log", commandhelper);
		addCommand("log", "Log", commandhelper);
		addCommand("l", "Log", commandhelper);
		
		commandhelper = new ListCommand(plugin);
		addCommand("list", "List", commandhelper);
		addCommand("listall", "List.All", commandhelper);
		
		commandhelper = new ViewCommand(plugin);
		addCommand("view", "View", commandhelper);
		addCommand("v", "View", commandhelper);
		
		commandhelper = new WarpCommand(plugin);
		addCommand("warp", "Warp", commandhelper);
		addCommand("w", "Warp", commandhelper);
		
		commandhelper = new AssignCommand(plugin);
		addCommand("assign", "Assign", commandhelper);
		addCommand("a", "Assign", commandhelper);
		
		commandhelper = new UnAssignCommand(plugin);
		addCommand("unassign", "Unassign", commandhelper);
		addCommand("u", "Unassign", commandhelper);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		this.sender = sender;
		// TODO: Console Stuff
		if (args.length == 0 || args[0].equals("help")) {
			PrintHelp(cmd.getName());
			return true;
		} else if (subcommands.get(args[0].toLowerCase()) == null) {
			error("Unkown command: " + args[0].toLowerCase());
		} else if (!(permNodes.get(args[0]).isEmpty() || sender.hasPermission(plugin.getDescription().getName() + "." + permNodes
				.get(args[0])) || sender.isOp())) {
			error("You do not have access to that command!");
			return true;
		} else {
			List<String> largs = Arrays.asList(args);
			String Command = largs.get(0);
			largs = largs.subList(1, largs.size());
			(subcommands.get(args[0].toLowerCase())).execute(sender, Command, (largs.size() > 0 ? largs.subList(0, largs.size())
					: new ArrayList<String>()));
		}
		return true;
	}

	private void addCommand(String command, String permode, CommandHelper commandclass) {
		permNodes.put(command, permode);
		subcommands.put(command, commandclass);
	}

	protected void print(String cmd, String command, String args, String message) {
		if (sender.hasPermission(plugin.getDescription().getName() + "." + permNodes.get(command))) sender
				.sendMessage(ChatColor.DARK_GRAY + "-->" + ChatColor.GREEN + "/" + cmd + " " + command + " " + args + ChatColor.DARK_AQUA + " - " + message);
	}

	public void PrintHelp(String cmd) {
		sender.sendMessage(ChatColor.BLUE + "[" + plugin.getDescription().getFullName() + "] by INemesisI");
		print(cmd, "open/o", "<Nachricht>", "Öffnet ein neues Ticket");
		print(cmd, "comment/log", "<#>", "Kommentiert ein Ticket");
		print(cmd, "close/c", "<#>", "Schliesst ein Ticket");
		print(cmd, "reopen/r", "<Typ>", "Öffnet ein geschlossenes Ticket");
		print(cmd, "list", "", "Listet deine Tickets auf");
		print(cmd, "view/v", "<#>", "Zeigt alle Informationen eines Tickets");
		print(cmd, "warp/w", "<#>", "Teleport zum Ticket");
		print(cmd, "assign/a", "<#>", "Weiterleiten eines Tickets");
		print(cmd, "unassign/u", "<#>", "Weiterleitung aufheben");
		print(cmd, "listall", "", "listet ALLE offenen Tickets auf");
	}

	@Override
	public void execute(CommandSender sender, String Command, List<String> list) {
	}
}
