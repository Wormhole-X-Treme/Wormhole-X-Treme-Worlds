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

import org.bukkit.Chunk;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The Class ChunkUnload.
 * 
 * @author alron
 */
public class ChunkUnload extends WorldListener {

    /** The Constant thisPlugin. */
    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /* (non-Javadoc)
     * @see org.bukkit.event.world.WorldListener#onChunkUnload(org.bukkit.event.world.ChunkUnloadEvent)
     */
    @Override
    public void onChunkUnload(final ChunkUnloadEvent event) {
        if ( !event.isCancelled()) {
            final String worldName = event.getWorld().getName();
            final Chunk stickyChunk = event.getChunk();
            thisPlugin.prettyLog(Level.FINEST, false, "Chunk Unload Caught: " + stickyChunk.toString() + " World: " + worldName);
            final WormholeWorld wormholeWorld = WorldManager.getWorld(worldName);
            if ((wormholeWorld != null) && wormholeWorld.isStickyChunk(stickyChunk)) {
                event.setCancelled(true);
                thisPlugin.prettyLog(Level.FINE, false, "Chunk Unload Cancelled: " + stickyChunk.toString() + " World: " + worldName);
            }
        }
    }
}
