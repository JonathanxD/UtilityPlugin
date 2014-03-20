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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("unused")
public class Main extends JavaPlugin implements Listener{
	public String[] sublist = {"%","!","@","&","-","*","¨", "$", "#","=",".",",","_"};
	public HashMap<Player, Integer> warns = new HashMap<Player, Integer>();
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerChat(AsyncPlayerChatEvent e){
		if(BadUtils.itIsBadWord(e.getPlayer(), e.getMessage(), getConfig().getStringList("bannedwords"))){
			e.getPlayer().sendMessage(formString(getConfig().getString("bannedwordsoptions.bannedworddetectmsg"), e.getPlayer(), e.getPlayer().getWorld(), null, null, null));
			e.setMessage(getConfig().getString("bannedwordsoptions.setbadmsg"));
			e.setCancelled(true);
			e.setCancelled(true);
		}
	}
    public class Runner extends Thread  
    {  
        public void run()  
        {  
            try  
            {  
        	    if(getConfig().getBoolean("checkupdates")){
        	    	Update check = new Update(72492, null, "7.2.4", "Release", "1.6.4");
        	    }                  
            }  
              
            catch ( Exception e )  
            {  
                System.out.println( "ERROR: " + e.toString() );  
            }  
        }  
    }  	
    public HashMap<Player, Player> kickHash = new HashMap<Player, Player>();
	public void onEnable(){
		BadUtils.setupNLL();
		getConfig().addDefault("debugblocks", "false");
		getConfig().addDefault("removebannedblockonclick", "false");
		getConfig().addDefault("removebannedifcancelled", "false");		
		getConfig().addDefault("checkupdates", true);
		getConfig().addDefault("bannedwordsignore", 4);
		getConfig().addDefault("msg.joinmsg", "Hello ![player], and you is on world ![world], your entityid is ![player:entityid], thank you for playing!!@[stripline]you can modify this message in config.yml, for more informations see wiki: http://dev.bukkit.org/bukkit-plugins/utilityplugin/pages/wiki/.");
		getConfig().addDefault("msg.kickmsg", "![kicked] Has kicked by ![kicker] reason: ![kickreason].");
		getConfig().addDefault("msg.forkickedmsg", "![kicker] Has kicked you, reason: ![kickreason].");

		//getConfig().addDefault("msg.modifyworld", "You can't modify this world.");	
		
		getConfig().addDefault("bannedwordsoptions.bannedworddetectmsg", "![player] don't use banned word.");//bannedwords
		getConfig().addDefault("bannedwordsoptions.setbadmsg", "> Is a bad player.");//bannedwords
 //		getConfig().addDefault("bannedwordsoptions.warnplayer", true);//bannedwords
//		getConfig().addDefault("bannedwordsoptions.maxwarns", 10);//bannedwords
		
		getConfig().addDefault("bannedblocks", "");//bannedwords
		getConfig().addDefault("bannedwords", "");
		getConfig().addDefault("bannedcmds", "");
		getConfig().addDefault("msg.banmsghelp", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp ban [Material:Data] [World], ex: /ulp ban STONE:0 world, use: Data -1 for ignore Data.");
		getConfig().addDefault("msg.unbanmsghelp", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp unban [Material:Data] [World], ex: /ulp unban STONE:0 world, use: Data -1 for ignore Data.");		
		getConfig().addDefault("msg.banwordmsghelp", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp banword [Word], ex: /ulp ban Shit!.");
		getConfig().addDefault("msg.unbanwordmsghelp", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp unbanword [Word], ex: /ulp unban ops.");		
		getConfig().addDefault("msg.bancmdmsghelp", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp bancmd [CMD], ex: /ulp bancmd /kick!.");
		getConfig().addDefault("msg.unbancmdmsghelp", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp unbancmd [CMD], ex: /ulp unbancmd /op.");		
		getConfig().options().copyDefaults(true);
		saveConfig();
		BadUtils.ignoreP = getConfig().getInt("bannedwordsignore");
		new Runner().start();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	    PluginDescriptionFile pdfFile = this.getDescription();
	    //System.out.println("Major: "+check.majorVersion("2.5.9", "2.5.7"));
		System.out.println("[UtilityPlugin] Plugin Enabled");
		System.out.println("[UtilityPlugin] Converting Config Settings");
		System.out.println("[UtilityPlugin] Converted "+ConvertConfigFile()+" Lines");
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
    	this.getConfig().set("bannedblocks", sm);
    	saveConfig();
		return ConvertedLines;
		//this.set
		
		
	}
	public void onDisable(){
		System.out.println("[UtilityPlugin] Plugin Disabled");		
	}	
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent pje){
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {                
            public void run() {                 
        		if(getConfig().getString("msg.joinmsg").equalsIgnoreCase("disable"))return;
        		String message = formString(getConfig().getString("msg.joinmsg"), pje.getPlayer(), pje.getPlayer().getWorld(), null, null, null);
        		pje.getPlayer().sendMessage(message);
            }
        }, 20L);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
	    String k[] = event.getMessage().split(" ");
		String command = k[0];
		String reason = null;
		if(getConfig().getString("bannedcmds").contains(command) && !event.getPlayer().isOp() && !event.getPlayer().hasPermission("UtilityPlugin.usebancmd."+command)){
			event.getPlayer().sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+command+" is banned.");
			event.setCancelled(true);
			return;
		}else{
			
		}
	    if (command.equalsIgnoreCase("/kick")) {
			if(getConfig().getString("msg.kickmsg").equalsIgnoreCase("disable"))return;
	    	if(k.length >= 2){
		    	for(int a=2; a < k.length; ++a)
		    	{
		    		if(reason == null)reason = k[a];
		    		else if(a != 1)reason += " " + k[a];
		    	}
				if(Bukkit.getPlayer(k[1]) == null)return;
				Bukkit.broadcastMessage(formString(getConfig().getString("msg.kickmsg"), Bukkit.getPlayer(k[1]), Bukkit.getPlayer(k[1]).getWorld(), event.getPlayer(), reason, null));
				kickHash.put(Bukkit.getPlayer(k[1]), event.getPlayer());
	    	}
	    }
/*		if(BadUtils.itIsBadWord(event.getMessage().replaceFirst("/",""), getConfig().getStringList("bannedwords"))){
			event.getPlayer().sendMessage(formString(getConfig().getString("bannedwordsoptions.bannedworddetectmsg"), event.getPlayer(), event.getPlayer().getWorld(), null, null, null));
			event.setMessage(getConfig().getString("bannedwordsoptions.setbadmsg"));
			event.setCancelled(true);
		}*/	    
	}	
	@EventHandler
	public void PlayerKick(PlayerKickEvent pke){
		if(getConfig().getString("msg.forkickedmsg").equalsIgnoreCase("disable"))return;
		Player kicker = kickHash.get(pke.getPlayer());
		kickHash.remove(pke.getPlayer());
		pke.setReason(formString(getConfig().getString("msg.forkickedmsg"), pke.getPlayer(), pke.getPlayer().getWorld(), kicker, pke.getReason(), null));
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("ulp")){
				if(args.length < 1 || args[0] == null || args[0].isEmpty() || args[0].equalsIgnoreCase("help")){
					if(player.hasPermission("UtilityPlugin.cmd.use.ulp.help")){
						player.sendMessage("[UtilityPlugin] /ulp reload");						
						player.sendMessage("[UtilityPlugin] /ulp ban");						
						player.sendMessage("[UtilityPlugin] /ulp unban");						
						player.sendMessage("[UtilityPlugin] /ulp bancmd");						
						player.sendMessage("[UtilityPlugin] /ulp unbancmd");						
						player.sendMessage("[UtilityPlugin] /ulp banword");
						player.sendMessage("[UtilityPlugin] /ulp unbanword");												
						player.sendMessage("[UtilityPlugin] /ulp removebanneds");						
						player.sendMessage("[UtilityPlugin] /ulp removeifcancelled");						
						player.sendMessage("[UtilityPlugin] /ulp debugblockplace");//
						player.sendMessage("[UtilityPlugin] /ulp showbannedwords");//
					}
					return true;
				}
				if(!player.hasPermission("UtilityPlugin.cmd.use.ulp."+args[0])){
					player.sendMessage("You don't has permission to use: /ulp "+args[0]);
					return false;
				}else{
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("reload"))
					{
						//reset.ResetPlugin(this);
						this.reloadConfig();
						//Bukkit.getServer().getPluginManager().disablePlugin(this);
						//Bukkit.getServer().getPluginManager().enablePlugin(this);
						player.sendMessage(ChatColor.GOLD+"[UtilityPlugin] "+ChatColor.RED+"UtilityPlugin reloaded!");						
					}
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("showbannedwords"))
					{
						StringBuilder sb=new StringBuilder();
						List<String> list = getConfig().getStringList("bannedwords");
						String x;
						for(int g = 0; g < list.size(); ++ g){
							x = list.get(g);
							sb.append(x);
							if(g != list.size()-1)sb.append(", ");
							else sb.append(".");
						}
						player.sendMessage(ChatColor.GOLD+"[UtilityPlugin > BannedWords] "+ChatColor.RED+sb.toString());						
					}
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("ban")){
						if(args.length > 1){						
							if(!args[1].isEmpty()){
								String[] s = null;
								if(args[1].indexOf(":") == -1){
									String a = args[1] + ":-1";
									s = a.split(":");
								}else{
									s = args[1].split(":");
								}
								if(s.length >= 2 && s != null){
									if(!s[0].isEmpty() && !s[1].isEmpty()){
										Material m = TransformMaterial((Object)s[0]);
										if(m != null){
											String w = "all";
											if(args.length >= 3){
												if(args[2] != null && !args[2].isEmpty())w = args[2];
											}
											if(SetBanned(m, Integer.parseInt(s[1]), w, player)){
												player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Material "+m.toString().replace("_"," ")+":"+s[1]+" Banned successfully.");												
											}else player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Failed to ban: "+m.toString().replace("_"," ")+":"+s[1]+", It's already banned!");												

										}else{
											player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"This material don't exists.");
										}
									}else{
										player.sendMessage(getConfig().getString("msg.banmsghelp"));
									}								
								}else player.sendMessage(getConfig().getString("msg.banmsghelp"));
							}else player.sendMessage(getConfig().getString("msg.banmsghelp"));
						}else player.sendMessage(getConfig().getString("msg.banmsghelp"));
					}
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("unban")){
						if(args.length > 1){						
							if(!args[1].isEmpty()){
								String[] s = null;
								if(args[1].indexOf(":") == -1){
									String a = args[1] + ":-1";
									s = a.split(":");
								}else{
									s = args[1].split(":");
								}
								if(s.length == 2 && s != null){
									if(!s[0].isEmpty() && !s[1].isEmpty()){
										Material m = TransformMaterial((Object)s[0]); 
										if(m != null){
											String w = "all";
											if(args.length >= 3){
												if(args[2] != null && !args[2].isEmpty())w = args[2];
											}
											if(SetUnBanned(m, Integer.parseInt(s[1]), w, player))player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Material "+m.toString().replace("_"," ")+" Unbanned successfully.");
											else player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Failed to unban: "+m.toString().replace("_"," ")+", It's banned?.");
										}else{
											player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"This material don't exists.");
										}
									}else{
										player.sendMessage(getConfig().getString("msg.unbanmsghelp"));
									}								
								}else player.sendMessage(getConfig().getString("msg.unbanmsghelp"));
							}else player.sendMessage(getConfig().getString("msg.unbanmsghelp"));
						}else player.sendMessage(getConfig().getString("msg.unbanmsghelp"));
					}
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("removebanneds")){
						if(args.length > 1){						
							if(!args[1].isEmpty()){
								if(Boolean.parseBoolean(args[1]) == true || Boolean.parseBoolean(args[1]) == false){
									ActiveRemove(Boolean.parseBoolean(args[1]));
									player.sendMessage("\""+args[0]+"\" has been seted to:"+args[1]);
								}else{
									player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp removebanneds [bool], ex: /ulp removebanneds true.");
								}
							}else{
								player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp removebanneds [bool], ex: /ulp removebanneds true.");
							}
						}else player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp removebanneds [bool], ex: /ulp removebanneds true.");
					}
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("removeifcancelled")){
						if(args.length > 1){
							if(!args[1].isEmpty() && args[1] != null){
								if(Boolean.parseBoolean(args[1]) == true || Boolean.parseBoolean(args[1]) == false){
									ActiveCanc(Boolean.parseBoolean(args[1]));
									player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"\""+args[0]+"\" has been seted to:"+args[1]);
								}else{
									player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp removeifcancelled [bool], ex: /ulp removeifcancelled true.");
								}
							}else{
								player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp removeifcancelled [bool], ex: /ulp removeifcancelled true.");
							}
						}else player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp removeifcancelled [bool], ex: /ulp removeifcancelled true.");						
					}
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("debugblockplace")){
						if(args.length > 1){
							if(!args[1].isEmpty() && args[1] != null){
								if(Boolean.parseBoolean(args[1]) == true || Boolean.parseBoolean(args[1]) == false){
									ActiveDebug(Boolean.parseBoolean(args[1]));
									player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"\""+args[0]+"\" has been seted to:"+args[1]);
								}else{
									player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp debugblockplace [bool], ex: /ulp debugblockplace true.");
								}
							}else{
								player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp debugblockplace [bool], ex: /ulp debugblockplace true.");
							}
						}else player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Usage: /ulp debugblockplace [bool], ex: /ulp debugblockplace true.");;
					}

					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("banword")){
						if(args.length > 1){
							if(!args[1].isEmpty() && args[1] != null){
								SetBannedWord(args[1]);
								player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Word: "+args[1]+" Banned successfully.");
							}else{
								player.sendMessage(getConfig().getString("msg.banwordmsghelp"));
							}
						}else player.sendMessage(getConfig().getString("msg.banwordmsghelp"));
					}

					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("unbanword")){
						if(args.length > 1){
							if(!args[1].isEmpty() && args[1] != null && args[1].length() > 1){
								SetUnBannedWord(args[1]);
								player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Word: "+args[1]+" Banned successfully..");
							}else{
								player.sendMessage("msg.unbanwordmsghelp");
							}							
						}else{
							player.sendMessage(getConfig().getString("msg.unbanwordmsghelp"));
						}							

					}
