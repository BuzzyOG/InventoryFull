package me.clip.inventoryfull.hooks;

import org.bukkit.entity.Player;

import me.clip.actionannouncer.ActionAPI;

public class ActionMsg {
		
	public ActionMsg() {
	}
	
	public void send(Player p, String msg, String block) {
		ActionAPI.sendPlayerAnnouncement(p, msg.replace("%player%", p.getName()).replace("%block%", block));
		
	}
	
}
