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
package com.wormhole_xtreme.worlds.world;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

// TODO: Auto-generated Javadoc
/**
 * The Class WorldManager.
 * 
 * @author alron
 */
public class WorldManager {

    /** The world list. */
    private static ConcurrentHashMap<String, WormholeWorld> worldList = new ConcurrentHashMap<String, WormholeWorld>();

    /**
     * Gets the world.
     * 
     * @param worldName
     *            the world name
     * @return the world
     */
    public static WormholeWorld getWorld(final String worldName) {
        if (worldList.containsKey(worldName)) {
            return worldList.get(worldName);
        }
        else {
            return null;
        }
    }

    /**
     * Adds the world.
     * 
     * @param world
     *            the world
     */
    public static void addWorld(final WormholeWorld world) {
        if (world != null) {
            worldList.put(world.getWorldName(), world);
        }
    }

    /**
     * Removes the world.
     * 
     * @param world
     *            the world
     */
    public static void removeWorld(final WormholeWorld world) {
        if (world != null) {
            worldList.remove(world);
        }
    }
    
    public static boolean createWorld(Player player, String worldName, String[] options)
    {
        if (worldName != null && getWorld(worldName) == null && player != null)
        {
            WormholeWorld world = new WormholeWorld();
            world.setWorldName(worldName);
            world.setWorldOwner(player.getName());
            //world.setThisWorld()
        
            addWorld(world);
            return true;
        }
        return false;
    }
}