//*************************************
					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("bancmd")){
						if(args.length > 1){
							if(!args[1].isEmpty() && args[1] != null){
								SetBannedCmd(args[1]);
								player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Cmd: "+args[1]+" Banned successfully.");
							}else{
								player.sendMessage(getConfig().getString("msg.bancmdmsghelp"));
							}
						}else player.sendMessage(getConfig().getString("msg.bancmdmsghelp"));
					}

					if(!args[0].isEmpty() && args[0].equalsIgnoreCase("unbancmd")){
						if(args.length > 1){
							if(!args[1].isEmpty() && args[1] != null && args[1].length() > 1){
								SetUnBannedCmd(args[1]);
								player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Cmd: "+args[1]+" Banned successfully..");
							}else{
								player.sendMessage("msg.unbancmdmsghelp");
							}							
						}else{
							player.sendMessage(getConfig().getString("msg.unbancmdmsghelp"));
						}							
					}					
				}
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private Material TransformMaterial(Object c) {
		
		if(c instanceof String && isInteger((String)c)){
			Material thisM = Material.getMaterial(Integer.valueOf((String) c));
			return thisM;
/*			for(Material x : Material.values()){
				//Material thisM = Material.values()[Integer.parseInt((String) c)];				
				if(thisM == x){
					return thisM;
				}
			}*/
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
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
       	String bl = event.getBlock().getType().toString();
		if(ItIsBanned(event.getBlock().getType(), event.getBlock().getData(), player, player.getWorld().getName(), true)){
			event.setCancelled(true);
			player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+bl.replace("_"," ")+" is banned.");			
		}
       	if(getConfig().getString("debugblocks").equals("true")){
       		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say placed block (Block to String): "+event.getBlock().toString());
       		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say placed block (Block type to string): "+event.getBlock().getType().toString());
       		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say placed block (Block Data): "+event.getBlock().getData());
       	}       	
	}
	
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onBlockBreak(BlockBreakEvent event){
    	Player player = event.getPlayer();
       	String bl = event.getBlock().getType().toString();
		if(ItIsBanned(event.getBlock().getType(), event.getBlock().getData(), event.getPlayer(), event.getPlayer().getWorld().getName(), true)){
			event.setCancelled(true);
			player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+bl.replace("_"," ")+" is banned.");
		}
    }
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {    	
   		Player player = event.getPlayer();    	
    	ItemStack zstack = player.getItemInHand();
    	if(event.getClickedBlock() != null){
    		if(getConfig().get("removebannedblockonclick").equals("true")){
    			if(event.getClickedBlock().getType() != Material.AIR){
    				if(ItIsBanned(event.getClickedBlock().getType(), event.getClickedBlock().getData(), player, player.getWorld().getName(), true)){
    					if(Boolean.parseBoolean(getConfig().get("removebannedifcancelled").toString()) || !event.isCancelled()){
    		        		String zbl = event.getClickedBlock().getType().toString();
    	   	   				player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+zbl.replace("_"," ")+" is banned and has been removed from the world.");
    						event.getClickedBlock().setType(Material.AIR);    						
    					}
    				}
    			}
    		}    		
    	}
   		if(zstack != null && !zstack.equals(Material.AIR)){
   			Material zitem = zstack.getType();
   			if(player.getItemInHand().getType().toString() != null){
   	   			String zbl = player.getItemInHand().getType().toString();
   	   			if(ItIsBanned(zitem, zstack.getData().getData(), player, player.getWorld().getName(), true)){
   	   				event.setCancelled(true);
   	   				player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+zbl.replace("_"," ")+" is banned.");
   	   			}  				
   			}
   		}
   		if(event.getItem() != null){
        	ItemStack stack = event.getItem();
       		Material item = stack.getType();
        	String bl = event.getItem().getType().toString();
    		if(ItIsBanned(item, stack.getData().getData(), player, player.getWorld().getName(), true)){
    			event.setCancelled(true);
    			player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+bl.replace("_"," ")+" is banned.");
    		}
        }
    	if(event.getClickedBlock() != null){
        	Material block = event.getClickedBlock().getType();
           	String bl = event.getClickedBlock().getType().toString();
    		if(ItIsBanned(block, event.getClickedBlock().getData(), player, player.getWorld().getName(), true)){
    			event.setCancelled(true);
    			player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+bl.replace("_"," ")+" is banned.");
    		}       	
        }
    }
    @SuppressWarnings("deprecation")
	@EventHandler
    public void DamageEvent(final EntityDamageEvent event) {
    	if(event.getCause() == DamageCause.SUFFOCATION && event instanceof EntityDamageByBlockEvent){
    		Block damager = ((EntityDamageByBlockEvent) event).getDamager();
    		if(ItIsBanned(damager.getType(), damager.getData(), null, event.getEntity().getWorld().getName(), true)){
				event.setDamage(0);
    			event.setCancelled(true);
    		}
    	}
    	if (event instanceof EntityDamageByEntityEvent) {
    		Entity entity = (Entity)((EntityDamageByEntityEvent) event).getDamager();
    		if(entity instanceof Player){
    			Player player = (Player) entity;
    			if(ItIsBanned(player.getItemInHand().getType(), player.getItemInHand().getData().getData(), player, player.getWorld().getName(), true)){
    				event.setDamage(0);
    				event.setCancelled(true);
    			}
    		}
    		// do stuff
    	}
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void PHE(PlayerItemHeldEvent event){
    	ItemStack stack = event.getPlayer().getInventory().getItem(event.getNewSlot());
    	if(stack != null){
    		Material item = stack.getType();
    		Player player = event.getPlayer();    	
        	if(item != null){
        		String bl = item.toString();
        		if(ItIsBanned(item, stack.getData().getData(), player, player.getWorld().getName(), true)){
   	    			event.setCancelled(true);
   					player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+bl.replace("_"," ")+" is banned.");   					
    			}
        	}	   		
    	}
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
    	//FWriter.Write("utili", toWrite);
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
           			(getConfig().getString("bannedblocks").contains("{W:"+world+"}"+bl+"@"+meta) 
           					|| getConfig().getString("bannedblocks").contains("{W:all}"+bl+"@"+meta) 
           					|| getConfig().getString("bannedblocks").contains("{W:"+world+"}"+bl+"@-1") 
           					|| getConfig().getString("bannedblocks").contains("{W:all}"+bl+"@-1"))){
       			return true;
           	}else return false;       		
        }
        return false;
	}
    public boolean SetBanned(Material m, int meta, String world, Player p){
    	//FWriter.Write("utili", toWrite);
    	if(ItIsBanned(m, meta, p, world, false)){
    		return false;
    	}
		List<String> list = this.getConfig().getStringList("bannedblocks");
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
    	this.getConfig().set("bannedblocks", list);
    	saveConfig();
    	return true;
    }
    public boolean SetUnBanned(Material m, int meta, String world, Player p){
    	if(!ItIsBanned(m, meta, p, world, false)){
    		return false;
    	}
		List<String> list = this.getConfig().getStringList("bannedblocks");
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
    	//list.remove(r);
    	this.getConfig().set("bannedblocks", list);
    	saveConfig();
    	return true;
    }
    public static String Tempo(){  
        return new SimpleDateFormat("[dd/MMM/yyyy HH:mm:ss]").format(new Date());  
    }
    public static String formString(String s, Player p, World w, Player k, String kickReason, Player kill){
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
        				System.out.println("[UtilityPlugin] JLangg > Entry "+tmp+" cannot be resolved.");
    				}
    			}
    		}
    	}
    	return s;

    }

}
