package de.xcraft.INemesisI.XcraftTickets.Commands.Admin;


import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "reload",
		command = "ticket",
		pattern = "rel.*",
		permission = "XcraftTickets.Reload",
		usage = "",
		desc = "bearbeiten aller mögl. Assignees")
public class ReloadCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		manager.getPlugin().configManager.load();

		this.reply(sender, "wurde erfolgreich neu geladen");
		manager.getPlugin().log.info(manager.getPlugin().getDescription().getName() + " reloaded!");
		return true;
	}
}
