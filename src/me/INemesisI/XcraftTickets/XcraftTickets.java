package me.INemesisI.XcraftTickets;

import java.util.logging.Logger;


import me.INemesisI.XcraftTickets.Commands.*;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class XcraftTickets extends JavaPlugin {

	public XcraftTicketsData data = new XcraftTicketsData(this);
       
    public Logger log = Logger.getLogger("Minecraft");//Define your logger
    public PermissionHandler permissionHandler;
    public boolean assignSupport;
    
    @Override
	public void onDisable() {
    	data.save();
    	log.info(getName()+"disabled!");
    }
 
    @Override
	public void onEnable() {
    
    setupPermissions();
    registerCommands();
    data.load();
    
    log.info(getName()+"by INemesisI loaded!");
	
    } // End onEnable
    
    private void registerCommands(){
    	XcraftTicketsCommandHandler commandHandler = new XcraftTicketsCommandHandler(this);
	     getCommand("ticket").setExecutor(commandHandler);
	     getCommand("ti").setExecutor(commandHandler);
	     getCommand("tk").setExecutor(commandHandler);
	     getCommand("pe").setExecutor(commandHandler);
	     commandHandler.registerExecutor("open", new OpenCommand(this), ""); 
	     commandHandler.registerExecutor("reopen", new OpenCommand(this), ""); 
	     commandHandler.registerExecutor("close", new CloseCommand(this), ""); 
	     commandHandler.registerExecutor("comment", new CommentCommand(this), "");
	     commandHandler.registerExecutor("log", new CommentCommand(this), "");
	     commandHandler.registerExecutor("view", new ViewCommand(this), ""); 
	     commandHandler.registerExecutor("info", new ViewCommand(this), ""); 
	     commandHandler.registerExecutor("list", new ListCommand(this), ""); 
	     commandHandler.registerExecutor("listall", new ListCommand(this), "XcraftTickets.Mod"); 
	     commandHandler.registerExecutor("warp", new WarpCommand(this), "XcraftTickets.Mod"); 
	     commandHandler.registerExecutor("goto", new WarpCommand(this), "XcraftTickets.Mod");
	     commandHandler.registerExecutor("assign", new AssignCommand(this), "XcraftTickets.Mod"); 
	     commandHandler.registerExecutor("unassign", new AssignCommand(this), "XcraftTickets.Mod"); 
   }
        
	private void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		
		if (permissionHandler == null) {
			if (permissionsPlugin != null) {
				permissionHandler = ((Permissions) permissionsPlugin).getHandler();
				String name = permissionsPlugin.getDescription().getFullName();
				log.info(getName() +"Hooked into "+name+"!");
				if (name.contains("Permissions v3."))
					assignSupport = true;
				else
					assignSupport = false;
			}
			else {
				log.info(getName() +"Could not find permissions! Using OPs");
				assignSupport = false;
			}
		}
		
			
	}
    
    public boolean hasPermission(Player player, String node) {
		 if (permissionHandler != null)
		      return permissionHandler.has(player, node);
		 else 
			 if (player.isOp())
				 return true;
			 else return false;
	}

    public String getName() {
      	return "["+this.getDescription().getName()+"] ";
   	}
}