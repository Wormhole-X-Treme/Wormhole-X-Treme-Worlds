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
package com.wormhole_xtreme.worlds.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager.WorldOptionKeys;
import com.wormhole_xtreme.worlds.config.ResponseType;
import com.wormhole_xtreme.worlds.permissions.PermissionType;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The wxw command class.
 * 
 * @author alron
 */
class Wxw implements CommandExecutor {

    /** The Constant thisPlugin. */
    private static final WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String[] cleanArgs = CommandUtilities.commandCleaner(args);
        if (cleanArgs != null) {
            if (CommandUtilities.playerCheck(sender)) {
                thisPlugin.prettyLog(Level.FINE, false, "Command \"wxw\" args: \"" + Arrays.toString(cleanArgs) + "\"");
            }
            if (cleanArgs.length >= 1) {
                if (cleanArgs[0].equalsIgnoreCase("list")) {
                    return doListWorlds(sender);
                }
                else if (cleanArgs[0].equalsIgnoreCase("create")) {
                    return doCreateWorld(sender, CommandUtilities.commandRemover(cleanArgs));
                }
                else if (cleanArgs[0].equalsIgnoreCase("remove")) {
                    return doRemoveWorld(sender, CommandUtilities.commandRemover(cleanArgs));
                }
                else if (cleanArgs[0].equalsIgnoreCase("connect")) {
                    return doConnectWorld(sender, CommandUtilities.commandRemover(cleanArgs));
                }
                else if (cleanArgs[0].equalsIgnoreCase("modify")) {
                    return doModifyWorld(sender, CommandUtilities.commandRemover(cleanArgs));
                }
                else if (cleanArgs[0].equalsIgnoreCase("info")) {
                    return doInfoWorld(sender, CommandUtilities.commandRemover(cleanArgs));
                }
                else if (cleanArgs[0].equalsIgnoreCase("go")) {
                    if (CommandUtilities.playerCheck(sender)) {
                        return doGoWorld((Player) sender, CommandUtilities.commandRemover(cleanArgs));
                    }
                    else {
                        sender.sendMessage(ResponseType.ERROR_IN_GAME_ONLY.toString());
                        return true;
                    }
                }
                else if (cleanArgs[0].equalsIgnoreCase("setspawn")) {
                    if (CommandUtilities.playerCheck(sender)) {
                        return doSetSpawnWorld((Player) sender);
                    }
                    else {
                        sender.sendMessage(ResponseType.ERROR_IN_GAME_ONLY.toString());
                        return true;
                    }
                }
                else if (cleanArgs[0].equalsIgnoreCase("spawn")) {
                    if (CommandUtilities.playerCheck(sender)) {
                        return doSpawnWorld((Player) sender);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Do spawn world.
     * 
     * @param player
     *            the player
     * @return true, if successful
     */
    private boolean doSpawnWorld(final Player player) {
        if (PermissionType.SPAWN.checkPermission(player)) {
            final WormholeWorld wormholeWorld = WorldManager.getWorld(player.getWorld().getName());
            if (wormholeWorld != null) {
                player.teleport(wormholeWorld.getWorldSpawn());
            }
            else {
                player.sendMessage(ResponseType.ERROR_COMMAND_ONLY_MANAGED_WORLD.toString() + "spawn");
            }
        }
        else {
            player.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do connect world.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean doConnectWorld(final CommandSender sender, final String[] args) {
        boolean allowed = false;
        if (CommandUtilities.playerCheck(sender)) {
            allowed = PermissionType.CONNECT.checkPermission((Player) sender);
        }
        else {
            allowed = true;
        }
        if (allowed) {
            if ((args != null) && (args.length == 1)) {
                final WormholeWorld wormholeWorld = WorldManager.getWorld(args[0]);
                if (wormholeWorld != null) {
                    WorldManager.connectWorld(wormholeWorld);
                    sender.sendMessage(ResponseType.NORMAL_HEADER + "Connected world: " + args[0]);
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_COMMAND_ONLY_MANAGED_WORLD.toString() + "connect");
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "connect");
            }
        }
        else {
            sender.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do set spawn world.
     * 
     * @param player
     *            the player
     * @return true, if successful
     */
    private static boolean doSetSpawnWorld(final Player player) {
        if (PermissionType.SET_SPAWN.checkPermission(player)) {
            final Location playerLocation = player.getLocation();
            final World playerWorld = playerLocation.getWorld();
            final WormholeWorld playerWormholeWorld = WorldManager.getWorld(playerWorld.getName());
            if (playerWormholeWorld != null) {
                if (playerWorld.setSpawnLocation((int) playerLocation.getX(), (int) playerLocation.getY(), (int) playerLocation.getZ())) {
                    playerWormholeWorld.setWorldSpawn(playerLocation);
                    playerWormholeWorld.setWorldCustomSpawn(new int[]{
                        (int) playerLocation.getX(),(int) playerLocation.getY(),(int) playerLocation.getZ()});
                    WorldManager.addWorld(playerWormholeWorld);
                    player.sendMessage(ResponseType.NORMAL_HEADER.toString() + "Spawn for world \"" + playerWorld.getName() + "\" set to current location.");
                }
                else {
                    player.sendMessage(ResponseType.ERROR_HEADER.toString() + "Unable to set spawn for world \"" + playerWorld.getName() + "\"!");
                }
            }
            else {
                player.sendMessage(ResponseType.ERROR_COMMAND_ONLY_MANAGED_WORLD.toString() + "setspawn");
            }
        }
        else {
            player.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do info world.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean doInfoWorld(final CommandSender sender, final String[] args) {
        boolean allowed = false;
        if (CommandUtilities.playerCheck(sender)) {
            allowed = PermissionType.INFO.checkPermission((Player) sender);
        }
        else {
            allowed = true;
        }
        if (allowed) {
            if ((args != null) && (args.length == 1)) {
                final WormholeWorld world = WorldManager.getWorld(args[0]);
                if (world != null) {
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "World: " + args[0] + " Owner: " + world.getWorldOwner() + " Nether: " + world.isNetherWorld());
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "Hostiles: " + world.isAllowHostiles() + " Neutrals: " + world.isAllowNeutrals());
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "Autoload at start: " + world.isAutoconnectWorld());
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_WORLD_NOT_EXIST.toString() + args[0]);
                    sender.sendMessage(ResponseType.ERROR_WORLD_MAY_BE_ON_DISK.toString());
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "info");
            }
        }
        else {
            sender.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do modify world.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean doModifyWorld(final CommandSender sender, final String[] args) {
        boolean allowed = false;
        if (CommandUtilities.playerCheck(sender)) {
            allowed = PermissionType.MODIFY.checkPermission((Player) sender);
        }
        else {
            allowed = true;
        }
        if (allowed) {
            if ((args != null) && (args.length >= 1)) {
                String worldName = null, playerName = null;
                boolean doHostiles = false, hostiles = false, doNeutrals = false, neutrals = false, doAutoload = false, autoload = false;
                int hostileCount = 0, neutralCount = 0, autoloadCount = 0, nameCount = 0, playerCount = 0;
                for (final String arg : args) {
                    final String atlc = arg.toLowerCase();
                    if (atlc.startsWith("-name")) {
                        if (atlc.contains("|")) {
                            worldName = arg.split("\\|")[1].trim();
                            nameCount++;
                        }
                        else {
                            sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "-name");
                            return true;
                        }
                    }
                    else if (atlc.startsWith("-owner")) {
                        if (atlc.contains("|")) {
                            playerName = arg.split("\\|")[1].trim();
                            playerCount++;
                        }
                        else {
                            if (CommandUtilities.playerCheck(sender)) {
                                playerName = ((Player) sender).getName();
                                playerCount++;
                            }
                            else {
                                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_OWNER_ON_CONSOLE.toString() + "-owner");
                                return true;
                            }
                        }
                    }
                    else if (atlc.startsWith("-autoload")) {
                        doAutoload = true;
                        autoload = true;
                        autoloadCount++;
                    }
                    else if (atlc.startsWith("-noautoload")) {
                        doAutoload = true;
                        autoload = false;
                        autoloadCount++;
                    }
                    else if (atlc.startsWith("-hostiles")) {
                        doHostiles = true;
                        hostiles = true;
                        hostileCount++;
                    }
                    else if (atlc.startsWith("-nohostiles")) {
                        doHostiles = true;
                        hostiles = false;
                        hostileCount++;
                    }
                    else if (atlc.startsWith("-neutrals")) {
                        doNeutrals = true;
                        neutrals = true;
                        neutralCount++;
                    }
                    else if (atlc.startsWith("-noneutrals")) {
                        doNeutrals = true;
                        neutrals = false;
                        neutralCount++;
                    }
                }
                if ((worldName != null) && (nameCount == 1)) {
                    final WormholeWorld world = WorldManager.getWorld(worldName);
                    if (world != null) {
                        if (doHostiles || doNeutrals || doAutoload || (playerName != null)) {
                            if (doHostiles && (hostileCount == 1)) {
                                world.setAllowHostiles(hostiles);
                            }
                            else if (doHostiles) {
                                sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Conflicting or multiple hostile commands specified.");
                            }
                            if (doNeutrals && (neutralCount == 1)) {
                                world.setAllowNeutrals(neutrals);
                            }
                            else if (doNeutrals) {
                                sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Conflicting or multiple neutral commands specified.");
                            }
                            if (doAutoload && (autoloadCount == 1)) {
                                world.setAutoconnectWorld(autoload);
                                WorldManager.connectWorld(world);
                            }
                            else if (doAutoload) {
                                sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Conflicting or multiple autoload commands specified.");
                            }
                            if ((playerName != null) && (playerCount == 1)) {
                                world.setWorldOwner(playerName);
                            }
                            else if (playerName != null) {
                                sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Conflicting or multiple owner commands specified.");
                            }
                            WorldManager.addWorld(world);
                            final String[] w = new String[1];
                            w[0] = worldName;
                            return doInfoWorld(sender, w);
                        }
                        else {
                            sender.sendMessage(ResponseType.ERROR_NO_ARGS_GIVEN.toString() + "modify");
                            sender.sendMessage(ResponseType.NORMAL_COMMAND_REQUIRED_ARGS.toString());
                            sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_OPTIONAL_ARGS.toString());
                        }
                    }
                    else {
                        sender.sendMessage(ResponseType.ERROR_WORLD_NOT_EXIST.toString() + worldName);
                    }
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Must specify a single world to modify.");
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_NO_ARGS_GIVEN.toString() + "modify");
                sender.sendMessage(ResponseType.NORMAL_COMMAND_REQUIRED_ARGS.toString());
                sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_OPTIONAL_ARGS.toString());
            }
        }
        else {
            sender.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do go world.
     * 
     * @param player
     *            the player
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean doGoWorld(final Player player, final String[] args) {
        if (PermissionType.GO.checkPermission(player)) {
            if ((args != null) && (args.length == 1)) {
                final WormholeWorld wormholeWorld = WorldManager.getWorld(args[0]);
                if (wormholeWorld != null) {
                    player.teleport(wormholeWorld.getWorldSpawn());
                }
                else {
                    player.sendMessage(ResponseType.ERROR_WORLD_NOT_EXIST.toString() + args[0]);
                }
            }
            else {
                player.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "go");
            }

        }
        else {
            player.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do remove world.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean doRemoveWorld(final CommandSender sender, final String[] args) {
        boolean allowed = false;
        if (CommandUtilities.playerCheck(sender)) {
            allowed = PermissionType.REMOVE.checkPermission((Player) sender);
        }
        else {
            allowed = true;
        }
        if (allowed) {
            if ((args != null) && (args.length >= 1)) {
                final WormholeWorld wormholeWorld = WorldManager.getWorld(args[0]);
                if (wormholeWorld != null) {
                    WorldManager.removeWorld(wormholeWorld);
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "Removed world: " + args[0] + ". Deleted world config file, world will be unavailable at next server restart.");
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_WORLD_NOT_EXIST.toString() + args[0]);
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "remove");
            }
        }
        else {
            sender.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do create world.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean doCreateWorld(final CommandSender sender, final String[] args) {
        boolean allowed = false;
        if (CommandUtilities.playerCheck(sender)) {
            allowed = PermissionType.CREATE.checkPermission((Player) sender);
        }
        else {
            allowed = true;
        }
        if (allowed) {
            if ((args != null) && (args.length >= 1)) {
                String playerName = null;
                String worldName = null;
                long worldSeed = 0;
                final ArrayList<WorldOptionKeys> worldOptionKeyList = new ArrayList<WorldOptionKeys>();

                for (final String arg : args) {
                    final String atlc = arg.toLowerCase();
                    if (atlc.startsWith("-owner")) {
                        if (atlc.contains("|")) {
                            playerName = arg.split("\\|")[1].trim();
                        }
                        else {
                            if (CommandUtilities.playerCheck(sender)) {
                                playerName = ((Player) sender).getName();
                            }
                            else {
                                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_OWNER_ON_CONSOLE.toString() + "-owner");
                                return true;
                            }
                        }
                    }
                    else if (atlc.startsWith("-name")) {
                        if (atlc.contains("|")) {
                            worldName = arg.split("\\|")[1].trim();
                        }
                        else {
                            sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "-name");
                            return true;
                        }
                    }
                    else if (atlc.startsWith("-nether")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNether);
                    }
                    else if (atlc.startsWith("-noautoload")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoConnect);
                    }
                    else if (atlc.startsWith("-nohostiles")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoHostiles);
                    }
                    else if (atlc.startsWith("-noneutrals")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoNeutrals);
                    }
                    else if (atlc.startsWith("-seed")) {
                        if (atlc.contains("|")) {
                            try {
                                worldSeed = Long.valueOf(arg.split("\\|")[1].trim());
                                worldOptionKeyList.add(WorldOptionKeys.worldOptionSeed);
                            }
                            catch (final NumberFormatException e) {
                                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_NUMBER.toString() + "-seed");
                                return true;
                            }
                        }
                        else {
                            sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_NUMBER.toString() + "-seed");
                            return true;
                        }
                    }

                }

                if (worldName == null) {
                    sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "create");
                    return true;
                }

