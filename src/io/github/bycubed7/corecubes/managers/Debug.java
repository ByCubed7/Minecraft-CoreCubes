package io.github.bycubed7.corecubes.managers;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.CubePlugin;

public class Debug {

	private static ConsoleCommandSender logger;
	private static String prefix;
	private static String version;
	private static String serverVersion;

	public Debug(JavaPlugin plugin) {
		prefix = "[" + plugin.getDescription().getPrefix() + "] ";
		version = plugin.getDescription().getVersion();
		logger = plugin.getServer().getConsoleSender();

		String a = plugin.getServer().getClass().getPackage().getName();
		serverVersion = a.substring(a.lastIndexOf('.') + 1);
	}

	public static void log(String s) {
		logger.sendMessage(prefix + s);
	}

	public static void log(String s, ChatColor color) {
		log(color + s);
	}

	public static void error(String s) {
		log(ChatColor.RED +"ERROR: " + s);
	}

	public static void warn(String s) {
		log(ChatColor.YELLOW +"WARNING: " + s);
	}

	public static void banner(CubePlugin plugin) {

		ChatColor c = ChatColor.GRAY;
		ChatColor a = ChatColor.DARK_GRAY;
		ChatColor v = ChatColor.DARK_GREEN;

		int i = 0;
		for (String str : plugin.banner) {
			if (i + 1 == plugin.banner.size())
				logger.sendMessage(c + str + v + " v" + Debug.version);
			else
				logger.sendMessage(c + str);
			i++;
		}
		logger.sendMessage(v + "  Running on " + Debug.serverVersion + a + "  ~ By ByCubed7");
		logger.sendMessage("");
	}

}
