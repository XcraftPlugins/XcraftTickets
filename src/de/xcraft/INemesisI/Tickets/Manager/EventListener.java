package de.xcraft.INemesisI.Tickets.Manager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import de.xcraft.INemesisI.Library.XcraftEventListener;
import de.xcraft.INemesisI.Tickets.XcraftTickets;

public class EventListener extends XcraftEventListener {
	private TicketManager tManager;

	public EventListener(XcraftTickets plugin) {
		super(plugin);
		tManager = plugin.getPluginManager();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		tManager.onJoinInform(event.getPlayer());
	}

}
