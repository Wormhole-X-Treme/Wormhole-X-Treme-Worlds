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

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * The WormholeWorld instance. Everything that we know about a world can be found here.
 * What we know about the world is distrusted though, as we cannot be sure data fed to us
 * by users is reliable.
 * 
 * @author alron
 */
public class WormholeWorld {

    /**
     * Core world configuration options.
     */
    /** The world name. */
    private String worldName = "";
    /** The world owner. */
    private String worldOwner = null;
    /** The this world. */
    private World thisWorld = null;
    /** The world spawn. */
    private Location worldSpawn = null;
    /** The world custom spawn. */
    private int[] worldCustomSpawn = null;
    /** The autoconnect world. */
    private boolean autoconnectWorld = true;
    /** The world seed. */
    private long worldSeed = 0;

    /**
     * Player related world configuration options.
     */
    /** The allow player damage. */
    private boolean allowPlayerDamage = true;
    /** Allow Player versus Player. */
    private boolean allowPvP = true;
    /** The allow player contact damage. */
    private boolean allowPlayerContactDamage = true;
    /** The allow player suffocation. */
    private boolean allowPlayerSuffocation = true;
    /** The allow player fall damage. */
    private boolean allowPlayerFallDamage = true;
    /** Allow player fire damage. */
    private boolean allowPlayerFireDamage = true;
    /** The allow player lava damage. */
    private boolean allowPlayerLavaDamage = true;
    /** The allow player drown. */
    private boolean allowPlayerDrown = true;
    /** The allow player explosion damage. */
    private boolean allowPlayerExplosionDamage = true;
    /** The allow player void damage. */
    private boolean allowPlayerVoidDamage = true;
    /** Allow player lightning damage. */
    private boolean allowPlayerLightningDamage = true;
    /** Allow players to start fires. */
    private boolean allowPlayerFireStart = true;

    /*
     * World related configuration options.
     */
    /** The allow fire spread. */
    private boolean allowFireSpread = true;
    /** The allow lava fire. */
    private boolean allowLavaFire = true;
    /** The allow lava spread. */
    private boolean allowLavaSpread = true;
    /** The allow water spread. */
    private boolean allowWaterSpread = true;
    /** Allow lightning fire. */
    private boolean allowLightningFire = true;
    /** The nether world. */
    private boolean netherWorld = false;
    /** The allow hostiles. */
    private boolean allowHostiles = true;
    /** The allow neutrals. */
    private boolean allowNeutrals = true;
    /** The weather lock. */
    private boolean weatherLock = false;
    /** The weather lock type. */
    private WeatherLockType weatherLockType = WeatherLockType.NONE;
    /** The allow thunder. */
    private boolean allowWeatherThunder = true;
    /** The time lock. */
    private boolean timeLock = false;
    /** The time lock type. */
    private TimeLockType timeLockType = TimeLockType.NONE;

    /** The loaded. */
    private boolean loaded = false;

    /** The sticky chunks. */
    private final ConcurrentHashMap<Chunk, ArrayList<String>> stickyChunks = new ConcurrentHashMap<Chunk, ArrayList<String>>();

    /**
     * Instantiates a new world.
     */
    public WormholeWorld() {

    }

    /**
     * Adds the sticky chunk.
     * 
     * @param stickyChunk
     *            the sticky chunk
     * @param ownerPlugin
     *            the owner plugin
     * @return true, if successful
     */
    public boolean addStickyChunk(final Chunk stickyChunk, final String ownerPlugin) {
        if ((ownerPlugin != null) && (stickyChunk != null)) {
            ArrayList<String> pluginArrayList = new ArrayList<String>();
            if (stickyChunks.containsKey(stickyChunk)) {
                pluginArrayList = stickyChunks.get(stickyChunk);
                if ((pluginArrayList != null) && !pluginArrayList.contains(ownerPlugin)) {
                    pluginArrayList.add(ownerPlugin);
                    stickyChunks.put(stickyChunk, pluginArrayList);
                }
                else if (pluginArrayList == null) {
                    pluginArrayList = new ArrayList<String>();
                    pluginArrayList.add(ownerPlugin);
                    stickyChunks.put(stickyChunk, pluginArrayList);
                }
            }
            else {
                pluginArrayList.add(ownerPlugin);
                stickyChunks.put(stickyChunk, pluginArrayList);
            }
            return true;
        }
        return false;
    }

