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
package com.wormhole_xtreme.worlds.events.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.worlds.world.WorldManager;

/**
 * @author alron
 * 
 */
class PlayerRespawn {

    /**
     * Handle player respawn.
     * 
     * @param player
     *            the player
     * @return the location
     */
    static Location handlePlayerRespawn(final Player player) {
        final String worldName = player.getWorld().getName();
        return WorldManager.isWormholeWorld(worldName)
            ? WorldManager.getSafeSpawnLocation(WorldManager.getWorld(worldName), player) : null;
    }
}
