package me.INemesisI.XcraftTickets.Commands.Admin;

import me.INemesisI.XcraftTickets.XcraftTickets;
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

	protected SaveCommand(XcraftTickets instance) {
		super(instance);
	}


	@Override
	public boolean execute(TicketManager manager, CommandSender sender, String[] args) {

		plugin.configManager.save();

		this.reply(sender, "Wurde erfolgreich gespeichert!");
		plugin.log.info(plugin.getDescription().getName() + "saved!");
		return true;
	}
}
