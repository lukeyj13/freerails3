/*
 * Created on Apr 13, 2004
 */
package jfreerails.network;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import jfreerails.world.common.FreerailsSerializable;

import org.apache.log4j.Logger;

/**
 * Implementation of GameServer that simply echoes whatever clients send it.
 * 
 * @author Luke
 * 
 */
public class EchoGameServer implements GameServer, Runnable {
    private static final Logger logger = Logger.getLogger(EchoGameServer.class
            .getName());

    private final Vector<Connection2Client> connections = new Vector<Connection2Client>();

    private final SynchronizedFlag status = new SynchronizedFlag(false);

    private final LinkedList<FreerailsSerializable> messsages2send = new LinkedList<FreerailsSerializable>();

    private EchoGameServer() {
    }

    /**
     * Creates an EchoGameServer, starts it in a new Thread, and waits for its
     * status to change to isOpen before returning.
     */
    public static EchoGameServer startServer() {
        EchoGameServer server = new EchoGameServer();
        Thread t = new Thread(server);
        t.start();

        try {
            /* Wait for the server to start before returning. */
            synchronized (server.status) {
                server.status.wait();
            }

            return server;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    public synchronized void addConnection(Connection2Client connection) {
        if (null == connection) {
            throw new NullPointerException();
        }

        if (!status.isOpen()) {
            throw new IllegalArgumentException();
        }

        connections.add(connection);
    }

    public synchronized int countOpenConnections() {
        Iterator<Connection2Client> it = connections.iterator();

        while (it.hasNext()) {
            Connection2Client connection = it.next();

            if (!connection.isOpen()) {
                it.remove();
            }
        }

        return connections.size();
    }

    public synchronized void stop() {
        status.close();

        for (int i = 0; i < connections.size(); i++) {
            AbstractInetConnection connection = (AbstractInetConnection) connections
                    .get(i);

            if (connection.isOpen()) {
                try {
                    connection.setTimeOut(0);
                    connection.disconnect();
                } catch (Exception e) {
                    // Do nothing.
                }
            }
        }
    }

    public void run() {
        status.open();

        while (status.isOpen()) {
            update();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // do nothing.
            }
        }
    }

    synchronized void sendMessage(FreerailsSerializable m) {
        /* Send messages. */
        for (int i = 0; i < connections.size(); i++) {
            Connection2Client connection = connections.get(i);

            try {
                connection.writeToClient(m);
                connection.flush();
                if (logger.isDebugEnabled()) {
                    logger.debug("Sent ok: " + m);
                }
            } catch (IOException e) {
                try {
                    if (connection.isOpen()) {
                        connection.disconnect();
                    }
                } catch (IOException e1) {
                    // hope this doesn't happen.
                    e1.printStackTrace();
                }
            }
        }
    }

    public void update() {
        synchronized (this) {
            /* Read messages. */
            for (int i = 0; i < connections.size(); i++) {
                Connection2Client connection = connections.get(i);

                try {
                    FreerailsSerializable[] messages = connection
                            .readFromClient();

                    for (int j = 0; j < messages.length; j++) {
                        messsages2send.add(messages[j]);
                    }
                } catch (IOException e) {
                    try {
                        if (connection.isOpen()) {
                            connection.disconnect();
                        }
                    } catch (IOException e1) {
                        // 
                        e1.printStackTrace();
                    }
                }
            }

            /* Send messages. */
            Iterator<FreerailsSerializable> messagesIterator = messsages2send
                    .iterator();

            while (messagesIterator.hasNext()) {
                FreerailsSerializable message = messagesIterator.next();
                sendMessage(message);
            }
        }
    }
}