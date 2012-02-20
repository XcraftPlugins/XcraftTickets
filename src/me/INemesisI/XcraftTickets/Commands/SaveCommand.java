package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.command.CommandSender;

public class SaveCommand extends CommandHelper {

	protected SaveCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		plugin.configHandler.save();

		reply("Wurde erfolgreich gespeichert!");
		plugin.log.info(plugin.getDescription().getName() + "saved!");
	}
}
