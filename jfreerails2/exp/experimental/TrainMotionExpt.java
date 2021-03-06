package experimental;

import static jfreerails.world.common.Step.EAST;
import static jfreerails.world.common.Step.NORTH;
import static jfreerails.world.common.Step.NORTH_EAST;
import static jfreerails.world.common.Step.NORTH_WEST;
import static jfreerails.world.common.Step.SOUTH;
import static jfreerails.world.common.Step.SOUTH_EAST;
import static jfreerails.world.common.Step.SOUTH_WEST;
import static jfreerails.world.common.Step.WEST;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import jfreerails.client.common.ModelRootImpl;
import jfreerails.client.top.GameLoop;
import jfreerails.controller.AddTrainPreMove;
import jfreerails.controller.ModelRoot;
import jfreerails.controller.MoveExecutor;
import jfreerails.controller.MoveTrainPreMove;
import jfreerails.controller.OccupiedTracks;
import jfreerails.controller.ScreenHandler;
import jfreerails.controller.SimpleMoveExecutor;
import jfreerails.controller.TrackMoveProducer;
import jfreerails.move.Move;
import jfreerails.move.MoveStatus;
import jfreerails.server.MapFixtureFactory2;
import jfreerails.world.common.ActivityIterator;
import jfreerails.world.common.FreerailsPathIterator;
import jfreerails.world.common.ImInts;
import jfreerails.world.common.ImPoint;
import jfreerails.world.common.IntLine;
import jfreerails.world.common.Step;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.World;
import jfreerails.world.track.FreerailsTile;
import jfreerails.world.track.NullTrackType;
import jfreerails.world.train.ImmutableSchedule;
import jfreerails.world.train.PathOnTiles;
import jfreerails.world.train.TrainMotion;
import jfreerails.world.train.TrainOrdersModel;
import jfreerails.world.train.TrainPositionOnMap;

/**
 * This class is a visual test for the train movement code.
 * 
 * TODO: Update the trains position when necessary. Make the train stop at
 * intevals, and slowly accelerate.
 * 
 * @author Luke Lindsay
 * 
 */
public class TrainMotionExpt extends JComponent {

    private static final long serialVersionUID = 3690191057862473264L;

    private final World world;

    private final FreerailsPrincipal principal;

    private double finishTime = 0;

    private long startTime;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Shade tiles with track..
        g.setColor(Color.GREEN);
        for (int x = 0; x < world.getMapWidth(); x++) {
            for (int y = 0; y < world.getMapHeight(); y++) {
                FreerailsTile tile = (FreerailsTile) world.getTile(x, y);
                if (tile.getTrackPiece().getTrackTypeID() != NullTrackType.NULL_TRACK_TYPE_RULE_NUMBER) {
                    int w = Step.TILE_DIAMETER;
                    int h = Step.TILE_DIAMETER;
                    g.drawRect(x * Step.TILE_DIAMETER, y * Step.TILE_DIAMETER,
                            w, h);

                }
            }
        }

        long l = System.currentTimeMillis() - startTime;

        double ticks = (double) l / 1000;

        while (ticks > finishTime) {

            updateTrainPosition();

        }

        ActivityIterator ai = world.getActivities(principal, 0);
        while (ai.getFinishTime() < ticks && ai.hasNext()) {
            ai.nextActivity();
        }
        double t = Math.min(ticks, ai.getFinishTime());
        t = t - ai.getStartTime();

        TrainMotion motion = (TrainMotion) ai.getActivity();

        TrainPositionOnMap pos = (TrainPositionOnMap) ai.getState(ticks);

        PathOnTiles pathOT = motion.getPath();
        Iterator<ImPoint> it = pathOT.tiles();
        while (it.hasNext()) {
            ImPoint tile = it.next();
            int x = tile.x * Step.TILE_DIAMETER;
            int y = tile.y * Step.TILE_DIAMETER;
            int w = Step.TILE_DIAMETER;
            int h = Step.TILE_DIAMETER;
            g.setColor(Color.WHITE);
            g.fillRect(x, y, w, h);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, w, h);
        }

        pathOT = motion.getTiles(t);
        it = pathOT.tiles();
        while (it.hasNext()) {
            ImPoint tile = it.next();
            int x = tile.x * Step.TILE_DIAMETER;
            int y = tile.y * Step.TILE_DIAMETER;
            int w = Step.TILE_DIAMETER;
            int h = Step.TILE_DIAMETER;
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, y, w, h);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, w, h);
        }

        g.setColor(Color.BLACK);
        IntLine line = new IntLine();
        FreerailsPathIterator path = pos.path();
        while (path.hasNext()) {
            path.nextSegment(line);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }

        int speed = (int) Math.round(pos.getSpeed());
        g.drawString("Speed: " + speed, 260, 60);

    }

    private void updateTrainPosition() {
        Random rand = new Random(System.currentTimeMillis());
        MoveTrainPreMove moveTrain = new MoveTrainPreMove(0, principal,
                new OccupiedTracks(principal, world));
        Move m;
        if (rand.nextInt(10) == 0) {
            m = moveTrain.stopTrain(world);
        } else {
            m = moveTrain.generateMove(world);
        }
        MoveStatus ms = m.doMove(world, principal);
        if (!ms.ok)
            throw new IllegalStateException(ms.message);

        ActivityIterator ai = world.getActivities(principal, 0);

        while (ai.hasNext()) {
            ai.nextActivity();
            finishTime = ai.getFinishTime();
        }
    }

    public TrainMotionExpt() {
        world = MapFixtureFactory2.getCopy();
        MoveExecutor me = new SimpleMoveExecutor(world, 0);
        principal = me.getPrincipal();
        ModelRoot mr = new ModelRootImpl();
        TrackMoveProducer producer = new TrackMoveProducer(me, world, mr);
        Step[] trackPath = { EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST,
                NORTH_WEST, NORTH, NORTH_EAST };
        ImPoint from = new ImPoint(5, 5);
        MoveStatus ms = producer.buildTrack(from, trackPath);
        if (!ms.ok)
            throw new IllegalStateException(ms.message);

        TrainOrdersModel[] orders = {};
        ImmutableSchedule is = new ImmutableSchedule(orders, -1, false);
        AddTrainPreMove addTrain = new AddTrainPreMove(0, new ImInts(), from,
                principal, is);

        Move m = addTrain.generateMove(world);
        ms = m.doMove(world, principal);
        if (!ms.ok)
            throw new IllegalStateException(ms.message);

        startTime = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.setProperty("SHOWFPS", "true");

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.getContentPane().add(new TrainMotionExpt());

        ScreenHandler screenHandler = new ScreenHandler(f,
                ScreenHandler.WINDOWED_MODE);
        screenHandler.apply();

        GameLoop gameLoop = new GameLoop(screenHandler);
        Thread t = new Thread(gameLoop);
        t.start();
    }
}