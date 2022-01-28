package io.github.bycubed7.corecubes.managers;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Debug {

	public static ConsoleCommandSender logger;

	public static String prefix;
	public static String version;
	public static String serverVersion;

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

		// .sendMessage(ChatColor.DARK_GRAY + "Reload Complete");
		ChatColor c = ChatColor.GRAY;
		ChatColor a = ChatColor.DARK_GRAY;
		ChatColor v = ChatColor.DARK_GREEN;

		logger.sendMessage(c + "");
		logger.sendMessage(c + "");
		logger.sendMessage(c + "" + v + " v" + Debug.version);
		logger.sendMessage(v + "  Running on " + Debug.serverVersion + a + "   ~ By ByCubed7");
		logger.sendMessage("");
	}

}
