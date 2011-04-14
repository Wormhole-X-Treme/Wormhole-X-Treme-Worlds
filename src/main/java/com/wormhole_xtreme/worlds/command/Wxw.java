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

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager.WorldOptionKeys;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The wxw command class.
 * 
 * @author alron
 */
class Wxw implements CommandExecutor {

    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        // TODO: Add initial permissions calls here
        final String[] cleanArgs = CommandUtilities.commandCleaner(args);
        if (cleanArgs != null) {
            sender.sendMessage("wxw: " + Arrays.toString(cleanArgs));
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
                else if (cleanArgs[0].equalsIgnoreCase("modify")) {
                    return doModifyWorld(sender, CommandUtilities.commandRemover(cleanArgs));
                }
                else if (cleanArgs[0].equalsIgnoreCase("info")) {
                    return doInfoWorld(sender, CommandUtilities.commandRemover(cleanArgs));
                }
                else if (cleanArgs[0].equalsIgnoreCase("go")) {
                    if (CommandUtilities.playerCheck(sender)) {
                        return goGoWorld((Player) sender, CommandUtilities.commandRemover(cleanArgs));
                    }
                    else {
                        sender.sendMessage("This is an in game only command");
                        return true;
                    }
                }
            }
        }
        return false;
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
        if ((args != null) && (args.length == 1)) {
            final WormholeWorld world = WorldManager.getWorld(args[0]);
            if (world != null) {
                sender.sendMessage("World: " + args[0] + " Owner: " + world.getWorldOwner() + " Nether: " + world.isNetherWorld());
                sender.sendMessage("Hostiles: " + world.isAllowHostiles() + " Neutrals: " + world.isAllowNeutrals());
                sender.sendMessage("Autoload at start: " + world.isAutoconnectWorld());
            }
            else {
                sender.sendMessage("World does not exist: " + args[0]);
                sender.sendMessage("World may exist on disk, but has not been registered with Wormholw X-Treme Worlds.");
            }
        }
        else {
            sender.sendMessage("Requires world name as an argument.");
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
        if ((args != null) && (args.length >= 1)) {
            sender.sendMessage("modify: " + Arrays.toString(args));
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
                        sender.sendMessage("Must specify world name with -name command");
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
                            sender.sendMessage("Must specifiy owner when adding worlds from console.");
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
                            sender.sendMessage("Conflicting or multiple hostile commands specified.");
                        }
                        if (doNeutrals && (neutralCount == 1)) {
                            world.setAllowNeutrals(neutrals);
                        }
                        else if (doNeutrals) {
                            sender.sendMessage("Conflicting or multiple neutral commands specified.");
                        }
                        if (doAutoload && (autoloadCount == 1)) {
                            world.setAutoconnectWorld(autoload);
                        }
                        else if (doAutoload) {
                            sender.sendMessage("Conflicting or multiple autoload commands specified.");
                        }
                        if ((playerName != null) && (playerCount == 1)) {
                            world.setWorldOwner(playerName);
                        }
                        else if (playerName != null) {
                            sender.sendMessage("Conflicting or multiple owner commands specified.");
                        }
                        final String[] w = new String[1];
                        w[0] = worldName;
                        return doInfoWorld(sender, w);
                    }
                    else {
                        sender.sendMessage("No Modification commands specified.");
                        sender.sendMessage("Required Args: -name <WorldName>");
                        sender.sendMessage("Optional Args: -owner <Owner>, -(no)autoload, -(no)hostiles, -(no)neutrals, ");
                        return true;
                    }
                }
                else {
                    sender.sendMessage("Specified world invalid.");
                    return true;
                }

            }
            else {
                sender.sendMessage("Must specify a single world to modify.");
                return true;
            }
        }
        else {
            sender.sendMessage("Required Args: -name <WorldName>");
            sender.sendMessage("Optional Args: -owner <Owner>, -(no)autoload, -(no)hostiles, -(no)neutrals, ");
            return true;
        }
    }

    /**
     * Go go world.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @return true, if successful
     */
    private static boolean goGoWorld(final Player player, final String[] args) {
        if ((args != null) && (args.length >= 1)) {
            // TODO: Add world teleportation logic calls here
            player.sendMessage("go: " + Arrays.toString(args));
        }
        else {
            // TODO: Add help output here
            return false;
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
        if ((args != null) && (args.length >= 1)) {
            final WormholeWorld world = WorldManager.getWorld(args[0]);
            if (world != null) {
                WorldManager.removeWorld(world);
                sender.sendMessage("Removed world: " + args[0] + " World will become unavailable at next server restart.");
            }
            else {
                sender.sendMessage("Specified world does not exist: " + args[0]);
            }
        }
        else {
            sender.sendMessage("World to be removed must be specified. This command will not delete the world on disk, just remove the WXW configuration for the specified world.");
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
        if ((args != null) && (args.length >= 1)) {
            sender.sendMessage("create: " + Arrays.toString(args));
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
                        sender.sendMessage("Must specify player name with -owner command");
                        return true;
                    }
                }
                else if (atlc.startsWith("-name")) {
                    if (atlc.contains("|")) {
                        worldName = arg.split("\\|")[1].trim();
                    }
                    else {
                        sender.sendMessage("Must specify world name with -name command");
                        return true;
                    }
                }
                else if (atlc.startsWith("-nether")) {
                    worldOptionKeyList.add(WorldOptionKeys.worldOptionNether);
                }
                else if (atlc.startsWith("-autoload")) {
                    worldOptionKeyList.add(WorldOptionKeys.worldOptionConnect);
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
                            sender.sendMessage("Must specify numeric seed value with the -seed command");
                            return true;
                        }
                    }
                    else {
                        sender.sendMessage("Must specify seed value with the -seed command");
                        return true;
                    }
                }

            }

            if (worldName == null) {
                sender.sendMessage("Must specify world name to create world.");
                return true;
            }

            if (playerName == null) {
                if (CommandUtilities.playerCheck(sender)) {
                    playerName = ((Player) sender).getName();
                }
                else {
                    sender.sendMessage("Must specifiy owner when adding worlds from console.");
                    return true;
                }
            }
            final WorldOptionKeys[] worldOptionKeys = worldOptionKeyList.toArray(new WorldOptionKeys[worldOptionKeyList.size()]);
            if (WorldManager.createWorld(playerName, worldName, worldOptionKeys, worldSeed)) {
                sender.sendMessage("World: " + worldName + " created with owner: " + playerName);
                return true;
            }
            else {
                sender.sendMessage("World Creation Failed?!");
                return true;
            }
        }
        else {
            sender.sendMessage("Required Args: -name <WorldName>");
            sender.sendMessage("Optional Args: -owner <Owner>, -seed <seed>, -nether, -autoload, -nohostiles, -noneutrals, ");
            return true;
        }
    }

    /**
     * Do list worlds.
     * 
     * @param sender
     *            the sender
     * @return true, if successful
     */
    private static boolean doListWorlds(final CommandSender sender) {
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
            sender.sendMessage("List - loaded worlds: " + s.toString());
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
            sender.sendMessage("List - configured worlds: " + s.toString());
        }
        return true;
    }

}
