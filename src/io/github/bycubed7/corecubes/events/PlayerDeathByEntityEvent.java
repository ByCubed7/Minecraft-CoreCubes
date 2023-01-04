package io.github.bycubed7.corecubes.events;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown whenever a {@link Player} dies by an entity action
 * @author ByCubed7
 */
public class PlayerDeathByEntityEvent extends PlayerDeathEvent {
	
	private final Entity attacker;
	
	// Named constructor
	public static PlayerDeathByEntityEvent fromPlayerDeath(PlayerDeathEvent event, Entity attacker) {
		return new PlayerDeathByEntityEvent(
			event.getEntity(), 
			event.getDrops(), 
			event.getDroppedExp(), 
			event.getNewExp(), 
			event.getDeathMessage(),
			attacker
		);
	}
	
	public PlayerDeathByEntityEvent(
			@NotNull Player player,
			@NotNull List<ItemStack> drops, 
			int droppedExp, 
			int newExp,
			@Nullable String deathMessage, 
			@NotNull Entity attacker) {
		super(player, drops, droppedExp, newExp, deathMessage);
		this.attacker = attacker;
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	// Return the entity that caused the death
	public Entity getAttacker() {
		return attacker;
	}
	
	// Return the entity that caused the death
	public EntityType getAttackerType() {
		return attacker.getType();
	}
	
	
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
