/*
 * Created on 10-Aug-2003
 *
 */
package jfreerails.move;

import jfreerails.world.common.ImPoint;
import jfreerails.world.player.Player;
import jfreerails.world.top.MapFixtureFactory;
import jfreerails.world.top.SKEY;
import jfreerails.world.top.World;
import jfreerails.world.top.WorldImpl;
import jfreerails.world.track.FreerailsTile;
import jfreerails.world.track.TrackConfiguration;
import jfreerails.world.track.TrackPiece;
import jfreerails.world.track.TrackPieceImpl;
import jfreerails.world.track.TrackRule;
import junit.framework.TestCase;

/**
 * JUnit test case for TrackMoveTransactionsGenerator.
 * 
 * @author Luke Lindsay
 * 
 */
public class TrackMoveTransactionsGeneratorTest extends TestCase {
    private World world;

    private TrackMoveTransactionsGenerator transactionGenerator;

    private Player player;

    @Override
    protected void setUp() throws Exception {
        world = new WorldImpl(20, 20);
        MapFixtureFactory.generateTrackRuleList(world);
        player = new Player("test player", 0);
        world.addPlayer(player);
        transactionGenerator = new TrackMoveTransactionsGenerator(world, player
                .getPrincipal());
    }

    public void testAddTrackMove() {
        TrackPiece oldTrackPiece;
        TrackPiece newTrackPiece;
        TrackConfiguration newConfig;
        TrackMove move;

        // Try building the simplest piece of track.
        newConfig = TrackConfiguration.getFlatInstance("000010000");
        oldTrackPiece = ((FreerailsTile) world.getTile(0, 0)).getTrackPiece();

        TrackRule r = (TrackRule) world.get(SKEY.TRACK_RULES, 0);
        int owner = ChangeTrackPieceCompositeMove.getOwner(
                MapFixtureFactory.TEST_PRINCIPAL, world);
        newTrackPiece = new TrackPieceImpl(newConfig, r, owner, 0);
        move = new ChangeTrackPieceMove(oldTrackPiece, newTrackPiece,
                new ImPoint(0, 0));

        Move m = transactionGenerator.addTransactions(move);
        assertNotNull(m);
    }
}