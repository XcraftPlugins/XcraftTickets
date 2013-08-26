package de.xcraft.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftUsage;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

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
	public List<String> onTabComplete(List<String> list, CommandSender sender, String token) {
		if (sender.hasPermission("XcraftTickets.Phrases")) {
			for (String phrase : tManager.getPhrases().keySet()) {
				if (phrase.toLowerCase().startsWith(token)) {
					list.add(phrase);
				}
			}
		}
		return list;
	}

}
