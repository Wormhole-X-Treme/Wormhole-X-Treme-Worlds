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
package com.wormhole_xtreme.worlds;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.plugin.PluginDescriptionFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class WormholeXTtremeWorldsConfig.
 *
 * @author alron
 */
public class WormholeXTtremeWorldsConfig 
{

    private final static WormholeXTremeWorlds thisPlugin = WormholeXTremeWorlds.getThisPlugin();
    private static File configFile = null;

    static void readXmlConfig(PluginDescriptionFile desc)
    {
        final File directory = new File("plugins" + File.separator + desc.getName() + File.separator);
        if (!directory.exists())
        {
            try 
            {
                directory.mkdir();
                thisPlugin.prettyLog(Level.INFO, false, "Created config directory: " + directory.toString());
            }
            catch (Exception e)
            {
                thisPlugin.prettyLog(Level.SEVERE, false, "Uable to create config directory: " + directory.toString());
            }
        }
        final String input = directory.getPath() + File.separator + "config.xml";
        configFile = new File(input);
        if (!configFile.exists())
        {
            // writeXmlConfig(configFile, desc, defaultSettings);
        }
        try
        {
            readXmlConfig(configFile,desc);
        }
        catch (IOException e)
        {
            thisPlugin.prettyLog(Level.SEVERE, false, "Failed to read file: " + e.getMessage() );
        }
        catch (SAXException e) 
        {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) 
        {
            e.printStackTrace();
        }     

        
    }
    
    private static void readXmlConfig(File file, PluginDescriptionFile desc) throws IOException, SAXException, ParserConfigurationException
    {
        
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();        
        final Document configDoc = db.parse(file);
        final Element rootElement = configDoc.getDocumentElement();
        thisPlugin.prettyLog(Level.INFO, false, "plugin for root element: " + rootElement.getAttribute("plugin"));
        final NodeList worldOptionList = rootElement.getElementsByTagName("*");
        boolean worldOptionHelp = false;
        boolean worldOptionPermissions = false;
        //final NodeList worldOptionList = rootElement.getElementsByTagName("worldOption");
        for (int i = 0; i < worldOptionList.getLength(); i++)
        {
            Node worldOptionNode = worldOptionList.item(i);
            final String worldOptionContent = worldOptionNode.getTextContent();
            if (worldOptionNode.getNodeName().equals("worldOptionPermissions") && !worldOptionNode.getTextContent().isEmpty() && checkIfBoolean(worldOptionContent))
            {
                if (parseBooleanString(worldOptionContent))
                {
                    worldOptionPermissions = true;
                }
                thisPlugin.prettyLog(Level.INFO, false, "Permissions enabled:" + worldOptionPermissions );
            }
            else if (worldOptionNode.getNodeName().equals("worldOptionHelp") && !worldOptionNode.getTextContent().isEmpty() && checkIfBoolean(worldOptionContent))
            {
                if (parseBooleanString(worldOptionContent))
                {
                    worldOptionHelp = true;
                }
                thisPlugin.prettyLog(Level.INFO, false, "Help enabled:" + worldOptionHelp );
            }
        }        
    }
    
    private static boolean checkIfBoolean(String ifBooleanString)
    {
        if (ifBooleanString.equalsIgnoreCase("true") || ifBooleanString.equalsIgnoreCase("false") || 
            ifBooleanString.equalsIgnoreCase("1") || ifBooleanString.equalsIgnoreCase("0"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private static boolean parseBooleanString(String parseBooleanString)
    {
        if (parseBooleanString.equalsIgnoreCase("true") || parseBooleanString.equalsIgnoreCase("1"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
