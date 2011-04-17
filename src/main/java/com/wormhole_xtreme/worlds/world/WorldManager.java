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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.World.Environment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager.WorldOptionKeys;
import com.wormhole_xtreme.worlds.config.XMLConfig;


// TODO: Auto-generated Javadoc
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
        return worldList.values().toArray(new WormholeWorld[worldList.size()]);
    }

    /**
     * Gets the all world names.
     * 
     * @return the all world names
     */
    public static String[] getAllWorldNames() {
        return worldList.keySet().toArray(new String[worldList.size()]);
    }

    /**
     * Gets the world.
     * 
     * @param worldName
     *            the world name
     * @return the world
     */
    public static WormholeWorld getWorld(final String worldName) {
        return worldList.get(worldName);
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
            XMLConfig.deleteXmlWorldConfig(world.getWorldName());
            worldList.remove(world.getWorldName());
        }
    }

    /**
     * Load autoconnect worlds.
     */
    public static void loadAutoconnectWorlds() {
        for (final WormholeWorld wormholeWorld : getAllWorlds()) {
            if (wormholeWorld.isAutoconnectWorld()) {
                connectWorld(wormholeWorld);
                
            }
        }
    }

    /**
     * Clear world creatures.
     * 
     * @param wormholeWorld
     *            the wormhole world
     */
    public static void clearWorldCreatures(final WormholeWorld wormholeWorld) {
        if (!wormholeWorld.isAllowHostiles() || !wormholeWorld.isAllowNeutrals()) {
            final List<Entity> entityList = wormholeWorld.getThisWorld().getEntities();
            for (final Entity entity : entityList) {
                if ((!wormholeWorld.isAllowHostiles() && (entity instanceof Monster)) || (!wormholeWorld.isAllowHostiles() && (entity instanceof Animals))) {
                    entity.remove();
                    thisPlugin.prettyLog(Level.FINE, false, "Removed entity: " + entity.getEntityId());
                }
            }
        }
    }

    /**
     * Connect world.
     * 
     * @param wormholeWorld
     *            the wormhole world
     */
    public static void connectWorld(final WormholeWorld wormholeWorld) {
        wormholeWorld.setThisWorld(wormholeWorld.getWorldSeed() == 0 ? thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), wormholeWorld.isNetherWorld() ? Environment.NETHER : Environment.NORMAL) : thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), wormholeWorld.isNetherWorld() ? Environment.NETHER : Environment.NORMAL, wormholeWorld.getWorldSeed()));
        wormholeWorld.getThisWorld().setSpawnLocation(wormholeWorld.getWorldCustomSpawn()[0], wormholeWorld.getWorldCustomSpawn()[1], wormholeWorld.getWorldCustomSpawn()[2]);
        wormholeWorld.setWorldSpawn(wormholeWorld.getThisWorld().getSpawnLocation());
        if (wormholeWorld.getWorldSeed() == 0) {
            wormholeWorld.setWorldSeed(wormholeWorld.getThisWorld().getId());
        }
        addWorld(wormholeWorld);
        clearWorldCreatures(wormholeWorld);
    }

    /**
     * Creates the world.
     * 
     * @param playerName
     *            the player name
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
            boolean connect = true;
            boolean seed = false;
            wormholeWorld.setWorldName(worldName);
            wormholeWorld.setWorldOwner(playerName);
            if (worldOptionKeys != null) {
                for (final WorldOptionKeys worldOptionKey : worldOptionKeys) {
                    switch (worldOptionKey) {
                        case worldOptionNether :
                            wormholeWorld.setNetherWorld(true);
                            break;
                        case worldOptionNoHostiles :
                            wormholeWorld.setAllowHostiles(false);
                            break;
                        case worldOptionNoNeutrals :
                            wormholeWorld.setAllowNeutrals(false);
                            break;
                        case worldOptionNoConnect :
                            connect = false;
                            break;
                        case worldOptionSeed :
                            seed = true;
                            break;
                        default :
                            break;
                    }
                }
            }


            if (wormholeWorld.isNetherWorld()) {
                worldEnvironment = Environment.NETHER;
            }
            if (thisPlugin.getServer().getWorld(worldName) == null) {
                if (seed) {
                    wormholeWorld.setThisWorld(thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), worldEnvironment, worldSeed));
                    wormholeWorld.setWorldSeed(worldSeed);
                }
                else {
                    wormholeWorld.setThisWorld(thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), worldEnvironment));
                    wormholeWorld.setWorldSeed(wormholeWorld.getThisWorld().getId());
                }
                wormholeWorld.getThisWorld().save();
            }
            else {
                wormholeWorld.setThisWorld(thisPlugin.getServer().getWorld(worldName));
                wormholeWorld.setWorldSeed(wormholeWorld.getThisWorld().getId());
            }
            wormholeWorld.setWorldSpawn(wormholeWorld.getThisWorld().getSpawnLocation());

            final int[] tempSpawn = {
                (int) wormholeWorld.getWorldSpawn().getX(),(int) wormholeWorld.getWorldSpawn().getY(),
                (int) wormholeWorld.getWorldSpawn().getZ()};
            wormholeWorld.setWorldCustomSpawn(tempSpawn);
            if (!connect) {
                wormholeWorld.setAutoconnectWorld(false);
            }
            addWorld(wormholeWorld);
            clearWorldCreatures(wormholeWorld);
            return true;
        }
        return false;
    }
}
