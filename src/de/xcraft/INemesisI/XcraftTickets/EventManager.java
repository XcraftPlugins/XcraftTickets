package de.xcraft.INemesisI.XcraftTickets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import de.xcraft.INemesisI.Utils.XcraftEventListener;
import de.xcraft.INemesisI.Utils.XcraftPlugin;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

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
