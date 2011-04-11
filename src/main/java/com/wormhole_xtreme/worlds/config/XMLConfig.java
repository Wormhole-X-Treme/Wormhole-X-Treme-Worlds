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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.bukkit.plugin.PluginDescriptionFile;
import com.wormhole_xtreme.worlds.WormholeXTremeWorlds;
import com.wormhole_xtreme.worlds.config.ConfigManager.OptionKeys;

/**
 * The Class WormholeXTtremeWorldsConfig.
 *
 * @author alron
 */
public class XMLConfig 
{

    /** The Constant thisPlugin. */
    private final static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();
    
    /** The config file. */
    private static File configFile = null;

    
    /**
     * Load xml config.
     *
     * @param desc the {@link PluginDescriptionFile}
     */
    public static void loadXmlConfig(PluginDescriptionFile desc)
    {
        try 
        {
            readXmlConfig(desc);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Save xml config.
     *
     * @param desc the {@link PluginDescriptionFile}
     */
    public static void saveXmlConfig(PluginDescriptionFile desc)
    {
        try 
        {
            writeXmlConfig(desc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Write xml config.
     *
     * @param desc the {@link PluginDescriptionFile}
     * @throws FileNotFoundException the file not found exception
     * @throws XMLStreamException the xML stream exception
     */
    private static void writeXmlConfig(PluginDescriptionFile desc) throws FileNotFoundException, XMLStreamException
    {
        final File directory = new File("plugins" + File.separator + desc.getName() + File.separator);
        if (!directory.exists())
        {
            try 
            {
                directory.mkdir();
                thisPlugin.prettyLog(Level.CONFIG, false, "Created config directory: " + directory.toString());
            }
            catch (Exception e)
            {
                thisPlugin.prettyLog(Level.SEVERE, false, "Uable to create config directory: " + directory.toString());
            }
        }
        final String configFileLocation = directory.getPath() + File.separator + "config.xml";
        setConfigFile(new File(configFileLocation));
        Set<OptionKeys> keys = ConfigManager.options.keySet();
        ArrayList<OptionKeys> list = new ArrayList<OptionKeys>(keys);
        Collections.sort(list);
        Option[] optionArray = new Option[list.size()];
        int i = 0;
        for (OptionKeys key : list)
        {
            Option o = ConfigManager.options.get(key);
            if (o != null)
            {
                optionArray[i] = new Option(o.getOptionKey(), o.getOptionDescription(), o.getOptionType(), o.getOptionValue(), o.getOptionPlugin());
                i++;
            }
        }
        saveConfig(optionArray);
        thisPlugin.prettyLog(Level.INFO, false, "Configuration saved.");
    }
    
    /**
     * Read xml config.
     *
     * @param desc the {@link PluginDescriptionFile}
     * @throws XMLStreamException 
     * @throws FileNotFoundException 
     */
    private static void readXmlConfig(PluginDescriptionFile desc) throws FileNotFoundException, XMLStreamException
    {
        final File directory = new File("plugins" + File.separator + desc.getName() + File.separator);
        
        if (!directory.exists())
        {
            try 
            {
                directory.mkdir();
                thisPlugin.prettyLog(Level.CONFIG, false, "Created config directory: " + directory.toString());
            }
            catch (Exception e)
            {
                thisPlugin.prettyLog(Level.SEVERE, false, "Uable to create config directory: " + directory.toString());
            }
        }
        
        final String configFileLocation = directory.getPath() + File.separator + "config.xml";
        setConfigFile(new File(configFileLocation));
        if (!getConfigFile().exists())
        {
            thisPlugin.prettyLog(Level.WARNING, false, "No configuration file found, generating fresh.");
            saveConfig(DefaultOptions.defaultOptions);
        }
        readConfig();  
    }


    /**
     * Read config.
     *
     * @throws FileNotFoundException the file not found exception
     * @throws XMLStreamException the xML stream exception
     */
    private static void readConfig() throws FileNotFoundException, XMLStreamException 
    {
        final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        final XMLEventReader eventReader = inputFactory.createXMLEventReader(new FileInputStream(getConfigFile()));
        XMLEvent event;
        
        while (eventReader.hasNext())
        {
            OptionKeys optionName = null;
            String optionType = null;
            Object optionValue = null;
            String optionDescription = null;
            boolean v = false;
            boolean t = false;
            int i = 0;
            while ( i < 2 && eventReader.hasNext())
            {
                event = eventReader.nextEvent();
                if (event.isStartElement())
                {
                    String elementType = event.asStartElement().getName().toString();
                    if (elementType.equals("type"))
                    {
                        t = true;
                    }
                    else if (elementType.equals("value"))
                    {
                        v = true;
                    }
                    else if (elementType.startsWith("serverOption"))
                    {
                        optionName = OptionKeys.valueOf(elementType);
                    }
                }
                else if (event.isCharacters() && t )
                {
                    t = false;
                    i++;
                    optionType = event.asCharacters().getData();
                }
                else if (event.isCharacters() && v )
                {
                    v = false;
                    i++;
                    optionValue = event.asCharacters().getData();
                }
            }

            if (optionName != null)
            {
                final Option[] defaultOptions =  DefaultOptions.defaultOptions.clone();
                for (int id = 0; id < defaultOptions.length; id++)
                {
                    if (defaultOptions[id].getOptionKey() == optionName)
                    {
                        if (optionType == null || optionType != defaultOptions[id].getOptionType())
                        {
                            optionType = defaultOptions[id].getOptionType();
                        }
                        if ( optionValue == null || !verifyOptionValue(optionType, optionValue))
                        {
                            optionValue = defaultOptions[id].getOptionValue().toString();
                        }
                        optionDescription = defaultOptions[id].getOptionDescription();
                        break;
                    }
                }
                
                thisPlugin.prettyLog(Level.CONFIG, false,"Got from XML read: " + optionName + ", " + optionDescription + ", " + optionType + ", " + optionValue + ", WormholeXTremeWorlds");
                Option option = new Option(optionName, optionDescription, optionType, optionValue , "WormholeXTremeWorlds");
                ConfigManager.options.put(optionName, option);
            }
        }
    }
    
    /**
     * Verify option value.
     *
     * @param optionType the option type
     * @param optionValue the option value
     * @return true, if successful
     */
    private static boolean verifyOptionValue(String optionType, Object optionValue)
    {
        if (optionType.trim().equals("boolean"))
        {
            String oV = (String) optionValue;
            if (oV.contains("true") || oV.contains("false"))
            {
                return true;
            }
        }
        else if (optionType.trim().equals("double"))
        {
            try
            {
                double test = Double.valueOf(((String) optionValue).trim()).doubleValue();
                if (test >= 0.0)
                {
                    return true;
                }
            } 
            catch (NumberFormatException nfe) 
            {
               return false; 
            }
        }
        return false;
    }
    
    /**
     * Save config.
     *
     * @param option the option
     * @throws FileNotFoundException the file not found exception
     * @throws XMLStreamException the xML stream exception
     */
    private static void saveConfig(Option[] option) throws FileNotFoundException, XMLStreamException
    {
        final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        final XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(getConfigFile()));
        final XMLEventFactory eventFactory = XMLEventFactory.newFactory();
        final XMLEvent end = eventFactory.createDTD("\n");
        
        eventWriter.add(eventFactory.createStartDocument());
        eventWriter.add(end);
        eventWriter.add(eventFactory.createStartElement("","","WormholeXTremeWorlds"));
        eventWriter.add(end);
        
        for (int i = 0; i < option.length; i++)
        {
            createNode(eventWriter, option[i].getOptionKey(), option[i].getOptionType() , option[i].getOptionValue().toString(), option[i].getOptionDescription());
        }
        
        eventWriter.add(eventFactory.createEndElement("","","WormholeXTremeWorlds"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }
    
    /**
     * Creates the node.
     *
     * @param eventWriter the event writer
     * @param name the name
     * @param type the type
     * @param value the value
     * @param description the description
     * @throws XMLStreamException the xML stream exception
     */
    private static void createNode(XMLEventWriter eventWriter, OptionKeys name, String type, String value, String description) throws XMLStreamException
    {
        final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        final XMLEvent end = eventFactory.createDTD("\n");
        final XMLEvent tab = eventFactory.createDTD("\t");
        final String optionName = name.toString();
        
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createComment(description));
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", optionName));
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
        eventWriter.add(eventFactory.createEndElement("", "", optionName));
        eventWriter.add(end);
        eventWriter.add(end);
    }
    
    /**
     * Sets the config file.
     *
     * @param configFile the new config file
     */
    private static void setConfigFile(File configFile) {
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
}
