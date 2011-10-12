package me.INemesisI.XcraftTickets;

import java.util.logging.Logger;


import me.INemesisI.XcraftTickets.Commands.*;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class XcraftTickets extends JavaPlugin {

	public XcraftTicketsData data = new XcraftTicketsData(this);
    public Logger log = Logger.getLogger("Minecraft");//Define your logger
    
    @Override
	public void onDisable() {
    	data.save();
    	log.info(getName()+"disabled!");
    }
 
    @Override
	public void onEnable() {
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
	     commandHandler.registerExecutor("appl", new ApplicationCommand(this), "XcraftTickets.Mod"); 
	     commandHandler.registerExecutor("accept", new ApplicationCommand(this), "XcraftTickets.Mod"); 
   }
        
    
    public boolean hasPermission(Player player, String node) {
		      return player.hasPermission(node);
	}

    public String getName() {
      	return "["+this.getDescription().getName()+"] ";
   	}
}