package io.github.bycubed7.corecubes.events;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown whenever a {@link Player} dies by enviromental causes
 * @author ByCubed7
 */
public class PlayerDeathByEnviromentEvent extends PlayerDeathEvent {
	
	// Named constructor
	public static PlayerDeathByEnviromentEvent fromPlayerDeath(PlayerDeathEvent event) {
		return new PlayerDeathByEnviromentEvent(
			event.getEntity(), 
			event.getDrops(), 
			event.getDroppedExp(), 
			event.getNewExp(), 
			event.getDeathMessage()
		);
	}
	
	public PlayerDeathByEnviromentEvent(
			@NotNull Player player,
			@NotNull List<ItemStack> drops, 
			int droppedExp, 
			int newExp,
			@Nullable String deathMessage) {
		super(player, drops, droppedExp, newExp, deathMessage);
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	// Return the player that was being hit
	public Player getDefender() {
		return getEntity();
	}
	
	// Return the player that was being hit
	public EntityType getDefenderType() {
		return EntityType.PLAYER;
	}
		
	
	public EntityDamageEvent getLastDamage() {
		return entity.getLastDamageCause();
	}
	public DamageCause getLastDamageCause() {
		return entity.getLastDamageCause().getCause();
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// HandlerList
	
	private static final HandlerList HANDLERS = new HandlerList();
    public static HandlerList getHandlerList() { return HANDLERS; }
	@Override public HandlerList getHandlers() { return HANDLERS; }
	
}
