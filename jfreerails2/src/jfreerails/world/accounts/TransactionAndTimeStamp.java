/*
 * Created on 22-Jul-2005
 *
 */
package jfreerails.world.accounts;

import jfreerails.world.common.FreerailsSerializable;
import jfreerails.world.common.GameTime;

public class TransactionAndTimeStamp implements FreerailsSerializable {

    private static final long serialVersionUID = 1540065347606694456L;

    private final Transaction t;

    private final GameTime timeStamp;

    public TransactionAndTimeStamp(Transaction t, GameTime stamp) {
        this.t = t;
        timeStamp = stamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TransactionAndTimeStamp))
            return false;

        final TransactionAndTimeStamp transactionAndTimeStamp = (TransactionAndTimeStamp) o;

        if (!t.equals(transactionAndTimeStamp.t))
            return false;
        if (!timeStamp.equals(transactionAndTimeStamp.timeStamp))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = t.hashCode();
        result = 29 * result + timeStamp.hashCode();
        return result;
    }

    public Transaction getT() {
        return t;
    }

    public GameTime getTimeStamp() {
        return timeStamp;
    }
}
