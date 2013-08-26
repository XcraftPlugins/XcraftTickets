package de.xcraft.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftUsage;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class AssigneeUsage extends XcraftUsage {
	TicketManager tManager;

	public AssigneeUsage(TicketManager tManager) {
		super("ASSIGNEE", Msg.USAGE_ASSIGNEE.toString());
		this.tManager = tManager;
	}

	@Override
	public boolean validate(String arg) {
		return tManager.getAssignees().contains(arg);
	}

	@Override
	public String getFailMessage() {
		return Msg.USAGE_NO_ASSIGNEE.toString();
	}

	@Override
	public List<String> onTabComplete(List<String> list, CommandSender sender, String token) {
		for (String assignee : tManager.getAssignees()) {
			if (assignee.toLowerCase().startsWith(token)) list.add(assignee);
		}
		return list;
	}


}
