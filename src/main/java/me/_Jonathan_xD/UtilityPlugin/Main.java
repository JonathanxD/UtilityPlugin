package me._Jonathan_xD.UtilityPlugin;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me._Jonathan_xD.UtilityPlugin.FileMan.BadUtils;
import me._Jonathan_xD.UtilityPlugin.FileMan.FReader;
import me._Jonathan_xD.UtilityPlugin.FileMan.FWriter;
import me._Jonathan_xD.UtilityPlugin.commands.ULPExecutor;
import me._Jonathan_xD.UtilityPlugin.listener.ULPListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("unused")
public class Main extends JavaPlugin{
	public String[] sublist = {"%","!","@","&","-","*","¨", "$", "#","=",".",",","_"};
	private static Main instance; 
    public class Runner extends Thread  
    {  
        public void run()  
        {  
            try  
            {  
        	    if(getConfig().getBoolean("checkupdates")){
        	    	Update check = new Update(72492, null, "8.0*", "Release", "1.7.2");
        	    }                  
            }  
              
            catch ( Exception e )  
            {  
                System.out.println( "ERROR: " + e.toString() );
            }  
        }  
    }  	
    public HashMap<Player, Player> kickHash = new HashMap<Player, Player>();
    @Override
	public void onEnable(){
		instance = this;
		BadUtils.setupNLL();
		for(ConfigDefaults cd : ConfigDefaults.values()){
			getConfig().addDefault(cd.getKey(), cd.getDef());
		}
		getConfig().options().copyDefaults(true);
		saveConfig();
		BadUtils.ignoreP = getConfig().getInt(ConfigDefaults.Ignore.getKey());
		new Runner().start();
		Bukkit.getServer().getPluginManager().registerEvents(new ULPListener(), this);
	    PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println("[UtilityPlugin] Plugin Enabled");
		System.out.println("[UtilityPlugin] Converting Config Settings");
		System.out.println("[UtilityPlugin] Converted "+ConvertConfigFile()+" Lines");
		this.getCommand("ulp").setExecutor(new ULPExecutor());;
	}
	private int ConvertConfigFile() {
		ArrayList<String> myArray = new ArrayList<String>();
		ArrayList<String> pArray = new ArrayList<String>();
		File f = new File("UtilityPlugin.log");
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
		pArray = FReader.ReadFile("UtilityPlugin.log");
		String g;
		for(int ea = 0; ea < pArray.size(); ++ea){
			if(ea != 0){
				myArray.add(pArray.get(ea)+"\n");
			}else{
				myArray.add(pArray.get(ea));
			}
		}
		myArray.add("=========================================================================================================\n");
		List<String> sm;
		sm = getConfig().getStringList("bannedblocks");
		if(sm == null || sm.isEmpty())return 0;
		String ms;
		int ConvertedLines = 0;
		String date = Tempo();
		for(int ii = 0; ii < sm.size(); ++ ii){
			myArray.add(Tempo() + "Reading line ["+ii+"].\n");
			ms = sm.get(ii);
			if(ms.indexOf("{W:") == -1){				
				myArray.add(Tempo() + "Converting: "+ms+" -> {W:all}"+ms+"\n");
				myArray.add(Tempo()+"Converting 10%\n");
				ms = "{W:all}"+ms;
				myArray.add(Tempo()+"Converting 50%\n");
				sm.set(ii, ms);
				myArray.add(Tempo()+"Converting 100%\n");
				++ ConvertedLines;
				myArray.add(Tempo()+"Converted!!!\n");
			}
		}
		myArray.add(Tempo()+"Converted "+ConvertedLines+" Lines\n");
		myArray.add("=========================================================================================================\n");
		FWriter.Write("UtilityPlugin.log", myArray);	
    	this.getConfig().set(ConfigDefaults.BannedBlocks.getKey(), sm);
    	saveConfig();
		return ConvertedLines;
	}
	@Override
	public void onDisable(){
		System.out.println("[UtilityPlugin] Plugin Disabled");
		HandlerList.unregisterAll((Plugin)instance);
	}	
	