    /**
     * Gets the all sticky chunks.
     * 
     * @return the all sticky chunks
     */
    public Set<Chunk> getAllStickyChunks() {
        return stickyChunks.keySet();
    }

    /**
     * Gets the this world.
     * 
     * @return the thisWorld
     */
    public World getThisWorld() {
        return thisWorld;
    }

    /**
     * Gets the time lock type.
     * 
     * @return the time lock type
     */
    public TimeLockType getTimeLockType() {
        return timeLockType;
    }

    /**
     * Gets the weather lock type.
     * 
     * @return the weather lock type
     */
    public WeatherLockType getWeatherLockType() {
        return weatherLockType;
    }

    /**
     * Gets the world custom spawn.
     * 
     * @return the world custom spawn
     */
    public int[] getWorldCustomSpawn() {
        return worldCustomSpawn.clone();
    }

    /**
     * Gets the world name.
     * 
     * @return the worldName
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * Gets the world owner.
     * 
     * @return the worldOwner
     */
    public String getWorldOwner() {
        return worldOwner;
    }

    /**
     * Gets the world seed.
     * 
     * @return the world seed
     */
    public long getWorldSeed() {
        return worldSeed;
    }

    /**
     * Gets the world custom spawn.
     * 
     * @return the worldCustomSpawn
     */
    public Location getWorldSpawn() {
        return worldSpawn;
    }

    /**
     * Gets the world spawn to int[].
     * 
     * @return the world spawn to int[]
     */
    public int[] getWorldSpawnToInt() {
        return new int[]{
            (int) worldSpawn.getX(), (int) worldSpawn.getY(), (int) worldSpawn.getZ()
        };
    }

    /**
     * Checks if is allow fire spread.
     * 
     * @return true, if is allow fire spread
     */
    public boolean isAllowFireSpread() {
        return allowFireSpread;
    }

    /**
     * Checks if is allow hostiles.
     * 
     * @return the allowHostiles
     */
    public boolean isAllowHostiles() {
        return allowHostiles;
    }

    /**
     * Checks if is allow lava fire.
     * 
     * @return true, if is allow lava fire
     */
    public boolean isAllowLavaFire() {
        return allowLavaFire;
    }

    /**
     * Checks if is allow lava spread.
     * 
     * @return true, if is allow lava spread
     */
    public boolean isAllowLavaSpread() {
        return allowLavaSpread;
    }

    /**
     * Checks if is allow lightning fire.
     * 
     * @return the allow lightning fire
     */
    public boolean isAllowLightningFire() {
        return allowLightningFire;
    }

    /**
     * Checks if is the allow neutrals.
     * 
     * @return the allowNeutrals
     */
    public boolean isAllowNeutrals() {
        return allowNeutrals;
    }

    /**
     * Checks if is allow player contact damage.
     * 
     * @return true, if is allow player contact damage
     */
    public boolean isAllowPlayerContactDamage() {
        return allowPlayerContactDamage;
    }

    /**
     * Checks if is allow player damage.
     * 
     * @return true, if is allow player damage
     */
    public boolean isAllowPlayerDamage() {
        return allowPlayerDamage;
    }

    /**
     * Checks if is allow player drown.
     * 
     * @return true, if is allow player drown
     */
    public boolean isAllowPlayerDrown() {
        return allowPlayerDrown;
    }

    /**
     * Checks if is allow player explosion damage.
     * 
     * @return true, if is allow player explosion damage
     */
    public boolean isAllowPlayerExplosionDamage() {
        return allowPlayerExplosionDamage;
    }

    /**
     * Checks if is allow player fall damage.
     * 
     * @return true, if is allow player fall damage
     */
    public boolean isAllowPlayerFallDamage() {
        return allowPlayerFallDamage;
    }

    /**
     * Checks if is allow player fire damage.
     * 
     * @return the allow player fire damage
     */
    public boolean isAllowPlayerFireDamage() {
        return allowPlayerFireDamage;
    }

    /**
     * Checks if is allow players to start fires.
     * 
     * @return the allow players to start fires
     */
    public boolean isAllowPlayerFireStart() {
        return allowPlayerFireStart;
    }

