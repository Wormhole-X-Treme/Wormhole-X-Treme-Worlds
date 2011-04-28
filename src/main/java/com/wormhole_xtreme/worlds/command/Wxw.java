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
import com.wormhole_xtreme.worlds.scheduler.ScheduleAction;
import com.wormhole_xtreme.worlds.scheduler.ScheduleAction.ActionType;
import com.wormhole_xtreme.worlds.world.TimeLockType;
import com.wormhole_xtreme.worlds.world.WeatherLockType;
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

    /**
     * Colorize boolean.
     * 
     * @param b
     *            the b
     * @return the string
     */
    private static String colorizeBoolean(final boolean b) {
        if (b) {
            return "\u00A72true";
        }
        else {
            return "\u00A74false";
        }
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
                    else if (atlc.startsWith("-nopvp")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoPvP);
                    }
                    else if (atlc.equals("-night")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionTimeLockNight);
                    }
                    else if (atlc.equals("-day")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionTimeLockDay);
                    }
                    else if (atlc.equals("-clear")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionWeatherClear);
                    }
                    else if (atlc.equals("-rain")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionWeatherRain);
                    }
                    else if (atlc.equals("-storm")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionWeatherStorm);
                    }
                    else if (atlc.startsWith("-nolavaspread")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoLavaSpread);
                    }
                    else if (atlc.startsWith("-nodrown")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoPlayerDrown);
                    }
                    else if (atlc.startsWith("-nofirespread")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoFireSpread);
                    }
                    else if (atlc.startsWith("-nolavafire")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoLavaFire);
                    }
                    else if (atlc.startsWith("-nowaterspread")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoWaterSpread);
                    }
                    else if (atlc.startsWith("-nolightningfire")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoLightningFire);
                    }
                    else if (atlc.startsWith("-nolightningdamage")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoPlayerLightningDamage);
                    }
                    else if (atlc.startsWith("-nodamage")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoPlayerDamage);
                    }
                    else if (atlc.startsWith("-nolavadamage")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoPlayerLavaDamage);
                    }
                    else if (atlc.startsWith("-nofalldamage")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoPlayerFallDamage);
                    }
                    else if (atlc.startsWith("-nofiredamage")) {
                        worldOptionKeyList.add(WorldOptionKeys.worldOptionNoPlayerFireDamage);
                    }
                    else if (atlc.startsWith("-seed")) {
                        if (atlc.contains("|")) {
                            try {
                                worldSeed = Long.parseLong(arg.split("\\|")[1].trim());
                                worldOptionKeyList.add(WorldOptionKeys.worldOptionSeed);
                            }
                            catch (final NumberFormatException e) {
                                worldSeed = Long.valueOf(arg.split("\\|")[1].trim().hashCode());
                                worldOptionKeyList.add(WorldOptionKeys.worldOptionSeed);
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
                if (worldOptionKeyList.contains(WorldOptionKeys.worldOptionTimeLockNight) && worldOptionKeyList.contains(WorldOptionKeys.worldOptionTimeLockDay)) {
                    sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Conflicting time lock commands specified.");
                    return true;
                }
                final WorldOptionKeys[] worldOptionKeys = worldOptionKeyList.toArray(new WorldOptionKeys[worldOptionKeyList.size()]);
                if (WorldManager.createWormholeWorld(playerName, worldName, worldOptionKeys, worldSeed)) {
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "World: " + worldName + " created with owner: " + playerName);
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_WORLD_ALLREADY_EXISTS.toString() + worldName);
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_NO_ARGS_GIVEN.toString() + "create");
                sender.sendMessage(ResponseType.NORMAL_CREATE_COMMAND_ARGS1.toString());
                sender.sendMessage(ResponseType.NORMAL_CREATE_COMMAND_ARGS2.toString());
                sender.sendMessage(ResponseType.NORMAL_CREATE_COMMAND_ARGS3.toString());
                sender.sendMessage(ResponseType.NORMAL_CREATE_COMMAND_ARGS4.toString());
                sender.sendMessage(ResponseType.NORMAL_CREATE_COMMAND_ARGS5.toString());
                sender.sendMessage(ResponseType.NORMAL_CREATE_COMMAND_ARGS6.toString());
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
                if ((wormholeWorld != null) && (thisPlugin.getServer().getWorld(args[0]) != null)) {
                    player.teleport(WorldManager.getSafeSpawnLocation(wormholeWorld, player));
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
            if (CommandUtilities.playerCheck(sender) || ((args != null) && (args.length == 1))) {
                final WormholeWorld world = args.length == 0 ? WorldManager.getWorldFromPlayer((Player) sender)
                    : WorldManager.getWorld(args[0]);
                if (world != null) {
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A76=======================\u00A7fINFO\u00A76=======================");
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A7fWorld:\u00A7b" + world.getWorldName() + " \u00A7fOwner:\u00A7b" + world.getWorldOwner() + " \u00A7fNether:" + colorizeBoolean(world.isNetherWorld()) + " \u00A7fTime:\u00A7b" + world.getTimeLockType().toString());
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A7fAutoload:" + colorizeBoolean(world.isAutoconnectWorld()) + " \u00A7fSeed:\u00A7b" + world.getWorldSeed() + " \u00A7fWeather:\u00A7b" + world.getWeatherLockType().toString());
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A76=================\u00A7fWORLD PROTECTION\u00A76=================");
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A7fHostiles:" + colorizeBoolean(world.isAllowHostiles()) + " \u00A7fNeutrals:" + colorizeBoolean(world.isAllowNeutrals()) + " \u00A7fFireSPRD:" + colorizeBoolean(world.isAllowFireSpread()) + " \u00A7fLavaFIRE:" + colorizeBoolean(world.isAllowLavaFire()));
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A7fWaterSPRD:" + colorizeBoolean(world.isAllowWaterSpread()) + " \u00A7fLightningFIRE:" + colorizeBoolean(world.isAllowLightningFire()) + " \u00A7fLavaSPRD:" + colorizeBoolean(world.isAllowLavaSpread()));
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A76=================\u00A7fPLAYER PROTECTION\u00A76================");
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A7fDrown:" + colorizeBoolean(world.isAllowPlayerDrown()) + " \u00A7fLavaDMG:" + colorizeBoolean(world.isAllowPlayerLavaDamage()) + " \u00A7fFallDMG:" + colorizeBoolean(world.isAllowPlayerFallDamage()) + " \u00A7fLgtngDMG:" + colorizeBoolean(world.isAllowPlayerLightningDamage()));
                    sender.sendMessage(ResponseType.NORMAL_HEADER.toString() + "\u00A7fFireDMG:" + colorizeBoolean(world.isAllowPlayerFireDamage()) + " \u00A7fPvPDMG:" + colorizeBoolean(world.isAllowPvP()) + " \u00A7fAllDMG:" + colorizeBoolean(world.isAllowPlayerDamage()));

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

    /**
     * Do connect world.
     * 
     * @param sender
     *            the sender
     * @param args
     *            the args
     * @param commandName
     *            the command name
     * @return true, if successful
     */
    private static boolean doLoadWorld(final CommandSender sender, final String[] args, final String commandName) {
        boolean allowed = false;
        if (CommandUtilities.playerCheck(sender)) {
            allowed = PermissionType.LOAD.checkPermission((Player) sender);
        }
        else {
            allowed = true;
        }
        if (allowed) {
            if ((args != null) && (args.length == 1)) {
                final WormholeWorld wormholeWorld = WorldManager.getWorld(args[0]);
                if (wormholeWorld != null) {
                    WorldManager.loadWorld(wormholeWorld);
                    sender.sendMessage(ResponseType.NORMAL_HEADER + "Connected world: " + args[0]);
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_COMMAND_ONLY_MANAGED_WORLD.toString() + commandName);
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + commandName);
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
                String worldName = "", playerName = "";
                boolean doHostiles = false, doNeutrals = false, doAutoLoad = false, doPvP = false;
                boolean hostiles = false, neutrals = false, autoLoad = false, pvp = false;
                boolean doLavaSpread = false, doFireSpread = false, doLavaFire = false, doWaterSpread = false, doLightningFire = false;
                boolean lavaSpread = false, fireSpread = false, lavaFire = false, waterSpread = false, lightningFire = false;
                boolean doPlayerLightningDamage = false, doPlayerDamage = false, doPlayerDrown = false, doPlayerLavaDamage = false, doPlayerFallDamage = false, doPlayerFireDamage = false;
                boolean playerLightningDamage = false, playerDamage = false, playerDrown = false, playerLavaDamage = false, playerFallDamage = false, playerFireDamage = false;
                TimeLockType timeLockType = null;
                WeatherLockType weatherLockType = null;
                boolean conflict = false;
                for (final String arg : args) {
                    final String atlc = arg.toLowerCase();
                    if (atlc.startsWith("-")) {
                        if (atlc.startsWith("-name")) {
                            if (worldName.length() > 0) {
                                conflict = true;
                            }
                            else if (atlc.contains("|")) {
                                worldName = arg.split("\\|")[1].trim();
                            }
                            else {
                                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "-name");
                                return true;
                            }
                        }
                        else if (atlc.startsWith("-owner")) {
                            if (playerName.length() > 0) {
                                conflict = true;
                            }
                            else if (atlc.contains("|")) {
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
                        else if (atlc.startsWith("-time")) {
                            if (timeLockType != null) {
                                conflict = true;
                            }
                            else if (atlc.contains("|")) {
                                timeLockType = TimeLockType.getTimeType(arg.split("\\|")[1].trim().toUpperCase());
                            }
                            else {
                                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "-time");
                            }
                        }
                        else if (atlc.startsWith("-weather")) {
                            if (weatherLockType != null) {
                                conflict = true;
                            }
                            else if (atlc.contains("|")) {
                                weatherLockType = WeatherLockType.getWeatherType(arg.split("\\|")[1].trim().toUpperCase());
                            }
                            else {
                                sender.sendMessage(ResponseType.ERROR_COMMAND_REQUIRES_WORLDNAME.toString() + "-weather");
                            }
                        }
                        else if (atlc.contains("autoload")) {
                            if (doAutoLoad) {
                                conflict = true;
                            }
                            else {
                                doAutoLoad = true;
                                if (atlc.startsWith("-no")) {
                                    autoLoad = false;
                                }
                                else {
                                    autoLoad = true;
                                }
                            }
                        }
                        else if (atlc.contains("hostiles")) {
                            if (doHostiles) {
                                conflict = true;
                            }
                            else {
                                doHostiles = true;
                                if (atlc.startsWith("-no")) {
                                    hostiles = false;
                                }
                                else {
                                    hostiles = true;
                                }
                            }
                        }
                        else if (atlc.contains("neutrals")) {
                            if (doNeutrals) {
                                conflict = true;
                            }
                            else {
                                doNeutrals = true;
                                if (atlc.startsWith("-no")) {
                                    neutrals = false;
                                }
                                else {
                                    neutrals = true;
                                }
                            }
                        }
                        else if (atlc.contains("pvp")) {
                            if (doPvP) {
                                conflict = true;
                            }
                            else {
                                doPvP = true;
                                if (atlc.startsWith("-no")) {
                                    pvp = false;
                                }
                                else {
                                    pvp = true;
                                }
                            }
                        }

                        else if (atlc.contains("lightningdamage")) {
                            if (doPlayerLightningDamage) {
                                conflict = true;
                            }
                            else {
                                doPlayerLightningDamage = true;
                                if (atlc.startsWith("-no")) {
                                    playerLightningDamage = false;
                                }
                                else {
                                    playerLightningDamage = true;
                                }
                            }
                        }
                        else if (atlc.startsWith("-damage") || atlc.startsWith("-nodamage")) {
                            if (doPlayerDamage) {
                                conflict = true;
                            }
                            else {
                                doPlayerDamage = true;
                                if (atlc.startsWith("-no")) {
                                    playerDamage = false;
                                }
                                else {
                                    playerDamage = true;
                                }
                            }
                        }
                        else if (atlc.contains("drown")) {
                            if (doPlayerDrown) {
                                conflict = true;
                            }
                            else {
                                doPlayerDrown = true;
                                if (atlc.startsWith("-no")) {
                                    playerDrown = false;
                                }
                                else {
                                    playerDrown = true;
                                }
                            }
                        }
                        else if (atlc.contains("lavadamage")) {
                            if (doPlayerLavaDamage) {
                                conflict = true;
                            }
                            else {
                                doPlayerLavaDamage = true;
                                if (atlc.startsWith("-no")) {
                                    playerLavaDamage = false;
                                }
                                else {
                                    playerLavaDamage = true;
                                }
                            }
                        }
                        else if (atlc.contains("falldamage")) {
                            if (doPlayerFallDamage) {
                                conflict = true;
                            }
                            else {
                                doPlayerFallDamage = true;
                                if (atlc.startsWith("-no")) {
                                    playerFallDamage = false;
                                }
                                else {
                                    playerFallDamage = true;
                                }
                            }
                        }
                        else if (atlc.contains("firedamage")) {
                            if (doPlayerFireDamage) {
                                conflict = true;
                            }
                            else {
                                doPlayerFireDamage = true;
                                if (atlc.startsWith("-no")) {
                                    playerFireDamage = false;
                                }
                                else {
                                    playerFireDamage = true;
                                }
                            }
                        }
                        else if (atlc.contains("lavaspread")) {
                            if (doLavaSpread) {
                                conflict = true;
                            }
                            else {
                                doLavaSpread = true;
                                if (atlc.startsWith("-no")) {
                                    lavaSpread = false;
                                }
                                else {
                                    lavaSpread = true;
                                }
                            }
                        }
                        else if (atlc.contains("firespread")) {
                            if (doFireSpread) {
                                conflict = true;
                            }
                            else {
                                doFireSpread = true;
                                if (atlc.startsWith("-no")) {
                                    fireSpread = false;
                                }
                                else {
                                    fireSpread = true;
                                }
                            }
                        }
                        else if (atlc.contains("lavafire")) {
                            if (doLavaFire) {
                                conflict = true;
                            }
                            else {
                                doLavaFire = true;
                                if (atlc.startsWith("-no")) {
                                    lavaFire = false;
                                }
                                else {
                                    lavaFire = true;
                                }
                            }
                        }
                        else if (atlc.contains("waterspread")) {
                            if (doWaterSpread) {
                                conflict = true;
                            }
                            else {
                                doWaterSpread = true;
                                if (atlc.startsWith("-no")) {
                                    waterSpread = false;
                                }
                                else {
                                    waterSpread = true;
                                }
                            }
                        }
                        else if (atlc.contains("lightningfire")) {
                            if (doLightningFire) {
                                conflict = true;
                            }
                            else {
                                doLightningFire = true;
                                if (atlc.startsWith("-no")) {
                                    lightningFire = false;
                                }
                                else {
                                    lightningFire = true;
                                }
                            }
                        }
                    }
                }
                if (conflict) {
                    sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Conflicting or duplicate commands specified.");
                }
                if (worldName.length() > 0) {
                    final WormholeWorld world = WorldManager.getWorld(worldName);
                    if (world != null) {
                        if (doHostiles || doNeutrals || doAutoLoad || doPvP || (playerName.length() > 0) || (weatherLockType != null) || (timeLockType != null) || doPlayerLightningDamage || doPlayerDamage || doPlayerDrown || doPlayerLavaDamage || doPlayerFallDamage || doPlayerFireDamage || doLavaSpread || doFireSpread || doLavaFire || doWaterSpread || doLightningFire) {
                            if (doHostiles) {
                                world.setAllowHostiles(hostiles);
                            }
                            if (doNeutrals) {
                                world.setAllowNeutrals(neutrals);
                            }
                            if (doPvP) {
                                world.setAllowPvP(pvp);
                            }
                            if (doAutoLoad) {
                                world.setAutoconnectWorld(autoLoad);
                            }
                            if (playerName.length() > 0) {
                                world.setWorldOwner(playerName);
                            }
                            if (doPlayerLightningDamage) {
                                world.setAllowPlayerLightningDamage(playerLightningDamage);
                            }
                            if (doPlayerDamage) {
                                world.setAllowPlayerDamage(playerDamage);
                            }
                            if (doPlayerDrown) {
                                world.setAllowPlayerDrown(playerDrown);
                            }
                            if (doPlayerLavaDamage) {
                                world.setAllowPlayerLavaDamage(playerLavaDamage);
                            }
                            if (doPlayerFallDamage) {
                                world.setAllowPlayerFallDamage(playerFallDamage);
                            }
                            if (doPlayerFireDamage) {
                                world.setAllowPlayerFireDamage(playerFireDamage);
                            }
                            if (doLavaSpread) {
                                world.setAllowLavaSpread(lavaSpread);
                            }
                            if (doFireSpread) {
                                world.setAllowFireSpread(fireSpread);
                            }
                            if (doLavaFire) {
                                world.setAllowLavaFire(lavaFire);
                            }
                            if (doWaterSpread) {
                                world.setAllowWaterSpread(waterSpread);
                            }
                            if (doLightningFire) {
                                world.setAllowLightningFire(lightningFire);
                            }
                            if (timeLockType != null) {
                                world.setTimeLockType(timeLockType);
                            }
                            if (weatherLockType != null) {
                                world.setWeatherLockType(weatherLockType);
                            }
                            WorldManager.addWorld(world);
                            if (doAutoLoad) {
                                WorldManager.loadWorld(world);
                            }
                            if (thisPlugin.getServer().getWorld(worldName) != null) {
                                WormholeXTremeWorlds.getScheduler().scheduleSyncDelayedTask(thisPlugin, new ScheduleAction(world, ActionType.ClearEntities));
                                WormholeXTremeWorlds.getScheduler().scheduleSyncDelayedTask(thisPlugin, new ScheduleAction(world, ActionType.SetWeather));
                            }
                            final String[] w = new String[1];
                            w[0] = worldName;
                            return doInfoWorld(sender, w);
                        }
                        else {
                            sender.sendMessage(ResponseType.ERROR_NO_ARGS_GIVEN.toString() + "modify");
                            sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS1.toString());
                            sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS2.toString());
                            sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS3.toString());
                            sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS4.toString());
                            sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS5.toString());
                            sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS6.toString());
                        }
                    }
                    else {
                        sender.sendMessage(ResponseType.ERROR_WORLD_NOT_EXIST.toString() + worldName);
                    }
                }
                else {
                    sender.sendMessage(ResponseType.ERROR_HEADER.toString() + "Must specify a world to modify.");
                }
            }
            else {
                sender.sendMessage(ResponseType.ERROR_NO_ARGS_GIVEN.toString() + "modify");
                sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS1.toString());
                sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS2.toString());
                sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS3.toString());
                sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS4.toString());
                sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS5.toString());
                sender.sendMessage(ResponseType.NORMAL_MODIFY_COMMAND_ARGS6.toString());
            }
        }
        else {
            sender.sendMessage(ResponseType.ERROR_PERMISSION_NO.toString());
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
                        (int) playerLocation.getX(), (int) playerLocation.getY(), (int) playerLocation.getZ()
                    });
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
                else if (cleanArgs[0].equalsIgnoreCase("connect") || cleanArgs[0].equalsIgnoreCase("load")) {
                    return doLoadWorld(sender, CommandUtilities.commandRemover(cleanArgs), cleanArgs[0]);
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
                        return CommandUtilities.doSpawnWorld((Player) sender);
                    }
                }
            }
        }
        return false;
    }

}
