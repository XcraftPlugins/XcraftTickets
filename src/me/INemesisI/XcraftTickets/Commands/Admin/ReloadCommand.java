package me.INemesisI.XcraftTickets.Commands.Admin;

import me.INemesisI.XcraftTickets.XcraftTickets;
import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.command.CommandSender;

@CommandInfo(name = "reload",
		command = "ticket|t",
		pattern = "rel.*",
		permission = "XcraftTickets.Reload",
		usage = "/ticket reload",
		desc = "Lädt alle Tickets aus den zuletzt gespeicherten Daten")
public class ReloadCommand extends Command {

	protected ReloadCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {
		plugin.configManager.load();

		this.reply(sender, "wurde erfolgreich neu geladen");
		plugin.log.info(plugin.getDescription().getName() + " reloaded!");
		return true;
	}
}
