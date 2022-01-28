package io.github.bycubed7.corecubes.managers;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Tell {

//	public Tell(JavaPlugin plugin) {
//
//	}

	public static void player(Player player, String s) {
		player.sendMessage(s);
	}

	public static void title(Player player, String top, String bottom) {
		player.sendTitle(top, bottom, 5, 40, 5);
	}

	public static void actionbar(Player player, String s) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(s));
	}

}
