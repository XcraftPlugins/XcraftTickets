package me.INemesisI.XcraftTickets.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabManager implements TabCompleter {
	private final TicketManager tManager;
	private final CommandManager cManager;

	public TabManager(TicketManager ticketManager, CommandManager commandManager) {
		this.tManager = ticketManager;
		this.cManager = commandManager;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bcmd, String alias, String[] args) {
		List<String> list = new ArrayList<String>();
		for (Command cmd : cManager.getCommands().values()) {
			CommandInfo info = cManager.getCommandInfo(cmd.getClass());
			if (bcmd.getName().matches(info.command()) && sender.hasPermission(info.permission())) {
				if (args.length > 1 && args[0].matches(info.pattern())) {
					String[] usages = info.usage().split(" ");
					if (usages.length > args.length - 2) {
						String usage = usages[args.length - 2]; // what the Plugin wants the user to use
						String token = args[args.length - 1].toLowerCase(); // what the user typed in so far
						// Look for different cases
						if (usage.equals("[#]")) {
							int lastTicket = tManager.getLastTicket(sender);
							List<Ticket> ticketsUnassigned = new ArrayList<Ticket>();
							List<Ticket> ticketsAssigned = new ArrayList<Ticket>();
							List<Ticket> ticketsOwned = new ArrayList<Ticket>();
							for (Ticket ticket : tManager.getTickets()) {
								if (lastTicket != -1 && lastTicket == ticket.getId())
									continue;
								if ((ticket.isAssigned() && ticket.isAssignee(sender, tManager))) {
									ticketsAssigned.add(ticket);
								} else if (!ticket.isAssigned()) {
									ticketsUnassigned.add(ticket);
								} else if (ticket.getOwner().equals(sender.getName())) {
									ticketsOwned.add(ticket);
								}
							}
							sort(ticketsAssigned);
							sort(ticketsUnassigned);
							sort(ticketsOwned);
							// add to the actual Tab-List
							if (lastTicket != -1)
								list.add(String.valueOf(lastTicket));
							for (Ticket ticket : ticketsAssigned) {
								list.add(String.valueOf(ticket.getId()));
							}
							for (Ticket ticket : ticketsUnassigned) {
								list.add(String.valueOf(ticket.getId()));
							}
							for (Ticket ticket : ticketsOwned) {
								list.add(String.valueOf(ticket.getId()));
							}
							// done adding
						} else if (usage.equals("[Nachricht]")) {
							Map<String, String> phrases = tManager.getPhrases();
							for (String phrase : phrases.keySet()) {
								if (phrase.equals("") || phrase.toLowerCase().startsWith(token)) {
									list.add(phrase);
								}
							}
						} else if (usage.equals("[Name|Gruppe]")) {
							List<String> assignees = tManager.getAssignees();
							for (String assignee : assignees) {
								if (token.equals("") || assignee.toLowerCase().startsWith(token))
									list.add(assignee);
							}
						} else if (!usage.startsWith("[") && !usage.endsWith("]")) {
							for (String key : usage.split("\\|")) {
								if (token.equals("") || key.toLowerCase().startsWith(token))
									list.add(key);
							}
						} else {
							for (Player player : tManager.getPlugin().getServer().getOnlinePlayers()) {
								if (token.equals("") || player.getName().toLowerCase().startsWith(token))
									list.add(player.getName());
							}
						}
					}
					break;
				} else if (args.length <= 1 && (args[0].equals("") || info.name().startsWith(args[0]))) {
					list.add(info.name());
				}
			}
		}
		return list;
	}

	private void sort(List<Ticket> ticketsAssigned) {
		for (int n = ticketsAssigned.size(); n > 1; n--) {
			for (int i = 0; i < n - 1; i++) {
				if (ticketsAssigned.get(i).getProcessed() > ticketsAssigned.get(i + 1).getProcessed()) {
					Ticket temp = ticketsAssigned.get(i);
					ticketsAssigned.set(i, ticketsAssigned.get(i + 1));
					ticketsAssigned.set(i + 1, temp);
				}
			}
		}
	}
}
