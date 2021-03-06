package jfreerails.server.parser;

import org.apache.log4j.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * The class reads XML documents according to specified DTD and translates all
 * related events into CargoAndTerrainHandler events.
 * <p>
 * Usage sample:
 * 
 * <pre>
 *      RulesParser parser = new RulesParser(...);
 *      parser.parse(new InputSource(&quot;...&quot;));
 * </pre>
 * 
 * <p>
 * <b>Warning:</b> the class is machine generated. DO NOT MODIFY
 * </p>
 * 
 * @author Luke
 */
public class CargoAndTerrainParser implements ContentHandler {
    private static final Logger logger = Logger
            .getLogger(CargoAndTerrainParser.class.getName());

    private java.lang.StringBuffer buffer;

    private CargoAndTerrainHandler handler;

    private java.util.Stack<Object[]> context;

    private EntityResolver resolver;

    /**
     * Creates a parser instance.
     * 
     * @param handler
     *            handler interface implementation (never <code>null</code>
     * @param resolver
     *            SAX entity resolver implementation or <code>null</code>. It
     *            is recommended that it could be able to resolve at least the
     *            DTD.
     */
    public CargoAndTerrainParser(final CargoAndTerrainHandler handler,
            final EntityResolver resolver) {
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack<Object[]>();
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void setDocumentLocator(Locator locator) {
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void startDocument() throws SAXException {
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void endDocument() throws SAXException {
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void startElement(java.lang.String ns, java.lang.String name,
            java.lang.String qname, Attributes attrs) throws SAXException {
        dispatch(true);
        context.push(new Object[] { qname,
                new org.xml.sax.helpers.AttributesImpl(attrs) });

        if ("Converts".equals(name)) {
            handler.handle_Converts(attrs);
        } else if ("Tile".equals(name)) {
            handler.start_Tile(attrs);
        } else if ("Cargo".equals(name)) {
            handler.handle_Cargo(attrs);
        } else if ("Cargo_Types".equals(name)) {
            handler.start_Cargo_Types(attrs);
        } else if ("Terrain_Types".equals(name)) {
            handler.start_Terrain_Types(attrs);
        } else if ("Types".equals(name)) {
            handler.start_Types(attrs);
        } else if ("Consumes".equals(name)) {
            handler.handle_Consumes(attrs);
        } else if ("Produces".equals(name)) {
            handler.handle_Produces(attrs);
        }
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void endElement(java.lang.String ns, java.lang.String name,
            java.lang.String qname) throws SAXException {
        dispatch(false);
        context.pop();

        if ("Tile".equals(name)) {
            handler.end_Tile();
        } else if ("Cargo_Types".equals(name)) {
            handler.end_Cargo_Types();
        } else if ("Terrain_Types".equals(name)) {
            handler.end_Terrain_Types();
        } else if ("Types".equals(name)) {
            handler.end_Types();
        }
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void characters(char[] chars, int start, int len)
            throws SAXException {
        buffer.append(chars, start, len);
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void ignorableWhitespace(char[] chars, int start, int len)
            throws SAXException {
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void processingInstruction(java.lang.String target,
            java.lang.String data) throws SAXException {
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void startPrefixMapping(final java.lang.String prefix,
            final java.lang.String uri) throws SAXException {
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void endPrefixMapping(final java.lang.String prefix)
            throws SAXException {
    }

    /**
     * This SAX interface method is implemented by the parser.
     * 
     */
    public final void skippedEntity(java.lang.String name) throws SAXException {
    }

    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
        if (fireOnlyIfMixed && buffer.length() == 0) {
            return; // skip it
        }

        buffer.delete(0, buffer.length());
    }

    /**
     * The recognizer entry method taking an Inputsource.
     * 
     * @param input
     *            InputSource to be parsed.
     * @throws java.io.IOException
     *             on I/O error.
     * @throws SAXException
     *             propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException
     *             a parser satisfining requested configuration can not be
     *             created.
     * 
     */
    public static void parse(final InputSource input,
            final CargoAndTerrainHandler handler) throws SAXException,
            javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, new CargoAndTerrainParser(handler, null));
    }

    /**
     * The recognizer entry method taking a URL.
     * 
     * @param url
     *            URL source to be parsed.
     * @throws java.io.IOException
     *             on I/O error.
     * @throws SAXException
     *             propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException
     *             a parser satisfining requested configuration can not be
     *             created.
     * 
     */
    public static void parse(final java.net.URL url,
            final CargoAndTerrainHandler handler) throws SAXException,
            javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }

    private static void parse(final InputSource input,
            final CargoAndTerrainParser recognizer) throws SAXException,
            javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory
                .newInstance();
        factory.setValidating(true); // the code was generated according DTD
        factory.setNamespaceAware(true); // the code was generated according
        // DTD

        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());

        if (recognizer.resolver != null) {
            parser.setEntityResolver(recognizer.resolver);
        }

        parser.parse(input);
    }

    /**
     * Creates default error handler used by this parser.
     * 
     * @return org.xml.sax.ErrorHandler implementation
     * 
     */
    protected ErrorHandler getDefaultErrorHandler() {
        return new ErrorHandler() {
            public void error(SAXParseException ex) throws SAXException {
                if (context.isEmpty()) {
                    logger.error("Missing DOCTYPE.");
                }

                throw ex;
            }

            public void fatalError(SAXParseException ex) throws SAXException {
                throw ex;
            }

            public void warning(SAXParseException ex) throws SAXException {
                // ignore
            }
        };
    }
}