package de.xcraft.INemesisI.Tickets.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftUsage;
import de.xcraft.INemesisI.Tickets.Msg;
import de.xcraft.INemesisI.Tickets.Manager.TicketManager;

public class MessageUsage extends XcraftUsage {
	TicketManager tManager;

	public MessageUsage(TicketManager tManager) {
		super("MESSAGE", Msg.USAGE_MESSAGE.toString());
		this.tManager = tManager;
	}

	@Override
	public boolean validate(String arg) {
		return true;
	}

	@Override
	public String getFailMessage() {
		return Msg.USAGE_NO_MESSAGE.toString();
	}

	@Override
	public List<String> onTabComplete(List<String> list, CommandSender sender) {
		if (sender.hasPermission("XcraftTickets.Phrases")) {
			list.addAll(tManager.getPhrases().keySet());
		}
		return list;
	}

}
