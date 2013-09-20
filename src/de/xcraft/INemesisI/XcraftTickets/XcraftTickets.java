package de.xcraft.INemesisI.XcraftTickets;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;

import de.xcraft.INemesisI.Library.XcraftPlugin;
import de.xcraft.INemesisI.Library.Message.Messenger;
import de.xcraft.INemesisI.XcraftTickets.Manager.CommandManager;
import de.xcraft.INemesisI.XcraftTickets.Manager.ConfigManager;
import de.xcraft.INemesisI.XcraftTickets.Manager.EventListener;
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

public class XcraftTickets extends XcraftPlugin {

	private TicketManager ticketManager = null;
	private ConfigManager configManager = null;
	private CommandManager commandManager = null;
	private EventListener eventListener = null;
	private Messenger messenger = null;
	private Permission permission;

	@Override
	protected void setup() {
		Msg.init(this);
		this.messenger = Messenger.getInstance(this);
		this.ticketManager = new TicketManager(this);
		this.configManager = new ConfigManager(this);
		this.eventListener = new EventListener(this);
		this.commandManager = new CommandManager(this);
		this.setupPermissions();
		configManager.load();
	}

	@Override
	public TicketManager getPluginManager() {
		return ticketManager;
	}

	@Override
	public ConfigManager getConfigManager() {
		return configManager;
	}

	@Override
	public CommandManager getCommandManager() {
		return commandManager;
	}

	@Override
	public EventListener getEventListener() {
		return eventListener;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}

	public Permission getPermission() {
		return permission;
	}

	private Boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = this.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return permission != null;
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
				getPluginManager().informPlayers(XcraftTickets.this.getServer());
			}
		};
		this.getServer().getScheduler().runTaskTimerAsynchronously(this, task, ((min * 60) + sec) * 20, 60 * delay * 20);

	}
}