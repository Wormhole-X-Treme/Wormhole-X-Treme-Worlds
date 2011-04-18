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

import java.util.logging.Level;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Monster;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The Class CreatureSpawn.
 * 
 * @author alron
 */
public class CreatureSpawn extends EntityListener {

    /** The this plugin. */
    private static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /* (non-Javadoc)
     * @see org.bukkit.event.entity.EntityListener#onCreatureSpawn(org.bukkit.event.entity.CreatureSpawnEvent)
     */
    @Override
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (!event.isCancelled()) {
            thisPlugin.prettyLog(Level.FINEST, false, "Caught creature spawn on world: " + event.getLocation().getWorld().getName() + " creature type: " + event.getCreatureType().toString());
            final WormholeWorld wormholeWorld = WorldManager.getWorld(event.getLocation().getWorld().getName());
            final Entity eventEntity = event.getEntity();
            if ((eventEntity != null) && (wormholeWorld != null)) {
                if ((!wormholeWorld.isAllowHostiles() && ((eventEntity instanceof Monster) || (eventEntity instanceof Flying))) || (!wormholeWorld.isAllowNeutrals() && ((eventEntity instanceof Animals) || (eventEntity instanceof WaterMob)))) {
                    event.setCancelled(true);
                    thisPlugin.prettyLog(Level.FINEST, false, "Denied hostile creature spawn on world: " + event.getLocation().getWorld().getName() + " creature type: " + event.getCreatureType().toString());
                }
            }
        }
    }
}
