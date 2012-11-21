package me.INemesisI.XcraftTickets.Commands;

import java.util.List;

import me.INemesisI.XcraftTickets.Log;
import me.INemesisI.XcraftTickets.Ticket;
import me.INemesisI.XcraftTickets.XcraftTickets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends CommandHelper {

	protected SetWarpCommand(XcraftTickets instance) {
		super(instance);
	}

	@Override
	protected void execute(CommandSender sender, String Command, List<String> list) {
		this.init(sender);

		if (list.size() < 1 || !list.get(0).matches("\\d*")) {
			error("Du hast keine Ticketnummer angegeben" + "\n" + ChatColor.GRAY + "(/ticket " + Command + " <Nr>)");
			return;
		}
		int id = Integer.parseInt(list.get(0));
		Ticket ticket = th.getTicket(id);
		if (ticket == null) {
			error("Ein Ticket mit der Nummer " + ChatColor.GOLD + id + ChatColor.RED + " konnte nicht gefunden werden");
			return;
		}
		if (!getName().equals(ticket.owner)) error("Du kannst den Warp nur für dein eigenes Ticket ändern!");
		if (!(sender instanceof Player)) error("Das ist über die Konsole nicht möglich!");
		else {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ticket.loc = loc;
			ticket.log.add(new Log(th.getCurrentDate(), player.getName(), Log.Type.COMMENT, "Der Warppunkt wurde aktualisiert"));
			reply("Der Warp für Ticket #" + ticket.id + " wurde an deine derzeitige Position verlegt!");
		}
	}

}
