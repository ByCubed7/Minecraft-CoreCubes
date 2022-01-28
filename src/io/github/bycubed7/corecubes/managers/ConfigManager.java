package io.github.bycubed7.corecubes.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

	public static ConfigManager instance;

	public JavaPlugin plugin;

	// Config
	private String filename;

	private static File customConfigFile;
	private static FileConfiguration config;

	public ConfigManager(JavaPlugin plugin, String filename) {
		instance = this;

		this.plugin = plugin;
		this.filename = filename;

		CreateConfig();

	}

	private void CreateConfig() {
		customConfigFile = new File(plugin.getDataFolder(), filename);
		if (!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs();
			plugin.saveResource(filename, false);
		}

		config = new YamlConfiguration();
		try {
			config.load(customConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration config() {
		return config;
	}

	public static String getString(String path) {
		return config.getString(path);
	}

	public static Integer getInt(String path) {
		return config.getInt(path);
	}

	public static Boolean getBool(String path) {
		return config.getBoolean(path);
	}

	public static Color getColor(String path) {
		return config.getColor(path);
	}
}
