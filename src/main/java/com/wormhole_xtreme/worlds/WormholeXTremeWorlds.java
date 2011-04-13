/*
 *   Wormhole X-Treme Worlds Plugin for Bukkit
 *   Copyright (C) 2011  Dean Bailey
 *
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.wormhole_xtreme.worlds;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.taylorkelly.help.Help;

import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.wormhole_xtreme.worlds.command.CommandUtilities;
import com.wormhole_xtreme.worlds.config.XMLConfig;
import com.wormhole_xtreme.worlds.plugin.HelpSupport;
import com.wormhole_xtreme.worlds.plugin.PermissionsSupport;


/**
 * The Class WormholeXTremeWorlds.
 *
 * @author alron
 */
public class WormholeXTremeWorlds extends JavaPlugin 
{

    /** this plugin. */
    private static WormholeXTremeWorlds thisPlugin = null;
    
    /** this logger. */
    private static Logger thisLogger = null;
    
    /** The help. */
    private static Help help = null;
    
    /** The permission handler. */
    private static PermissionHandler permissionHandler = null;
    
    /* (non-Javadoc)
     * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
     */
    @Override
    public void onLoad() 
    {
        setThisPlugin(this);
        setThisLogger(getThisPlugin().getServer().getLogger());
        prettyLog(Level.INFO,true,getThisPlugin().getDescription().getAuthors().toString() + "Load Beginning." );
        // TODO: Add World loading for existing worlds here.
        XMLConfig.loadXmlConfig(getThisPlugin().getDescription());
        prettyLog(Level.INFO,true, "Load Completed.");
    }

    /* (non-Javadoc)
     * @see org.bukkit.plugin.Plugin#onDisable()
     */
    @Override
    public void onDisable() 
    {
        XMLConfig.saveXmlConfig(getThisPlugin().getDescription());
        prettyLog(Level.INFO, true, "Successfully shutdown.");
    }

    /* (non-Javadoc)
     * @see org.bukkit.plugin.Plugin#onEnable()
     */
    @Override
    public void onEnable() 
    {
        prettyLog(Level.INFO, true, "Enable Beginning.");
        PermissionsSupport.enablePermissions();
        HelpSupport.enableHelp();
        CommandUtilities.registerCommands();
        HelpSupport.registerHelpCommands();
        prettyLog(Level.INFO, true, "Enable Completed.");
    }

    /**
     * Sets this plugin.
     *
     * @param thisPlugin The new plugin
     */
    private static void setThisPlugin(WormholeXTremeWorlds thisPlugin) 
    {
        WormholeXTremeWorlds.thisPlugin = thisPlugin;
    }

    /**
     * Gets this plugin.
     *
     * @return This plugin
     */
    public static WormholeXTremeWorlds getThisPlugin() 
    {
        return thisPlugin;
    }

    /**
     * Sets this logger.
     *
     * @param thisLogger The new logger
     */
    private static void setThisLogger(Logger thisLogger) 
    {
        WormholeXTremeWorlds.thisLogger = thisLogger;
    }

    /**
     * Gets this logger.
     *
     * @return This logger
     */
    public static Logger getThisLogger() 
    {
        return thisLogger;
    }

    
    /**
     * 
     * prettyLog: A quick and dirty way to make log output clean, unified, and with versioning as needed.
     * 
     * @param severity Level of severity in the form of INFO, WARNING, SEVERE, etc.
     * @param version true causes version display in log entries.
     * @param message to prettyLog.
     * 
     */
    public void prettyLog(Level severity, boolean version, String message) 
    {
        final String prettyName = (String)("[" + getThisPlugin().getDescription().getName() + "]");
        final String prettyVersion = (String)("[v" + getThisPlugin().getDescription().getVersion() + "]");
        String prettyLogLine = prettyName;
        if (version)
        {
            prettyLogLine += prettyVersion;
            getThisLogger().log(severity,prettyLogLine + message);
        } 
        else
        {
            getThisLogger().log(severity,prettyLogLine + message);
        }
    }

    /**
     * Sets the help.
     *
     * @param help the new help
     */
    public static void setHelp(Help help)
    {
        WormholeXTremeWorlds.help = help;
    }

    /**
     * Gets the help.
     *
     * @return the help
     */
    public static Help getHelp()
    {
        return help;
    }

    /**
     * Sets the permission handler.
     *
     * @param permissionHandler the new permission handler
     */
    public static void setPermissionHandler(PermissionHandler permissionHandler)
    {
        WormholeXTremeWorlds.permissionHandler = permissionHandler;
    }

    /**
     * Gets the permission handler.
     *
     * @return the permission handler
     */
    public static PermissionHandler getPermissionHandler()
    {
        return permissionHandler;
    }
}
