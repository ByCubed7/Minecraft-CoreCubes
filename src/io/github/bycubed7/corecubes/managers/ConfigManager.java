package io.github.bycubed7.corecubes.managers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

	private final JavaPlugin plugin;

	// Config
	private String filename;

	private File file;
	private FileConfiguration config;

	public ConfigManager(JavaPlugin plugin, String filename) {
		this.plugin = plugin;
		this.filename = filename;

		load();
	}
	
	// File handling
	
	public void save() {
		// Save to file
		//plugin.saveResource(filename, true);

        if (config == null || file == null)
            return;

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void load() {
		file = new File(plugin.getDataFolder(), filename);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource(filename, false);
		}
		
		config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void revert() {
		file.getParentFile().mkdirs();
		plugin.saveResource(filename, true);
	}
	
	// Gets

	public String getString(String path) {
		return config.getString(path);
	}

	public Integer getInt(String path) {
		return config.getInt(path);
	}

	public Float getFloat(String path) {
		return (float) config.getDouble(path);
	}

	public Boolean getBool(String path) {
		return config.getBoolean(path);
	}

	public Color getColor(String path) {
		return config.getColor(path);
	}

	public List<?> getList(String path) {
		return config.getList(path);
	}
	
	// Sets
	
	public void set(String path, Object value) {
		config.set(path, value);
	}
}
