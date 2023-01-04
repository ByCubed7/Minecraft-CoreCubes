package io.github.bycubed7.corecubes.managers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.CubePlugin;

public class Debug {

	private static ConsoleCommandSender console;
	private static String prefix;
	private static String version;
	private static String serverVersion;
	
	private static Logger logger;

	public Debug(JavaPlugin plugin) {
		prefix = "[" + plugin.getDescription().getPrefix() + "] ";
		version = plugin.getDescription().getVersion();
		console = plugin.getServer().getConsoleSender();

		String a = plugin.getServer().getClass().getPackage().getName();
		serverVersion = a.substring(a.lastIndexOf('.') + 1);
		
		logger = plugin.getLogger();
	}

	public static void log(String s) {
        //logger.log(Level.INFO, s);
		console.sendMessage(s);
	}

	public static void log(String s, ChatColor color) {
		log(color + s);
	}

	public static void error(String s) {
        logger.log(Level.SEVERE, s);
	}

	public static void warn(String s) {
        logger.log(Level.WARNING, s);
	}

	public static void banner(CubePlugin plugin) {

		ChatColor c = ChatColor.GRAY;
		ChatColor a = ChatColor.DARK_GRAY;
		ChatColor v = ChatColor.DARK_GREEN;

		int i = 0;
		for (String str : plugin.banner) {
			if (i + 1 == plugin.banner.size())
				console.sendMessage(c + str + v + " v" + Debug.version);
			else
				console.sendMessage(c + str);
			i++;
		}
		console.sendMessage(v + "  Running on " + Debug.serverVersion + a + "  ~ By ByCubed7");
		console.sendMessage("");
	}

}
