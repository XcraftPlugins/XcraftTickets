package me.INemesisI.XcraftTickets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import me.INemesisI.XcraftTickets.Commands.CommandHandler;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class XcraftTickets extends JavaPlugin {

	private final EventListener eventlistener = new EventListener(this);
	public ConfigHandler configHandler;
	public TicketHandler ticketHandler;

	private Permission permission = null;

	public Logger log = Logger.getLogger("Minecraft");
	private boolean logging;

	@Override
	public void onDisable() {
		configHandler.save();
		log.info("[" + this.getDescription().getName() + "] v" + this.getDescription().getVersion() + " by INemesisI disabled!");
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(eventlistener, this);
		registerCommands();
		setupPermissions();
		setupHandler();
		configHandler.load();
		startScheduler();
		log.info("[" + this.getDescription().getName() + "] v" + this.getDescription().getVersion() + " by INemesisI enabled!");

	} // End onEnable

	private void registerCommands() {
		CommandHandler commandHandler = new CommandHandler(this);
		getCommand("ticket").setExecutor(commandHandler);
		getCommand("t").setExecutor(commandHandler);
		getCommand("pe").setExecutor(commandHandler);
	}

	private Boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(
				net.milkbowl.vault.permission.Permission.class);
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

	public void startScheduler() {
		SimpleDateFormat d = new SimpleDateFormat();
		d.applyPattern("mm:ss");
		String current = d.format(new Date());
		String[] split = current.split(":");
		int min = Integer.parseInt(split[0]);
		int sec = Integer.parseInt(split[1]);
		int delay = 10; //mins
		min = delay - (min % delay)-1;
		if (min == -1 ) min = delay--;
		sec = 60 - sec;
		Runnable task = new Runnable() {
			@Override
			public void run() {
				for (Player player : getServer().getOnlinePlayers()) {
					ticketHandler.inform(player);
					configHandler.save();
				}
			}
		};
		getServer().getScheduler().scheduleSyncRepeatingTask(this, task, (min * 60 + sec) * 20, (60 * delay) * 20);

	}

	public void Log(String message) {
		if (isLogging()) log.info(this.getDescription().getFullName() + message);
	}

	public String getCName() {
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