package me.INemesisI.XcraftTickets.Commands.Admin;

import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.command.CommandSender;

@CommandInfo(name = "reload",
		command = "ticket",
		pattern = "rel.*",
		permission = "XcraftTickets.Reload",
		usage = "",
		desc = "Lädt alle Daten neu")
public class ReloadCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		manager.getPlugin().configManager.load();

		this.reply(sender, "wurde erfolgreich neu geladen");
		manager.getPlugin().log.info(manager.getPlugin().getDescription().getName() + " reloaded!");
		return true;
	}
}
