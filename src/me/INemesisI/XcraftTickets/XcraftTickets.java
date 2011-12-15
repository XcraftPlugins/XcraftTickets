package me.INemesisI.XcraftTickets;

import java.util.logging.Logger;

import me.INemesisI.XcraftTickets.Commands.CommandHandler;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class XcraftTickets extends JavaPlugin {

	public ConfigHandler configHandler;
	public TicketHandler ticketHandler;
	
	private Permission permission = null;
	
    public Logger log = Logger.getLogger("Minecraft");
    private boolean logging;
    
    @Override
	public void onDisable() {
    	configHandler.save();
    	log.info(this.getDescription().getName() + "disabled!");
    }
 
    @Override
	public void onEnable() {
    registerCommands();
    setupPermissions();
    setupHandler();
    configHandler.load();
    
    log.info(this.getDescription().getName() + "enabled!");
	
    } // End onEnable
    
    private void registerCommands(){
    	CommandHandler commandHandler = new CommandHandler(this);
	    getCommand("ticket").setExecutor(commandHandler);
	    getCommand("t").setExecutor(commandHandler);
	    getCommand("pe").setExecutor(commandHandler);
    }
    
	private Boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
	
	private void setupHandler() {
		this.configHandler = new ConfigHandler(this);
		this.ticketHandler = new TicketHandler(this);
	}

	public Permission getPermission() {
		return permission;
	}
	
	public void Log(String message) {
		if (isLogging())
			log.info(this.getDescription().getFullName() + message);
	}
    
    public String getName() {
		return ChatColor.DARK_GRAY + "[" + this.getDescription().getName() + "] " + getChatColor();
	}

	public ChatColor getChatColor() {
		return ChatColor.DARK_AQUA;
	}

	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}
}