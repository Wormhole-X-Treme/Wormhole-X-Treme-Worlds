/**
 *
 */
package com.wormhole_xtreme.worlds.world;

import org.bukkit.Location;

/**
 * @author alron
 * 
 */
public class World {

    public String worldName = "";
    public String worldOwner = null;
    public World thisWorld = null;
    public Location worldCustomSpawn = null;
    public boolean allowHostiles = true;
    public boolean allowNeutrals = true;

    public World() {

    }

    /**
     * @return the worldName
     */
    public String getWorldName() {
        return worldName;
    }


    /**
     * @param worldName
     *            the worldName to set
     */
    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }


    /**
     * @return the worldOwner
     */
    public String getWorldOwner() {
        return worldOwner;
    }


    /**
     * @param worldOwner
     *            the worldOwner to set
     */
    public void setWorldOwner(final String worldOwner) {
        this.worldOwner = worldOwner;
    }


    /**
     * @return the thisWorld
     */
    public World getThisWorld() {
        return thisWorld;
    }


    /**
     * @param thisWorld
     *            the thisWorld to set
     */
    public void setThisWorld(final World thisWorld) {
        this.thisWorld = thisWorld;
    }


    /**
     * @return the worldCustomSpawn
     */
    public Location getWorldCustomSpawn() {
        return worldCustomSpawn;
    }


    /**
     * @param worldCustomSpawn
     *            the worldCustomSpawn to set
     */
    public void setWorldCustomSpawn(final Location worldCustomSpawn) {
        this.worldCustomSpawn = worldCustomSpawn;
    }


    /**
     * @return the allowHostiles
     */
    public boolean isAllowHostiles() {
        return allowHostiles;
    }


    /**
     * @param allowHostiles
     *            the allowHostiles to set
     */
    public void setAllowHostiles(final boolean allowHostiles) {
        this.allowHostiles = allowHostiles;
    }


    /**
     * @return the allowNeutrals
     */
    public boolean isAllowNeutrals() {
        return allowNeutrals;
    }


    /**
     * @param allowNeutrals
     *            the allowNeutrals to set
     */
    public void setAllowNeutrals(final boolean allowNeutrals) {
        this.allowNeutrals = allowNeutrals;
    }


}