	@SuppressWarnings("deprecation")
	public Material TransformMaterial(Object c) {
		
		if(c instanceof String && isInteger((String)c)){
			Material thisM = Material.getMaterial(Integer.valueOf((String) c));
			return thisM;
		}else if(c instanceof String){
			return Material.getMaterial((String) c);									
		}
		return null;
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}	
	public String getBlockNameByMaterial(Material blockMaterial)
	{
		return (blockMaterial.toString().toLowerCase().replace("_"," "));		
	}
	public boolean SetBannedCmd(String cmd) {
    	//FWriter.Write("utili", toWrite);
		List<String> list = this.getConfig().getStringList("bannedcmds");
		if(list.contains(cmd))return false;
    	list.add(cmd);
    	this.getConfig().set("bannedcmds", list);
    	saveConfig();
    	return true;
		
	}
	public boolean SetUnBannedCmd(String cmd) {
		List<String> list = this.getConfig().getStringList("bannedcmds");	
    	String b;
		if(!list.contains(cmd))return false;    	
    	for(int x = 0; x < list.size(); ++x){
    		b = list.get(x);
    		if(b.equalsIgnoreCase(cmd)){
    			list.remove(b);
        		x = 0;
    		}
    	}
    	//list.remove(r);
    	this.getConfig().set("bannedcmds", list);
    	saveConfig();
    	return true;
		
	}
    public boolean SetBannedWord(String Word){
		List<String> list = this.getConfig().getStringList("bannedwords");    	    
		if(list.contains(Word))return false;    	
		list.add(Word);
    	this.getConfig().set("bannedwords", list);
    	saveConfig();
    	return true;
    }
    public boolean SetUnBannedWord(String Word){
		List<String> list = this.getConfig().getStringList("bannedwords");
    	String b;
		if(!list.contains(Word))return false;    	
    	for(int x = 0; x < list.size(); ++x){
    		b = list.get(x);
    		if(b.equalsIgnoreCase(Word)){
    			list.remove(b);
    			x=0;
    		}
    	}
    	//list.remove(r);
    	this.getConfig().set("bannedwords", list);
    	saveConfig();
    	return true;
    }
    public boolean ActiveDebug(boolean s){
    	this.getConfig().set("debugblocks", String.valueOf(s));
    	saveConfig();
    	return true;
    }
    public boolean ActiveRemove(boolean s){
    	this.getConfig().set("removebannedblockonclick", String.valueOf(s));    	
    	saveConfig();
    	return true;
    }
    public boolean ActiveCanc(boolean s){
    	this.getConfig().set("removebannedifcancelled", String.valueOf(s));    	
    	saveConfig();
    	return true;
    }
    public boolean ItIsBanned(Material i, int meta, Player p, String world, boolean perm){
        if(p == null)return true;
        if(perm && (p.isOp() || p.hasPermission("UtilityPlugin.usebanblock."+world+"."+i.name()+":"+meta) || p.hasPermission("UtilityPlugin.usebanblock.all."+i.name()+":"+meta) || p.hasPermission("UtilityPlugin.usebanblock."+world+"."+i.name()+":-1") || p.hasPermission("UtilityPlugin.usebanblock.all."+i.name()+":-1"))){
        	return false;
        }
    	if(i != null){
           	String bl = i.toString();
           	if(p != null && p.getWorld() != null && 
           			(getConfig().getString(ConfigDefaults.BannedBlocks.getKey()).contains("{W:"+world+"}"+bl+"@"+meta) 
           					|| getConfig().getString(ConfigDefaults.BannedBlocks.getKey()).contains("{W:all}"+bl+"@"+meta) 
           					|| getConfig().getString(ConfigDefaults.BannedBlocks.getKey()).contains("{W:"+world+"}"+bl+"@-1") 
           					|| getConfig().getString(ConfigDefaults.BannedBlocks.getKey()).contains("{W:all}"+bl+"@-1"))){
       			return true;
           	}else return false;       		
        }
        return false;
	}
    public boolean SetBanned(Material m, int meta, String world, Player p){
    	if(ItIsBanned(m, meta, p, world, false)){
    		return false;
    	}
		List<String> list = this.getConfig().getStringList(ConfigDefaults.BannedBlocks.getKey());
    	String r = m.toString();    	
    	if(world != null){
    		r = "{W:"+world+"}"+r;
    	}
    	if(meta == -1){
    		r = r+"@-1";
    	}else{
    		r = r+"@"+meta;
    	}
    	list.add(r);
    	this.getConfig().set(ConfigDefaults.BannedBlocks.getKey(), list);
    	saveConfig();
    	return true;
    }
    public boolean SetUnBanned(Material m, int meta, String world, Player p){
    	if(!ItIsBanned(m, meta, p, world, false)){
    		return false;
    	}
		List<String> list = this.getConfig().getStringList(ConfigDefaults.BannedBlocks.getKey());
    	String r = m.toString();
    	if(world != null){
    		r = "{W:"+world+"}" + r;
    	}    	
    	if(meta == -1){
    		r = r+"@-1";
    	}else{
    		r = r+"@"+meta;
    	}
    	String b;
    	for(int x = 0; x < list.size(); ++x){
    		b = list.get(x);
    		if(b.equalsIgnoreCase(r)){
    			list.remove(b);
    			x=0;
    		}
    	}
    	this.getConfig().set(ConfigDefaults.BannedBlocks.getKey(), list);
    	saveConfig();
    	return true;
    }
    public static String Tempo(){  
        return new SimpleDateFormat("[dd/MMM/yyyy HH:mm:ss]").format(new Date());  
    }
    public String formString(String s, Player p, World w, Player k, String kickReason, Player kill){
    	if(s != null){
    		s = s.replace("@[stripline]", "\n");
        	String tmp = null;
    		if(p != null){
    			s = s.replace("![player]", p.getName());
    			s = s.replace("![player:entityid]", String.valueOf(p.getEntityId()));
    			s = s.replace("![kicked]", p.getName());
    		}
    		if(w != null){
    			s = s.replace("![world]", w.getName().toString());    			
    		}
    		if(k != null){
    			s = s.replace("![kicker]", k.getName());    			
    			//s = s.replace("![world]", String.valueOf(w.getTime()));    			    			    			
    		}else{
    			s = s.replace("![kicker]", "Console");    			    			
    		}
    		if(kickReason != null){
    			s = s.replace("![kickreason]", kickReason);    			    			
    		}
    		if(s.indexOf("![") != -1){
    			for(int a = 0; (a = s.indexOf("![", a)) != -1;++a){
    				tmp = s.substring(s.indexOf("![", a),s.indexOf("]",a)+1);
    				if(!tmp.equalsIgnoreCase("@[stripline]") 
    						&& !tmp.equalsIgnoreCase("![player]")
    						&& !tmp.equalsIgnoreCase("![player:entityid]")
    						&& !tmp.equalsIgnoreCase("![world]")
    						&& !tmp.equalsIgnoreCase("![kicker]")
    						&& !tmp.equalsIgnoreCase("![kickreason]"))
    				{    					
        				System.out.println("[UtilityPlugin] Entry "+tmp+" cannot be resolved.");
    				}
    			}
    		}
    	}
    	return s;

    }
    public static Main getInstance(){
    	return instance;
    }

}
