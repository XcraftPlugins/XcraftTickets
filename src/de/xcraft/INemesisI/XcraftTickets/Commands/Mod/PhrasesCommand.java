package de.xcraft.INemesisI.XcraftTickets.Commands.Mod;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Msg.Replace;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class PhrasesCommand extends XcraftCommand { // TODO: workaround TEXT?

	public PhrasesCommand() {
		super("ticket", "phrases", "p.*", "<list/add/remove/append> [MESSAGE] [Text]", Msg.COMMAND_PHRASES.toString(), "XcraftTickets.Phrases");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;
		Map<String, String> map = manager.getPhrases();
		if (args.length < 1)
			return false;
		if (args[0].equals("list")) {
			pManager.plugin.messenger.sendInfo(sender, Msg.COMMAND_PHRASES_LIST.toString(), true);
			for (String key : map.keySet()) {
				pManager.plugin.messenger.sendInfo(sender, ChatColor.DARK_AQUA + key + ": " + ChatColor.DARK_GRAY + map.get(key), true);
			}
		} else if (args[0].equals("add")) {
			String key = args[1];
			String msg = "";
			for (int i = 2; i < args.length; i++) {
				msg += args[i] + " ";
			}
			msg.trim();
			manager.getPhrases().put(key, msg);
			pManager.plugin.messenger.sendInfo(sender, "Successuflly added the phrase (" + key + " = " + msg + ")", true);
			pManager.plugin.messenger.sendInfo(sender, Msg.COMMAND_PHRASES_ADD.toString(Replace.NAME(key), Replace.MESSAGE(msg)), true);
		} else if (args[0].equals("remove")) {
			String key = args[1];
			if (manager.getPhrases().remove(key) != null) {
				pManager.plugin.messenger.sendInfo(sender, Msg.COMMAND_PHRASES_REMOVE.toString(Replace.NAME(key)), true);
			} else {
				pManager.plugin.messenger.sendInfo(sender, Msg.ERR_PHRASE_NOT_FOUND.toString(Replace.NAME(key)), true);
			}
		} else if (args[0].equals("append")) {
			String key = args[1];
			String msg = "";
			for (int i = 2; i < args.length; i++) {
				msg += args[i] + " ";
			}
			msg.trim();
			if (manager.getPhrases().containsKey(key)) {
				manager.getPhrases().put(key, manager.getPhrases().get(key) + msg);
				pManager.plugin.messenger.sendInfo(sender, Msg.COMMAND_PHRASES_APPEND.toString(Replace.NAME(key), Replace.MESSAGE(msg)), true);
			} else {
				pManager.plugin.messenger.sendInfo(sender, Msg.ERR_PHRASE_NOT_FOUND.toString(Replace.NAME(key)), true);
			}
		}
		return true;
	}
}
