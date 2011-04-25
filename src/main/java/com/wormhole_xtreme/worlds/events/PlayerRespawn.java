/**
 *
 */
package com.wormhole_xtreme.worlds.events;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * @author alron
 *
 */
public class PlayerRespawn extends PlayerListener {

    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (ConfigManager.getServerOptionSpawnCommand()) {
            final Player player = event.getPlayer();
            final String worldName = player.getWorld().getName();
            if (WorldManager.isWormholeWorld(worldName)) {
                final WormholeWorld wormholeWorld = WorldManager.getWorld(worldName);
                event.setRespawnLocation(WorldManager.getSafeSpawnLocation(wormholeWorld, player));
                thisPlugin.prettyLog(Level.FINE, false, "Respawned Player  " + event.getPlayer().getName() + " on " + wormholeWorld.getWorldName());
            }
        }
    }
}
