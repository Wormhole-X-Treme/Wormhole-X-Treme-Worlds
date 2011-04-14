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
package com.wormhole_xtreme.worlds.config;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.PluginDescriptionFile;


/**
 * The Class ConfigManager.
 * 
 * @author alron
 */
public class ConfigManager {

    /** The Constant options. */
    public static final ConcurrentHashMap<ServerOptionKeys, ServerOption> serverOptions = new ConcurrentHashMap<ServerOptionKeys, ServerOption>();

    /**
     * Sets the up options.
     * 
     * @param desc
     *            the new up options
     */
    public static void setupOptions(final PluginDescriptionFile desc) {
        XMLConfig.loadXmlConfig(desc);
    }

    /**
     * Sets the option value.
     * 
     * @param optionKey
     *            the option key
     * @param optionValue
     *            the option value
     */
    private static void setOptionValue(final ServerOptionKeys optionKey, final Object optionValue) {
        final ServerOption o = serverOptions.get(optionKey);
        if (optionValue != null) {
            o.setOptionValue(optionValue);
        }
    }

    /**
     * Sets the server option permissions.
     * 
     * @param b
     *            the new server option permissions
     */
    public static void setServerOptionPermissions(final boolean b) {
        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionPermissions, b);
    }

    /**
     * Sets the server option ops bypass permissions.
     * 
     * @param b
     *            the new server option ops bypass permissions
     */
    public static void setServerOptionOpsBypassPermissions(final boolean b) {
        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionOpsBypassPermissions, b);
    }

    /**
     * Sets the server option iconomy.
     * 
     * @param b
     *            the new server option iconomy
     */
    public static void setServerOptionIconomy(final boolean b) {
        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionIconomy, b);
    }

    /**
     * Sets the iconomy cost for normal world generation.
     * 
     * @param d
     *            the new iconomy cost for normal world generation
     */
    public static void setIconomyCostForNormalWorldGeneration(final double d) {
        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionIconomyCostForNormalWorldGeneration, d);
    }

    /**
     * Sets the iconomy cost for nether world generation.
     * 
     * @param d
     *            the new iconomy cost for nether world generation
     */
    public static void setIconomyCostForNetherWorldGeneration(final double d) {
        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionIconomyCostForNetherWorldGeneration, d);
    }

    /**
     * Sets the world option world list.
     * 
     * @param s
     *            the new world option world list
     */
    public static void setWorldOptionWorldList(final String[] s) {
        ConfigManager.setOptionValue(ServerOptionKeys.worldOptionWorldList, s);
    }

    /**
     * The Enum OptionKeys.
     */
    public enum ServerOptionKeys {

        /** The server option permissions. */
        serverOptionPermissions,

        /** The server option ops bypass permissions. */
        serverOptionOpsBypassPermissions,

        /** The server option iconomy. */
        serverOptionIconomy,

        /** The server option iconomy cost for normal world generation. */
        serverOptionIconomyCostForNormalWorldGeneration,

        /** The server option iconomy cost for nether world generation. */
        serverOptionIconomyCostForNetherWorldGeneration,

        /** The server option help. */
        serverOptionHelp,

        /** The world option world list. */
        worldOptionWorldList,

        /** The world option normal defaults. */
        worldOptionNormalDefaults,

        /** The world option nether defaults. */
        worldOptionNetherDefaults;
    }

    public enum WorldOptionKeys {
        worldOptionNether,
        worldOptionNoHostiles,
        worldOptionNoNeutrals,
        worldOptionSeed,
        worldOptionConnect
    }
}
