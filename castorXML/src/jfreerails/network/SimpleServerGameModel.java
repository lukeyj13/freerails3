/*
 * Created on Sep 12, 2004
 *
 */
package jfreerails.network;

import java.io.IOException;
import java.io.ObjectOutputStream;

import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.KEY;
import jfreerails.world.top.World;

/**
 * A ServerGameModel that has a world object but no automation.
 * 
 * @author Luke
 * 
 */
public class SimpleServerGameModel implements ServerGameModel {
    private static final long serialVersionUID = 3546074757457131826L;

    private World w;

    private String[] passwords;

    public void setWorld(World w, String[] passwords) {
        this.w = w;
        this.passwords = passwords.clone();
    }

    public World getWorld() {
        return w;
    }

    public void init(MoveReceiver moveExecuter) {
    }

    public void write(ObjectOutputStream objectOut) throws IOException {
    }

    public void update() {
    }

    public void listUpdated(KEY key, int index, FreerailsPrincipal principal) {
    }

    public void itemAdded(KEY key, int index, FreerailsPrincipal principal) {
    }

    public void itemRemoved(KEY key, int index, FreerailsPrincipal principal) {
    }

    public String[] getPasswords() {
        return passwords.clone();
    }

}