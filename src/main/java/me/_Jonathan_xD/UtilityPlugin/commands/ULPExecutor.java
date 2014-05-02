package me._Jonathan_xD.UtilityPlugin.commands;

import java.util.List;

import me._Jonathan_xD.UtilityPlugin.ConfigDefaults;
import me._Jonathan_xD.UtilityPlugin.Main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ULPExecutor implements CommandExecutor {
	private Main ins = Main.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("ulp")){
				if(args.length < 1 || args[0] == null || args[0].isEmpty() || args[0].equalsIgnoreCase("help")){
					if(player.hasPermission("UtilityPlugin.cmd.use.ulp.help")){
						player.sendMessage("[UtilityPlugin] [] = Required. () = Optional");
						player.sendMessage("[UtilityPlugin] /ulp reload - Reload plugin");
						player.sendMessage("[UtilityPlugin] /ulp ban [Material(:Data)] [World]. (In World use ALL to ban in all wolrds) Ex: /ulp ban 0 all");
						player.sendMessage("[UtilityPlugin] /ulp unban  [Material(:Data)] [World]. (In World use ALL to unban in all wolrds) Ex: /ulp unban 0 all");
						player.sendMessage("[UtilityPlugin] /ulp bancmd /[Command]");
						player.sendMessage("[UtilityPlugin] /ulp unbancmd /[Command]");
						player.sendMessage("[UtilityPlugin] /ulp banword [Word]");
						player.sendMessage("[UtilityPlugin] /ulp unbanword [Word]");												
						player.sendMessage("[UtilityPlugin] /ulp removebanneds [true/false]");
						player.sendMessage("[UtilityPlugin] /ulp removeifcancelled [true/false]");						
						player.sendMessage("[UtilityPlugin] /ulp debugblockplace [true/false]");
						player.sendMessage("[UtilityPlugin] /ulp showbannedwords [true/false]");
					}
					return true;
				}
				if(!player.hasPermission("UtilityPlugin.cmd.use.ulp."+args[0])){
					player.sendMessage("You don't has permission to use: /ulp "+args[0]);
					return false;
				}else{
					try{
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("reload"))
						{
							ins.reloadConfig();
							player.sendMessage(ChatColor.GOLD+"[UtilityPlugin] "+ChatColor.RED+"UtilityPlugin reloaded!");						
						}
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("showbannedwords"))
						{
							StringBuilder sb=new StringBuilder();
							List<String> list = ins.getConfig().getStringList(ConfigDefaults.BannedWords.getKey());
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
											Material m = ins.TransformMaterial((Object)s[0]);
											if(m != null){
												String w = "all";
												if(args.length >= 3){
													if(args[2] != null && !args[2].isEmpty())w = args[2];
												}
												if(ins.SetBanned(m, Integer.parseInt(s[1]), w, player)){
													player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Material "+m.toString().replace("_"," ")+":"+s[1]+" Banned successfully.");												
												}else player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Failed to ban: "+m.toString().replace("_"," ")+":"+s[1]+", It's already banned!");												
	
											}else{
												args[-1] = null;
											}
										}else{
											args[-1] = null;
										}								
									}else args[-1] = null;
								}else args[-1] = null;
							}else args[-1] = null;
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
											Material m = ins.TransformMaterial((Object)s[0]); 
											if(m != null){
												String w = "all";
												if(args.length >= 3){
													if(args[2] != null && !args[2].isEmpty())w = args[2];
												}
												if(ins.SetUnBanned(m, Integer.parseInt(s[1]), w, player))player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Material "+m.toString().replace("_"," ")+" Unbanned successfully.");
												else player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Failed to unban: "+m.toString().replace("_"," ")+", It's banned?.");
											}else{
												args[-1] = null;
											}
										}else{
											args[-1] = null;
										}								
									}else args[-1] = null;
								}else args[-1] = null;
							}else args[-1] = null;
						}
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("removebanneds")){
							ins.ActiveRemove(Boolean.parseBoolean(args[1]));
							player.sendMessage("\""+args[0]+"\" has been seted to:"+args[1]);
						}
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("removeifcancelled")){
							ins.ActiveCanc(Boolean.parseBoolean(args[1]));
							player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"\""+args[0]+"\" has been seted to:"+args[1]);
						}
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("debugblockplace")){
							ins.ActiveDebug(Boolean.parseBoolean(args[1]));
							player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"\""+args[0]+"\" has been seted to:"+args[1]);
						}
	
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("banword")){
							ins.SetBannedWord(args[1]);
							player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Word: "+args[1]+" Banned successfully.");
						}
	
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("unbanword")){
							ins.SetUnBannedWord(args[1]);
							player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Word: "+args[1]+" Banned successfully..");
						}
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("bancmd")){
							ins.SetBannedCmd(args[1]);
							player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Cmd: "+args[1]+" Banned successfully.");
						}
	
						if(!args[0].isEmpty() && args[0].equalsIgnoreCase("unbancmd")){
							ins.SetUnBannedCmd(args[1]);
							player.sendMessage(ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Cmd: "+args[1]+" Banned successfully..");
						}
					}catch(Exception e){
						player.sendMessage(ins.getConfig().getString(ConfigDefaults.ErrorMsg.getKey()));						
					}
				}
				return true;
			}
		}		
		return false;
	}

}
