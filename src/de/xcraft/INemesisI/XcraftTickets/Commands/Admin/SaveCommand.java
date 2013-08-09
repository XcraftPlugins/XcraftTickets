package de.xcraft.INemesisI.XcraftTickets.Commands.Admin;


import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.XcraftTickets.Commands.Command;
import de.xcraft.INemesisI.XcraftTickets.Commands.CommandInfo;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

@CommandInfo(name = "save",
		command = "ticket",
		pattern = "save",
		permission = "XcraftTickets.Save",
		usage = "",
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
