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

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;

/**
 * The Class EventUtilities.
 * 
 * @author alron
 */
public class EventUtilities {

    /** The Constant thisPlugin. */
    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /** The Constant pluginManager. */
    private static final PluginManager pluginManager = thisPlugin.getServer().getPluginManager();

    /** The Constant plugin. */
    private static final PluginEnable pluginEnable = new PluginEnable();

    /** The Constant pluginDisable. */
    private static final PluginDisable pluginDisable = new PluginDisable();

    /** The Constant world. */
    private static final WorldLoad worldLoad = new WorldLoad();

    /** The Constant worldSave. */
    private static final WorldSave worldSave = new WorldSave();

    /** The Constant spawnChange. */
    private static final SpawnChange spawnChange = new SpawnChange();

    /** The Constant creatureSpawn. */
    private static final CreatureSpawn creatureSpawn = new CreatureSpawn();

    /** The Constant entityDamage. */
    private static final EntityDamage entityDamage = new EntityDamage();

    /** The Constant chunkUnload. */
    private static final ChunkUnload chunkUnload = new ChunkUnload();

    /** The Constant blockFromTo. */
    private static final BlockFromTo blockFromTo = new BlockFromTo();

    /** The Constant blockIgnite. */
    private static final BlockIgnite blockIgnite = new BlockIgnite();
    
    private static final BlockBurn blockBurn = new BlockBurn();

    /**
     * Register events.
     */
    public static void registerEvents() {
        pluginManager.registerEvent(Event.Type.PLUGIN_ENABLE, pluginEnable, Priority.Monitor, thisPlugin);
        pluginManager.registerEvent(Event.Type.PLUGIN_DISABLE, pluginDisable, Priority.Monitor, thisPlugin);
        pluginManager.registerEvent(Event.Type.WORLD_LOAD, worldLoad, Priority.Monitor, thisPlugin);
        pluginManager.registerEvent(Event.Type.WORLD_SAVE, worldSave, Priority.Monitor, thisPlugin);
        pluginManager.registerEvent(Event.Type.SPAWN_CHANGE, spawnChange, Priority.Monitor, thisPlugin);
        pluginManager.registerEvent(Event.Type.CREATURE_SPAWN, creatureSpawn, Priority.Lowest, thisPlugin);
        pluginManager.registerEvent(Event.Type.ENTITY_DAMAGE, entityDamage, Priority.Lowest, thisPlugin);
        pluginManager.registerEvent(Event.Type.CHUNK_UNLOAD, chunkUnload, Priority.High, thisPlugin);
        pluginManager.registerEvent(Event.Type.BLOCK_FROMTO, blockFromTo, Priority.Lowest, thisPlugin);
        pluginManager.registerEvent(Event.Type.BLOCK_IGNITE, blockIgnite, Priority.Lowest, thisPlugin);
        pluginManager.registerEvent(Event.Type.BLOCK_BURN, blockBurn, Priority.Lowest, thisPlugin);
    }
}
