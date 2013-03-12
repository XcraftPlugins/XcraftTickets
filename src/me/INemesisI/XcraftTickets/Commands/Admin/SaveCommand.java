package me.INemesisI.XcraftTickets.Commands.Admin;

import me.INemesisI.XcraftTickets.Commands.Command;
import me.INemesisI.XcraftTickets.Commands.CommandInfo;
import me.INemesisI.XcraftTickets.Manager.TicketManager;

import org.bukkit.command.CommandSender;

@CommandInfo(name = "save",
		command = "ticket|t",
		pattern = "sav.*",
		permission = "XcraftTickets.Save",
		usage = "/ticket save",
		desc = "Speichert alle nötigen Daten")
public class SaveCommand extends Command {

	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {

		manager.getPlugin().configManager.save();

		this.reply(sender, "Wurde erfolgreich gespeichert!");
		manager.getPlugin().log.info(manager.getPlugin().getDescription().getName() + "saved!");
		return true;
	}
}
