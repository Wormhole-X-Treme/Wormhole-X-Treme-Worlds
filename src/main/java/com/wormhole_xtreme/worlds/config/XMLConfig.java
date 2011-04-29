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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.bukkit.plugin.PluginDescriptionFile;

import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager.ServerOptionKeys;
import com.wormhole_xtreme.worlds.world.TimeLockType;
import com.wormhole_xtreme.worlds.world.WeatherLockType;
import com.wormhole_xtreme.worlds.world.WorldManager;
import com.wormhole_xtreme.worlds.world.WormholeWorld;

/**
 * The Class WormholeXTtremeWorldsConfig.
 * 
 * @author alron
 */
public class XMLConfig {

    /** The Constant thisPlugin. */
    private final static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();

    /** The config file. */
    private static File configFile = null;

    /** The config directory. */
    private static File configDirectory = null;

    /** The world config directory. */
    private static File worldConfigDirectory = null;

    /**
     * Creates the node.
     * 
     * @param eventWriter
     *            the event writer
     * @param name
     *            the name
     * @param type
     *            the type
     * @param value
     *            the value
     * @param description
     *            the description
     * @throws XMLStreamException
     *             the xML stream exception
     */
    private static void createConfigNode(final XMLEventWriter eventWriter, final String name, final String type, final String value, final String description) throws XMLStreamException {
        final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        final XMLEvent end = eventFactory.createDTD("\n");
        final XMLEvent tab = eventFactory.createDTD("\t");

        eventWriter.add(tab);
        eventWriter.add(eventFactory.createComment(description));
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", name));
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "type"));
        eventWriter.add(eventFactory.createCharacters(type));
        eventWriter.add(eventFactory.createEndElement("", "", "type"));
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "value"));
        eventWriter.add(eventFactory.createCharacters(value));
        eventWriter.add(eventFactory.createEndElement("", "", "value"));
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createEndElement("", "", name));
        eventWriter.add(end);
        eventWriter.add(end);
    }

    private static void createHeaderFooter(final XMLEventWriter eventWriter, final boolean header) throws XMLStreamException {
        final XMLEventFactory eventFactory = XMLEventFactory.newFactory();
        final XMLEvent end = eventFactory.createDTD("\n");
        if (header) {
            eventWriter.add(eventFactory.createStartDocument());
            eventWriter.add(end);
            eventWriter.add(eventFactory.createStartElement("", "", "WormholeXTremeWorlds"));
            eventWriter.add(end);
        }
        else {
            eventWriter.add(eventFactory.createEndElement("", "", "WormholeXTremeWorlds"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());

        }
    }

    /**
     * @param worldName
     */
    public static void deleteXmlWorldConfig(final String worldName) {
        final File deleteWorld = new File(getWorldConfigDirectory() + File.separator + worldName + ".xml");
        if (deleteWorld.exists()) {
            if ( !deleteWorld.delete()) {
                thisPlugin.prettyLog(Level.WARNING, false, "Unable to delete config file for world: " + worldName);
            }
        }
    }

    /**
     * Gets the config directory.
     * 
     * @return the config directory
     */
    private static File getConfigDirectory() {
        return configDirectory;
    }

    /**
     * Gets the config file.
     * 
     * @return the config file
     */
    private static File getConfigFile() {
        return configFile;
    }

    /**
     * Gets the world config directory.
     * 
     * @return the worldConfigDirectory
     */
    private static File getWorldConfigDirectory() {
        return worldConfigDirectory;
    }

    /**
     * Load xml config.
     * 
     * @param desc
     *            the {@link PluginDescriptionFile}
     */
    public static void loadXmlConfig(final PluginDescriptionFile desc) {
        try {
            readXmlConfig(desc);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepare config directories.
     * 
     * @param desc
     *            the desc
     * @return true, if successful
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws XMLStreamException
     *             the xML stream exception
     */
    private static boolean prepareConfig(final PluginDescriptionFile desc) throws FileNotFoundException, XMLStreamException {
        setConfigDirectory(new File("plugins" + File.separator + desc.getName() + File.separator));
        setWorldConfigDirectory(new File(getConfigDirectory().getPath() + File.separator + "worlds" + File.separator));
        setConfigFile(new File(getConfigDirectory().getPath() + File.separator + "config.xml"));

        if ( !getConfigDirectory().exists()) {
            if (getConfigDirectory().mkdir()) {
                thisPlugin.prettyLog(Level.CONFIG, false, "Created config directory: " + getConfigDirectory().toString());
            }
            else {
                thisPlugin.prettyLog(Level.SEVERE, false, "Uable to create config directory: " + getConfigDirectory().toString());
                return false;
            }
        }
        if ( !getWorldConfigDirectory().exists()) {
            if (getWorldConfigDirectory().mkdir()) {
                thisPlugin.prettyLog(Level.CONFIG, false, "Created world config directory: " + getWorldConfigDirectory().toString());
            }
            else {
                thisPlugin.prettyLog(Level.SEVERE, false, "Uable to create world config directory: " + getWorldConfigDirectory().toString());
                return false;
            }
        }
        if ( !getConfigFile().exists()) {
            thisPlugin.prettyLog(Level.WARNING, false, "No configuration file found, generating fresh.");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(getConfigFile());
                saveServerConfigFile(fileOutputStream, DefaultOptions.defaultServerOptions);
            }
            finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                }
                catch (final IOException e) {
                }
            }
        }
        return true;
    }

    /**
     * Read config.
     * 
     * @param fileInputStream
     *            the file input stream
     * @throws XMLStreamException
     *             the xML stream exception
     * @throws FactoryConfigurationError
     *             the factory configuration error
     */
    private static void readConfigFile(final FileInputStream fileInputStream) throws XMLStreamException, FactoryConfigurationError {
        final XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(fileInputStream);
        XMLEvent event;

        while (eventReader.hasNext()) {
            ServerOptionKeys optionName = null;
            String optionType = null;
            Object optionValue = null;
            String optionDescription = null;
            boolean v = false;
            boolean t = false;
            int i = 0;
            while ((i < 2) && eventReader.hasNext()) {
                event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    final String elementType = event.asStartElement().getName().toString();
                    if (elementType.equals("type")) {
                        t = true;
                    }
                    else if (elementType.equals("value")) {
                        v = true;
                    }
                    else if (elementType.startsWith("serverOption")) {
                        try {
                            optionName = ServerOptionKeys.valueOf(elementType);
                        }
                        catch (final IllegalArgumentException e) {
                            optionName = null;
                        }
                    }
                }
                else if (event.isCharacters() && t) {
                    t = false;
                    i++;
                    optionType = event.asCharacters().getData();
                }
                else if (event.isCharacters() && v) {
                    v = false;
                    i++;
                    optionValue = event.asCharacters().getData();
                }
            }

            if (optionName != null) {
                final ServerOption[] defaultOptions = DefaultOptions.defaultServerOptions.clone();
                for (final ServerOption defaultOption : defaultOptions) {
                    if (defaultOption.getOptionKey() == optionName) {
                        if ((optionType == null) || ( !optionType.equals(defaultOption.getOptionType()))) {
                            optionType = defaultOption.getOptionType();
                        }
                        if ((optionValue == null) || !verifyOptionValue(optionType, optionValue)) {
                            optionValue = defaultOption.getOptionValue().toString();
                        }
                        optionDescription = defaultOption.getOptionDescription();
                        break;
                    }
                }

                thisPlugin.prettyLog(Level.CONFIG, false, "Got from XML read: " + optionName + ", " + optionDescription + ", " + optionType + ", " + optionValue);
                ConfigManager.serverOptions.put(optionName, new ServerOption(optionName, optionDescription, optionType, optionValue));
            }
        }
        for (final ServerOption defaultOption : DefaultOptions.defaultServerOptions) {
            if ( !ConfigManager.serverOptions.containsKey(defaultOption.getOptionKey())) {
                ConfigManager.serverOptions.put(defaultOption.getOptionKey(), defaultOption);
                thisPlugin.prettyLog(Level.CONFIG, false, "Added default config for missing ServerOption: " + defaultOption.getOptionKey().toString());
            }
        }
    }

    /**
     * Read world config file.
     * 
     * @param fileInputStream
     *            the file input stream
     * @throws XMLStreamException
     *             the xML stream exception
     * @throws FactoryConfigurationError
     *             the factory configuration error
     */
    private static void readWorldConfigFile(final FileInputStream fileInputStream) throws XMLStreamException, FactoryConfigurationError {
        final XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(fileInputStream);
        XMLEvent event;
        String worldName = null, worldOwner = null, worldCustomSpawn = null;
        TimeLockType timeLockType = TimeLockType.NONE;
        WeatherLockType weatherLockType = WeatherLockType.NONE;
        boolean allowHostiles = true, allowNeutrals = true;
        boolean netherWorld = false, autoconnectWorld = true;
        boolean allowPlayerDamage = true, allowPlayerDrown = true, allowPvP = true;
        final boolean allowPlayerLavaDamage = true;
        boolean allowPlayerFallDamage = true, allowPlayerLightningDamage = true, allowPlayerFireDamage = true;
        boolean allowFireSpread = true, allowLavaFire = true, allowLavaSpread = true, allowWaterSpread = true, allowLightningFire = true;

        long worldSeed = 0;
        while (eventReader.hasNext()) {
            String optionName = null;
            String optionType = null;
            Object optionValue = null;
            boolean v = false;
            boolean t = false;
            int i = 0;
            while ((i < 2) && eventReader.hasNext()) {
                event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    final String elementType = event.asStartElement().getName().toString();
                    if (elementType.equals("type")) {
                        t = true;
                    }
                    else if (elementType.equals("value")) {
                        v = true;
                    }
                    else {
                        optionName = elementType;
                    }
                }
                else if (event.isCharacters() && t) {
                    t = false;
                    i++;
                    optionType = event.asCharacters().getData();
                }
                else if (event.isCharacters() && v) {
                    v = false;
                    i++;
                    optionValue = event.asCharacters().getData();
                }
            }
            if ((optionName != null) && (optionValue != null)) {
                thisPlugin.prettyLog(Level.CONFIG, false, "Got from World XML read: " + optionName + ", " + optionType + ", " + optionValue);
                if (optionName.equals("worldName")) {
                    worldName = String.valueOf(optionValue).trim();
                }
                else if (optionName.equals("worldOwner")) {
                    worldOwner = String.valueOf(optionValue).trim();
                }
                else if (optionName.equals("worldCustomSpawn")) {
                    worldCustomSpawn = String.valueOf(optionValue).trim();
                }
                else if (optionName.equals("allowHostiles")) {
                    allowHostiles = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowNeutrals")) {
                    allowNeutrals = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("netherWorld")) {
                    netherWorld = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowPlayerDamage")) {
                    allowPlayerDamage = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowPlayerDrown")) {
                    allowPlayerDrown = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowFireSpread")) {
                    allowFireSpread = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowPlayerFallDamage")) {
                    allowPlayerFallDamage = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowPlayerFireDamage")) {
                    allowPlayerFireDamage = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowLavaFire")) {
                    allowLavaFire = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowLavaSpread")) {
                    allowLavaSpread = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowWaterSpread")) {
                    allowWaterSpread = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowLightningFire")) {
                    allowLightningFire = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowPlayerLightningDamage")) {
                    allowPlayerLightningDamage = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("autoconnectWorld")) {
                    autoconnectWorld = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("allowPvP")) {
                    allowPvP = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
                else if (optionName.equals("timeLockType")) {
                    timeLockType = TimeLockType.getTimeType(String.valueOf(optionValue).trim().toUpperCase());
                }
                else if (optionName.equals("weatherLockType")) {
                    weatherLockType = WeatherLockType.getWeatherType(String.valueOf(optionValue).trim().toUpperCase());
                }
                else if (optionName.equals("worldSeed")) {
                    try {
                        worldSeed = Long.valueOf(optionValue.toString().trim());
                    }
                    catch (final NumberFormatException e) {
                        final char[] seedCharArray = optionValue.toString().trim().toCharArray();
                        final StringBuilder seedString = new StringBuilder();
                        for (final char seedChar : seedCharArray) {
                            seedString.append((int) seedChar);
                        }
                        worldSeed = Long.valueOf(seedString.toString());
                    }
                }
            }
        }
        if ((worldName != null) && (worldOwner != null) && (worldCustomSpawn != null)) {
            final WormholeWorld world = new WormholeWorld();
            world.setWorldName(worldName);
            world.setWorldOwner(worldOwner);
            world.setTimeLockType(timeLockType);
            world.setWeatherLockType(weatherLockType);
            final int[] wcs = {
                Integer.valueOf(worldCustomSpawn.split("\\|")[0]), Integer.valueOf(worldCustomSpawn.split("\\|")[1]),
                Integer.valueOf(worldCustomSpawn.split("\\|")[2])
            };
            world.setWorldCustomSpawn(wcs);
            if ( !allowHostiles) {
                world.setAllowHostiles(allowHostiles);
            }
            if ( !allowNeutrals) {
                world.setAllowNeutrals(allowNeutrals);
            }
            if (netherWorld) {
                world.setNetherWorld(netherWorld);
            }
            if ( !autoconnectWorld) {
                world.setAutoconnectWorld(autoconnectWorld);
            }
            if ( !allowPvP) {
                world.setAllowPvP(allowPvP);
            }
            if ( !allowPlayerDamage) {
                world.setAllowPlayerDamage(allowPlayerDamage);
            }
            if ( !allowPlayerDrown) {
                world.setAllowPlayerDrown(allowPlayerDrown);
            }
            if ( !allowPlayerLavaDamage) {
                world.setAllowPlayerLavaDamage(allowPlayerLavaDamage);
            }
            if ( !allowPlayerFallDamage) {
                world.setAllowPlayerFallDamage(allowPlayerFallDamage);
            }
            if ( !allowPlayerLightningDamage) {
                world.setAllowPlayerLightningDamage(allowPlayerLightningDamage);
            }
            if ( !allowPlayerFireDamage) {
                world.setAllowPlayerFireDamage(allowPlayerFireDamage);
            }
            if ( !allowFireSpread) {
                world.setAllowFireSpread(allowFireSpread);
            }
            if ( !allowLavaFire) {
                world.setAllowLavaFire(allowLavaFire);
            }
            if ( !allowLavaSpread) {
                world.setAllowLavaSpread(allowLavaSpread);
            }
            if ( !allowWaterSpread) {
                world.setAllowWaterSpread(allowWaterSpread);
            }
            if ( !allowLightningFire) {
                world.setAllowLightningFire(allowLightningFire);
            }
            if (worldSeed != 0) {
                world.setWorldSeed(worldSeed);
            }
            WorldManager.addWorld(world);
        }
    }

    /**
     * Read xml config.
     * 
     * @param desc
     *            the {@link PluginDescriptionFile}
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws XMLStreamException
     *             the xML stream exception
     */
    private static void readXmlConfig(final PluginDescriptionFile desc) throws FileNotFoundException, XMLStreamException {
        if ( !prepareConfig(desc)) {
            return;
        }
        if (getConfigFile().exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(getConfigFile());
                readConfigFile(fileInputStream);
                thisPlugin.prettyLog(Level.INFO, false, "Config Loaded");
            }
            finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                }
                catch (final IOException e) {
                }
            }
        }
        if (getWorldConfigDirectory().exists()) {
            for (final File worldFile : getWorldConfigDirectory().listFiles()) {
                if ( !worldFile.isDirectory() && !worldFile.isHidden() && worldFile.getPath().endsWith("xml")) {
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(worldFile);
                        readWorldConfigFile(fileInputStream);
                        thisPlugin.prettyLog(Level.INFO, false, "World Config Loaded: " + worldFile.getName());
                    }
                    finally {
                        try {
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                        }
                        catch (final IOException e) {
                        }
                    }
                }
            }
        }
    }

    private static void saveServerConfigFile(final FileOutputStream fileOutputStream, final ServerOption[] options) throws XMLStreamException {
        final XMLEventWriter eventWriter = XMLOutputFactory.newInstance().createXMLEventWriter(fileOutputStream);
        createHeaderFooter(eventWriter, true);
        for (final ServerOption element : options) {
            createConfigNode(eventWriter, element.getOptionKey().toString(), element.getOptionType(), element.getOptionValue().toString(), element.getOptionDescription());
        }
        createHeaderFooter(eventWriter, false);
        eventWriter.close();
    }

    private static void saveWorldConfigFile(final FileOutputStream fileOutputStream, final WormholeWorld world) throws XMLStreamException {
        final XMLEventWriter eventWriter = XMLOutputFactory.newInstance().createXMLEventWriter(fileOutputStream);
        createHeaderFooter(eventWriter, true);
        createConfigNode(eventWriter, "worldName", "String", world.getWorldName(), "The name of this world. Do not change unless you have renamed the world on disk.");
        createConfigNode(eventWriter, "worldOwner", "String", world.getWorldOwner(), "The owner of this world. Can be any player. Factors into permissions and iConomy support.");
        createConfigNode(eventWriter, "netherWorld", "boolean", Boolean.valueOf(world.isNetherWorld()).toString(), "Is this a nether world? BE SURE TO HAVE THIS RIGHT!");
        createConfigNode(eventWriter, "autoconnectWorld", "boolean", Boolean.valueOf(world.isAutoconnectWorld()).toString(), "Does this world automatically get loaded at server start? Non connected worlds can be loaded in game as needed.");
        createConfigNode(eventWriter, "allowHostiles", "boolean", Boolean.valueOf(world.isAllowHostiles()).toString(), "Are hostiles allowed on this world?");
        createConfigNode(eventWriter, "allowNeutrals", "boolean", Boolean.valueOf(world.isAllowNeutrals()).toString(), "Are neutrals allowed on this world?");
        createConfigNode(eventWriter, "allowPlayerDamage", "boolean", Boolean.valueOf(world.isAllowPlayerDamage()).toString(), "Can players take damage on this world?");
        createConfigNode(eventWriter, "allowPlayerDrown", "boolean", Boolean.valueOf(world.isAllowPlayerDrown()).toString(), "Can players drown on this world?");
        createConfigNode(eventWriter, "allowPvP", "boolean", Boolean.valueOf(world.isAllowPvP()).toString(), "Does this world allow PvP?");
        createConfigNode(eventWriter, "allowPlayerLavaDamage", "boolean", Boolean.valueOf(world.isAllowPlayerLavaDamage()).toString(), "Do players take lava damage on this world?");
        createConfigNode(eventWriter, "allowPlayerFallDamage", "boolean", Boolean.valueOf(world.isAllowPlayerFallDamage()).toString(), "Do players take fall damage on this world?");
        createConfigNode(eventWriter, "allowPlayerLightningDamage", "boolean", Boolean.valueOf(world.isAllowPlayerLightningDamage()).toString(), "Do players take damage from lightning on this world?");
        createConfigNode(eventWriter, "allowPlayerFireDamage", "boolean", Boolean.valueOf(world.isAllowPlayerFireDamage()).toString(), "Do players take fire damage on this world?");
        createConfigNode(eventWriter, "allowFireSpread", "boolean", Boolean.valueOf(world.isAllowFireSpread()).toString(), "Is fire spread allowed on this world?");
        createConfigNode(eventWriter, "allowLavaFire", "boolean", Boolean.valueOf(world.isAllowLavaFire()).toString(), "Is lava fire allowed on this world?");
        createConfigNode(eventWriter, "allowLavaSpread", "boolean", Boolean.valueOf(world.isAllowLavaSpread()).toString(), "Is lava spread allowed on this world?");
        createConfigNode(eventWriter, "allowWaterSpread", "boolean", Boolean.valueOf(world.isAllowWaterSpread()).toString(), "Does water spread happen on this world?");
        createConfigNode(eventWriter, "allowLightningFire", "boolean", Boolean.valueOf(world.isAllowLightningFire()).toString(), "Is lightning fire allowed on this world?");
        createConfigNode(eventWriter, "timeLockType", "TimeType", world.getTimeLockType().toString(), "What time lock type this world has enabled. DAY, NIGHT, NONE. Anything else becomes NONE.");
        createConfigNode(eventWriter, "weatherLockType", "WeatherType", world.getWeatherLockType().toString(), "What weather lock type this world has enabled. CLEAR, RAIN, STORM, NONE. Anything else becomes NONE.");
        createConfigNode(eventWriter, "worldCustomSpawn", "int[]", world.getWorldCustomSpawn()[0] + "|" + world.getWorldCustomSpawn()[1] + "|" + world.getWorldCustomSpawn()[2], "World custom spawn location in X|Y|Z ints.");
        createConfigNode(eventWriter, "worldSeed", "long", Long.valueOf(world.getWorldSeed()).toString(), "The seed used when this world was generated. Can be used to generate a new world with the exact same terrain.");
        createHeaderFooter(eventWriter, false);
        eventWriter.close();
    }

    /**
     * Save xml config.
     * 
     * @param desc
     *            the {@link PluginDescriptionFile}
     */
    public static void saveXmlConfig(final PluginDescriptionFile desc) {
        try {
            writeXmlConfig(desc);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the config directory.
     * 
     * @param configDirectory
     *            the new config directory
     */
    private static void setConfigDirectory(final File configDirectory) {
        XMLConfig.configDirectory = configDirectory;
    }

    /**
     * Sets the config file.
     * 
     * @param configFile
     *            the new config file
     */
    private static void setConfigFile(final File configFile) {
        XMLConfig.configFile = configFile;
    }

    /**
     * Sets the world config directory.
     * 
     * @param worldConfigDirectory
     *            the worldConfigDirectory to set
     */
    private static void setWorldConfigDirectory(final File worldConfigDirectory) {
        XMLConfig.worldConfigDirectory = worldConfigDirectory;
    }

    /**
     * Verify option value.
     * 
     * @param optionType
     *            the option type
     * @param optionValue
     *            the option value
     * @return true, if successful
     */
    private static boolean verifyOptionValue(final String optionType, final Object optionValue) {
        if (optionType.trim().equals("boolean")) {
            final String oV = (String) optionValue;
            if (oV.contains("true") || oV.contains("false")) {
                return true;
            }
        }
        else if (optionType.trim().equals("double")) {
            try {
                final double test = Double.valueOf(((String) optionValue).trim()).doubleValue();
                if (test >= 0.0) {
                    return true;
                }
            }
            catch (final NumberFormatException nfe) {
                return false;
            }
        }
        return false;
    }

    /**
     * Write xml config.
     * 
     * @param desc
     *            the {@link PluginDescriptionFile}
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws XMLStreamException
     *             the xML stream exception
     */
    private static void writeXmlConfig(final PluginDescriptionFile desc) throws FileNotFoundException, XMLStreamException {
        if ( !prepareConfig(desc)) {
            return;
        }

        if (getConfigDirectory().exists()) {
            final Set<ServerOptionKeys> keys = ConfigManager.serverOptions.keySet();
            final ArrayList<ServerOptionKeys> list = new ArrayList<ServerOptionKeys>(keys);
            Collections.sort(list);
            final ServerOption[] optionArray = new ServerOption[list.size()];
            int i = 0;
            for (final ServerOptionKeys key : list) {
                final ServerOption o = ConfigManager.serverOptions.get(key);
                if ((o != null) && (o.getOptionKey() != null) && (o.getOptionDescription() != null) && (o.getOptionType() != null) && (o.getOptionValue() != null)) {
                    optionArray[i] = new ServerOption(o.getOptionKey(), o.getOptionDescription(), o.getOptionType(), o.getOptionValue());
                    i++;
                }
            }

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(getConfigFile());
                saveServerConfigFile(fileOutputStream, optionArray);
                thisPlugin.prettyLog(Level.INFO, false, "Configuration saved.");

            }
            finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                }
                catch (final IOException e) {
                }
            }
        }
        if (getWorldConfigDirectory().exists()) {
            final WormholeWorld[] wormholeWorlds = WorldManager.getAllWorlds();
            for (final WormholeWorld wormholeWorld : wormholeWorlds) {
                if (wormholeWorld != null) {
                    final String worldName = wormholeWorld.getWorldName();
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(getWorldConfigDirectory() + File.separator + worldName + ".xml");
                        saveWorldConfigFile(fileOutputStream, wormholeWorld);
                        thisPlugin.prettyLog(Level.INFO, false, "World Configuration saved: " + worldName);
                    }
                    finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                        }
                        catch (final IOException e) {
                        }
                    }
                }
            }
        }
    }
}
