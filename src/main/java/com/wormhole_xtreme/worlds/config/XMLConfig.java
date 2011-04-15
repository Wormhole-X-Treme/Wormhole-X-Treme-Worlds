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

        if (!getConfigDirectory().exists()) {
            if (getConfigDirectory().mkdir()) {
                thisPlugin.prettyLog(Level.CONFIG, false, "Created config directory: " + getConfigDirectory().toString());
            }
            else {
                thisPlugin.prettyLog(Level.SEVERE, false, "Uable to create config directory: " + getConfigDirectory().toString());
                return false;
            }
        }
        if (!getWorldConfigDirectory().exists()) {
            if (getWorldConfigDirectory().mkdir()) {
                thisPlugin.prettyLog(Level.CONFIG, false, "Created world config directory: " + getWorldConfigDirectory().toString());
            }
            else {
                thisPlugin.prettyLog(Level.SEVERE, false, "Uable to create world config directory: " + getWorldConfigDirectory().toString());
                return false;
            }
        }
        if (!getConfigFile().exists()) {
            thisPlugin.prettyLog(Level.WARNING, false, "No configuration file found, generating fresh.");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(getConfigFile());
                saveConfigFile(fileOutputStream, DefaultOptions.defaultServerOptions, null);
            }
            finally {
                try {
                    fileOutputStream.close();
                }
                catch (final IOException e) {
                }
            }
        }
        return true;
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
        if (!prepareConfig(desc)) {
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
                if (o != null) {
                    optionArray[i] = new ServerOption(o.getOptionKey(), o.getOptionDescription(), o.getOptionType(), o.getOptionValue(), o.getOptionPlugin());
                    i++;
                }
            }

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(getConfigFile());
                saveConfigFile(fileOutputStream, optionArray, null);
                thisPlugin.prettyLog(Level.INFO, false, "Configuration saved.");

            }
            finally {
                try {
                    fileOutputStream.close();
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
                        saveConfigFile(fileOutputStream, null, wormholeWorld);
                        thisPlugin.prettyLog(Level.INFO, false, "World Configuration saved: " + worldName);
                    }
                    finally {
                        try {
                            fileOutputStream.close();
                        }
                        catch (final IOException e) {
                        }
                    }
                }
            }

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
        if (!prepareConfig(desc)) {
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
                    fileInputStream.close();
                }
                catch (final IOException e) {
                }
            }
        }
        if (getWorldConfigDirectory().exists()) {
            for (final File worldFile : getWorldConfigDirectory().listFiles()) {
                if (!worldFile.isDirectory() && !worldFile.isHidden() && worldFile.getPath().endsWith("xml")) {
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(worldFile);
                        readWorldConfigFile(fileInputStream);
                        thisPlugin.prettyLog(Level.INFO, false, "World Config Loaded: " + worldFile.getName());
                    }
                    finally {
                        try {
                            fileInputStream.close();
                        }
                        catch (final IOException e) {
                        }
                    }
                }
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
        boolean allowHostiles = true, allowNeutrals = true, netherWorld = false, autoconnectWorld = true;
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
            if (optionName != null) {
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
                else if (optionName.equals("autoconnectWorld")) {
                    autoconnectWorld = Boolean.valueOf(optionValue.toString().trim().toLowerCase());
                }
            }
        }
        if ((worldName != null) && (worldOwner != null) && (worldCustomSpawn != null)) {
            final WormholeWorld world = new WormholeWorld();
            world.setWorldName(worldName);
            world.setWorldOwner(worldOwner);
            final int[] wcs = {
                Integer.valueOf(worldCustomSpawn.split("\\|")[0]),Integer.valueOf(worldCustomSpawn.split("\\|")[1]),
                Integer.valueOf(worldCustomSpawn.split("\\|")[2])};
            world.setWorldCustomSpawn(wcs);
            if (!allowHostiles) {
                world.setAllowHostiles(allowHostiles);
            }
            if (!allowNeutrals) {
                world.setAllowHostiles(allowNeutrals);
            }
            if (netherWorld) {
                world.setNetherWorld(netherWorld);
            }
            if (!autoconnectWorld) {
                world.setAutoconnectWorld(autoconnectWorld);
            }
            WorldManager.addWorld(world);
        }
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
                        optionName = ServerOptionKeys.valueOf(elementType);
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
                        if ((optionType == null) || (!optionType.equals(defaultOption.getOptionType()))) {
                            optionType = defaultOption.getOptionType();
                        }
                        if ((optionValue == null) || !verifyOptionValue(optionType, optionValue)) {
                            optionValue = defaultOption.getOptionValue().toString();
                        }
                        optionDescription = defaultOption.getOptionDescription();
                        break;
                    }
                }

                thisPlugin.prettyLog(Level.CONFIG, false, "Got from XML read: " + optionName + ", " + optionDescription + ", " + optionType + ", " + optionValue + ", WormholeXTremeWorlds");
                ConfigManager.serverOptions.put(optionName, new ServerOption(optionName, optionDescription, optionType, optionValue, "WormholeXTremeWorlds"));
            }
        }
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
     * Save config.
     * 
     * @param fileOutputStream
     *            the file stream
     * @param options
     *            the options
     * @param world
     *            the world
     * @throws XMLStreamException
     *             the xML stream exception
     * @throws FactoryConfigurationError
     *             the factory configuration error
     */
    private static void saveConfigFile(final FileOutputStream fileOutputStream, final ServerOption[] options, final WormholeWorld world) throws XMLStreamException, FactoryConfigurationError {
        final XMLEventWriter eventWriter = XMLOutputFactory.newInstance().createXMLEventWriter(fileOutputStream);
        final XMLEventFactory eventFactory = XMLEventFactory.newFactory();
        final XMLEvent end = eventFactory.createDTD("\n");

        eventWriter.add(eventFactory.createStartDocument());
        eventWriter.add(end);
        eventWriter.add(eventFactory.createStartElement("", "", "WormholeXTremeWorlds"));
        eventWriter.add(end);
        if (options != null) {
            for (final ServerOption element : options) {
                createConfigNode(eventWriter, element.getOptionKey().toString(), element.getOptionType(), element.getOptionValue().toString(), element.getOptionDescription());
            }
        }
        else if (world != null) {
            createConfigNode(eventWriter, "worldName", "String", world.getWorldName(), "The name of this world. Do not change unless you have renamed the world on disk.");
            createConfigNode(eventWriter, "worldOwner", "String", world.getWorldOwner(), "The owner of this world. Can be any player. Factors into permissions and iConomy support.");
            createConfigNode(eventWriter, "worldCustomSpawn", "int[]", world.getWorldCustomSpawn()[0] + "|" + world.getWorldCustomSpawn()[1] + "|" + world.getWorldCustomSpawn()[2], "World custom spawn location in X|Y|Z ints.");
            createConfigNode(eventWriter, "allowHostiles", "boolean", Boolean.valueOf(world.isAllowHostiles()).toString(), "Are hostiles allowed on this world?");
            createConfigNode(eventWriter, "allowNeutrals", "boolean", Boolean.valueOf(world.isAllowNeutrals()).toString(), "Are neutrals allowed on this world?");
            createConfigNode(eventWriter, "netherWorld", "boolean", Boolean.valueOf(world.isNetherWorld()).toString(), "Is this a nether world? BE SURE TO HAVE THIS RIGHT!");
            createConfigNode(eventWriter, "autoconnectWorld", "boolean", Boolean.valueOf(world.isAutoconnectWorld()).toString(), "Does this world automatically get loaded at server start? Non connected worlds can be loaded in game as needed.");
        }
        eventWriter.add(eventFactory.createEndElement("", "", "WormholeXTremeWorlds"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }


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
     * Gets the config file.
     * 
     * @return the config file
     */
    private static File getConfigFile() {
        return configFile;
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
     * Gets the config directory.
     * 
     * @return the config directory
     */
    private static File getConfigDirectory() {
        return configDirectory;
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
     * Sets the world config directory.
     * 
     * @param worldConfigDirectory
     *            the worldConfigDirectory to set
     */
    private static void setWorldConfigDirectory(final File worldConfigDirectory) {
        XMLConfig.worldConfigDirectory = worldConfigDirectory;
    }

    /**
     * @param worldName
     */
    public static void deleteXmlWorldConfig(final String worldName) {
        final File deleteWorld = new File(getWorldConfigDirectory() + File.separator + worldName + ".xml");
        if (deleteWorld.exists()) {
            if (!deleteWorld.delete()) {
                thisPlugin.prettyLog(Level.WARNING, false, "Unable to delete config file for world: " + worldName);
            }
        }
    }
}
