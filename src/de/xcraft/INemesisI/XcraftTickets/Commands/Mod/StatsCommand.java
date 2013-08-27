package de.xcraft.INemesisI.XcraftTickets.Commands.Mod;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.XcraftTickets.Msg;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

public class StatsCommand extends XcraftCommand {

	public StatsCommand() {
		super("ticket", "stats", "s.*", "", Msg.COMMAND_STATS.toString(), "XcraftTickets.Stats");
	}

	@Override
	public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
		TicketManager manager = (TicketManager) pManager;

		pManager.plugin.getMessenger().sendInfo(sender, "&6Stats:", true);
		Map<String, Integer> list = manager.cManager.getStats();

		Map<String, Integer> map = new HashMap<String, Integer>();
		int rest = 0;

		for (String entry : list.keySet()) {
			if (!manager.getAssignees().contains(entry)) {
				rest += list.get(entry);
			} else {
				map.put(entry.trim(), list.get(entry));
			}
		}
		list = null;
		ValueComparator bvc = new ValueComparator(map);
		Map<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
		sorted_map.putAll(map);
		for (String key : sorted_map.keySet()) {
			pManager.plugin.getMessenger().sendInfo(sender, "    &3" + key + ": &7" + map.get(key), true);
		}
		pManager.plugin.getMessenger().sendInfo(sender, "    &8Rest: &7" + rest, true);

		return true;
	}

	class ValueComparator implements Comparator<String> {

		Map<String, Integer> base;

		public ValueComparator(Map<String, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		@Override
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b))
				return -1;
			else
				return 1;
		}
	}
}
