/*
 * Created on Apr 13, 2004
 */
package jfreerails.network;

import junit.framework.TestCase;

/**
 * Test cases that use FreerailsGameServer <b>and</b> connect over the Internet
 * should extend this class .
 * 
 * @author Luke
 * 
 */
public abstract class AbstractFreerailsServerTestCase extends TestCase {
    private InetConnectionAccepter connectionAccepter;

    FreerailsGameServer server;

    private final String ipAddress = "127.0.0.1";

    @Override
    protected synchronized void setUp() throws Exception {
        server = FreerailsGameServer
                .startServer(new SavedGamesManager4UnitTests());
        connectionAccepter = new InetConnectionAccepter(0, server);

        Thread serverThread = new Thread(connectionAccepter);
        serverThread.start();
    }

    @Override
    protected synchronized void tearDown() throws Exception {
        connectionAccepter.stop();
    }

    int getPort() {
        return connectionAccepter.getLocalPort();
    }

    String getIpAddress() {
        return ipAddress;
    }
}