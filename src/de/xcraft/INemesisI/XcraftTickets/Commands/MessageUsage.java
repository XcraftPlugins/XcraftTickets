package de.xcraft.INemesisI.XcraftTickets.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Utils.Command.XcraftUsage;
import de.xcraft.INemesisI.Utils.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class MessageUsage extends XcraftUsage {
	TicketManager tManager;

	public MessageUsage() {
		super("MESSAGE", Msg.USAGE_MESSAGE.toString());
	}

	@Override
	public List<String> onTabComplete(XcraftPluginManager pManager, List<String> list, CommandSender sender, String token) {
		TicketManager tManager = (TicketManager) pManager;
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
