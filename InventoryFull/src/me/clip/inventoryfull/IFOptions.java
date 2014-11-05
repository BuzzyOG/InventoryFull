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

import java.util.List;

import org.bukkit.ChatColor;

public class IFOptions {
	
	private int cooldownTime;
	private int maxAlerts;
	
	private boolean useHolo;

	private List<String> holoMsg;
	private int holoTime;

	private boolean useTitleManager;
	private String titleMsg;
	private String subTitleMsg;
	private int fadeIn;
	private int fadeOut;
	private int duration;
	private boolean useTitleABar;
	private String titleABarMsg;

	private boolean useActionAnnouncer;

	private String actionMsg;

	private boolean useChatMsg;
	private List<String> chatMsg;
	
	
	public IFOptions(InventoryFull i) {
		setCooldownTime(i.getConfig().getInt("cooldown_time"));
		setMaxAlerts(i.getConfig().getInt("max_alerts_until_cooldown"));
		setUseHolo(i.getConfig().getBoolean("holographicdisplays.use_hologram"));
		setHoloMsg(i.getConfig().getStringList("holographicdisplays.message"));
		setHoloTime(i.getConfig().getInt("holographicdisplays.display_time"));
		setUseTitleManager(i.getConfig().getBoolean("titlemanager.use_title"));
		setTitleMsg(ChatColor.translateAlternateColorCodes('&', i.getConfig().getString("titlemanager.title")));
		setSubTitleMsg(ChatColor.translateAlternateColorCodes('&', i.getConfig().getString("titlemanager.subtitle")));
		setFadeIn(i.getConfig().getInt("titlemanager.fade_in"));
		setFadeOut(i.getConfig().getInt("titlemanager.fade_out"));
		setDuration(i.getConfig().getInt("titlemanager.duration"));
		setUseTitleABar(i.getConfig().getBoolean("titlemanager.use_actionbar"));
		setTitleABarMsg(ChatColor.translateAlternateColorCodes('&', i.getConfig().getString("titlemanager.actionbar_message")));
		setUseActionAnnouncer(i.getConfig().getBoolean("actionannouncer.use_actionbar"));
		setActionMsg(i.getConfig().getString("actionannouncer.message"));
		setUseChatMsg(i.getConfig().getBoolean("chat_message.use_chat_message"));
		setChatMsg(i.getConfig().getStringList("chat_message.message"));
	}

	public int getCooldownTime() {
		return cooldownTime;
	}

	private void setCooldownTime(int cooldownTime) {
		this.cooldownTime = cooldownTime;
	}


	public int getMaxAlerts() {
		return maxAlerts;
	}


	private void setMaxAlerts(int maxAlerts) {
		this.maxAlerts = maxAlerts;
	}


	public boolean useHolo() {
		return useHolo;
	}


	private void setUseHolo(boolean useHolo) {
		this.useHolo = useHolo;
	}


	public List<String> getHoloMsg() {
		return holoMsg;
	}


	private void setHoloMsg(List<String> holoMsg) {
		this.holoMsg = holoMsg;
	}


	public int getHoloTime() {
		return holoTime;
	}


	private void setHoloTime(int holoTime) {
		this.holoTime = holoTime;
	}


	public boolean useTitleManager() {
		return useTitleManager;
	}


	private void setUseTitleManager(boolean useTitleManager) {
		this.useTitleManager = useTitleManager;
	}


	public String getTitleMsg() {
		return titleMsg;
	}


	private void setTitleMsg(String titleMsg) {
		this.titleMsg = titleMsg;
	}


	public String getSubTitleMsg() {
		return subTitleMsg;
	}


	private void setSubTitleMsg(String subTitleMsg) {
		this.subTitleMsg = subTitleMsg;
	}


	public int getFadeIn() {
		return fadeIn;
	}


	private void setFadeIn(int fadeIn) {
		this.fadeIn = fadeIn;
	}


	public int getFadeOut() {
		return fadeOut;
	}


	private void setFadeOut(int fadeOut) {
		this.fadeOut = fadeOut;
	}


	public int getDuration() {
		return duration;
	}


	private void setDuration(int duration) {
		this.duration = duration;
	}


	public boolean useTitleABar() {
		return useTitleABar;
	}


	private void setUseTitleABar(boolean useTitleABar) {
		this.useTitleABar = useTitleABar;
	}


	public String getTitleABarMsg() {
		return titleABarMsg;
	}


	private void setTitleABarMsg(String titleABarMsg) {
		this.titleABarMsg = titleABarMsg;
	}


	public boolean useActionAnnouncer() {
		return useActionAnnouncer;
	}


	private void setUseActionAnnouncer(boolean useActionAnnouncer) {
		this.useActionAnnouncer = useActionAnnouncer;
	}


	public String getActionMsg() {
		return actionMsg;
	}


	private void setActionMsg(String actionMsg) {
		this.actionMsg = actionMsg;
	}


	public boolean useChatMsg() {
		return useChatMsg;
	}


	private void setUseChatMsg(boolean useChatMsg) {
		this.useChatMsg = useChatMsg;
	}


	public List<String> getChatMsg() {
		return chatMsg;
	}


	private void setChatMsg(List<String> chatMsg) {
		this.chatMsg = chatMsg;
	}
	
}
