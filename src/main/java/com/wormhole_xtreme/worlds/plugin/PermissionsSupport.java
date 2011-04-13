/**
 *
 */
package com.wormhole_xtreme.worlds.plugin;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;

/**
 * @author alron
 *
 */
public class PermissionsSupport {

    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();
    private static final PluginManager pluginManager = WormholeXTremeWorlds.getThisPlugin().getServer().getPluginManager();
    
    public static void enablePermissions()
    {
        if (WormholeXTremeWorlds.getPermissionHandler() == null)
        {
            final Plugin test = pluginManager.getPlugin("Permissions");
            if (test != null)
            {
                final String version = test.getDescription().getVersion();
                if ( !version.equals("2.4") && !version.startsWith("2.5") && !version.startsWith("2.6"))
                {
                    thisPlugin.prettyLog(Level.WARNING, false, "Not a supported version of Permissions. Recommended is 2.6.x" );
                }
                try
                {
                    WormholeXTremeWorlds.setPermissionHandler(((Permissions)test).getHandler());
                    thisPlugin.prettyLog(Level.INFO, false, "Attached to Permissions version " + version);
                }
                catch (ClassCastException e)
                {
                    thisPlugin.prettyLog(Level.WARNING, false, "Failed to get cast to Permissions: " + e.getMessage());
                }
            }
            else
            {
                thisPlugin.prettyLog(Level.INFO, false, "Permission Plugin not yet available.");
            }
        }
    }
    
    public static void disablePermissions()
    {
        if (WormholeXTremeWorlds.getPermissionHandler() != null)
        {
            WormholeXTremeWorlds.setPermissionHandler(null);
            thisPlugin.prettyLog(Level.INFO, false, "Detached from Permissions plugin.");
        }
    }
}
