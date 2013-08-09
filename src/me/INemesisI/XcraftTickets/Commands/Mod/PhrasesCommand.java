package me.INemesisI.XcraftTickets.Commands.Mod;

import java.util.Map;

import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "phrases",
		command = "ticket",
		pattern = "p.*",
		permission = "XcraftTickets.Phrases",
		usage = "list|add|remove|append <Phrase> [Message]",
		desc = "bearbeiten aller Ticket-Phrasen")
public class PhrasesCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		Map<String, String> map = manager.getPhrases();
		if (args.length < 1)
			return false;
		if (args[0].equals("list")) {
			reply(sender, ChatColor.DARK_AQUA + "Predefined Phrases: ");
			for (String key : map.keySet()) {
				reply(sender, ChatColor.DARK_AQUA + key + ": " + ChatColor.DARK_GRAY + map.get(key));
			}
		} else if (args[0].equals("add")) {
			String key = args[1];
			String msg = "";
			for (int i = 2; i < args.length; i++) {
				msg += args[i] + " ";
			}
			msg.trim();
			manager.getPhrases().put(key, msg);
			reply(sender, "Successuflly added the phrase (" + key + " = " + msg + ")");
		} else if (args[0].equals("remove")) {
			String key = args[1];
			if (manager.getPhrases().remove(key) != null) {
				reply(sender, "Successuflly removed the phrase");
			} else {
				error(sender, "Could not find a phrase with that name");
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
				reply(sender, "Successuflly edited the phrase: " + ChatColor.GRAY + manager.getPhrases().get(key));
			} else {
				error(sender, "Could not find a phrase with that name");
			}
		}
		return true;
	}
}
