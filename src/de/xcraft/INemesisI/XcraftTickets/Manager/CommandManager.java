package de.xcraft.INemesisI.XcraftTickets.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Commands.Admin.ModCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Admin.ReloadCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Admin.SaveCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Admin.StatsCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.AssignCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.PhrasesCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.UnAssignCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.UndoCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.WarpCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.User.CloseCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.User.ListCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.User.LogCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.User.OpenCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.User.ReOpenCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.User.SetWarpCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.User.ViewCommand;

public class CommandManager implements CommandExecutor, TabCompleter {

	private final TicketManager manager;
	private final Map<String, Command> commands = new TreeMap<String, Command>();;
	private final TabManager tabmanager;

	public CommandManager(TicketManager manager) {
		this.manager = manager;
		this.registerCommands();
		this.tabmanager = new TabManager(manager, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command bcmd, String label, String[] args) {
		// Grab the base and arguments.
		String cmd = (args.length > 0 ? args[0] : "");
		String last = (args.length > 0 ? args[args.length - 1] : "");
		// /tl as shortcut for /ticket list
		if (bcmd.getName().equals("tl")) {
			commands.get(getCommandInfo(ListCommand.class).pattern()).execute(manager, sender, args);
			return true;
		}
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
		if (!command.execute(manager, sender, params))
			showUsage(sender, command);
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
		sender.sendMessage(ChatColor.GOLD + "Verfügbare Befehle für " + manager.getPlugin().getDescription().getFullName() + " By INemesisI :");
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
		sender.sendMessage(ChatColor.DARK_GRAY + "->" + ChatColor.GREEN + "/" + info.command() + " " + info.name() + " " + info.usage() + " "
				+ ChatColor.DARK_AQUA + "- " + info.desc());
	}

	private void registerCommands() {
		// user Commands
		this.register(OpenCommand.class);
		this.register(LogCommand.class);
		this.register(ListCommand.class);
		this.register(ViewCommand.class);
		this.register(CloseCommand.class);
		this.register(ReOpenCommand.class);
		this.register(SetWarpCommand.class);
		// Mod Commands
		this.register(WarpCommand.class);
		this.register(AssignCommand.class);
		this.register(UnAssignCommand.class);
		this.register(UndoCommand.class);
		this.register(PhrasesCommand.class);
		// Admin Commands
		this.register(ReloadCommand.class);
		this.register(SaveCommand.class);
		this.register(ModCommand.class);
		this.register(StatsCommand.class);

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

	public Map<String, Command> getCommands() {
		return commands;
	}

	public CommandInfo getCommandInfo(Class<? extends Command> cmd) {
		return cmd.getAnnotation(CommandInfo.class);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
		return tabmanager.onTabComplete(sender, command, alias, args);
	}
}
