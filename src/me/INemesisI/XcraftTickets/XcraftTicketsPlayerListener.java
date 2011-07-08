package me.INemesisI.XcraftTickets;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;



public class XcraftTicketsPlayerListener extends PlayerListener{
	
 
        //You HAVE to have this!
        public static XcraftTickets plugin;
       
        public XcraftTicketsPlayerListener(XcraftTickets instance) {
                plugin = instance;
        }
        
        public void onPlayerJoin(PlayerJoinEvent event) {
        	if(plugin.hasPermission(event.getPlayer(), "XcraftTickets.mod"))
        		plugin.data.addToMods(event.getPlayer());
        }
        public void onPlayerQuit(PlayerQuitEvent event) {
        	if(plugin.hasPermission(event.getPlayer(), "XcraftTickets.mod"))
        		plugin.data.removeFromMods(event.getPlayer());
        }

}
