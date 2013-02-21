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

public class CommandManager implements CommandExecutor {
	public XcraftTickets plugin;
	private static Map<String, PluginCommand> commands = new HashMap<String, PluginCommand>();

	public CommandManager(XcraftTickets plugin) {
		this.plugin = plugin;
		this.addCommand("save", new SaveCommand(plugin, "Save"));
		this.addCommand("reload", new ReloadCommand(plugin, "Reload"));
		this.addCommand("open, o", new OpenCommand(plugin, "Open"));
		this.addCommand("reopen, r", new ReOpenCommand(plugin, "Reopen"));
		this.addCommand("close, c", new CloseCommand(plugin, "Close"));
		this.addCommand("comment, log, l", new LogCommand(plugin, "Log"));
		this.addCommand("undo, u", new UndoCommand(plugin, "Undo"));
		this.addCommand("list, li", new ListCommand(plugin, "List"));
		this.addCommand("listall, la", new ListCommand(plugin, "Listall"));
		this.addCommand("view, v", new ViewCommand(plugin, "View"));
		this.addCommand("warp, w", new WarpCommand(plugin, "Warp"));
		this.addCommand("setwarp, sw", new SetWarpCommand(plugin, "SetWarp"));
		this.addCommand("assign, a", new AssignCommand(plugin, "Assign"));
		this.addCommand("unassign, u", new UnAssignCommand(plugin, "Unassign"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// TODO: Console Stuff
		String command = args.length == 0 ? null : args[0].toLowerCase();
		if (cmd.getName().equals("tl")) command = "list";
		if ((command == null) || command.equals("help")) {
			this.PrintHelp(sender, cmd.getName());
			return true;
		} else if (commands.get(command) == null) {
			sender.sendMessage(ChatColor.RED + "Unbekannter Befehl: /" + cmd.getName() + " " + command);
			this.PrintHelp(sender, cmd.getName());
		} else if (!(commands.get(command).permnode.isEmpty() || sender.hasPermission(commands.get(command).permnode))) {
			sender.sendMessage(ChatColor.RED + "Du hast keinen Zugriff auf diesen Befehl!");
			return true;
		} else {
			List<String> largs = Arrays.asList(args);
			String Command = largs.get(0);
			largs = largs.subList(1, largs.size());
			commands.get(command).execute(sender, Command, largs.size() > 0 ? largs : new ArrayList<String>());
		}
		return true;
	}

	private void addCommand(String command, PluginCommand plugincommand) {
		for (String cmd : command.split(", ")) {
			commands.put(cmd, plugincommand);
		}

	}

	public void PrintHelp(CommandSender sender, String cmd) {
		sender.sendMessage(ChatColor.BLUE + "Befehle für [" + plugin.getDescription().getFullName() + "] by INemesisI");
		this.print(sender, cmd, "open", "<Nachricht>", "Öffnet ein neues Ticket");
		this.print(sender, cmd, "log", "<#>", "Kommentiert ein Ticket");
		this.print(sender, cmd, "close", "<#>", "Schliesst ein Ticket");
		this.print(sender, cmd, "reopen", "<Typ>", "Öffnet ein geschlossenes Ticket");
		this.print(sender, cmd, "undo", "<Typ>", "Macht den letzten Befehl rückgängig");
		this.print(sender, cmd, "list", "", "Listet deine Tickets auf");
		this.print(sender, cmd, "view", "<#>", "Zeigt alle Informationen eines Tickets");
		this.print(sender, cmd, "setwarp", "<#>", "Ändert den Warp eines Tickets");
		this.print(sender, cmd, "warp", "<#>", "Teleport zum Ticket");
		this.print(sender, cmd, "assign", "<#>", "Weiterleiten eines Tickets");
		this.print(sender, cmd, "unassign", "<#>", "Weiterleitung aufheben");
		this.print(sender, cmd, "listall", "", "listet ALLE offenen Tickets auf");
		this.print(sender, cmd, "save", "", "Speichert alle nötigen Daten");
		this.print(sender, cmd, "reload", "", "Lädt die Tickets aus den gespeicherten Daten");
	}

	protected void print(CommandSender sender, String cmd, String command, String args, String message) {
		if (sender.hasPermission(commands.get(command).permnode)) {
			sender.sendMessage(ChatColor.DARK_GRAY + "->" + ChatColor.GREEN + "/" + cmd + " " + command + " " + args + ChatColor.DARK_AQUA + " - " + message);
		}
	}
}
