package de.xcraft.INemesisI.XcraftTickets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.xcraft.INemesisI.XcraftTickets.Manager.CommandManager;
import de.xcraft.INemesisI.XcraftTickets.Manager.ConfigManager;
import de.xcraft.INemesisI.XcraftTickets.Manager.TicketManager;

//@formatter:off
/***
 * @author INemesisI
 *     by _____   __                         _      ____
 *       /  _/ | / /__  ____ ___  ___  _____(_)____/  _/ 
 *       / //  |/ / _ \/ __ `__ \/ _ \/ ___/ / ___// /  
 *     _/ // /|  /  __/ / / / / /  __(__  ) (__  )/ /   
 *    /___/_/ |_/\___/_/ /_/ /_/\___/____/_/____/___/                                              
 */ 
//@formatter:on

public class XcraftTickets extends JavaPlugin {

	private final EventListener eventlistener = new EventListener(this);
	public ConfigManager configManager;
	public TicketManager ticketManager;
	public CommandManager commandManager;

	private Permission permission = null;

	public Logger log = Logger.getLogger("Minecraft");
	private boolean logging;

	@Override
	public void onDisable() {
		configManager.save();
	}

	@Override
	public void onEnable() {
		this.setupPermissions();
		this.setupManager();
		this.startScheduler();
		this.getServer().getPluginManager().registerEvents(eventlistener, this);
	} // End onEnable

	private Boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = this.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return permission != null;
	}

	private void setupManager() {
		configManager = new ConfigManager(this);
		ticketManager = new TicketManager(this);
		configManager.load();
		commandManager = new CommandManager(ticketManager);
		registerCommands();
	}

	private void registerCommands() {
		this.getCommand("ticket").setExecutor(commandManager);
		this.getCommand("t").setExecutor(commandManager);
		this.getCommand("tl").setExecutor(commandManager);
	}

	public Permission getPermission() {
		return permission;
	}

	public void startScheduler() {
		SimpleDateFormat d = new SimpleDateFormat();
		d.applyPattern("mm:ss");
		String current = d.format(new Date());
		String[] split = current.split(":");
		int min = Integer.parseInt(split[0]);
		int sec = Integer.parseInt(split[1]);
		int delay = 5; // mins
		min = delay - (min % delay) - 1;
		if (min == -1) {
			min = delay--;
		}
		sec = 60 - sec;
		Runnable task = new Runnable() {
			@Override
			public void run() {
				ticketManager.informPlayers(XcraftTickets.this.getServer());
				configManager.save();
			}
		};
		this.getServer().getScheduler().runTaskTimerAsynchronously(this, task, ((min * 60) + sec) * 20, 60 * delay * 20);

	}

	public void Log(String message) {
		if (this.isLogging()) {
			log.info(this.getDescription().getFullName() + message);
		}
	}

	public String getCName() {
		return ChatColor.DARK_GRAY + "[" + this.getDescription().getName() + "] " + this.getChatColor();
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