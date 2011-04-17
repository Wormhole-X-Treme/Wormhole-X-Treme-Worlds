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

/**
 * The Enum ResponseType for sending consistent messages.
 */
public enum ResponseType {

    /** HEADER. */
    HEADER("\u00A73:: "),

    /** FOOTER. */
    FOOTER("\u00A77 "),

    /** The NORMAL HEADER for normal messages. */
    NORMAL_HEADER(HEADER.toString() + FOOTER.toString()),

    /** The ERROR HEADER for error messages. */
    ERROR_HEADER(HEADER.toString() + " \u00A75error " + NORMAL_HEADER.toString()),

    /** The NO PERMISSION ERROR message. */
    ERROR_PERMISSION_NO(ERROR_HEADER.toString() + "You lack the permissions to do this."),

    /** The IN GAME ONLY ERROR message. */
    ERROR_IN_GAME_ONLY(ERROR_HEADER.toString() + "This command requires a player to call it, not a console."),

    /** The WORLD DOES NOT EXIST ERROR message. */
    ERROR_WORLD_NOT_EXIST(ERROR_HEADER.toString() + "World does not exist or is not loaded: "),

    /** The WORLD MAY BE ON DISK ERROR message. */
    ERROR_WORLD_MAY_BE_ON_DISK(ERROR_HEADER.toString() + "World may exist on disk, but has not been registered with Wormholw X-Treme Worlds."),

    /** The NO ARGS GIVEN ERROR message. */
    ERROR_NO_ARGS_GIVEN(ERROR_HEADER.toString() + "Command specified requires arguments: "),

    /** The COMMAND REQUIRED ARGS message. */
    NORMAL_COMMAND_REQUIRED_ARGS(NORMAL_HEADER.toString() + "Required: -name <world>"),

    /** The MODIFY COMMAND OPTIONAL ARGS message. */
    NORMAL_MODIFY_COMMAND_OPTIONAL_ARGS(NORMAL_HEADER.toString() + "Optional: -owner <player>, -(no)autoload, -(no)hostiles, -(no)neutrals"),
    
    /** The CREATE COMMAND OPTIONAL ARGS message. */
    NORMAL_CREATE_COMMAND_OPTIONAL_ARGS(NORMAL_HEADER.toString() + "Optional: -owner <player>, -seed <num>, -nether, -noautoload, -nohostiles, -noneutrals"),
    
    /** The COMMAND REQUIRES A WORLD NAME ERROR message. */
    ERROR_COMMAND_REQUIRES_WORLDNAME(ERROR_HEADER.toString() + "Command requires world name: "),

    /** The COMMAND REQUIRES NUMBER VALUE ERROR message. */
    ERROR_COMMAND_REQUIRES_NUMBER(ERROR_HEADER.toString() + "Command requires numeric value: "),

    /** The COMMAND REQUIRES OWNER SPECIFIED ON CONSOLE ERROR message. */
    ERROR_COMMAND_REQUIRES_OWNER_ON_CONSOLE(ERROR_HEADER.toString() + "Command requires an owner to be specified when called from console: "),

    /** The COMMAND REQUIRES AN OWNER ERROR message. */
    ERROR_COMMAND_REQUIRES_OWNER(ERROR_HEADER.toString() + "Command requires owner to be specified: "),
    
    /** The COMMAND ONLY AVAILABLE ON MANAGED WORLDS message. */
    ERROR_COMMAND_ONLY_MANAGED_WORLD(ERROR_HEADER.toString() + "Command only available on managed worlds: ");

    /** The response string. */
    private String responseString;

    /**
     * Instantiates a new response type.
     * 
     * @param responseString
     *            the response string
     */
    private ResponseType(final String responseString) {
        this.responseString = responseString;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return responseString;
    }
}
