package jfreerails.server;

import jfreerails.move.AddTransactionMove;
import jfreerails.move.Move;
import jfreerails.network.MoveReceiver;
import jfreerails.world.accounts.Bill;
import jfreerails.world.accounts.BondTransaction;
import jfreerails.world.accounts.Transaction;
import jfreerails.world.common.Money;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.World;

/**
 * This class iterates over the entries in the BankAccount and counts the number
 * of outstanding bonds, then calculates the interest due.
 * 
 * @author Luke Lindsay
 * 
 */
public class InterestChargeMoveGenerator {
    private final MoveReceiver moveReceiver;

    public InterestChargeMoveGenerator(MoveReceiver mr) {
        this.moveReceiver = mr;
    }

    private static AddTransactionMove generateMove(World w,
            FreerailsPrincipal principal) {
        long interestDue = 0;

        for (int i = 0; i < w.getNumberOfTransactions(principal); i++) {
            Transaction t = w.getTransaction(principal, i);

            if (t instanceof BondTransaction) {
                BondTransaction bt = (BondTransaction) t;
                int interestRate = bt.getType();
                long bondAmount = BondTransaction.BOND_VALUE_ISSUE.getAmount();
                interestDue += (interestRate * bondAmount / 100)
                        * bt.getQuantity();
            }
        }

        Transaction t = new Bill(new Money(interestDue),
                Transaction.Category.INTEREST_CHARGE);

        return new AddTransactionMove(principal, t);
    }

    public void update(World w) {
        for (int i = 0; i < w.getNumberOfPlayers(); i++) {
            FreerailsPrincipal principal = w.getPlayer(i).getPrincipal();
            Move m = generateMove(w, principal);
            moveReceiver.processMove(m);
        }
    }
}