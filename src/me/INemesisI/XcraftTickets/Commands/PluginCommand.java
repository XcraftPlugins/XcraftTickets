package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.command.CommandSender;

public class PluginCommand extends CommandHelper {

	protected PluginCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (Command.equals("reload")) {
			// TODO: do some stuff
			reply("Not implemented yet");
		}

		if (Command.equals("save")) {
			// TODO: do some stuff
			reply("Not implemented yet");
		}
	}

}
