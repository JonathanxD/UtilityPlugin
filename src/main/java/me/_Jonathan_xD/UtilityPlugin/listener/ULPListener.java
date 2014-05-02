package me._Jonathan_xD.UtilityPlugin.listener;

import me._Jonathan_xD.UtilityPlugin.ConfigDefaults;
import me._Jonathan_xD.UtilityPlugin.Main;
import me._Jonathan_xD.UtilityPlugin.FileMan.BadUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

public class ULPListener implements Listener {
	private Main ins = Main.getInstance();
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerChat(AsyncPlayerChatEvent e){
		switch(BadUtils.itIsBadWord(e.getPlayer(),
				e.getMessage(),
				ins.getConfig().getStringList(ConfigDefaults.BannedWords.getKey()),
				ins.getConfig().getBoolean(ConfigDefaults.DetectSpammers.getKey()),
				ins.getConfig().getInt(ConfigDefaults.CharRepeatMax.getKey())
				))
		{
		case BannedWord:{
			e.getPlayer().sendMessage(ins.getConfig().getString(ConfigDefaults.BannedWordMsg.getKey()).replace("<message>", e.getMessage()));
			e.setMessage("");
			e.setCancelled(true);
			e.setCancelled(true);
			break;
		}
		case Spam:{
			e.getPlayer().sendMessage(ins.getConfig().getString(ConfigDefaults.SpammerMessage.getKey()));
			e.setMessage("");
			e.setCancelled(true);
			e.setCancelled(true);
			break;			
		}
		case No:break;
		default: break;
		}
	}
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent pje){
		ins.getServer().getScheduler().scheduleSyncDelayedTask(ins, new Runnable() {                
            public void run() {                 
        		if(ins.getConfig().getString(ConfigDefaults.JoinMessage.getKey()).equalsIgnoreCase("disable"))return;
        		String message = ins.formString(ins.getConfig().getString(ConfigDefaults.JoinMessage.getKey()), pje.getPlayer(), pje.getPlayer().getWorld(), null, null, null);
        		pje.getPlayer().sendMessage(message);
            }
        }, 1L);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
	    String k[] = event.getMessage().split(" ");
		String command = k[0];
		String reason = null;
		if(ins.getConfig().getString(ConfigDefaults.BannedCmds.getKey()).contains(command) && !event.getPlayer().isOp() && !event.getPlayer().hasPermission("UtilityPlugin.usebancmd."+command)){
			event.getPlayer().sendMessage(ins.getConfig().getString(ConfigDefaults.BannedCmdMsg.getKey()).replace("<command>", command.toLowerCase()));
			event.setCancelled(true);
			return;
		}else{
			
		}
	    if (command.equalsIgnoreCase("/kick")) {
			if(ins.getConfig().getString(ConfigDefaults.ForKicked.getKey()).equalsIgnoreCase("disable"))return;
	    	if(k.length >= 2){
		    	for(int a=2; a < k.length; ++a)
		    	{
		    		if(reason == null)reason = k[a];
		    		else if(a != 1)reason += " " + k[a];
		    	}
				if(Bukkit.getPlayer(k[1]) == null)return;
				ins.kickHash.put(Bukkit.getPlayer(k[1]), event.getPlayer());
	    	}
	    }
	}	
	@EventHandler
	public void PlayerKick(PlayerKickEvent pke){
		if(ins.getConfig().getString(ConfigDefaults.ForKicked.getKey()).equalsIgnoreCase("disable"))return;
		Player kicker = ins.kickHash.get(pke.getPlayer());
		ins.kickHash.remove(pke.getPlayer());
		pke.setReason(ins.formString(ins.getConfig().getString(ConfigDefaults.ForKicked.getKey()), pke.getPlayer(), pke.getPlayer().getWorld(), kicker, pke.getReason(), null));
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
       	String bl = event.getBlock().getType().toString();
		if(ins.ItIsBanned(event.getBlock().getType(), event.getBlock().getData(), player, player.getWorld().getName(), true)){
			event.setCancelled(true);
			player.sendMessage(ins.getConfig().getString(ConfigDefaults.BannedBlocksMsg.getKey()).replace("<material>", bl.toLowerCase().replace("_", " ")));			
		}
       	if(ins.getConfig().getString("debugblocks").equals("true")){
       		player.sendMessage("placed block (Block type to string): "+event.getBlock().getType().toString());
       	}       	
	}
	
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onBlockBreak(BlockBreakEvent event){
    	Player player = event.getPlayer();
       	String bl = event.getBlock().getType().toString();
		if(ins.ItIsBanned(event.getBlock().getType(), event.getBlock().getData(), event.getPlayer(), event.getPlayer().getWorld().getName(), true)){
			event.setCancelled(true);
			player.sendMessage(ins.getConfig().getString(ConfigDefaults.BannedBlocksMsg.getKey()).replace("<material>", bl.toLowerCase().replace("_", " ")));
		}
    }
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {    	
   		Player player = event.getPlayer();    	
    	ItemStack zstack = player.getItemInHand();
    	if(event.getClickedBlock() != null){
    		if(ins.getConfig().get(ConfigDefaults.RemoveBannedOnClick.getKey()).equals("true")){
    			if(event.getClickedBlock().getType() != Material.AIR){
    				if(ins.ItIsBanned(event.getClickedBlock().getType(), event.getClickedBlock().getData(), player, player.getWorld().getName(), true)){
    					if(Boolean.parseBoolean(ins.getConfig().get(ConfigDefaults.RemoveIfCancelled.getKey()).toString()) || !event.isCancelled()){
    		        		String zbl = event.getClickedBlock().getType().toString();
    	   	   				player.sendMessage(ins.getConfig().getString(ConfigDefaults.BannedBlocksMsg.getKey()).replace("<material>", zbl.toLowerCase().replace("_", " ")));
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
   	   			if(ins.ItIsBanned(zitem, zstack.getData().getData(), player, player.getWorld().getName(), true)){
   	   				event.setCancelled(true);
   	   				player.sendMessage(ins.getConfig().getString(ConfigDefaults.BannedBlocksMsg.getKey()).replace("<material>", zbl.toLowerCase().replace("_", " ")));
   	   			}  				
   			}
   		}
   		if(event.getItem() != null){
        	ItemStack stack = event.getItem();
       		Material item = stack.getType();
        	String bl = event.getItem().getType().toString();
    		if(ins.ItIsBanned(item, stack.getData().getData(), player, player.getWorld().getName(), true)){
    			event.setCancelled(true);
    			player.sendMessage(ins.getConfig().getString(ConfigDefaults.BannedBlocksMsg.getKey()).replace("<material>", bl.toLowerCase().replace("_", " ")));    		
    		}
        }
    	if(event.getClickedBlock() != null){
        	Material block = event.getClickedBlock().getType();
           	String bl = event.getClickedBlock().getType().toString();
    		if(ins.ItIsBanned(block, event.getClickedBlock().getData(), player, player.getWorld().getName(), true)){
    			event.setCancelled(true);
    			player.sendMessage(ins.getConfig().getString(ConfigDefaults.BannedBlocksMsg.getKey()).replace("<material>", bl.toLowerCase().replace("_", " ")));
    		}       	
        }
    }
    @SuppressWarnings("deprecation")
	@EventHandler
    public void DamageEvent(final EntityDamageEvent event) {
    	if(event.getCause() == DamageCause.SUFFOCATION && event instanceof EntityDamageByBlockEvent && event.getEntity() instanceof Player){
    		Player p = (Player) event.getEntity();
    		Block damager = ((EntityDamageByBlockEvent) event).getDamager();
    		if(ins.ItIsBanned(damager.getType(), damager.getData(), p, event.getEntity().getWorld().getName(), true)){
				event.setDamage(0);
    			event.setCancelled(true);
    		}
    	}
    	if (event instanceof EntityDamageByEntityEvent) {
    		Entity entity = (Entity)((EntityDamageByEntityEvent) event).getDamager();
    		if(entity instanceof Player){
    			Player player = (Player) entity;
    			if(ins.ItIsBanned(player.getItemInHand().getType(), player.getItemInHand().getData().getData(), player, player.getWorld().getName(), true)){
    				event.setDamage(0);
    				event.setCancelled(true);
    			}
    		}
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
        		if(ins.ItIsBanned(item, stack.getData().getData(), player, player.getWorld().getName(), true)){
   	    			event.setCancelled(true);
   	    			player.sendMessage(ins.getConfig().getString(ConfigDefaults.BannedBlocksMsg.getKey()).replace("<material>", bl.toLowerCase().replace("_", " ")));   					
    			}
        	}	   		
    	}
	}

}
