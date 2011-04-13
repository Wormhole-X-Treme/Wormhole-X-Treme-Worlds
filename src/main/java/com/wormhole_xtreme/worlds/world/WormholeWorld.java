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
 * The Class World.
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

    /** The world custom spawn. */
    private Location worldCustomSpawn = null;

    /** The allow hostiles. */
    private boolean allowHostiles = true;

    /** The allow neutrals. */
    private boolean allowNeutrals = true;

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
    public Location getWorldCustomSpawn() {
        return worldCustomSpawn;
    }


    /**
     * Sets the world custom spawn.
     * 
     * @param worldCustomSpawn
     *            the worldCustomSpawn to set
     */
    public void setWorldCustomSpawn(final Location worldCustomSpawn) {
        this.worldCustomSpawn = worldCustomSpawn;
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


}
