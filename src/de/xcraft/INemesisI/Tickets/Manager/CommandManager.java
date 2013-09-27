package de.xcraft.INemesisI.Tickets.Manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.XcraftTickets;
import de.xcraft.INemesisI.Tickets.Commands.AssigneeUsage;
import de.xcraft.INemesisI.Tickets.Commands.IDUsage;
import de.xcraft.INemesisI.Tickets.Commands.MessageUsage;
import de.xcraft.INemesisI.Tickets.Commands.Admin.ModCommand;
import de.xcraft.INemesisI.Tickets.Commands.Mod.AssignCommand;
import de.xcraft.INemesisI.Tickets.Commands.Mod.PhrasesCommand;
import de.xcraft.INemesisI.Tickets.Commands.Mod.StatsCommand;
import de.xcraft.INemesisI.Tickets.Commands.Mod.UnAssignCommand;
import de.xcraft.INemesisI.Tickets.Commands.Mod.UndoCommand;
import de.xcraft.INemesisI.Tickets.Commands.Mod.WarpCommand;
import de.xcraft.INemesisI.Tickets.Commands.User.CloseCommand;
import de.xcraft.INemesisI.Tickets.Commands.User.ListCommand;
import de.xcraft.INemesisI.Tickets.Commands.User.LogCommand;
import de.xcraft.INemesisI.Tickets.Commands.User.OpenCommand;
import de.xcraft.INemesisI.Tickets.Commands.User.ReOpenCommand;
import de.xcraft.INemesisI.Tickets.Commands.User.SetWarpCommand;
import de.xcraft.INemesisI.Tickets.Commands.User.ViewCommand;

public class CommandManager extends XcraftCommandManager {
	TicketManager tManager;

	public CommandManager(XcraftTickets plugin) {
		super(plugin);
	}

	@Override
	protected void registerCommands() {
		this.tManager = (TicketManager) plugin.getPluginManager();
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