package de.xcraft.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftUsage;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class AssigneeUsage extends XcraftUsage {
	TicketManager tManager;

	public AssigneeUsage() {
		super("ASSIGNEE", Msg.USAGE_ASSIGNEE.toString());
	}

	@Override
	public List<String> onTabComplete(XcraftPluginManager pManager, List<String> list, CommandSender sender, String token) {
		TicketManager tManager = (TicketManager) pManager;
		List<String> assignees = tManager.getAssignees();
		for (String assignee : assignees) {
			if (assignee.toLowerCase().startsWith(token)) list.add(assignee);
		}
		return list;
	}

}
