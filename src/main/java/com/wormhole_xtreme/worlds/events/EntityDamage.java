/*
 * Wormhole X-Treme Worlds Plugin for Bukkit
 * Copyright (C) 2011 Dean Bailey
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.wormhole_xtreme.worlds.events;

import java.util.logging.Level;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ResponseType;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The Class EntityDamage.
 * 
 * @author alron
 */
public class EntityDamage extends EntityListener {

    /** The Constant thisPlugin. */
    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /**
     * Player drown stop.
     * 
     * @param player
     *            the player
     */
    private static void playerStopDrown(final Player player) {
        if (player.getRemainingAir() == 0) {
            player.setRemainingAir(100);
        }
    }

    /**
     * Player fire stop.
     * 
     * @param player
     *            the player
     */
    private static void playerStopFire(final Player player) {
        if (player.getFireTicks() > 0) {
            player.setFireTicks(0);
        }
    }

    /* (non-Javadoc)
     * @see org.bukkit.event.entity.EntityListener#onEntityDamage(org.bukkit.event.entity.EntityDamageEvent)
     */
    @Override
    public void onEntityDamage(final EntityDamageEvent event) {
        if ( !event.isCancelled() && (event.getEntity() instanceof Player)) {
            final Player player = (Player) event.getEntity();
            final WormholeWorld wormholeWorld = WorldManager.getWorldFromPlayer(player);
            if ((wormholeWorld != null)) {

                // Cancel player damage if world does not allow it.
                if ( !wormholeWorld.isAllowPlayerDamage()) {
                    event.setCancelled(true);
                    playerStopFire(player);
                    playerStopDrown(player);
                    thisPlugin.prettyLog(Level.FINE, false, "Player damage event cancelled on " + player.getName() + " on world " + wormholeWorld.getWorldName());
                    return;
                }

                if ( !wormholeWorld.isAllowPlayerDrown() && (event.getCause() == DamageCause.DROWNING)) {
                    event.setCancelled(true);
                    playerStopDrown(player);
                    thisPlugin.prettyLog(Level.FINE, false, "Player drown damage event cancelled on " + player.getName() + " on world " + wormholeWorld.getWorldName());
                    return;
                }

                // Cancel PvP damage if world does not allow it.
                if ( !wormholeWorld.isAllowPvP()) {
                    Entity entityDamager = null;
                    if (event instanceof EntityDamageByEntityEvent) {
                        entityDamager = ((EntityDamageByEntityEvent) event).getDamager();
                    }
                    else if (event instanceof EntityDamageByProjectileEvent) {
                        entityDamager = ((EntityDamageByProjectileEvent) event).getDamager();
                    }
                    if ((entityDamager != null) && (entityDamager instanceof Player)) {
                        event.setCancelled(true);
                        ((Player) entityDamager).sendMessage(ResponseType.ERROR_PVP_NOT_ALLOWED.toString() + wormholeWorld.getWorldName());
                        thisPlugin.prettyLog(Level.FINE, false, "PvP attempt denied. Attacker " + ((Player) entityDamager).getName() + " vs. Defender " + player.getName() + " on world " + wormholeWorld.getWorldName());
                        return;
                    }
                }

                if ( !wormholeWorld.isAllowPlayerLavaDamage() && (event.getCause() == DamageCause.LAVA)) {
                    event.setCancelled(true);
                    playerStopFire(player);
                    thisPlugin.prettyLog(Level.FINE, false, "Player lava damage event cancelled on " + player.getName() + " on world " + wormholeWorld.getWorldName());
                    return;
                }

                if ( !wormholeWorld.isAllowPlayerFallDamage() && (event.getCause() == DamageCause.FALL)) {
                    event.setCancelled(true);
                    thisPlugin.prettyLog(Level.FINE, false, "Player fall damage event cancelled on " + player.getName() + " on world " + wormholeWorld.getWorldName());
                    return;
                }

                if ( !wormholeWorld.isAllowPlayerLightningDamage() && (event.getCause() == DamageCause.LIGHTNING)) {
                    event.setCancelled(true);
                    playerStopFire(player);
                    thisPlugin.prettyLog(Level.FINE, false, "Player lightning damage event cancelled on " + player.getName() + " on world " + wormholeWorld.getWorldName());
                    return;
                }

                if ( !wormholeWorld.isAllowPlayerFireDamage() && ((event.getCause() == DamageCause.FIRE) || (event.getCause() == DamageCause.FIRE_TICK))) {
                    event.setCancelled(true);
                    playerStopFire(player);
                    thisPlugin.prettyLog(Level.FINE, false, "Player fire damage event cancelled on " + player.getName() + " on world " + wormholeWorld.getWorldName());
                    return;
                }
            }
        }
    }

}
