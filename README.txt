Wormhole X-Treme Worlds v0.1.1: A multiple worlds management plugin for Bukkit.

To Install:
1. Extract WormholeXTremeWorlds.jar into your plugins/ folder.
2. Start server.
3. Add all existing worlds to WXW via the '/wxw create' command.

To Upgrade: 
1. Extract updated WormholwXtremeWorlds.jar into your plugins/ folder.
2. Start Server.

Command List with Permissions Nodes:
    '/wxw go [world]' - Go to spawn of specified world. - wxw.admin.go
    '/wxw list' - List all loaded and configured worlds. - wxw.admin.list
    '/wxw remove [world]' - Remove world from configuration. - wxw.admin.remove
    '/wxw load [world]' - Load unloaded world. - wxw.admin.load
    '/wxw modify [args]' - Modify settings of specified world. - wxw.admin.modify
    '/wxw info [world]' - Get info about specified world. - wxw.admin.info
    '/wxw setspawn' - Set spawn of current world to current location. - wxw.admin.setspawn
    '/wxw create [args]' - Create new world with specified args. - wxw.admin.create
    '/wxw spawn' - Go to spawn of current world. - wxw.spawn

Config.xml options with defaults:
    serverOptionPermissions - Enable or disable Permissions plugin support. Default: true
    serverOptionOpsBypassPermissions - Ops bypass Permissions plugin access checks. Default: true
    serverOptionHelp - Enable or disable Help plugin support. Default: true
    
World configuration files are stored in xml, and are able to be edited by player, though it is not
recommended. 