    /**
     * Checks if is allow player lava damage.
     * 
     * @return true, if is allow player lava damage
     */
    public boolean isAllowPlayerLavaDamage() {
        return allowPlayerLavaDamage;
    }

    /**
     * Checks if is allow player lightning damage.
     * 
     * @return the allow player lightning damage
     */
    public boolean isAllowPlayerLightningDamage() {
        return allowPlayerLightningDamage;
    }

    /**
     * Checks if is allow player suffocation.
     * 
     * @return true, if is allow player suffocation
     */
    public boolean isAllowPlayerSuffocation() {
        return allowPlayerSuffocation;
    }

    /**
     * Checks if is allow player void damage.
     * 
     * @return true, if is allow player void damage
     */
    public boolean isAllowPlayerVoidDamage() {
        return allowPlayerVoidDamage;
    }

    /**
     * Checks if PvP is allowed on this world.
     * 
     * @return true, if PvP is allowed.
     */
    public boolean isAllowPvP() {
        return allowPvP;
    }

    /**
     * Checks if is allow water spread.
     * 
     * @return true, if is allow water spread
     */
    public boolean isAllowWaterSpread() {
        return allowWaterSpread;
    }

    /**
     * Checks if is allow thunder.
     * 
     * @return true, if is allow thunder
     */
    public boolean isAllowWeatherThunder() {
        return allowWeatherThunder;
    }

    /**
     * Checks if is autoconnect world.
     * 
     * @return true, if is autoconnect world
     */
    public boolean isAutoconnectWorld() {
        return autoconnectWorld;
    }

    /**
     * Checks if is nether world.
     * 
     * @return true, if is nether world
     */
    public boolean isNetherWorld() {
        return netherWorld;
    }

    /**
     * Checks if is sticky chunk.
     * 
     * @param stickyChunk
     *            the sticky chunk
     * @return true, if is sticky chunk
     */
    public boolean isStickyChunk(final Chunk stickyChunk) {
        return stickyChunks.containsKey(stickyChunk);
    }

    /**
     * Checks if is time lock.
     * 
     * @return true, if is time lock
     */
    public boolean isTimeLock() {
        return timeLock;
    }

    /**
     * Checks if is weather lock.
     * 
     * @return true, if is weather lock
     */
    public boolean isWeatherLock() {
        return weatherLock;
    }

    /**
     * Checks if is world loaded.
     * 
     * @return true, if is world loaded
     */
    public boolean isWorldLoaded() {
        return loaded;
    }

    /**
     * Removes the sticky chunk.
     * 
     * @param stickyChunk
     *            the sticky chunk
     * @param ownerPlugin
     *            the owner plugin
     * @return true, if successful
     */
    public boolean removeStickyChunk(final Chunk stickyChunk, final String ownerPlugin) {
        return removeStickyChunk(stickyChunk, ownerPlugin, false);
    }

