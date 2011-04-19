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

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
     * Check non safe type id.
     * 
     * @param typeId
     *            the type id
     * @return true, if successful
     */
    private static boolean checkNonSafeTypeId(final int typeId) {
        if ((typeId == 0) || (typeId == 10) || (typeId == 11)) {
            return false;
        }
        return true;
    }

    /**
     * Check safe block above.
     * 
     * @param safeBlock
     *            the safe block
     * @return true, if successful
     */
    private static boolean checkSafeBlockAbove(final Block safeBlock) {
        if (safeBlock != null) {
            final int typeId = safeBlock.getFace(BlockFace.UP).getTypeId();
            return checkSafeTypeId(typeId);
        }
        return false;
    }

    /**
     * Check safe block below.
     * 
     * @param safeBlock
     *            the safe block
     * @return true, if successful
     */
    private static boolean checkSafeBlockBelow(final Block safeBlock) {
        if (safeBlock != null) {
            final int typeId = safeBlock.getFace(BlockFace.DOWN).getTypeId();
            return checkNonSafeTypeId(typeId);
        }
        return false;
    }

    /**
     * Check safe type id.
     * 
     * @param typeId
     *            the type id
     * @return true, if successful
     */
    private static boolean checkSafeTypeId(final int typeId) {
        if ((typeId == 0) || (typeId == 8) || (typeId == 9) || (typeId == 66)) {
            return true;
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
            wormholeWorld.setWorldSpawn(wormholeWorld.isNetherWorld() ? findSafeSpawn(wormholeWorld.getThisWorld().getSpawnLocation()) : wormholeWorld.getThisWorld().getSpawnLocation());
            final int tsX = wormholeWorld.getWorldSpawn().getBlockX();
            final int tsY = wormholeWorld.getWorldSpawn().getBlockY();
            final int tsZ = wormholeWorld.getWorldSpawn().getBlockZ();
            if ( !wormholeWorld.getWorldSpawn().equals(wormholeWorld.getThisWorld().getSpawnLocation())) {
                wormholeWorld.getThisWorld().setSpawnLocation(tsX, tsY, tsZ);
            }
            final int[] tempSpawn = {
                tsX, tsY, tsZ
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
     * Find safe spawn.
     * 
     * @param spawnLocation
     *            the wormhole world
     * @return the location
     */
    public static Location findSafeSpawn(final Location spawnLocation) {
        if (spawnLocation != null) {
            final ConcurrentHashMap<Integer, Block[][]> blockYaxisPlane = new ConcurrentHashMap<Integer, Block[][]>();
            final int worldSpawnY = spawnLocation.getBlockY();
            final int worldSpawnX = spawnLocation.getBlockX();
            final int worldSpawnZ = spawnLocation.getBlockZ();
            if ((worldSpawnY <= 122) && (worldSpawnY >= 5)) {
                int iYY = 0;
                for (int iY = worldSpawnY - 4; iY < worldSpawnY + 4; iY++) {
                    final Block[][] xzAxis = new Block[9][9];
                    int iXX = 0;
                    for (int iX = worldSpawnX - 4; iX < worldSpawnX + 4; iX++) {
                        int iZZ = 0;
                        for (int iZ = worldSpawnZ - 4; iZ < worldSpawnZ + 4; iZ++) {
                            xzAxis[iXX][iZZ] = spawnLocation.getWorld().getBlockAt(iX, iY, iZ);
                            thisPlugin.prettyLog(Level.FINEST, false, "Y/YY: " + iY + "/" + iYY + " X/XX: " + iX + "/" + iXX + " Z/ZZ: " + iZ + "/" + iZZ + " Block: " + xzAxis[iXX][iZZ].toString());
                            iZZ++;
                        }
                        iXX++;
                    }
                    blockYaxisPlane.put(iYY, xzAxis);
                    iYY++;
                }
            }
            else if (worldSpawnY <= 4) {
                int iYY = 0;
                for (int iY = worldSpawnY; iY < worldSpawnY + 8; iY++) {
                    final Block[][] xzAxis = new Block[9][9];
                    int iXX = 0;
                    for (int iX = worldSpawnX - 4; iX < worldSpawnX + 4; iX++) {
                        int iZZ = 0;
                        for (int iZ = worldSpawnZ - 4; iZ < worldSpawnZ + 4; iZ++) {
                            xzAxis[iXX][iZZ] = spawnLocation.getWorld().getBlockAt(iX, iY, iZ);
                            thisPlugin.prettyLog(Level.FINEST, false, "Y/YY: " + iY + "/" + iYY + " X/XX: " + iX + "/" + iXX + " Z/ZZ: " + iZ + "/" + iZZ + " Block: " + xzAxis[iXX][iZZ].toString());
                            iZZ++;
                        }
                        iXX++;
                    }
                    blockYaxisPlane.put(iYY, xzAxis);
                    iYY++;
                }
            }
            else if (worldSpawnY >= 123) {
                int iYY = 0;
                for (int iY = worldSpawnY - 8; iY < worldSpawnY; iY++) {
                    final Block[][] xzAxis = new Block[9][9];
                    int iXX = 0;
                    for (int iX = worldSpawnX - 4; iX < worldSpawnX + 4; iX++) {
                        int iZZ = 0;
                        for (int iZ = worldSpawnZ - 4; iZ < worldSpawnZ + 4; iZ++) {
                            xzAxis[iXX][iZZ] = spawnLocation.getWorld().getBlockAt(iX, iY, iZ);
                            thisPlugin.prettyLog(Level.FINEST, false, "Y/YY: " + iY + "/" + iYY + " X/XX: " + iX + "/" + iXX + " Z/ZZ: " + iZ + "/" + iZZ + " Block: " + xzAxis[iXX][iZZ].toString());
                            iZZ++;
                        }
                        iXX++;
                    }
                    blockYaxisPlane.put(iYY, xzAxis);
                    iYY++;
                }
            }
            for (int y = 0; y < 8; y++) {
                final Block[][] tmpBlockArr = blockYaxisPlane.get(y);
                for (int x = 0; x < 8; x++) {
                    for (int z = 0; z < 8; z++) {
                        final Block tmpBlock = tmpBlockArr[x][z];
                        if (tmpBlock != null) {
                            final int typeId = tmpBlock.getTypeId();
                            if (checkSafeTypeId(typeId) && checkSafeBlockAbove(tmpBlock) && checkSafeBlockBelow(tmpBlock)) {
                                return tmpBlock.getLocation();
                            }
                        }
                    }
                }
            }
            return spawnLocation;
        }
        thisPlugin.prettyLog(Level.WARNING, false, "Returned null value for safe spawn!");
        return null;
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
