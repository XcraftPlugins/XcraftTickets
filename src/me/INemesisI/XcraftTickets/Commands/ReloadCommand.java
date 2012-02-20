package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends CommandHelper {

	protected ReloadCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		plugin.configHandler.load();

		reply("wurde erfolgreich neu geladen");
		plugin.log.info(plugin.getDescription().getName() + "reloaded!");
	}
}