    /**
     * Removes the sticky chunk.
     * 
     * @param stickyChunk
     *            the sticky chunk
     * @param ownerPlugin
     *            the owner plugin
     * @param force
     *            the force
     * @return true, if successful
     */
    private boolean removeStickyChunk(final Chunk stickyChunk, final String ownerPlugin, final boolean force) {
        if ((stickyChunk != null) && stickyChunks.containsKey(stickyChunk)) {
            final ArrayList<String> pluginArrayList = stickyChunks.get(stickyChunk);
            if (force) {
                stickyChunks.remove(stickyChunk);
                return true;
            }
            else if ((ownerPlugin != null) && (pluginArrayList != null) && pluginArrayList.contains(ownerPlugin)) {
                if (pluginArrayList.size() == 1) {
                    stickyChunks.remove(stickyChunk);
                }
                else {
                    pluginArrayList.remove(ownerPlugin);
                    stickyChunks.put(stickyChunk, pluginArrayList);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the allow fire spread.
     * 
     * @param allowFireSpread
     *            the new allow fire spread
     */
    public void setAllowFireSpread(final boolean allowFireSpread) {
        this.allowFireSpread = allowFireSpread;
    }

    /**
     * Sets the allow hostiles.
     * 
     * @param allowHostiles
     *            the allowHostiles to set
     */
    public void setAllowHostiles(final boolean allowHostiles) {
        this.allowHostiles = allowHostiles;
    }

    /**
     * Sets the allow lava fire.
     * 
     * @param allowLavaFire
     *            the new allow lava fire
     */
    public void setAllowLavaFire(final boolean allowLavaFire) {
        this.allowLavaFire = allowLavaFire;
    }

    /**
     * Sets the allow lava spread.
     * 
     * @param allowLavaSpread
     *            the new allow lava spread
     */
    public void setAllowLavaSpread(final boolean allowLavaSpread) {
        this.allowLavaSpread = allowLavaSpread;
    }

    /**
     * Sets the allow lightning fire.
     * 
     * @param allowLightningFire
     *            the new allow lightning fire
     */
    public void setAllowLightningFire(final boolean allowLightningFire) {
        this.allowLightningFire = allowLightningFire;
    }

    /**
     * Sets the allow neutrals.
     * 
     * @param allowNeutrals
     *            the allowNeutrals to set
     */
    public void setAllowNeutrals(final boolean allowNeutrals) {
        this.allowNeutrals = allowNeutrals;
    }

    /**
     * Sets the allow player contact damage.
     * 
     * @param allowPlayerContactDamage
     *            the new allow player contact damage
     */
    public void setAllowPlayerContactDamage(final boolean allowPlayerContactDamage) {
        this.allowPlayerContactDamage = allowPlayerContactDamage;
    }

    /**
     * Sets the allow player damage.
     * 
     * @param allowPlayerDamage
     *            the new allow player damage
     */
    public void setAllowPlayerDamage(final boolean allowPlayerDamage) {
        this.allowPlayerDamage = allowPlayerDamage;
    }

    /**
     * Sets the allow player drown.
     * 
     * @param allowPlayerDrown
     *            the new allow player drown
     */
    public void setAllowPlayerDrown(final boolean allowPlayerDrown) {
        this.allowPlayerDrown = allowPlayerDrown;
    }

    /**
     * Sets the allow player explosion damage.
     * 
     * @param allowPlayerExplosionDamage
     *            the new allow player explosion damage
     */
    public void setAllowPlayerExplosionDamage(final boolean allowPlayerExplosionDamage) {
        this.allowPlayerExplosionDamage = allowPlayerExplosionDamage;
    }

    /**
     * Sets the allow player fall damage.
     * 
     * @param allowPlayerFallDamage
     *            the new allow player fall damage
     */
    public void setAllowPlayerFallDamage(final boolean allowPlayerFallDamage) {
        this.allowPlayerFallDamage = allowPlayerFallDamage;
    }

    /**
     * Sets the allow player fire damage.
     * 
     * @param allowPlayerFireDamage
     *            the new allow player fire damage
     */
    public void setAllowPlayerFireDamage(final boolean allowPlayerFireDamage) {
        this.allowPlayerFireDamage = allowPlayerFireDamage;
    }

    /**
     * Sets the allow players to start fires.
     * 
     * @param allowPlayerFireStart
     *            the new allow players to start fires
     */
    public void setAllowPlayerFireStart(final boolean allowPlayerFireStart) {
        this.allowPlayerFireStart = allowPlayerFireStart;
    }

    /**
     * Sets the allow player lava damage.
     * 
     * @param allowPlayerLavaDamage
     *            the new allow player lava damage
     */
    public void setAllowPlayerLavaDamage(final boolean allowPlayerLavaDamage) {
        this.allowPlayerLavaDamage = allowPlayerLavaDamage;
    }

    /**
     * Sets the allow player lightning damage.
     * 
     * @param allowPlayerLightningDamage
     *            the new allow player lightning damage
     */
    public void setAllowPlayerLightningDamage(final boolean allowPlayerLightningDamage) {
        this.allowPlayerLightningDamage = allowPlayerLightningDamage;
    }

    /**
     * Sets the allow player suffocation.
     * 
     * @param allowPlayerSuffocation
     *            the new allow player suffocation
     */
    public void setAllowPlayerSuffocation(final boolean allowPlayerSuffocation) {
        this.allowPlayerSuffocation = allowPlayerSuffocation;
    }

    /**
     * Sets the allow player void damage.
     * 
     * @param allowPlayerVoidDamage
     *            the new allow player void damage
     */
    public void setAllowPlayerVoidDamage(final boolean allowPlayerVoidDamage) {
        this.allowPlayerVoidDamage = allowPlayerVoidDamage;
    }

    /**
     * Sets the allow pv p.
     * 
     * @param allowPvP
     *            the new allow pv p
     */
    public void setAllowPvP(final boolean allowPvP) {
        this.allowPvP = allowPvP;
    }

    /**
     * Sets the allow water spread.
     * 
     * @param allowWaterSpread
     *            the new allow water spread
     */
    public void setAllowWaterSpread(final boolean allowWaterSpread) {
        this.allowWaterSpread = allowWaterSpread;
    }

    /**
     * Sets the allow thunder.
     * 
     * @param allowThunder
     *            the new allow thunder
     */
    public void setAllowWeatherThunder(final boolean allowWeatherThunder) {
        this.allowWeatherThunder = allowWeatherThunder;
    }

    /**
     * Sets the autoconnect world.
     * 
     * @param autoconnectWorld
     *            the new autoconnect world
     */
    public void setAutoconnectWorld(final boolean autoconnectWorld) {
        this.autoconnectWorld = autoconnectWorld;
    }

    /**
     * Sets the nether world.
     * 
     * @param netherWorld
     *            the new nether world
     */
    public void setNetherWorld(final boolean netherWorld) {
        this.netherWorld = netherWorld;
    }

    /**
     * Sets the this world.
     * 
     * @param thisWorld
     *            the thisWorld to set
     */
    public void setThisWorld(final World thisWorld) {
        this.thisWorld = thisWorld;
    }

    /**
     * Sets the time lock type.
     * 
     * @param timeLockType
     *            the new time lock type
     */
    public void setTimeLockType(final TimeLockType timeLockType) {
        switch (timeLockType) {
            case DAY :
            case NIGHT :
                timeLock = true;
                this.timeLockType = timeLockType;
                break;
            case NONE :
                timeLock = false;
                this.timeLockType = timeLockType;
                break;
            default :
                break;
        }
    }

    /**
     * Sets the weather lock.
     * 
     * @param weatherLock
     *            the new weather lock
     */
    public void setWeatherLock(final boolean weatherLock) {
        this.weatherLock = weatherLock;
    }

    /**
     * Sets the weather lock type.
     * 
     * @param weatherLockType
     *            the new weather lock type
     */
    public void setWeatherLockType(final WeatherLockType weatherLockType) {
        switch (weatherLockType) {
            case CLEAR :
            case RAIN :
                weatherLock = true;
                allowWeatherThunder = false;
                this.weatherLockType = weatherLockType;
                break;
            case STORM :
                weatherLock = true;
                allowWeatherThunder = true;
                this.weatherLockType = weatherLockType;
                break;
            case NONE :
                weatherLock = false;
                allowWeatherThunder = true;
                this.weatherLockType = weatherLockType;
                break;
            default :
                break;
        }
    }

    /**
     * Sets the world custom spawn.
     * 
     * @param worldCustomSpawn
     *            the new world custom spawn
     */
    public void setWorldCustomSpawn(final int[] worldCustomSpawn) {
        this.worldCustomSpawn = worldCustomSpawn.clone();
    }

    /**
     * Sets the world loaded.
     * 
     * @param loaded
     *            the new world loaded
     */
    void setWorldLoaded(final boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Sets the world name.
     * 
     * @param worldName
     *            the worldName to set
     */
    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    /**
     * Sets the world owner.
     * 
     * @param worldOwner
     *            the worldOwner to set
     */
    public void setWorldOwner(final String worldOwner) {
        this.worldOwner = worldOwner;
    }

    /**
     * Sets the world seed.
     * 
     * @param worldSeed
     *            the new world seed
     */
    public void setWorldSeed(final long worldSeed) {
        this.worldSeed = worldSeed;
    }

    /**
     * Sets the world custom spawn.
     * 
     * @param worldSpawn
     *            the new world custom spawn
     */
    public void setWorldSpawn(final Location worldSpawn) {
        this.worldSpawn = worldSpawn;
    }
}
