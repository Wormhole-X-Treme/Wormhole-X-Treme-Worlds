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
package com.wormhole_xtreme.worlds.events.block;

import java.util.logging.Level;

import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockListener;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The Class BlockIgnite.
 * 
 * @author alron
 */
public class BlockIgnite extends BlockListener {

    /** The Constant thisPlugin. */
    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /* (non-Javadoc)
     * @see org.bukkit.event.block.BlockListener#onBlockIgnite(org.bukkit.event.block.BlockIgniteEvent)
     */
    @Override
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if ( !event.isCancelled() && (event.getBlock() != null)) {
            final String worldName = event.getBlock().getWorld().getName();
            if (WorldManager.isWormholeWorld(worldName)) {
                final WormholeWorld wormholeWorld = WorldManager.getWorld(worldName);
                final IgniteCause igniteCause = event.getCause();
                if (igniteCause != null) {
                    if ( !wormholeWorld.isAllowLavaFire() && (igniteCause == IgniteCause.LAVA)) {
                        event.setCancelled(true);
                        thisPlugin.prettyLog(Level.FINE, false, "Cancelled Lava Fire Event on " + wormholeWorld.getWorldName());
                    }
                    else if ( !wormholeWorld.isAllowFireSpread() && (igniteCause == IgniteCause.SPREAD)) {
                        event.setCancelled(true);
                        thisPlugin.prettyLog(Level.FINE, false, "Cancelled Fire Spread Event on " + wormholeWorld.getWorldName());
                    }
                    else if ( !wormholeWorld.isAllowLightningFire() && (igniteCause == IgniteCause.LIGHTNING)) {
                        event.setCancelled(true);
                        thisPlugin.prettyLog(Level.FINE, false, "Cancelled Lightning Fire Event on " + wormholeWorld.getWorldName());
                    }
                }
            }
        }
    }
}
