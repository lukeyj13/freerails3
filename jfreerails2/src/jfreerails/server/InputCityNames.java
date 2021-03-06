package jfreerails.server;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jfreerails.world.top.World;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class that calls the object to input the City names and co-ords from an xml
 * file.
 * 
 * @author Scott Bennett Date: 31st March 2003
 */
public class InputCityNames {
    public static void readCityNames(World w, URL filename) throws SAXException {
        InputSource is = new InputSource(filename.toString());

        DefaultHandler handler = new CitySAXParser(w);
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }
}