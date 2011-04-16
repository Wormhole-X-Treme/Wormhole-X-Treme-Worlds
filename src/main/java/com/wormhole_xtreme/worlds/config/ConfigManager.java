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


/**
 * The Class ConfigManager.
 * 
 * @author alron
 */
public class ConfigManager {

    /** The Constant options. */
    public static final ConcurrentHashMap<ServerOptionKeys, ServerOption> serverOptions = new ConcurrentHashMap<ServerOptionKeys, ServerOption>();

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
     * Gets the server option.
     * 
     * @param serverOptionKey
     *            the server option key
     * @return the server option
     */
    private static ServerOption getServerOption(final ServerOptionKeys serverOptionKey) {
        return serverOptions.get(serverOptionKey);
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
     * Gets the server option permissions.
     * 
     * @return the server option permissions
     */
    public static boolean getServerOptionPermissions() {
        final ServerOption o = getServerOption(ServerOptionKeys.serverOptionPermissions);
        if (o != null) {
            return Boolean.valueOf(o.getOptionValue().toString());
        }
        else {
            return true;
        }
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
     * Gets the server option ops bypass permissions.
     * 
     * @return the server option ops bypass permissions
     */
    public static boolean getServerOptionOpsBypassPermissions() {
        final ServerOption o = getServerOption(ServerOptionKeys.serverOptionOpsBypassPermissions);
        if (o != null) {
            return Boolean.valueOf(o.getOptionValue().toString());
        }
        else {
            return true;
        }
    }

    /**
     * Sets the server option iconomy.
     * 
     * @param b
     *            the new server option iconomy
     */
//    public static void setServerOptionIconomy(final boolean b) {
//        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionIconomy, b);
//    }

    /**
     * Gets the server option iconomy.
     * 
     * @return the server option iconomy
     */
//    public static boolean getServerOptionIconomy() {
//        final ServerOption o = getServerOption(ServerOptionKeys.serverOptionIconomy);
//        if (o != null) {
//            return Boolean.valueOf(o.getOptionValue().toString());
//        }
//        else {
//            return true;
//        }
//    }

    /**
     * Sets the server option ops bypass iconomy.
     * 
     * @param b
     *            the new server option ops bypass iconomy
     */
//    public static void setServerOptionOpsBypassIconomy(final boolean b) {
//        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionOpsBypassIconomy, b);
//    }

    /**
     * Gets the server option ops bypass iconomy.
     * 
     * @return the server option ops bypass iconomy
     */
//    public static boolean getServerOptionOpsBypassIconomy() {
//        final ServerOption o = getServerOption(ServerOptionKeys.serverOptionOpsBypassIconomy);
//        if (o != null) {
//            return Boolean.valueOf(o.getOptionValue().toString());
//        }
//        else {
//            return true;
//        }
//    }

    /**
     * Sets the iconomy cost for normal world generation.
     * 
     * @param d
     *            the new iconomy cost for normal world generation
     */
//    public static void setIconomyCostForNormalWorldGeneration(final double d) {
//        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionIconomyCostForNormalWorldGeneration, d);
//    }

    /**
     * Sets the iconomy cost for nether world generation.
     * 
     * @param d
     *            the new iconomy cost for nether world generation
     */
//    public static void setIconomyCostForNetherWorldGeneration(final double d) {
//        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionIconomyCostForNetherWorldGeneration, d);
//    }

    /**
     * Sets the server option help.
     * 
     * @param b
     *            the new server option help
     */
    public static void setServerOptionHelp(final boolean b) {
        ConfigManager.setOptionValue(ServerOptionKeys.serverOptionHelp, b);
    }

    /**
     * Gets the server option help.
     * 
     * @return the server option help
     */
    public static boolean getServerOptionHelp() {
        final ServerOption o = getServerOption(ServerOptionKeys.serverOptionHelp);
        if (o != null) {
            return Boolean.valueOf(o.getOptionValue().toString());
        }
        else {
            return true;
        }
    }

    /**
     * The Enum OptionKeys.
     */
    public enum ServerOptionKeys {

        /** The server option permissions. */
        serverOptionPermissions,

        /** The server option ops bypass permissions. */
        serverOptionOpsBypassPermissions,
        
        /** The server option help. */
        serverOptionHelp
        
        /** The server option iconomy. */
        //serverOptionIconomy,

        /** The server option ops bypass iconomy. */
        //serverOptionOpsBypassIconomy,

        /** The server option iconomy cost for normal world generation. */
        //serverOptionIconomyCostForNormalWorldGeneration,

        /** The server option iconomy cost for nether world generation. */
        //serverOptionIconomyCostForNetherWorldGeneration,


    }

    /**
     * The Enum WorldOptionKeys.
     */
    public enum WorldOptionKeys {

        /** The world option nether. */
        worldOptionNether,

        /** The world option no hostiles. */
        worldOptionNoHostiles,

        /** The world option no neutrals. */
        worldOptionNoNeutrals,

        /** The world option seed. */
        worldOptionSeed,

        /** The world option no connect. */
        worldOptionNoConnect
    }
}