                if (playerName == null) {
                    if (CommandUtilities.playerCheck(sender)) {
                        playerName = ((Player) sender).getName();
                    }
                    else {
                        sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_OWNER_ON_CONSOLE.toString() + "create");
                        return true;
                    }
                }
                final WorldOptionKeys[] worldOptionKeys = worldOptionKeyList.toArray(new WorldOptionKeys[worldOptionKeyList.size()]);
                if (WorldManager.createWorld(playerName, worldName, worldOptionKeys, worldSeed)) {
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "World: " + worldName + " created with owner: " + playerName);
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "World Creation Failed?!");
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_NO_ARGS_GIVEN.toString() + "create");
                sender.sendMessage(ResponseType.NORMAL_COMMAND_REQUIRED_ARGS.toString());
                sender.sendMessage(ResponseType.NORMAL_CREATE_COMMAND_OPTIONAL_ARGS.toString());
            }
        }
        else {
            sender.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

    /**
     * Do list worlds.
     * 
     * @param sender
     *            the sender
     * @return true, if successful
     */
    private static boolean doListWorlds(final CommandSender sender) {
        boolean allowed = false;
        if (CommandUtilities.playerCheck(sender)) {
            allowed = PermissionType.LIST.checkPermission((Player) sender);
        }
        else {
            allowed = true;
        }
        if (allowed) {
            final List<World> worldLoadedList = WormholeXTremeWorlds.getThisPlugin().getServer().getWorlds();
            final String[] wormholeWorldNames = WorldManager.getAllWorldNames();
            if (worldLoadedList != null) {
                final StringBuilder s = new StringBuilder();
                int i = 0;
                for (final World world : worldLoadedList) {
                    i++;
                    s.append(world.getName());
                    if (i < worldLoadedList.size()) {
                        s.append(", ");
                    }
                }
                sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "Loaded worlds: " + s.toString());
            }
            if (wormholeWorldNames != null) {
                final StringBuilder s = new StringBuilder();
                int i = 0;
                for (final String worldName : wormholeWorldNames) {
                    i++;
                    s.append(worldName);
                    if (i < wormholeWorldNames.length) {
                        s.append(", ");
                    }
                }
                sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "Configured worlds: " + s.toString());
            }
        }
        else {
            sender.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
        }
        return true;
    }

}
