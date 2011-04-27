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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Flying;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.WaterMob;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager;
import com.wormhole_xtreme.worlds.config.ConfigManager.WorldOptionKeys;
import com.wormhole_xtreme.worlds.config.XMLConfig;

/**
 * The Class WorldManager.
 * 
 * @author alron
 */
public class WorldManager {

    /** The world list. */
    private final static ConcurrentHashMap<String, WormholeWorld> worldList = new ConcurrentHashMap<String, WormholeWorld>();

    /** The Constant thisPlugin. */
    private final static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /**
     * Adds the world.
     * 
     * @param wormholeWorld
     *            the world
     * @return true, if successful
     */
    public static boolean addWorld(final WormholeWorld wormholeWorld) {
        if (wormholeWorld != null) {
            try {
                worldList.put(wormholeWorld.getWorldName(), wormholeWorld);
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
        thisPlugin.prettyLog(Level.FINE, false, "Did not find blockId to be unsafe material:" + typeId);
        return true;
    }

    /**
     * Check safe block.
     * 
     * @param safeBlock
     *            the safe block
     * @return true, if successful
     */
    private static boolean checkSafeBlock(final Block safeBlock) {
        if (safeBlock != null) {
            final int typeId = safeBlock.getTypeId();
            return checkSafeTypeId(typeId);
        }
        return false;
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
     * Check safe teleport destination.
     * 
     * @param destination
     *            the destination
     * @return true, if successful
     */
    private static boolean checkSafeTeleportDestination(final Location destination) {
        if (destination != null) {
            final Block safeBlock = destination.getBlock();
            if (checkSafeBlock(safeBlock) && checkSafeBlockAbove(safeBlock) && checkSafeBlockBelow(safeBlock)) {
                return true;
            }
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
     * Check timelock worlds.
     */
    public static void checkTimelockWorlds() {
        if (ConfigManager.getServerOptionTimelock()) {
            for (final String key : worldList.keySet()) {
                if (key != null) {
                    final WormholeWorld wormholeWorld = worldList.get(key);
                    if ((wormholeWorld != null) && wormholeWorld.isWorldLoaded() && wormholeWorld.isTimeLock()) {
                        final long worldRelativeTime = wormholeWorld.getThisWorld().getTime() % 24000;
                        if ((worldRelativeTime > 11800) && (wormholeWorld.getTimeLockType() == TimeLockType.DAY)) {
                            thisPlugin.prettyLog(Level.FINE, false, "Set world: " + key + " New time: 0" + " Old Time: " + worldRelativeTime);
                            wormholeWorld.getThisWorld().setTime(0);
                        }
                        else if (((worldRelativeTime < 13500) || (worldRelativeTime > 21800)) && (wormholeWorld.getTimeLockType() == TimeLockType.NIGHT)) {
                            thisPlugin.prettyLog(Level.FINE, false, "Set world: " + key + " New time: 13700" + " Old Time: " + worldRelativeTime);
                            wormholeWorld.getThisWorld().setTime(13700);
                        }
                    }
                }
            }
        }
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
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean createWorld(final WormholeWorld wormholeWorld) {
        if (wormholeWorld != null) {
            wormholeWorld.setWorldLoaded(true);
            final String worldName = wormholeWorld.getWorldName();
            final Environment worldEnvironment = wormholeWorld.isNetherWorld() ? Environment.NETHER
                : Environment.NORMAL;

            if (thisPlugin.getServer().getWorld(worldName) == null) {
                if (wormholeWorld.getWorldSeed() != 0) {
                    wormholeWorld.setThisWorld(thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), worldEnvironment, wormholeWorld.getWorldSeed()));
                }
                else {
                    wormholeWorld.setThisWorld(thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), worldEnvironment));
                    wormholeWorld.setWorldSeed(wormholeWorld.getThisWorld().getSeed());
                }
                wormholeWorld.getThisWorld().save();
            }
            else {
                wormholeWorld.setThisWorld(thisPlugin.getServer().getWorld(worldName));
                wormholeWorld.setWorldSeed(wormholeWorld.getThisWorld().getSeed());
            }

            wormholeWorld.setWorldSpawn(wormholeWorld.isNetherWorld()
                ? findSafeSpawn(wormholeWorld.getThisWorld().getSpawnLocation(), 13, 13)
                : wormholeWorld.getThisWorld().getSpawnLocation());

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
            clearWorldCreatures(wormholeWorld);
            setWorldWeather(wormholeWorld);
            return addWorld(wormholeWorld);
        }
        return false;
    }

    /**
     * Creates the world.
     * 
     * @param ownerName
     *            the player name
     * @param worldName
     *            the world name
     * @param worldOptionKeys
     *            the world option keys
     * @param worldSeed
     *            the world seed
     * @return true, if successful
     */
    public static boolean createWormholeWorld(final String ownerName, final String worldName, final WorldOptionKeys[] worldOptionKeys, final long worldSeed) {
        if ((worldName != null) && (getWorld(worldName) == null) && (ownerName != null)) {
            final WormholeWorld wormholeWorld = new WormholeWorld();
            wormholeWorld.setWorldName(worldName);
            wormholeWorld.setWorldOwner(ownerName);
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
                        case worldOptionNoPvP :
                            wormholeWorld.setAllowPvP(false);
                            break;
                        case worldOptionNoConnect :
                            wormholeWorld.setAutoconnectWorld(false);
                            break;
                        case worldOptionSeed :
                            wormholeWorld.setWorldSeed(worldSeed);
                            break;
                        case worldOptionTimeLockDay :
                            wormholeWorld.setTimeLockType(TimeLockType.DAY);
                            break;
                        case worldOptionTimeLockNight :
                            wormholeWorld.setTimeLockType(TimeLockType.NIGHT);
                            break;
                        case worldOptionWeatherClear :
                            wormholeWorld.setWeatherLockType(WeatherLockType.CLEAR);
                            break;
                        case worldOptionWeatherRain :
                            wormholeWorld.setWeatherLockType(WeatherLockType.RAIN);
                            break;
                        case worldOptionWeatherStorm :
                            wormholeWorld.setWeatherLockType(WeatherLockType.STORM);
                            break;
                        case worldOptionNoLavaSpread :
                            wormholeWorld.setAllowLavaSpread(false);
                            break;
                        case worldOptionNoPlayerDrown :
                            wormholeWorld.setAllowPlayerDrown(false);
                            break;
                        case worldOptionNoFireSpread :
                            wormholeWorld.setAllowFireSpread(false);
                            break;
                        case worldOptionNoLavaFire :
                            wormholeWorld.setAllowLavaFire(false);
                            break;
                        case worldOptionNoWaterSpread :
                            wormholeWorld.setAllowWaterSpread(false);
                            break;
                        case worldOptionNoLightningFire :
                            wormholeWorld.setAllowLightningFire(false);
                            break;
                        case worldOptionNoPlayerLightningDamage :
                            wormholeWorld.setAllowPlayerLightningDamage(false);
                            break;
                        case worldOptionNoPlayerDamage :
                            wormholeWorld.setAllowPlayerDamage(false);
                            break;
                        case worldOptionNoPlayerLavaDamage :
                            wormholeWorld.setAllowPlayerLavaDamage(false);
                            break;
                        case worldOptionNoPlayerFallDamage :
                            wormholeWorld.setAllowPlayerFallDamage(false);
                            break;
                        case worldOptionNoPlayerFireDamage :
                            wormholeWorld.setAllowPlayerFireDamage(false);
                            break;
                        default :
                            break;
                    }
                }
            }
            return createWorld(wormholeWorld);
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
    private static Location findSafeSpawn(final Location spawnLocation, final int yDepth, final int xzDepth) {
        if (spawnLocation != null) {
            final int worldSpawnY = spawnLocation.getBlockY();
            final int worldSpawnX = spawnLocation.getBlockX();
            final int worldSpawnZ = spawnLocation.getBlockZ();
            final World world = spawnLocation.getWorld();
            final int xzDepthOdd = oddNumberMaker(xzDepth);
            final int yDepthOdd = oddNumberMaker(yDepth);
            Location safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX, worldSpawnZ, yDepthOdd, xzDepthOdd);
            if (safeSpawn != null) {
                thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass 0/0.");
                return safeSpawn;
            }
            else {
                safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX + xzDepthOdd, worldSpawnZ, yDepthOdd, xzDepthOdd);
                if (safeSpawn != null) {
                    thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass +" + xzDepthOdd + "/0.");
                    return safeSpawn;
                }
                else {
                    safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX + xzDepthOdd, worldSpawnZ + xzDepthOdd, yDepthOdd, xzDepthOdd);
                    if (safeSpawn != null) {
                        thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass +" + xzDepthOdd + "/+" + xzDepthOdd + ".");
                        return safeSpawn;
                    }
                    else {
                        safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX, worldSpawnZ + xzDepthOdd, yDepthOdd, xzDepthOdd);
                        if (safeSpawn != null) {
                            thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass 0/+" + xzDepthOdd + ".");
                            return safeSpawn;
                        }
                        else {
                            safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX - xzDepthOdd, worldSpawnZ + xzDepthOdd, yDepthOdd, xzDepthOdd);
                            if (safeSpawn != null) {
                                thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass -" + xzDepthOdd + "/+" + xzDepthOdd + ".");
                                return safeSpawn;
                            }
                            else {
                                safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX - xzDepthOdd, worldSpawnZ, yDepthOdd, xzDepthOdd);
                                if (safeSpawn != null) {
                                    thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass -" + xzDepthOdd + "/0.");
                                    return safeSpawn;
                                }
                                else {
                                    safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX - xzDepthOdd, worldSpawnZ - xzDepthOdd, yDepthOdd, xzDepthOdd);
                                    if (safeSpawn != null) {
                                        thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass -" + xzDepthOdd + "/-" + xzDepthOdd + ".");
                                        return safeSpawn;
                                    }
                                    else {
                                        safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX, worldSpawnZ - xzDepthOdd, yDepthOdd, xzDepthOdd);
                                        if (safeSpawn != null) {
                                            thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass 0/-" + xzDepthOdd + ".");
                                            return safeSpawn;
                                        }
                                        else {
                                            safeSpawn = findSafeSpawnFromYXZ(world, worldSpawnY, worldSpawnX + xzDepthOdd, worldSpawnZ - xzDepthOdd, yDepthOdd, xzDepthOdd);
                                            if (safeSpawn != null) {
                                                thisPlugin.prettyLog(Level.FINE, false, "Found safe spawn in pass +" + xzDepthOdd + "/-" + xzDepthOdd + ".");
                                                return safeSpawn;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            thisPlugin.prettyLog(Level.FINE, false, ((xzDepthOdd * xzDepthOdd) * yDepthOdd) * 9 + " blocks later. No safe spawn in sight.");
            return spawnLocation;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Returned null value for safe spawn!");
        return null;
    }

    /**
     * Find safe spawn from yxz.
     * 
     * @param world
     *            the world
     * @param worldSpawnY
     *            the world spawn y
     * @param worldSpawnX
     *            the world spawn x
     * @param worldSpawnZ
     *            the world spawn z
     * @param ySize
     *            the yAxis depth
     * @param xzSize
     *            the xzAxis depth
     * @return the location
     */
    private static Location findSafeSpawnFromYXZ(final World world, final int worldSpawnY, final int worldSpawnX, final int worldSpawnZ, final int ySize, final int xzSize) {
        final int yAxisDepth = oddNumberMaker(ySize);
        final int yAxisSmall = ((yAxisDepth - 1) / 2);
        final int yAxisLarge = yAxisSmall + 1;
        final int xzAxisDepth = oddNumberMaker(xzSize);
        final int xzAxisSmall = ((xzAxisDepth - 1) / 2);
        final int xzAxisLarge = xzAxisSmall + 1;
        final HashMap<Integer, Block[][]> blockYaxisPlane = new HashMap<Integer, Block[][]>(yAxisDepth * 2);
        int iYY = 0;
        if ((worldSpawnY <= 127 - yAxisSmall) && (worldSpawnY >= yAxisLarge)) {
            for (int iY = worldSpawnY - yAxisSmall; iY < worldSpawnY + yAxisLarge; iY++) {
                blockYaxisPlane.put(iYY, populateYaxisPlane(world, worldSpawnX, worldSpawnZ, iY, iYY, xzAxisDepth, xzAxisSmall, xzAxisLarge));
                iYY++;
            }
        }
        else if (worldSpawnY <= yAxisSmall) {
            for (int iY = worldSpawnY; iY < worldSpawnY + yAxisDepth; iY++) {
                blockYaxisPlane.put(iYY, populateYaxisPlane(world, worldSpawnX, worldSpawnZ, iY, iYY, xzAxisDepth, xzAxisSmall, xzAxisLarge));
                iYY++;
            }
        }
        else if (worldSpawnY >= 127 - yAxisLarge) {
            for (int iY = worldSpawnY - yAxisDepth; iY < worldSpawnY; iY++) {
                blockYaxisPlane.put(iYY, populateYaxisPlane(world, worldSpawnX, worldSpawnZ, iY, iYY, xzAxisDepth, xzAxisSmall, xzAxisLarge));
                iYY++;
            }
        }
        for (int y = 0; y < yAxisDepth; y++) {
            final Block[][] tmpBlockArr = blockYaxisPlane.get(y);
            for (int x = 0; x < xzAxisDepth; x++) {
                for (int z = 0; z < xzAxisDepth; z++) {
                    final Block tmpBlock = tmpBlockArr[x][z];
                    if (tmpBlock != null) {
                        final int typeId = tmpBlock.getTypeId();
                        if (checkSafeTypeId(typeId) && checkSafeBlockAbove(tmpBlock) && checkSafeBlockBelow(tmpBlock)) {
                            blockYaxisPlane.clear();
                            return new Location(tmpBlock.getWorld(), tmpBlock.getX() + 0.5, tmpBlock.getY(), tmpBlock.getZ() + 0.5);
                        }
                    }
                }
            }
        }
        blockYaxisPlane.clear();
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
     * Gets the safe spawn location.
     * 
     * @param wormholeWorld
     *            the wormhole world
     * @param player
     *            the player
     * @return the safe teleport location
     */
    public static Location getSafeSpawnLocation(final WormholeWorld wormholeWorld, final Player player) {
        return wormholeWorld.isNetherWorld()
            ? WorldManager.checkSafeTeleportDestination(wormholeWorld.getWorldSpawn())
                ? new Location(wormholeWorld.getThisWorld(), wormholeWorld.getWorldSpawn().getBlockX() + 0.5, wormholeWorld.getWorldSpawn().getBlockY(), wormholeWorld.getWorldSpawn().getBlockZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch())
                : WorldManager.findSafeSpawn(wormholeWorld.getWorldSpawn(), 3, 3)
            : WorldManager.checkSafeTeleportDestination(wormholeWorld.getWorldSpawn())
                ? new Location(wormholeWorld.getThisWorld(), wormholeWorld.getWorldSpawn().getBlockX() + 0.5, wormholeWorld.getWorldSpawn().getBlockY(), wormholeWorld.getWorldSpawn().getBlockZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch())
                : new Location(wormholeWorld.getThisWorld(), wormholeWorld.getWorldSpawn().getBlockX() + 0.5, wormholeWorld.getThisWorld().getHighestBlockYAt(wormholeWorld.getWorldSpawn()), wormholeWorld.getWorldSpawn().getBlockZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
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
     * Gets the world from player.
     * 
     * @param player
     *            the player
     * @return the world from player
     */
    public static WormholeWorld getWorldFromPlayer(final Player player) {
        if (player != null) {
            return getWorld(player.getWorld().getName());
        }
        else {
            return null;
        }
    }

    /**
     * Checks if is wormhole world.
     * 
     * @param worldName
     *            the world name
     * @return true, if is wormhole world
     */
    public static boolean isWormholeWorld(final String worldName) {
        if ((worldName != null) && worldList.containsKey(worldName)) {
            return true;
        }
        return false;
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
            wormholeWorld.setWorldLoaded(true);
            wormholeWorld.setThisWorld(wormholeWorld.getWorldSeed() == 0
                ? thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), wormholeWorld.isNetherWorld()
                    ? Environment.NETHER : Environment.NORMAL)
                : thisPlugin.getServer().createWorld(wormholeWorld.getWorldName(), wormholeWorld.isNetherWorld()
                    ? Environment.NETHER : Environment.NORMAL, wormholeWorld.getWorldSeed()));
            wormholeWorld.getThisWorld().setSpawnLocation(wormholeWorld.getWorldCustomSpawn()[0], wormholeWorld.getWorldCustomSpawn()[1], wormholeWorld.getWorldCustomSpawn()[2]);
            wormholeWorld.setWorldSpawn(wormholeWorld.getThisWorld().getSpawnLocation());
            if (wormholeWorld.getWorldSeed() == 0) {
                wormholeWorld.setWorldSeed(wormholeWorld.getThisWorld().getSeed());
            }
            if (addWorld(wormholeWorld)) {
                final int c = clearWorldCreatures(wormholeWorld);
                if (c > 0) {
                    thisPlugin.prettyLog(Level.INFO, false, "Cleared \"" + c + "\" creature entities on world \"" + wormholeWorld.getWorldName() + "\"");
                }
                setWorldWeather(wormholeWorld);
                return true;
            }
        }
        return false;
    }

    /**
     * Odd number maker. Takes numbers 3 or larger and makes sure they are odd.
     * 
     * @param number
     *            the number
     * @return the odd int
     */
    private static int oddNumberMaker(final int number) {
        return ((number % 2 == 0) && (number > 3)) ? number - 1 : (number > 2) ? number : 3;
    }

    /**
     * Populate yaxis plane.
     * 
     * @param world
     *            the world
     * @param worldSpawnX
     *            the world spawn x
     * @param worldSpawnZ
     *            the world spawn z
     * @param iY
     *            the i y
     * @param iYY
     *            the i yy
     * @param xzAxisDepth
     *            the xzAxis depth
     * @param xzAxisSmall
     *            the xzAxis small half depth
     * @param xzAxisLarge
     *            the xzAxis large half depth
     * @return the block[][]
     */
    private static Block[][] populateYaxisPlane(final World world, final int worldSpawnX, final int worldSpawnZ, final int iY, final int iYY, final int xzAxisDepth, final int xzAxisSmall, final int xzAxisLarge) {
        final Block[][] xzAxis = new Block[xzAxisDepth][xzAxisDepth];
        int iXX = 0;
        for (int iX = worldSpawnX - xzAxisSmall; iX < worldSpawnX + xzAxisLarge; iX++) {
            int iZZ = 0;
            for (int iZ = worldSpawnZ - xzAxisSmall; iZ < worldSpawnZ + xzAxisLarge; iZ++) {
                xzAxis[iXX][iZZ] = world.getBlockAt(iX, iY, iZ);
                thisPlugin.prettyLog(Level.FINEST, false, "Y/YY: " + iY + "/" + iYY + " X/XX: " + iX + "/" + iXX + " Z/ZZ: " + iZ + "/" + iZZ + " Block: " + xzAxis[iXX][iZZ].toString());
                iZZ++;
            }
            iXX++;
        }
        return xzAxis;
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
     * Sets the world weather.
     * 
     * @param wormholeWorld
     *            the new world weather
     */
    public static void setWorldWeather(final WormholeWorld wormholeWorld) {
        if (wormholeWorld.isWeatherLock()) {
            final World world = wormholeWorld.getThisWorld();
            switch (wormholeWorld.getWeatherLockType()) {
                case CLEAR :
                    world.setStorm(false);
                    world.setThundering(false);
                    break;
                case RAIN :
                    world.setStorm(true);
                    world.setThundering(false);
                    break;
                case STORM :
                    world.setStorm(true);
                    world.setThundering(true);
                    break;
                default :
                    break;
            }
        }
    }
}
