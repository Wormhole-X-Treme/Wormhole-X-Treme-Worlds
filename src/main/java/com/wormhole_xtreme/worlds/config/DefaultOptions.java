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
package com.wormhole_xtreme.worlds.config;

import com.wormhole_xtreme.worlds.config.ConfigManager.OptionKeys;

/**
 * @author alron
 *
 */
public class DefaultOptions {

    /** The Constant defaultOptions. */
    final static Option[] defaultOptions = {
        new Option(
                   OptionKeys.serverOptionPermissions,
                   "Enable or disable Permissions plugin support.",
                   "boolean",
                   true,
                   "WormholeXTremeWorlds"
        ),
        new Option(
                   OptionKeys.serverOptionOpsBypassPermissions,
                   "Ops bypass Permissions plugin access checks.",
                   "boolean",
                   true,
                   "WormholeXTremeWorlds"
        ),
        new Option(
                   OptionKeys.serverOptionIconomy,
                   "Enable or disable iConomy plugin support.",
                   "boolean",
                   true,
                   "WormholeXTremeWorlds"
        ),
        new Option(
                   OptionKeys.serverOptionIconomyCostForNormalWorldGeneration,
                   "iConomy cost to generate a normal world.",
                   "double",
                   1000.0,
                   "WormholeXTremeWorlds"
        ),
        new Option(
                   OptionKeys.serverOptionIconomyCostForNetherWorldGeneration,
                   "iConomy cost to generate a nether world.",
                   "double",
                   2000.0,
                   "WormholeXTremeWorlds"
        ),
        new Option(
                   OptionKeys.serverOptionHelp,
                   "Enable or disable Help plugin support.",
                   "boolean",
                   true,
                   "WormholeXTremeWorlds"
        ),
    };
}
