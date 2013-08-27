package de.xcraft.INemesisI.XcraftTickets.Commands.Admin;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Msg.Replace;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class ModCommand extends XcraftCommand {

	public ModCommand() {
		super("ticket", "mod", "m.*", "<add/remove/list> [Assignee]", Msg.COMMAND_MOD.toString(), "XcraftTickets.Mod");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		List<String> assignees = manager.getAssignees();
		if (args.length < 1)
			return false;
		if (args[0].equals("list")) {
			pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_MOD_LIST.toString(Replace.MISC(assignees.toString())), true);
		} else if (args[0].equals("add")) {
			if (assignees.add(args[1])) {
				pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_MOD_ADD.toString(Replace.NAME(args[1])), true);
			}
		} else if (args[0].equals("remove")) {
			if (assignees.remove(args[1])) {
				pManager.plugin.getMessenger().sendInfo(sender, Msg.COMMAND_MOD_REMOVE.toString(Replace.NAME(args[1])), true);
			} else {
				pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_MOD_NOT_FOUND.toString(Replace.NAME(args[1])), true);
			}
		} else
			return false;
		return true;
	}

}
