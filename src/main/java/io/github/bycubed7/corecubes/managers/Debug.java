package io.github.bycubed7.corecubes.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Debug {

	public static ConsoleCommandSender logger;

	public static String prefix;
	public static String version;
	public static String serverVersion;

	public static List<String> banner = new ArrayList<String>();

	public Debug(JavaPlugin plugin) {
		prefix = "[" + plugin.getDescription().getPrefix() + "] ";
		version = plugin.getDescription().getVersion();
		logger = plugin.getServer().getConsoleSender();

		String a = plugin.getServer().getClass().getPackage().getName();
		serverVersion = a.substring(a.lastIndexOf('.') + 1);
	}

	public static void Log(String s) {
		logger.sendMessage(prefix + s);
	}

	public static void Log(String s, ChatColor color) {
		Log(color + s);
	}

	public static void Error(String s) {

	}

	public static void Banner() {

		ChatColor c = ChatColor.GRAY;
		ChatColor a = ChatColor.DARK_GRAY;
		ChatColor v = ChatColor.DARK_GREEN;

		int i = 0;
		for (String str : banner) {
			if (i + 1 == banner.size())
				logger.sendMessage(c + str + v + " v" + Debug.version);
			else
				logger.sendMessage(c + str);
			i++;
		}
		logger.sendMessage(v + "  Running on " + Debug.serverVersion + a + "  ~ By ByCubed7");
	}

}
