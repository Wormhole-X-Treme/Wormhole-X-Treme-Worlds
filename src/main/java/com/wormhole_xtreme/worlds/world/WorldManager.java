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
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Flying;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.WaterMob;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager.WorldOptionKeys;
import com.wormhole_xtreme.worlds.config.XMLConfig;

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
     * Adds the world.
     * 
     * @param world
     *            the world
     * @return true, if successful
     */
    public static boolean addWorld(final WormholeWorld world) {
        if (world != null) {
            try {
                worldList.put(world.getWorldName(), world);
                return true;
            }
            catch (final NullPointerException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Clear world creatures.
     * 
     * @param wormholeWorld
     *            the wormhole world
     * @return the int number of creatures cleared
     */
    public static int clearWorldCreatures(final WormholeWorld wormholeWorld) {
        int cleared = 0;
        if ( !wormholeWorld.isAllowHostiles() || !wormholeWorld.isAllowNeutrals()) {
            final List<LivingEntity> entityList = wormholeWorld.getThisWorld().getLivingEntities();

            for (final LivingEntity entity : entityList) {
                if (( !wormholeWorld.isAllowHostiles() && ((entity instanceof Monster) || (entity instanceof Flying))) || ( !wormholeWorld.isAllowNeutrals() && ((entity instanceof Animals) || (entity instanceof WaterMob)))) {
                    thisPlugin.prettyLog(Level.FINE, false, "Removed entity: " + entity);
                    entity.remove();
                    cleared++;
                }
            }

        }
        return cleared;
    }

//    /**
//     * Creates the safe spawn.
//     * 
//     * @param wormholeWorld
//     *            the wormhole world
//     */
//    public static void createSafeSpawn(final WormholeWorld wormholeWorld) {
//        if (wormholeWorld != null) {
//            if (wormholeWorld.isNetherWorld()) {
//                //BlockState[] spawnChunkBlockStates = wormholeWorld.getWorldSpawn().getBlock().getChunk().getTileEntities();
//                // <Y-AXIS 0-6,<[X-AXIS 0-6][Z-AXIS 0-6]Block>>
//                final ConcurrentHashMap<Integer, Block[][]> blockYaxisPlane = new ConcurrentHashMap<Integer, Block[][]>();
//                final int worldSpawnY = wormholeWorld.getWorldSpawn().getBlockY();
//                final int worldSpawnX = wormholeWorld.getWorldSpawn().getBlockX();
//                final int worldSpawnZ = wormholeWorld.getWorldSpawn().getBlockZ();
//                if ((worldSpawnY <= 124) && (worldSpawnY >= 3)) {
//                    final int iYY = 0;
//                    for (int iY = worldSpawnY - 3; iY < worldSpawnY + 3; iY++) {
//                        final Block[][] xzAxis = new Block[7][7];
//                        int iXX = 0;
//                        for (int iX = worldSpawnX - 3; iX < worldSpawnX + 3; iX++) {
//                            int iZZ = 0;
//                            for (int iZ = worldSpawnZ - 3; iZ < worldSpawnZ + 3; iZ++) {
//                                xzAxis[iXX][iZZ] = wormholeWorld.getThisWorld().getBlockAt(iX, iY, iZ);
//                                iZZ++;
//                            }
//                            iXX++;
//                        }
//                        blockYaxisPlane.put(iYY, xzAxis);
//                    }
//                }
//                for (int i = 0; i < 6; i++) {
//                    Block tmp = blockYaxisPlane.get(i)[3][3];
//                    if (tmp.getTypeId() == 0) {
//                        
//                    }
//                }
//            }
//        }
//    }

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
                (int) wormholeWorld.getWorldSpawn().getX(), (int) wormholeWorld.getWorldSpawn().getY(),
                (int) wormholeWorld.getWorldSpawn().getZ()
            };
            wormholeWorld.setWorldCustomSpawn(tempSpawn);
            if ( !connect) {
                wormholeWorld.setAutoconnectWorld(false);
            }
            addWorld(wormholeWorld);
            clearWorldCreatures(wormholeWorld);
            return true;
        }
        return false;
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
     * Gets the all worlds.
     * 
     * @return the all worlds
     */
    public static WormholeWorld[] getAllWorlds() {
        return worldList.values().toArray(new WormholeWorld[worldList.size()]);
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
     * Load autoconnect worlds.
     * 
     * @return the int number of worlds loaded.
     */
    public static int loadAutoconnectWorlds() {
        int loaded = 0;
        for (final WormholeWorld wormholeWorld : getAllWorlds()) {
            if (wormholeWorld.isAutoconnectWorld()) {
                if (loadWorld(wormholeWorld)) {
                    loaded++;
                }
            }
        }
        return loaded;
    }

    /**
     * Connect world.
     * 
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    public static boolean loadWorld(final WormholeWorld wormholeWorld) {
        if (wormholeWorld != null) {
            wormholeWorld.setThisWorld(wormholeWorld.getWorldSeed() == 0 ? thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), wormholeWorld.isNetherWorld() ? Environment.NETHER : Environment.NORMAL) : thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), wormholeWorld.isNetherWorld() ? Environment.NETHER : Environment.NORMAL, wormholeWorld.getWorldSeed()));
            wormholeWorld.getThisWorld().setSpawnLocation(wormholeWorld.getWorldCustomSpawn()[0], wormholeWorld.getWorldCustomSpawn()[1], wormholeWorld.getWorldCustomSpawn()[2]);
            wormholeWorld.setWorldSpawn(wormholeWorld.getThisWorld().getSpawnLocation());
            if (wormholeWorld.getWorldSeed() == 0) {
                wormholeWorld.setWorldSeed(wormholeWorld.getThisWorld().getId());
            }
            if (addWorld(wormholeWorld)) {
                final int c = clearWorldCreatures(wormholeWorld);
                if (c > 0) {
                    thisPlugin.prettyLog(Level.INFO, false, "Cleared \"" + c + "\" creature entities on world \"" + wormholeWorld.getWorldName() + "\"");
                }
                return true;
            }
        }
        return false;
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
}
