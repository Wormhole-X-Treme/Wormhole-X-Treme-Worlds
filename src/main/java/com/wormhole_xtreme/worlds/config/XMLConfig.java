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
import java.io.InputStream;
import java.util.logging.Level;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
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

    private final static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();
    private static File configFile = null;

    
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
    private static void writeXmlConfig(PluginDescriptionFile desc) throws Exception
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
        //saveConfig((Option[])Option);
        thisPlugin.prettyLog(Level.INFO, false, "Configuration saved.");
    }
    
    private static void readXmlConfig(PluginDescriptionFile desc) throws Exception
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


    private static void readConfig() throws FileNotFoundException, XMLStreamException 
    {
        //List<Option> configItems = new ArrayList<Option>();
        final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        final InputStream in = new FileInputStream(getConfigFile());
        final XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
        XMLEvent event;
        while (eventReader.hasNext())
        {
            OptionKeys optionName = null;
            String optionType = null;
            String optionValue = null;
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
                thisPlugin.prettyLog(Level.CONFIG, false, optionName + " : " + optionType + " : " + optionValue);
                final Option[] defaultOptions =  DefaultOptions.defaultOptions;
                for (int id = 0; id < defaultOptions.length; id++)
                {
                    if (defaultOptions[i].getOptionKey() == optionName)
                    {
                        if (optionType == null)
                        {
                            optionType = defaultOptions[i].getOptionType();
                        }
                        if ( optionValue == null)
                        {
                            optionValue = defaultOptions[i].getOptionValue().toString();
                        }
                        optionDescription = defaultOptions[i].getOptionDescription();
                    }
                }
                final Option option = new Option(optionName, optionDescription, optionType, optionValue , "WormholeXTremeWorlds");
                ConfigManager.options.put(optionName, option);
            }
        }
    }

    private static void saveConfig(Option[] option) throws FileNotFoundException, XMLStreamException
    {
        final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        final XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(getConfigFile()));
        final XMLEventFactory eventFactory = XMLEventFactory.newFactory();
        final XMLEvent end = eventFactory.createDTD("\n");
        final StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        eventWriter.add(end);
        final StartElement configStartElement = eventFactory.createStartElement("","","config");
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        for (int i = 0; i < option.length; i++)
        {
            createNode(eventWriter, option[i].getOptionKey(), option[i].getOptionType() , option[i].getOptionValue().toString(), option[i].getOptionDescription());
        }
        eventWriter.add(eventFactory.createEndElement("","","config"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }
    
    private static void createNode(XMLEventWriter eventWriter, OptionKeys name, String type, String value, String description) throws XMLStreamException
    {
        final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        final XMLEvent end = eventFactory.createDTD("\n");
        final XMLEvent tab = eventFactory.createDTD("\t");
        final String optionName = name.toString();
        final Comment descComment = eventFactory.createComment(description);
        final StartElement nameStartElement = eventFactory.createStartElement("", "", optionName);
        final StartElement typeStartElement = eventFactory.createStartElement("", "", "type");
        final Characters typeCharacters = eventFactory.createCharacters(type);
        final EndElement typeEndElement = eventFactory.createEndElement("", "", "type");
        final StartElement valueStartElement = eventFactory.createStartElement("", "", "value");
        final Characters valueCharacters = eventFactory.createCharacters(value);
        final EndElement valueEndElement = eventFactory.createEndElement("", "", "value");
        final EndElement nameEndElement = eventFactory.createEndElement("", "", optionName);
        
        eventWriter.add(tab);
        eventWriter.add(descComment);
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(nameStartElement);
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(typeStartElement);
        eventWriter.add(typeCharacters);
        eventWriter.add(typeEndElement);
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(valueStartElement);
        eventWriter.add(valueCharacters);
        eventWriter.add(valueEndElement);
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(nameEndElement);
        eventWriter.add(end);
        eventWriter.add(end);
    }
    
    private static void setConfigFile(File configFile) {
        XMLConfig.configFile = configFile;
    }

    private static File getConfigFile() {
        return configFile;
    }
}
