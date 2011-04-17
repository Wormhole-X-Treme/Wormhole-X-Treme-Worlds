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
package com.wormhole_xtreme.worlds.plugin;

import java.util.logging.Level;

import me.taylorkelly.help.Help;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager;
import com.wormhole_xtreme.worlds.permissions.PermissionType;

/**
 * The Class PluginUtilities.
 * 
 * @author alron
 */
public class HelpSupport {

    /** The Constant thisPlugin. */
    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /** The Constant pluginManager. */
    private static final PluginManager pluginManager = WormholeXTremeWorlds.getThisPlugin().getServer().getPluginManager();

    /**
     * Enable help.
     */
    public static void enableHelp() {
        if (ConfigManager.getServerOptionHelp()) {
            if (WormholeXTremeWorlds.getHelp() == null) {
                final Plugin helpTest = pluginManager.getPlugin("Help");
                if (helpTest != null) {
                    final String version = helpTest.getDescription().getVersion();
                    if (!version.startsWith("0.2")) {
                        thisPlugin.prettyLog(Level.WARNING, false, "Not a support version of Help: " + version + " Recommended is: 0.2.x");
                    }
                    try {
                        WormholeXTremeWorlds.setHelp((Help) helpTest);
                        thisPlugin.prettyLog(Level.INFO, false, "Attached to Help version: " + version);
                    }
                    catch (final ClassCastException e) {
                        thisPlugin.prettyLog(Level.WARNING, false, "Failed to get cast to Help: " + e.getMessage());
                    }
                }
                else {
                    thisPlugin.prettyLog(Level.INFO, false, "Help plugin is not yet available; there will be no Help integration until it is loaded.");
                }
            }
        }
        else {
            thisPlugin.prettyLog(Level.INFO, false, "Help Plugin support disabled via config.xml");
        }
    }

    /**
     * Disable help.
     */
    public static void disableHelp() {
        if (ConfigManager.getServerOptionHelp()) {
            if (WormholeXTremeWorlds.getHelp() != null) {
                WormholeXTremeWorlds.setHelp(null);
                thisPlugin.prettyLog(Level.INFO, false, "Detached from Help plugin.");
            }
        }
    }

    /**
     * Register help commands.
     */
    public static void registerHelpCommands() {
        if (WormholeXTremeWorlds.getHelp() != null && ConfigManager.getServerOptionHelp()) {
            WormholeXTremeWorlds.getHelp().registerCommand("wxw", "Wormhole X-Treme Worlds administration, configuration and utilities command", thisPlugin, true);
            if (WormholeXTremeWorlds.getPermissionHandler() != null && ConfigManager.getServerOptionPermissions()) {

                WormholeXTremeWorlds.getHelp().registerCommand("wxw go [world]", "Go to spawn of specified world.", thisPlugin, PermissionType.GO.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw list", "List all loaded and configured worlds.", thisPlugin, PermissionType.LIST.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw remove [world]", "Remove world from configuration.", thisPlugin, PermissionType.REMOVE.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw connect [world]", "Load unloaded world.", thisPlugin, PermissionType.LOAD.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw modify [args]", "Modify settings of specified world.", thisPlugin, PermissionType.MODIFY.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw info [world]", "Get info about specified world.", thisPlugin, PermissionType.INFO.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw setspawn", "Set spawn of current world to current location.", thisPlugin, PermissionType.SET_SPAWN.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw create [args]", "Create new world with specified args.", thisPlugin, PermissionType.CREATE.getPermission());
                WormholeXTremeWorlds.getHelp().registerCommand("wxw spawn", "Go to spawn of current world.", thisPlugin, PermissionType.SPAWN.getPermission());
            }
            else {
                WormholeXTremeWorlds.getHelp().registerCommand("wxw go [world]", "Go to spawn of specified world.", thisPlugin, "OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw list", "List all loaded and configured worlds.", thisPlugin,"OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw remove [world]", "Remove world from configuration.", thisPlugin, "OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw connect [world]", "Load unloaded world.", thisPlugin, "OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw modify [args]", "Modify settings of specified world.", thisPlugin, "OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw info [world]", "Get info about specified world.", thisPlugin, "OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw setspawn", "Set spawn of current world to current location.", thisPlugin, "OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw create [args]", "Create new world with specified args.", thisPlugin, "OP");
                WormholeXTremeWorlds.getHelp().registerCommand("wxw spawn", "Go to spawn of current world.", thisPlugin, "OP");
            }
        }
    }
}
