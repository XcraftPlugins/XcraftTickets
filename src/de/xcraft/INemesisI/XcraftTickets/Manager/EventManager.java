package de.xcraft.INemesisI.XcraftTickets.Manager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import de.xcraft.INemesisI.Library.XcraftEventListener;
import de.xcraft.INemesisI.Library.XcraftPlugin;

public class EventManager extends XcraftEventListener {
	TicketManager tManager;
	
	public EventManager(XcraftPlugin plugin) {
		super(plugin);
		tManager = (TicketManager) plugin.pluginManager;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		tManager.onJoinInform(event.getPlayer());
	}

}
