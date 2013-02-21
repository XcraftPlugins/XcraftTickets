package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.command.CommandSender;

public class SaveCommand extends PluginCommand {

	protected SaveCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		plugin.configManager.save();

		this.reply("Wurde erfolgreich gespeichert!");
		plugin.log.info(plugin.getDescription().getName() + "saved!");
	}
}
