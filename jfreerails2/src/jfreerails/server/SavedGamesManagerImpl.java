/*
 * Created on Sep 11, 2004
 *
 */
package jfreerails.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.log4j.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jfreerails.controller.ServerControlInterface;
import jfreerails.network.NewGameMessage2Server;
import jfreerails.network.SavedGamesManager;
import jfreerails.util.FreerailsProgressMonitor;

/**
 * A SavedGamesManager reads and writes gzipped saved games to the working
 * directory.
 * 
 * @author Luke
 * 
 */
class SavFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".sav"));
    }
}

public class SavedGamesManagerImpl implements SavedGamesManager {
    private static final Logger logger = Logger
            .getLogger(SavedGamesManagerImpl.class.getName());

    public String[] getSaveGameNames() {
        java.io.File dir = new File("./");
        FilenameFilter filter = new SavFileFilter();
        String[] files = dir.list(filter);
        return files;
    }

    public String[] getNewMapNames() {
        return NewGameMessage2Server.getMapNames();
    }

    public void saveGame(Serializable w, String s) throws IOException {
        long startTime = System.currentTimeMillis();
        logger.info("Saving game..  " + s);

        FileOutputStream out = new FileOutputStream(s);
        GZIPOutputStream zipout = new GZIPOutputStream(out);

        ObjectOutputStream objectOut = new ObjectOutputStream(zipout);

        objectOut.writeObject(ServerControlInterface.VERSION);
        objectOut.writeObject(w);

        objectOut.flush();
        objectOut.close();
        out.close();

        long finishTime = System.currentTimeMillis();
        long deltaTime = finishTime - startTime;
        logger.info("done, " + deltaTime + "ms");
    }

    public Serializable loadGame(String name) throws IOException {
        long startTime = System.currentTimeMillis();
        logger.info("Loading game..  " + name);

        FileInputStream in = new FileInputStream(name);
        GZIPInputStream zipin = new GZIPInputStream(in);
        ObjectInputStream objectIn = new ObjectInputStream(zipin);
        String version_string;

        try {
            version_string = (String) objectIn.readObject();

            if (!ServerControlInterface.VERSION.equals(version_string)) {
                throw new IOException(version_string);
            }

            Serializable game = (Serializable) objectIn.readObject();

            /**
             * load player private data
             */

            // for (int i = 0; i < world.getNumberOfPlayers(); i++) {
            // Player player = world.getPlayer(i);
            // player.loadSession(objectIn);
            // }
            long finishTime = System.currentTimeMillis();
            long deltaTime = finishTime - startTime;
            logger.info("done, " + deltaTime + "ms");

            return game;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (InvalidClassException e) {
            // e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    public Serializable newMap(String name) throws IOException {
        return OldWorldImpl.createWorldFromMapFile(name,
                FreerailsProgressMonitor.NULL_INSTANCE);
    }
}