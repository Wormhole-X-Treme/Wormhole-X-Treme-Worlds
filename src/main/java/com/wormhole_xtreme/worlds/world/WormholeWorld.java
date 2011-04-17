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

    /** The allow hostiles. */
    private boolean allowHostiles = true;

    /** The allow neutrals. */
    private boolean allowNeutrals = true;

    /** The nether world. */
    private boolean netherWorld = false;

    /** The autoconnect world. */
    private boolean autoconnectWorld = true;

    /** The world seed. */
    private long worldSeed = 0;

    /**
     * Instantiates a new world.
     */
    public WormholeWorld() {

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
     * Sets the world name.
     * 
     * @param worldName
     *            the worldName to set
     */
    public void setWorldName(final String worldName) {
        this.worldName = worldName;
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
     * Sets the world owner.
     * 
     * @param worldOwner
     *            the worldOwner to set
     */
    public void setWorldOwner(final String worldOwner) {
        this.worldOwner = worldOwner;
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
     * Sets the this world.
     * 
     * @param thisWorld
     *            the thisWorld to set
     */
    public void setThisWorld(final World thisWorld) {
        this.thisWorld = thisWorld;
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
        return new int[]{(int) worldSpawn.getX(),(int) worldSpawn.getY(),(int) worldSpawn.getZ()};
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


    /**
     * Checks if is allow hostiles.
     * 
     * @return the allowHostiles
     */
    public boolean isAllowHostiles() {
        return allowHostiles;
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
     * Checks if is the allow neutrals.
     * 
     * @return the allowNeutrals
     */
    public boolean isAllowNeutrals() {
        return allowNeutrals;
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
     * Sets the nether world.
     * 
     * @param netherWorld
     *            the new nether world
     */
    public void setNetherWorld(final boolean netherWorld) {
        this.netherWorld = netherWorld;
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
     * Sets the autoconnect world.
     * 
     * @param autoconnectWorld
     *            the new autoconnect world
     */
    public void setAutoconnectWorld(final boolean autoconnectWorld) {
        this.autoconnectWorld = autoconnectWorld;
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
     * Sets the world custom spawn.
     * 
     * @param worldCustomSpawn
     *            the new world custom spawn
     */
    public void setWorldCustomSpawn(final int[] worldCustomSpawn) {
        this.worldCustomSpawn = worldCustomSpawn.clone();
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
     * Sets the world seed.
     * 
     * @param worldSeed
     *            the new world seed
     */
    public void setWorldSeed(final long worldSeed) {
        this.worldSeed = worldSeed;
    }

    /**
     * Gets the world seed.
     * 
     * @return the world seed
     */
    public long getWorldSeed() {
        return worldSeed;
    }

}
