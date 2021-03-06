/*
 * Created on 28-Jul-2005
 *
 */
package jfreerails.world.top;

import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jfreerails.util.List1DDiff;
import jfreerails.util.List2DDiff;
import jfreerails.util.List3DDiff;
import jfreerails.util.ListKey;
import jfreerails.util.Utils;
import jfreerails.world.accounts.TransactionAndTimeStamp;
import jfreerails.world.common.FreerailsSerializable;
import jfreerails.world.common.ImPoint;
import jfreerails.world.common.Money;
import jfreerails.world.player.Player;

/**
 * An implemenation of World that only stores differences relative to an
 * underlying world object. Below is some stylised code showing what this class
 * does. The <code>key</code> object could be a location on the map, a
 * position in a list etc. <code><pre>
 * HashMap underlyingWorldObject;
 * 
 * HashMap differences;
 * 
 * public void put(Object key, Object value) {
 *     if (underlyingWorldObject.get(key).equals(value)) {
 *         if (differences.containsKey(key)) {
 *             differences.remove(key);
 *         }
 *     } else {
 *         differences.put(key, value);
 *     }
 * }
 * 
 * public Object get(Object key) {
 *     if (differences.containsKey(key)) {
 *         return differences.get(key);
 *     } else {
 *         return underlyingWorldObject.get(key);
 *     }
 * }
 * </code></pre>
 * 
 * The advantages of using an instance of this class instead of a copy of the
 * world object are:
 * <ol>
 * <li> Uses less memory.</li>
 * <li> Lets you pinpoint where differences on the map are, so you don't need to
 * check every tile. </li>
 * </ol>
 * 
 * 
 * @author Luke
 * @version 2
 * 
 */
public class WorldDiffs extends WorldImpl {

    public enum LISTID {
        ACTIVITY_LISTS, BANK_ACCOUNTS, CURRENT_BALANCE, ITEMS, LISTS, PLAYERS, SHARED_LISTS
    }

    private static final long serialVersionUID = -5993786533926919956L;

    private final SortedMap<ListKey, Object> listDiff;

    /** Stores the differences on the map, ImPoint are used as keys. */
    private final HashMap<ImPoint, Object> mapDiff;

    private final WorldImpl underlying;

    public WorldDiffs(ReadOnlyWorld row) {

        listDiff = new TreeMap<ListKey, Object>();
        mapDiff = new HashMap<ImPoint, Object>();

        // Bit of a hack but it's not clear there is a better way, LL
        underlying = (WorldImpl) row;

        activityLists = new List3DDiff<ActivityAndTime>(listDiff,
                underlying.activityLists, LISTID.ACTIVITY_LISTS);
        bankAccounts = new List2DDiff<TransactionAndTimeStamp>(listDiff,
                underlying.bankAccounts, LISTID.BANK_ACCOUNTS);
        currentBalance = new List1DDiff<Money>(listDiff,
                underlying.currentBalance, LISTID.CURRENT_BALANCE);
        items = new List1DDiff<FreerailsSerializable>(listDiff,
                underlying.items, LISTID.ITEMS);
        lists = new List3DDiff<FreerailsSerializable>(listDiff,
                underlying.lists, LISTID.LISTS);
        players = new List1DDiff<Player>(listDiff, underlying.players,
                LISTID.PLAYERS);
        sharedLists = new List2DDiff<FreerailsSerializable>(listDiff,
                underlying.sharedLists, LISTID.SHARED_LISTS);
        time = underlying.time;
    }

    /**
     * The iterator returns instances of java.awt.Point that store the
     * coordinates of tiles that are different to the underlying world object.
     */
    public Iterator<ImPoint> getMapDiffs() {
        return mapDiff.keySet().iterator();
    }

    public Iterator<ListKey> getListDiffs() {
        return listDiff.keySet().iterator();
    }

    public Object getDiff(ListKey key) {
        return listDiff.get(key);
    }

    @Override
    public int getMapHeight() {
        return underlying.getMapHeight();
    }

    @Override
    public int getMapWidth() {
        return underlying.getMapWidth();
    }

    @Override
    public FreerailsSerializable getTile(int x, int y) {
        ImPoint p = new ImPoint(x, y);

        if (this.mapDiff.containsKey(p)) {
            return (FreerailsSerializable) this.mapDiff.get(p);
        }
        return underlying.getTile(x, y);
    }

    /** Used by unit tests. */
    public int numberOfMapDifferences() {
        return this.mapDiff.size();
    }

    /** Used by unit tests. */
    public int listDiffs() {
        return listDiff.size();
    }

    /**
     * After this method returns, all differences are cleared and calls to
     * methods on this object should produce the same results as calls the the
     * corresponding methods on the underlying world object.
     */
    public void reset() {
        time = underlying.currentTime();
        mapDiff.clear();
        listDiff.clear();
    }

    @Override
    public void setTile(int x, int y, FreerailsSerializable tile) {
        ImPoint p = new ImPoint(x, y);

        if (Utils.equal(underlying.getTile(x, y), tile)) {
            if (this.mapDiff.containsKey(p)) {
                this.mapDiff.remove(p);

                return;
            }
        } else {
            this.mapDiff.put(p, tile);
        }
    }

    public boolean isDifferent() {
        return (mapDiff.size() != 0) || (listDiff.size() != 0);
    }

    public ReadOnlyWorld getUnderlying() {
        return underlying;
    }

}
