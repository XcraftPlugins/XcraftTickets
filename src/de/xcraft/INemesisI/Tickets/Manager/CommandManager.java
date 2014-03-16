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
		this.registerCommand(new OpenCommand(this, "ticket", "open", "o.*", "<MESSAGE> ...", Msg.COMMAND_OPEN.toString(), "xcrafttickets.open"));
		this.registerCommand(new LogCommand(this, "ticket", "log", "l|lo.*", "<ID> <MESSAGE> ...", Msg.COMMAND_LOG.toString(), "xcrafttickets.log"));
		this.registerCommand(new ListCommand(this, "ticket", "list", "li.*", "", Msg.COMMAND_LIST.toString(), "xcrafttickets.list"));
		this.registerCommand(new ViewCommand(this, "ticket", "view", "v.*|i.*", "<ID> [all]", Msg.COMMAND_VIEW.toString(), "xcrafttickets.view"));
		this.registerCommand(new CloseCommand(this, "ticket", "close", "c.*", "<ID> <MESSAGE> ...", Msg.COMMAND_CLOSE.toString(), "xcrafttickets.close"));
		this.registerCommand(new ReOpenCommand(this, "ticket", "reopen", "reo.*|ro", "<ID> <MESSAGE> ...", Msg.COMMAND_REOPEN.toString(), "xcrafttickets.reopen"));
		this.registerCommand(new SetWarpCommand(this, "ticket", "setwarp", "se.*|sw", "<ID> [MESSAGE] ...", Msg.COMMAND_SETWARP.toString(), "xcrafttickets.setwarp"));
		// Mod Commands
		this.registerCommand(new WarpCommand(this, "ticket", "warp", "w.*", "<ID>", Msg.COMMAND_WARP.toString(), "xcrafttickets.warp"));
		this.registerCommand(new AssignCommand(this, "ticket", "assign", "a.*", "<ID> <ASSIGNEE>", Msg.COMMAND_ASSIGN.toString(), "xcrafttickets.assign"));
		this.registerCommand(new UnAssignCommand(this, "ticket", "unassign", "una.*", "<ID>", Msg.COMMAND_UNASSIGN.toString(), "xcrafttickets.unassign"));
		this.registerCommand(new UndoCommand(this, "ticket", "undo", "und.*", "<ID>", Msg.COMMAND_UNDO.toString(), "xcrafttickets.undo"));
		this.registerCommand(new PhrasesCommand(this, "ticket", "phrases", "p.*", "<list/add/remove/append> [MESSAGE]", Msg.COMMAND_PHRASES.toString(), "xcrafttickets.phrases"));
		this.registerCommand(new StatsCommand(this, "ticket", "stats", "st.*", "", Msg.COMMAND_STATS.toString(), "xcrafttickets.stats"));
		// Admin Commands
		this.registerCommand(new ModCommand(this, "ticket", "mod", "m.*", "<add/remove/list> [Assignee]", Msg.COMMAND_MOD.toString(), "xcrafttickets.mod"));
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
	public void onLoad(CommandSender sender) {
		super.onLoad(sender);
		Msg.init(plugin);
	}

}
