/*The MIT License (MIT)

Copyright (c) 2014 Ryan McCarthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/
package me.clip.inventoryfull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.clip.inventoryfull.hooks.ActionMsg;
import me.clip.inventoryfull.hooks.HoloMsg;
import me.clip.inventoryfull.hooks.TitleMsg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryFull extends JavaPlugin implements Listener {
	
	protected HoloMsg holo;
	protected ActionMsg aa;
	protected TitleMsg tm;

	protected static Map<String, Integer> active = new HashMap<String, Integer>();

	public static IFOptions options;

	protected boolean hookTitleManager;
	protected boolean hookActionAnnouncer;
	protected boolean hookHolo;
	
	private boolean hookAutoSell;

	@Override
	public void onEnable() {
		loadConfig();
		options = new IFOptions(this);
		initHooks();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	private void initHooks() {
		
		hookAutoSell = Bukkit.getPluginManager().isPluginEnabled("AutoSell");

		if (hookAutoSell) {
			Bukkit.getServer().getPluginManager().registerEvents(new AutoSellListener(this), this);
			getLogger().info("*** Hooked into AutoSell! ***");
		}

		hookActionAnnouncer = Bukkit.getPluginManager().isPluginEnabled("ActionAnnouncer");

		if (hookActionAnnouncer) {
			aa = new ActionMsg();
			getLogger().info("*** Hooked into ActionAnnouncer! ***");
		} else {
			getLogger().info("*** Could not hook into ActionAnnouncer! ***");
		}

		hookTitleManager = Bukkit.getPluginManager().isPluginEnabled("TitleManager");

		if (hookTitleManager) {
			tm = new TitleMsg();
			getLogger().info("*** Hooked into TitleManager! ***");
		} else {
			getLogger().info("*** Could not hook into TitleManager! ***");
		}

		hookHolo = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");

		if (hookHolo) {
			holo = new HoloMsg(this);
			getLogger().info("*** Hooked into HolographicDisplays! ***");
		} else {
			getLogger().info("*** Could not hook into HolographicDisplays! ***");
		}
	}

	private void loadConfig() {
		FileConfiguration c = getConfig();
		c.options().header(
				"InventoryFull version " + getDescription().getVersion()
						+ "\nCreated by: extended_clip"
						+ "\nValid placeholders:"
						+ "\n%block% - display the dropped item type"
						+ "\n%player% - display the players name");
		c.addDefault("cooldown_time", 5);
		c.addDefault("max_alerts_until_cooldown", 5);
		c.addDefault("chat_message.use_chat_message", true);
		c.addDefault("chat_message.message", Arrays.asList(new String[] { "&cYour inventory is full!" }));
		c.addDefault("actionannouncer.use_actionbar", false);
		c.addDefault("actionannouncer.message", "&cYour inventory is full!");
		c.addDefault("titlemanager.use_title", false);
		c.addDefault("titlemanager.title", "&cYou don't have room in your inventory");
		c.addDefault("titlemanager.subtitle", "to collect that &f%block%&c!");
		c.addDefault("titlemanager.fade_in", 12);
		c.addDefault("titlemanager.fade_out", 12);
		c.addDefault("titlemanager.duration", 20);
		c.addDefault("titlemanager.use_actionbar", false);
		c.addDefault("titlemanager.actionbar_message", "&cYou don't have room in your inventory");
		c.addDefault("holographicdisplays.use_hologram", false);
		c.addDefault("holographicdisplays.message", Arrays.asList(new String[] { "&cYour inventory", "&cis full!" }));
		c.addDefault("holographicdisplays.display_time", 3);
		c.options().copyDefaults(true);
		saveConfig();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e) {
		
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
		
		Block b = e.getBlock();

		PlayerInventory i = p.getInventory();

		if (!tools.contains(i.getItemInHand().getType())) {
			return;
		}

		if (ignored.contains(b.getType())) {
			return;
		}

		if (b.getDrops(i.getItemInHand()) == null || b.getDrops(i.getItemInHand()).isEmpty()) {
			return;
		}

		String wontFit = "";
		
		ItemStack wont = null;

		for (ItemStack drop : b.getDrops(i.getItemInHand())) {

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
			
			if (InventoryFull.active.get(p.getName()) >= options.getMaxAlerts()) {
				
				return;
			} else {
				
				InventoryFull.active.put(p.getName(), InventoryFull.active.get(p.getName())+1);
			}
			
		} else {
			
			InventoryFull.active.put(p.getName(), 1);
		}

		delayDecrease(p.getName());
		
		InventoryFullEvent event = new InventoryFullEvent(p, wont);
		Bukkit.getPluginManager().callEvent(event);
		
		if (options.useChatMsg()) {
			for (String line : options.getChatMsg()) {
				sms(p, line.replace("%player%", p.getName()).replace("%block%", wontFit));
			}
		}

		if (hookHolo && options.useHolo()) {
			if (holo != null) {
				holo.send(p, options.getHoloMsg(), wontFit);
			}
		}

		if (hookActionAnnouncer && options.useActionAnnouncer()) {
			if (aa != null) {
				aa.send(p, options.getActionMsg(), wontFit);
			}
		}

		if (hookTitleManager && options.useTitleManager()) {
			if (tm != null) {
				tm.sendTitle(p, options.getTitleMsg(), options.getSubTitleMsg(), wontFit, options.getFadeIn(), options.getDuration(), options.getFadeOut());
			}
		}

		if (hookTitleManager && options.useTitleABar()) {
			if (tm != null) {
				tm.sendActionbar(p, options.getTitleABarMsg(), wontFit);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command command, String label,
			String[] args) {

		if (s instanceof Player) {
			
			Player p = (Player) s;
			
			if (!p.hasPermission("inventoryfull.admin")) {
				sms(s, "&cYou don't have permission to do that!");
				return true;
			}
		}

		if (args.length == 0) {
			
			sms(s, "&cInventoryFull &fversion " + getDescription().getVersion());
			sms(s, "&7Created by: &cextended_clip");
			sms(s, "&7/invfull reload &f- &cReload config file");
			
		} else if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
			
			reloadConfig();
			saveConfig();
			options = new IFOptions(this);
			initHooks();
			sms(s, "&cInventoryFull &7configuration successfully reloaded!");
			
		} else {
			
			sms(s, "&cIncorrect usage! Use &7/inventoryfull");
		}
		return true;
	}

	public void delayDecrease(final String p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {

				if (InventoryFull.active.containsKey(p)) {
					if (InventoryFull.active.get(p) == 1) {
						InventoryFull.active.remove(p);
					} else {
						InventoryFull.active.put(p,
								InventoryFull.active.get(p)-1);
					}
				}

			}
		}, 20L * options.getCooldownTime());
	}

	public void sms(CommandSender s, String msg) {
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static List<Material> tools = Arrays.asList(new Material[] {
			Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE,
			Material.STONE_SPADE, Material.WOOD_AXE, Material.WOOD_HOE,
			Material.WOOD_PICKAXE, Material.WOOD_SPADE, Material.IRON_AXE,
			Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SPADE,
			Material.GOLD_AXE, Material.GOLD_HOE, Material.GOLD_PICKAXE,
			Material.GOLD_SPADE, Material.DIAMOND_AXE, Material.DIAMOND_HOE,
			Material.DIAMOND_PICKAXE, Material.DIAMOND_SPADE });

	public static List<Material> ignored = Arrays.asList(new Material[] {
			Material.LONG_GRASS, Material.GRASS, Material.WHEAT,
			Material.SUGAR_CANE_BLOCK, Material.SUGAR_CANE, Material.BED_BLOCK,
			Material.BED, Material.BOAT, Material.BOOKSHELF,
			Material.BREWING_STAND, Material.BREWING_STAND_ITEM,
			Material.CACTUS, Material.CAKE, Material.CAKE_BLOCK,
			Material.CARPET, Material.CARROT, Material.CARROT_ITEM,
			Material.CAULDRON, Material.CAULDRON_ITEM, Material.CROPS,
			Material.COMMAND, Material.COMMAND_MINECART,
			Material.DAYLIGHT_DETECTOR, Material.DEAD_BUSH,
			Material.DETECTOR_RAIL, Material.DIODE, Material.DIODE_BLOCK_OFF,
			Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT, Material.DISPENSER,
			Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.ENDER_CHEST,
			Material.ENDER_PORTAL_FRAME, Material.EXPLOSIVE_MINECART,
			Material.FLOWER_POT, Material.FLOWER_POT_ITEM, Material.HOPPER,
			Material.HOPPER_MINECART, Material.ICE, Material.IRON_DOOR_BLOCK,
			Material.IRON_DOOR, Material.ITEM_FRAME, Material.JUKEBOX,
			Material.LEAVES, Material.LEAVES_2, Material.LEVER,
			Material.MELON_BLOCK, Material.MELON_STEM, Material.MINECART,
			Material.NOTE_BLOCK, Material.PACKED_ICE, Material.PAINTING,
			Material.PISTON_BASE, Material.PISTON_EXTENSION,
			Material.PISTON_STICKY_BASE, Material.PISTON_MOVING_PIECE,
			Material.POISONOUS_POTATO, Material.POTATO, Material.PORTAL,
			Material.POWERED_MINECART, Material.POWERED_RAIL, Material.RAILS,
			Material.RED_ROSE, Material.YELLOW_FLOWER, Material.REDSTONE,
			Material.REDSTONE_COMPARATOR, Material.REDSTONE_WIRE,
			Material.SAPLING, Material.SEEDS, Material.SIGN,
			Material.SIGN_POST, Material.SNOW, Material.SNOW_BLOCK,
			Material.STAINED_GLASS, Material.STAINED_GLASS_PANE,
			Material.STORAGE_MINECART, Material.TNT, Material.TRAP_DOOR,
			Material.TORCH, Material.TRAPPED_CHEST, Material.TRIPWIRE,
			Material.TRIPWIRE_HOOK, Material.VINE, Material.WALL_SIGN,
			Material.WATER_LILY, Material.WEB });

}
