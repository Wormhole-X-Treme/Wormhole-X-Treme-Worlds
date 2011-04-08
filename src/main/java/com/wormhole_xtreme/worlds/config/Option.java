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
 * The Class Option.
 *
 * @author alron
 */
public class Option {
    
    /** The option key. */
    private OptionKeys optionKey;
    
    /** The option description. */
    private String optionDescription;
   
    /** The option type. */
    private String optionType;
    
    /** The option value. */
    private Object optionValue;
   
    /** The option plugin. */
    private String optionPlugin;
    
    /**
     * Instantiates a new option.
     *
     * @param optionKey the option key
     * @param optionDescription the option description
     * @param optionType the option type
     * @param optionValue the option value
     * @param optionPlugin the option plugin
     */
    public Option(OptionKeys optionKey, String optionDescription, String optionType, Object optionValue, String optionPlugin )
    {
        if (optionKey != null && optionDescription != null && optionValue != null && optionPlugin != null)
        {
            this.setOptionKey(optionKey);
            this.setOptionDescription(optionDescription);
            this.setOptionType(optionType);
            this.setOptionValue(optionValue);
            this.setOptionPlugin(optionPlugin);
        }
    }

    /**
     * Sets the option key.
     *
     * @param optionKey the new option key
     */
    private void setOptionKey(OptionKeys optionKey) {
        this.optionKey = optionKey;
    }

    /**
     * Gets the option key.
     *
     * @return the option key
     */
    public OptionKeys getOptionKey() {
        return optionKey;
    }

    /**
     * Sets the option description.
     *
     * @param optionDescription the new option description
     */
    private void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    /**
     * Gets the option description.
     *
     * @return the option description
     */
    public String getOptionDescription() {
        return optionDescription;
    }

    /**
     * Sets the option value.
     *
     * @param optionValue the new option value
     */
    public void setOptionValue(Object optionValue) {
        this.optionValue = optionValue;
    }

    /**
     * Gets the option value.
     *
     * @return the option value
     */
    public Object getOptionValue() {
        return optionValue;
    }

    /**
     * Sets the option plugin.
     *
     * @param optionPlugin the new option plugin
     */
    private void setOptionPlugin(String optionPlugin) {
        this.optionPlugin = optionPlugin;
    }

    /**
     * Gets the option plugin.
     *
     * @return the option plugin
     */
    public String getOptionPlugin() {
        return optionPlugin;
    }

    /**
     * Sets the option type.
     *
     * @param optionType the new option type
     */
    private void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    /**
     * Gets the option type.
     *
     * @return the option type
     */
    public String getOptionType() {
        return optionType;
    }
}
