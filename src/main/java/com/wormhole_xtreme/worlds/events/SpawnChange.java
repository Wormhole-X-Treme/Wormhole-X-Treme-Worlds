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
package com.wormhole_xtreme.worlds.events;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.event.world.WorldListener;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * @author alron
 * 
 */
public class SpawnChange extends WorldListener {

    private static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();
    
    /* (non-Javadoc)
     * @see org.bukkit.event.world.WorldListener#onSpawnChange(org.bukkit.event.world.SpawnChangeEvent)
     */
    @Override
    public void onSpawnChange(final SpawnChangeEvent event) {
        thisPlugin.prettyLog(Level.FINE, false, "Caught Spawn Change Event for world: " + event.getWorld().getName());
        final WormholeWorld wormholeWorld = WorldManager.getWorld(event.getWorld().getName());
        if ((wormholeWorld != null) && (wormholeWorld.getWorldSpawn() != null) && !wormholeWorld.getWorldSpawn().equals(event.getWorld().getSpawnLocation())) {
            wormholeWorld.setWorldSpawn(event.getWorld().getSpawnLocation());
            wormholeWorld.setWorldCustomSpawn(wormholeWorld.getWorldSpawnToInt());
            WorldManager.addWorld(wormholeWorld);
            thisPlugin.prettyLog(Level.FINE, false, "Set worldSpawn to new location:" + Arrays.toString(wormholeWorld.getWorldCustomSpawn())+ " for world: " + event.getWorld().getName());
        }
    }
}
