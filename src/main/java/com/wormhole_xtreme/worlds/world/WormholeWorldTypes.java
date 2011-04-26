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
package com.wormhole_xtreme.worlds.world;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class WormholeWorldTypes.
 * 
 * @author alron
 */
public class WormholeWorldTypes {

    /**
     * The times we support locking a world to.
     */
    public static enum TimeType {

        /** No specific time. */
        NONE(0),

        /** Day. */
        DAY(1),

        /** Night. */
        NIGHT(2);

        /** The id. */
        private final int id;

        /** The Constant lookupId. */
        private static final Map<Integer, TimeType> lookupId = new HashMap<Integer, TimeType>();

        /** The Constant lookupName. */
        private static final Map<String, TimeType> lookupName = new HashMap<String, TimeType>();

        static {
            for (final TimeType timeType : values()) {
                lookupId.put(timeType.getId(), timeType);
                lookupName.put(timeType.name(), timeType);
            }
        }

        /**
         * Gets the time type.
         * 
         * @param id
         *            the id
         * @return the time type
         */
        public static TimeType getTimeType(final int id) {
            return lookupId.get(id);
        }

        /**
         * Gets the time type.
         * 
         * @param name
         *            the name
         * @return the time type
         */
        public static TimeType getTimeType(final String name) {
            final TimeType tT = lookupName.get(name);
            return tT != null ? tT : TimeType.NONE;
        }

        /**
         * Instantiates a new time type.
         * 
         * @param id
         *            the id
         */
        private TimeType(final int id) {
            this.id = id;
        }

        /**
         * Gets the id.
         * 
         * @return the id
         */
        public int getId() {
            return id;
        }
    }

    /**
     * The types of weather we support locking a world to.
     */
    public static enum WeatherType {

        /** No specific weather. */
        NONE(0),

        /** Clear Skys. */
        CLEAR(1),

        /** Rainy weather. */
        RAIN(2),

        /** Thunderstorms. */
        STORM(3);

        /** The id. */
        private final int id;

        /** The Constant lookupId. */
        private static final Map<Integer, WeatherType> lookupId = new HashMap<Integer, WeatherType>();

        /** The Constant lookupName. */
        private static final Map<String, WeatherType> lookupName = new HashMap<String, WeatherType>();

        static {
            for (final WeatherType weatherType : values()) {
                lookupId.put(weatherType.getId(), weatherType);
                lookupName.put(weatherType.name(), weatherType);
            }
        }

        /**
         * Gets the weather type.
         * 
         * @param id
         *            the id
         * @return the weather type
         */
        public static WeatherType getWeatherType(final int id) {
            return lookupId.get(id);
        }

        /**
         * Gets the weather type.
         * 
         * @param name
         *            the name
         * @return the weather type
         */
        public static WeatherType getWeatherType(final String name) {
            final WeatherType wT = lookupName.get(name);
            return wT != null ? wT : WeatherType.NONE;
        }

        /**
         * Instantiates a new weather type.
         * 
         * @param id
         *            the id
         */
        private WeatherType(final int id) {
            this.id = id;
        }

        /**
         * Gets the id.
         * 
         * @return the id
         */
        public int getId() {
            return id;
        }
    }
}
