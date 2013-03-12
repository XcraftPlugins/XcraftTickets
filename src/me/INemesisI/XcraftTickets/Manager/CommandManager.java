package me.INemesisI.XcraftTickets.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Commands.Admin.ReloadCommand;
import me.INemesisI.XcraftTickets.Commands.Admin.SaveCommand;
import me.INemesisI.XcraftTickets.Commands.Mod.AssignCommand;
import me.INemesisI.XcraftTickets.Commands.Mod.UnAssignCommand;
import me.INemesisI.XcraftTickets.Commands.Mod.UndoCommand;
import me.INemesisI.XcraftTickets.Commands.Mod.WarpCommand;
import me.INemesisI.XcraftTickets.Commands.User.CloseCommand;
import me.INemesisI.XcraftTickets.Commands.User.ListCommand;
import me.INemesisI.XcraftTickets.Commands.User.LogCommand;
import me.INemesisI.XcraftTickets.Commands.User.OpenCommand;
import me.INemesisI.XcraftTickets.Commands.User.ReOpenCommand;
import me.INemesisI.XcraftTickets.Commands.User.SetWarpCommand;
import me.INemesisI.XcraftTickets.Commands.User.ViewCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

	private final XcraftTickets plugin;
	private final TicketManager manager;
	private Map<String, Command> commands;

	public CommandManager(XcraftTickets plugin) {
		this.plugin = plugin;
		manager = this.plugin.ticketManager;
		this.registerCommands();
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command bcmd, String label, String[] args) {
		// Grab the base and arguments.
		String cmd = (args.length > 0 ? args[0] : "");
		String last = (args.length > 0 ? args[args.length - 1] : "");
		// The help command is a little special
		if (cmd.equals("") || cmd.equals("?") || cmd.equals("help")) {
			this.showHelp(sender, bcmd.getName());
			return true;
		}
		// Get all commands that match the base.
		List<Command> matches = this.getMatchingCommands(bcmd.getName(), cmd);
		// If there's more than one match, display them.
		if (matches.size() > 1) {
			sender.sendMessage("Meinst du villeicht einen dieser Befehle?");
			for (Command c : matches) {
				this.showUsage(sender, c);
			}
			return true;
		}
		// If there are no matches at all, notify.
		if (matches.size() == 0) {

			sender.sendMessage(ChatColor.RED + "Unbekannter Befehl: \"" + cmd + "\"!");
			this.showHelp(sender, bcmd.getName());
			return true;
		}
		// Grab the only match.
		Command command = matches.get(0);
		CommandInfo info = this.getCommandInfo(command.getClass());
		// First check if the sender has permission.
		if (!sender.hasPermission(info.permission())) {
			sender.sendMessage(ChatColor.RED + " Du hast keinen Zugriff auf diesen Befehl!");
			return true;
		}
		// Check if the last argument is a ?, in which case, display usage and
		// description
		if (last.equals("?") || last.equals("help")) {
			this.showUsage(sender, command);
			return true;
		}
		// Otherwise, execute the command!
		String[] params = Arrays.copyOfRange(args, 1, args.length);
		command.execute(manager, sender, params);
		return true;
	}

	private List<Command> getMatchingCommands(String cmd, String arg) {
		List<Command> result = new ArrayList<Command>();
		// Grab the commands that match the argument.
		for (Entry<String, Command> entry : commands.entrySet()) {
			if (arg.matches(entry.getKey()) && cmd.matches(this.getCommandInfo(entry.getValue().getClass()).command())) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	private void showHelp(CommandSender sender, String cmd) {
		sender.sendMessage(ChatColor.DARK_AQUA + "Verfügbare Befehle:");
		for (Command command : commands.values()) {
			if (cmd.matches(this.getCommandInfo(command.getClass()).command())) {
				this.showUsage(sender, command);
			}
		}
	}

	private void showUsage(CommandSender sender, Command cmd) {
		CommandInfo info = this.getCommandInfo(cmd.getClass());
		if (!sender.hasPermission(info.permission())) {
			return;
		}
		sender.sendMessage(info.usage() + " " + ChatColor.YELLOW + info.desc());
	}

	private void registerCommands() {
		commands = new HashMap<String, Command>();
		// Admin Commands
		this.register(ReloadCommand.class);
		this.register(SaveCommand.class);
		// Mod Commands
		this.register(AssignCommand.class);
		this.register(UnAssignCommand.class);
		this.register(UndoCommand.class);
		this.register(WarpCommand.class);
		// user Commands
		this.register(CloseCommand.class);
		this.register(ListCommand.class);
		this.register(LogCommand.class);
		this.register(OpenCommand.class);
		this.register(ReOpenCommand.class);
		this.register(SetWarpCommand.class);
		this.register(ViewCommand.class);

	}

	private void register(Class<? extends Command> cmd) {
		CommandInfo info = this.getCommandInfo(cmd);
		if (info == null) {
			return;
		}

		try {
			commands.put(info.pattern(), cmd.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CommandInfo getCommandInfo(Class<? extends Command> cmd) {
		return cmd.getAnnotation(CommandInfo.class);
	}
}
