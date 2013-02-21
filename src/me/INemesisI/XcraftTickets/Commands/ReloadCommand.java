package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends PluginCommand {

	protected ReloadCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		plugin.configManager.load();

		this.reply("wurde erfolgreich neu geladen");
		plugin.log.info(plugin.getDescription().getName() + " reloaded!");
	}
}
