package de.xcraft.INemesisI.XcraftTickets.Manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.XcraftTickets;
import de.xcraft.INemesisI.XcraftTickets.Commands.AssigneeUsage;
import de.xcraft.INemesisI.XcraftTickets.Commands.IDUsage;
import de.xcraft.INemesisI.XcraftTickets.Commands.MessageUsage;
import de.xcraft.INemesisI.XcraftTickets.Commands.Admin.ModCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.AssignCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.PhrasesCommand;
import de.xcraft.INemesisI.XcraftTickets.Commands.Mod.StatsCommand;
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

public class CommandManager extends XcraftCommandManager {
	TicketManager tManager;

	public CommandManager(XcraftTickets plugin) {
		super(plugin);
		tManager = plugin.getPluginManager();
	}

	@Override
	protected void registerCommands() {
		this.registerBukkitCommand("ticket");
		this.registerBukkitCommand("tl");
		// User Commands
		this.registerCommand(new OpenCommand());
		this.registerCommand(new LogCommand());
		this.registerCommand(new ListCommand());
		this.registerCommand(new ViewCommand());
		this.registerCommand(new CloseCommand());
		this.registerCommand(new ReOpenCommand());
		this.registerCommand(new SetWarpCommand());
		// Mod Commands
		this.registerCommand(new WarpCommand());
		this.registerCommand(new AssignCommand());
		this.registerCommand(new UnAssignCommand());
		this.registerCommand(new UndoCommand());
		this.registerCommand(new PhrasesCommand());
		// Admin Commands
		this.registerCommand(new ModCommand());
		this.registerCommand(new StatsCommand());
		// Usage
		this.registerUsage(new IDUsage(tManager));
		this.registerUsage(new MessageUsage(tManager));
		this.registerUsage(new AssigneeUsage(tManager));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command bcmd, String label, String[] args) {
		if (bcmd.getName().equals("tl")) {
			this.getCommands().get("li.*").execute(tManager, sender, args);
			return true;
		} else
			return super.onCommand(sender, bcmd, label, args);
	}

	@Override
	public void onReload(CommandSender sender) {
		super.onReload(sender);
		Msg.init(plugin);
	}

}
