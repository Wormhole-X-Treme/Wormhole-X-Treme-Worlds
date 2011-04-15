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
package com.wormhole_xtreme.worlds.permissions;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

// TODO: Auto-generated Javadoc
/**
 * The Class PermissionsManager.
 * 
 * @author alron
 */
public class PermissionsManager {

    /** The Constant thisPlugin. */
    private final static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /**
     * The Enum PermissionType.
     */
    public static enum PermissionType {

        /** The go permission. */
        go,

        /** The list permission. */
        list,

        /** The remove permission. */
        remove,

        /** The connect permission. */
        connect,

        /** The modify permission. */
        modify,

        /** The info permission. */
        info,

        /** The set spawn permission. */
        setSpawn,

        /** The create permission. */
        create
    }

    /**
     * Check permissions.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @param permissionType
     *            the permission type
     * @return true, if successful
     */
    public static boolean checkPermissions(final Player player, final WormholeWorld wormholeWorld, final PermissionType permissionType) {
        if ((player == null) || (wormholeWorld == null) || (permissionType == null)) {
            thisPlugin.prettyLog(Level.WARNING, false, "Permission check called with missing arguments!");
            return false;
        }
        if (player.isOp() && ConfigManager.getServerOptionOpsBypassPermissions()) {
            thisPlugin.prettyLog(Level.FINE, false, "Player op: \"" + player.getName() + "\" allowed permission \"" + permissionType.toString() + "\"");
            return true;
        }
        if (ConfigManager.getServerOptionPermissions() && (WormholeXTremeWorlds.getPermissionHandler() != null)) {
            switch (permissionType) {
                case go :
                    return checkGoPermission(player, wormholeWorld);
                case list :
                    return checkListPermission(player, wormholeWorld);
                case remove :
                    return checkRemovePermission(player, wormholeWorld);
                case connect :
                    return checkConnectPermission(player, wormholeWorld);
                case modify :
                    return checkModifyPermission(player, wormholeWorld);
                case info :
                    return checkInfoPermission(player, wormholeWorld);
                case setSpawn :
                    return checkSetSpawnPermission(player, wormholeWorld);
                case create :
                    return checkCreatePermission(player, wormholeWorld);
                default :
                    return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Check create permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkCreatePermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.create.normal") && !wormholeWorld.isNetherWorld()) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.create.nether") && wormholeWorld.isNetherWorld())) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted create permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied create permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }

    /**
     * Check set spawn permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkSetSpawnPermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.setspawn.all")) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.setspawn.own") && wormholeWorld.getWorldOwner().equalsIgnoreCase(player.getName()))) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted setspawn permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied setspawn permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }

    /**
     * Check info permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkInfoPermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.info.all")) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.info.own") && wormholeWorld.getWorldOwner().equalsIgnoreCase(player.getName()))) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted info permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied info permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }

    /**
     * Check modify permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkModifyPermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.modify.all")) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.modify.own") && wormholeWorld.getWorldOwner().equalsIgnoreCase(player.getName()))) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted modify permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied modify permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }

    /**
     * Check connect permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkConnectPermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.connect.all")) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.connect.own") && wormholeWorld.getWorldOwner().equalsIgnoreCase(player.getName()))) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted connect permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied connect permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }

    /**
     * Check remove permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkRemovePermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.remove.all")) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.remove.own") && wormholeWorld.getWorldOwner().equalsIgnoreCase(player.getName()))) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted remove permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied remove permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }

    /**
     * Check list permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkListPermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.list.all")) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.list.own") && wormholeWorld.getWorldOwner().equalsIgnoreCase(player.getName()))) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted list permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied list permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }

    /**
     * Check go permission.
     * 
     * @param player
     *            the player
     * @param wormholeWorld
     *            the wormhole world
     * @return true, if successful
     */
    private static boolean checkGoPermission(final Player player, final WormholeWorld wormholeWorld) {
        if ((WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.go.all")) || (WormholeXTremeWorlds.getPermissionHandler().has(player, "wxw.go.own") && wormholeWorld.getWorldOwner().equalsIgnoreCase(player.getName()))) {
            thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" granted go permission on\"" + wormholeWorld.getWorldName() + "\"");
            return true;
        }
        thisPlugin.prettyLog(Level.FINE, false, "Player: \"" + player.getName() + "\" denied go permission on: \"" + wormholeWorld.getWorldName() + "\"");
        return false;
    }
}
