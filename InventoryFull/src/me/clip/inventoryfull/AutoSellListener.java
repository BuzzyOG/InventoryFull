package me.clip.inventoryfull;

import java.util.List;

import me.clip.autosell.events.DropsToInventoryEvent;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AutoSellListener implements Listener {
	
	InventoryFull plugin;
	
	public AutoSellListener(InventoryFull i) {
		plugin = i;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockToInv(DropsToInventoryEvent e) {
		
		if (e.isCancelled()) {
			return;
		}

		Player p = e.getPlayer();

		if (!p.getGameMode().equals(GameMode.SURVIVAL)) {
			return;
		}

		if (!p.hasPermission("inventoryfull.alert")) {
			return;
		}
		
		List<ItemStack> drops = e.getDrops();
		
		if (drops == null || drops.isEmpty()) {
			return;
		}
		
		PlayerInventory i = p.getInventory();

		if (!InventoryFull.tools.contains(i.getItemInHand().getType())) {
			return;
		}	

		String wontFit = "block";
		
		ItemStack wont = null;

		for (ItemStack drop : drops) {

			for (ItemStack is : i.getContents()) {
				
				if (is == null) {
					//empty slot
					return;
				}
				
				if (is.getType().equals(drop.getType()) && is.getAmount()+drop.getAmount() <= is.getMaxStackSize()) {
					//will stack on existing itemstack
					return;
				}
			}

			wontFit = drop.getType().name();
			wont = drop;
			break;
		}
		
		if (wont == null) {
			return;
		}

		if (InventoryFull.active.containsKey(p.getName())) {
			
			if (InventoryFull.active.get(p.getName()) >= InventoryFull.options.getMaxAlerts()) {
				
				return;
			} else {
				
				InventoryFull.active.put(p.getName(), InventoryFull.active.get(p.getName())+1);
			}
			
		} else {
			
			InventoryFull.active.put(p.getName(), 1);
		}

		plugin.delayDecrease(p.getName());
		
		InventoryFullEvent event = new InventoryFullEvent(p, wont);
		Bukkit.getPluginManager().callEvent(event);
		
		if (InventoryFull.options.useChatMsg()) {
			for (String line : InventoryFull.options.getChatMsg()) {
				plugin.sms(p, line.replace("%player%", p.getName()).replace("%block%", wontFit));
			}
		}

		if (plugin.hookHolo && InventoryFull.options.useHolo()) {
			if (plugin.holo != null) {
				plugin.holo.send(p, InventoryFull.options.getHoloMsg(), wontFit);
			}
		}

		if (plugin.hookActionAnnouncer && InventoryFull.options.useActionAnnouncer()) {
			if (plugin.aa != null) {
				plugin.aa.send(p, InventoryFull.options.getActionMsg(), wontFit);
			}
		}

		if (plugin.hookTitleManager && InventoryFull.options.useTitleManager()) {
			if (plugin.tm != null) {
				plugin.tm.sendTitle(p, InventoryFull.options.getTitleMsg(), InventoryFull.options.getSubTitleMsg(), wontFit, InventoryFull.options.getFadeIn(), InventoryFull.options.getDuration(), InventoryFull.options.getFadeOut());
			}
		}

		if (plugin.hookTitleManager && InventoryFull.options.useTitleABar()) {
			if (plugin.tm != null) {
				plugin.tm.sendActionbar(p, InventoryFull.options.getTitleABarMsg(), wontFit);
			}
		}
		
	}

}
