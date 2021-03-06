/*
 *  TileViewList.java
 *
 *  Created on 08 August 2001, 17:11
 */
package jfreerails.client.renderer;

import java.util.ArrayList;

import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.top.SKEY;

/**
 * A list of TileRenderers stored in an array and created from an ArrayList.
 * 
 * @author Luke Lindsay 09 October 2001
 */
final public class TileRendererListImpl implements TileRendererList {
    private final TileRenderer[] tiles;

    public TileRenderer getTileViewWithNumber(int i) {
        return tiles[i];
    }

    public TileRendererListImpl(ArrayList<TileRenderer> t) {
        tiles = new TileRenderer[t.size()];

        for (int i = 0; i < t.size(); i++) {
            tiles[i] = t.get(i);
        }
    }

    public boolean validate(ReadOnlyWorld w) {
        // There should a TileRenderer for each terrain type.
        return w.size(SKEY.TERRAIN_TYPES) == tiles.length;
    }
}