package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends PluginCommand {

	protected SetWarpCommand(XcraftTickets instance, String permnode) {
		super(instance, permnode);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if ((list.size() < 1) || !list.get(0).matches("\\d*")) {
			this.error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = this.getTM().getTicket(id);
		if (ticket == null) {
			this.error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		if (!this.getName().equals(ticket.getOwner())) {
			this.error("Du kannst den Warp nur für dein eigenes Ticket ändern!");
		}
		if (!(sender instanceof Player)) {
			this.error("Das ist über die Konsole nicht möglich!");
		} else {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ticket.setLoc(loc);
			ticket.getLog().add(new Log(this.getTM().getCurrentDate(), player.getName(), Log.Type.COMMENT, "Der Warppunkt wurde aktualisiert"));
			this.reply("Der Warp für Ticket #" + ticket.getId() + " wurde an deine derzeitige Position verlegt!");
		}
	}

}
