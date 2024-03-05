package se.enji.lep;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Chat extends JavaPlugin implements Listener {
	FileConfiguration config;
	Logger log = Bukkit.getLogger();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		config=getConfig();
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String status = config.getString("chatformat.guest");
		String n = p.getDisplayName();
		n = n.replace("_", " ");
		if (p.hasPermission("chatformat.builder")) status = config.getString("chatformat.builder");
		if (p.hasPermission("chatformat.moderator")) status = config.getString("chatformat.moderator");
		if (p.hasPermission("chatformat.admin") || p.isOp()) status = config.getString("chatformat.admin");
		if (p.hasPermission("chatformat.owner")) status = config.getString("chatformat.owner");
		e.setFormat(status + "§7 " + n + ":§f %2$s");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!config.getBoolean("messages.join-server.enable")) return;
		String msg = config.getString("messages.join-server.text");
		String n = e.getPlayer().getDisplayName();
		n = n.replace("_", " ");
		msg = msg.replaceAll("%p", n);
		e.setJoinMessage(msg);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (!config.getBoolean("messages.leave-server.enable")) return;
		String msg = config.getString("messages.leave-server.text");
		String n = e.getPlayer().getDisplayName();
		n = n.replace("_", " ");
		msg = msg.replaceAll("%p", n);
		e.setQuitMessage(msg);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		if (sender instanceof Player) {
			Player p=(Player)sender;
			if (cmd.equalsIgnoreCase("kaos")) {
				if (!p.hasPermission("dr.kaos")) return false;
				if (args.length < 1) return false;
				StringBuilder message = new StringBuilder();
				for (String arg : args) {
					message.append(" ");
					message.append(arg);
				}
				Bukkit.getServer().broadcastMessage("§cDr. Kaos:§e" + message.toString());
				return true;
			}
		}
		return false;
	}
}
