package me._Jonathan_xD.UtilityPlugin;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public enum ConfigDefaults{
	Debug("debugblocks", "false"),
	RemoveBannedOnClick("removebannedblockonclick", "false"),
	RemoveIfCancelled("removebannedifcancelled", "false"),
	CheckUpdates("checkupdates", true),
	Ignore("bannedwordsignore", 2),
	JoinMessage("msg.joinmsg", "disable"),
	ForKicked("msg.forkickedmsg", "![kicker] Has kicked you, reason: ![kickreason]."),
	BannedBlocks("bannedblocks", new ArrayList<String>()),
	BannedWords("bannedwords", new ArrayList<String>()),
	BannedCmds("bannedcmds", new ArrayList<String>()),
	ErrorMsg("msg.error", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"Error, verify command syntax."),
	BannedBlocksMsg("msg.bannedblocks", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"<material> is banned."),
	BannedWordMsg("msg.bannedword", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"<message> will be not showed because contains a banned word."),
	BannedCmdMsg("msg.bannedcmd", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"<command> is banned."),
	SpammerMessage("msg.spammer", ChatColor.YELLOW+"[UtilityPlugin] "+ChatColor.RED+"PLEASE DON'T SPAM."),
	DetectSpammers("opt.detectspammers", true),
	CharRepeatMax("opt.maxcharrepeat", 3)
	;
	private final String key;
	private final Object def;
	private ConfigDefaults(String value, Object def) {
		this.key = value;
		this.def = def;
	}
	public String getKey() {
		return this.key;
	}
	public Object getDef() {
		return this.def;
	}
	@Override
	public String toString() {
		return this.key+"="+this.def;
	}
}
