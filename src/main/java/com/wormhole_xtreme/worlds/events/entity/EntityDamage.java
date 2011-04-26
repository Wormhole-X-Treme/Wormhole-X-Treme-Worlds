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
package com.wormhole_xtreme.worlds.events.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wormhole_xtreme.worlds.config.ResponseType;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The Class EntityDamage.
 * 
 * @author alron
 */
class EntityDamage {

    /**
     * Handle entity damage.
     * 
     * @param player
     *            the player
     * @param cause
     *            the cause
     * @param damager
     *            the damager
     * @return true, if successful
     */
    static boolean handleEntityDamage(final Player player, final DamageCause cause, final Entity damager) {
        final WormholeWorld wormholeWorld = WorldManager.getWorldFromPlayer(player);
        if ((wormholeWorld != null)) {
            if ( !wormholeWorld.isAllowPlayerDamage()) {
                playerStopFire(player);
                playerStopDrown(player);
                return true;
            }
            else if (cause != null) {
                switch (cause) {
                    case CONTACT :
                        return wormholeWorld.isAllowPlayerContactDamage() ? false : true;
                    case ENTITY_ATTACK :
                        return wormholeWorld.isAllowPvP() ? false : playerStopPvP(damager);
                    case SUFFOCATION :
                        return wormholeWorld.isAllowPlayerSuffocation() ? false : true;
                    case FALL :
                        return wormholeWorld.isAllowPlayerFallDamage() ? false : true;
                    case FIRE :
                    case FIRE_TICK :
                        return wormholeWorld.isAllowPlayerFireDamage() ? false : playerStopFire(player);
                    case LAVA :
                        return wormholeWorld.isAllowPlayerLavaDamage() ? false : playerStopFire(player);
                    case DROWNING :
                        return wormholeWorld.isAllowPlayerDrown() ? false : playerStopDrown(player);
                    case BLOCK_EXPLOSION :
                    case ENTITY_EXPLOSION :
                        return wormholeWorld.isAllowPlayerExplosionDamage() ? false : true;
                    case VOID :
                        return wormholeWorld.isAllowPlayerVoidDamage() ? false : true;
                    case LIGHTNING :
                        return wormholeWorld.isAllowPlayerLightningDamage() ? false : playerStopFire(player);
                    default :
                        break;
                }
            }
        }
        return false;
    }

    /**
     * Player drown stop.
     * 
     * @param player
     *            the player
     */
    private static boolean playerStopDrown(final Player player) {
        if (player.getRemainingAir() == 0) {
            player.setRemainingAir(100);
        }
        return true;
    }

    /**
     * Player fire stop.
     * 
     * @param player
     *            the player
     */
    private static boolean playerStopFire(final Player player) {
        if (player.getFireTicks() > 0) {
            player.setFireTicks(0);
        }
        return true;
    }

    /**
     * Player stop pv p.
     * 
     * @param damager
     *            the damager
     * @return true, if successful
     */
    private static boolean playerStopPvP(final Entity damager) {
        if ((damager != null) && (damager instanceof Player)) {
            ((Player) damager).sendMessage(ResponseType.ERROR_PVP_NOT_ALLOWED.toString());
            return true;
        }
        return false;
    }
}
