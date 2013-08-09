package me.INemesisI.XcraftTickets.Commands.Admin;

import java.util.List;

import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "mod",
		command = "ticket",
		pattern = "m.*",
		permission = "XcraftTickets.Asignee",
		usage = "add|remove|list",
		desc = "bearbeiten aller mögl. Mods/Gruppen")
public class ModCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		List<String> assignees = manager.getAssignees();
		if (args.length < 1)
			return false;
		if (args[0].equals("list")) {
			reply(sender, ChatColor.DARK_AQUA + "Current assignees: " + ChatColor.GRAY + assignees.toString());
		} else if (args[0].equals("add")) {
			if (assignees.add(args[1]))
				reply(sender, "Successuflly added the assignee" + args[1]);
		} else if (args[0].equals("remove")) {
			if (assignees.remove(args[1])) {
				reply(sender, "Successuflly removed the assignee");
			} else {
				error(sender, "Could not find a assignee with that name");
			}
		}
		return true;
	}
}
