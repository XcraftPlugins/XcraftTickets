package me.INemesisI.XcraftTickets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
	private XcraftTickets plugin;

	public EventListener(XcraftTickets instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.ticketManager.informPlayer(event.getPlayer());
	}

}
