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

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World.Environment;
import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager.WorldOptionKeys;


/**
 * The Class WorldManager.
 * 
 * @author alron
 */
public class WorldManager {

    /** The world list. */
    private static ConcurrentHashMap<String, WormholeWorld> worldList = new ConcurrentHashMap<String, WormholeWorld>();

    /** The Constant thisPlugin. */
    private final static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /**
     * Gets the all worlds.
     * 
     * @return the all worlds
     */
    public static WormholeWorld[] getAllWorlds() {
        final Set<String> keys = worldList.keySet();
        final ArrayList<String> list = new ArrayList<String>(keys);
        final WormholeWorld[] wormholeWorlds = new WormholeWorld[list.size()];
        int i = 0;
        for (final String key : list) {
            final WormholeWorld w = worldList.get(key);
            if (w != null) {
                wormholeWorlds[i] = w;
                i++;
            }
        }
        return wormholeWorlds;
    }

    /**
     * Gets the all world names.
     * 
     * @return the all world names
     */
    public static String[] getAllWorldNames() {
        final Set<String> keys = worldList.keySet();
        final ArrayList<String> list = new ArrayList<String>(keys);
        final String[] wormholeWorlds = new String[list.size()];
        int i = 0;
        for (final String key : list) {
            final WormholeWorld w = worldList.get(key);
            if (w != null) {
                wormholeWorlds[i] = w.getWorldName();
                i++;
            }
        }
        return wormholeWorlds;
    }

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
            worldList.remove(world.getWorldName());
        }
    }


    /**
     * Creates the world.
     * 
     * @param player
     *            the player
     * @param worldName
     *            the world name
     * @param worldOptionKeys
     *            the world option keys
     * @param worldSeed
     *            the world seed
     * @return true, if successful
     */
    public static boolean createWorld(final String playerName, final String worldName, final WorldOptionKeys[] worldOptionKeys, final long worldSeed) {
        if ((worldName != null) && (getWorld(worldName) == null) && (playerName != null)) {
            final WormholeWorld wormholeWorld = new WormholeWorld();
            Environment worldEnvironment = Environment.NORMAL;
            boolean connect = false;
            boolean seed = false;
            wormholeWorld.setWorldName(worldName);
            wormholeWorld.setWorldOwner(playerName);
            if (worldOptionKeys != null) {
                for (final WorldOptionKeys worldOptionKey : worldOptionKeys) {
                    if (worldOptionKey == WorldOptionKeys.worldOptionNether) {
                        wormholeWorld.setNetherWorld(true);
                    }
                    else if (worldOptionKey == WorldOptionKeys.worldOptionNoHostiles) {
                        wormholeWorld.setAllowHostiles(false);
                    }
                    else if (worldOptionKey == WorldOptionKeys.worldOptionNoNeutrals) {
                        wormholeWorld.setAllowNeutrals(false);
                    }
                    else if (worldOptionKey == WorldOptionKeys.worldOptionConnect) {
                        connect = true;
                    }
                    else if (worldOptionKey == WorldOptionKeys.worldOptionSeed) {
                        seed = true;
                    }
                }
            }


            if (wormholeWorld.isNetherWorld()) {
                worldEnvironment = Environment.NETHER;
            }
            if (thisPlugin.getServer().getWorld(worldName) == null) {
                // Send Generating new world message
                if (seed) {
                    wormholeWorld.setThisWorld(thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), worldEnvironment, worldSeed));
                }
                else {
                    wormholeWorld.setThisWorld(thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), worldEnvironment));
                }
                wormholeWorld.getThisWorld().save();
            }
            else {
                // send connecting to existing world message
                wormholeWorld.setThisWorld(thisPlugin.getServer().getWorld(worldName));
            }
            wormholeWorld.setWorldCustomSpawn(wormholeWorld.getThisWorld().getSpawnLocation());
            
            if (connect) {
                wormholeWorld.setAutoconnectWorld(true);
            }

            addWorld(wormholeWorld);
            return true;
        }
        return false;
    }
}
