/*
 * Created on 30-Jul-2003
 *
 */
package jfreerails.controller;

import java.util.ArrayList;
import java.util.Iterator;

import jfreerails.move.AddTransactionMove;
import jfreerails.move.Move;
import jfreerails.world.accounts.DeliverCargoReceipt;
import jfreerails.world.cargo.CargoBatch;
import jfreerails.world.cargo.CargoBundle;
import jfreerails.world.common.Money;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.station.StationModel;
import jfreerails.world.top.KEY;
import jfreerails.world.top.ReadOnlyWorld;

/**
 * This class generates Moves that pay the player for delivering the cargo.
 * 
 * @author Luke Lindsay
 * 
 */
public class ProcessCargoAtStationMoveGenerator {
    /**
     * Determines how much the player gets for delivering cargo. Changed from
     * 100 to 75 to fix bug 910132 (Too easy to make money!)
     */
    private final static int MAGIC_NUMBER = 75;

    public static ArrayList<Move> processCargo(ReadOnlyWorld w,
            CargoBundle bundle, int stationID, FreerailsPrincipal p, int trainId) {
        StationModel thisStation = (StationModel) w.get(p, KEY.STATIONS,
                stationID);
        Iterator<CargoBatch> batches = bundle.cargoBatchIterator();

        ArrayList<Move> moves = new ArrayList<Move>();

        while (batches.hasNext()) {
            CargoBatch batch = batches.next();
            double distanceSquared = (batch.getSourceX() - thisStation.x)
                    * (batch.getSourceX() - thisStation.x)
                    + (batch.getSourceY() - thisStation.y)
                    * (batch.getSourceY() - thisStation.y);
            double dist = Math.sqrt(distanceSquared);
            int quantity = bundle.getAmount(batch);

            double amount = quantity * Math.log(dist) * MAGIC_NUMBER;
            Money money = new Money((long) amount);
            DeliverCargoReceipt receipt = new DeliverCargoReceipt(money,
                    quantity, stationID, batch, trainId);
            moves.add(new AddTransactionMove(p, receipt));
        }

        return moves;
    }
}