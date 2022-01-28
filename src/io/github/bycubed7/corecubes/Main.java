package io.github.bycubed7.corecubes;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Debug;

public class Main extends JavaPlugin {

	public static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		new Debug(this);

		// Read config
		Debug.Log("Reading Config..");
		new ConfigManager(this, "default.yml");

		// Loading is done!
		Debug.Log(ChatColor.GREEN + "Done!");

		// Using a schedular so that it runs once ALL plugins are loaded.
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				onStart();
			}
		});
	}

	public void onStart() {
		// Called at the start and after /reload
	}

}
