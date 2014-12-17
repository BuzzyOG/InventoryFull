package me.clip.inventoryfull.hooks;

import java.util.List;

import me.clip.inventoryfull.InventoryFull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;

public class HoloMsg {	
	
	private InventoryFull plugin;
	
	public HoloMsg(InventoryFull i) {
		plugin = i;
	}
	
	public void send(Player p, List<String> msg, String wontFit) {

		Vector v = p.getLocation().getDirection().multiply(1);
		Location dLoc = p.getEyeLocation().add(v);

		Hologram full = HolographicDisplaysAPI.createIndividualHologram(plugin, dLoc, p, "");

		int pos = 0;
		
		for (String line : msg) {
			
			if (pos == 0) {
				full.setLine(0, ChatColor.translateAlternateColorCodes(
						'&', line.replace("%player%", p.getName()).replace("%block%", wontFit)));
			} else {
				full.addLine(ChatColor.translateAlternateColorCodes(
						'&', line.replace("%player%", p.getName()).replace("%block%", wontFit)));
			}
			pos = pos+1;
		}
		
		full.update();
		removeHologram(full);
	}
	
	private void removeHologram(final Hologram h) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {

				h.delete();
			}
		}, 20L * InventoryFull.options.getHoloTime());
	}

}
