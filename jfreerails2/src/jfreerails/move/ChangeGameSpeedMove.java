package jfreerails.move;

import jfreerails.world.common.GameSpeed;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.ITEM;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.top.World;

/**
 * 
 * Changes the game speed item on the world object.
 * 
 * @author Jan Tozicka
 * 
 */
public class ChangeGameSpeedMove implements Move {
    private static final long serialVersionUID = 3545794368956086071L;

    private final GameSpeed oldSpeed;

    private final GameSpeed newSpeed;

    public static ChangeGameSpeedMove getMove(ReadOnlyWorld w,
            GameSpeed newGameSpeed) {
        return new ChangeGameSpeedMove((GameSpeed) w.get(ITEM.GAME_SPEED),
                newGameSpeed);
    }

    private ChangeGameSpeedMove(GameSpeed before, GameSpeed after) {
        oldSpeed = before;
        newSpeed = after;
    }

    public MoveStatus tryDoMove(World w, FreerailsPrincipal p) {
        if (w.get(ITEM.GAME_SPEED).equals(oldSpeed)) {
            return MoveStatus.MOVE_OK;
        }
        String string = "oldSpeed = " + oldSpeed.getSpeed() + " <=> "
                + "currentSpeed "
                + ((GameSpeed) w.get(ITEM.GAME_SPEED)).getSpeed();

        return MoveStatus.moveFailed(string);
    }

    public MoveStatus tryUndoMove(World w, FreerailsPrincipal p) {
        GameSpeed speed = ((GameSpeed) w.get(ITEM.GAME_SPEED));

        if (speed.equals(newSpeed)) {
            return MoveStatus.MOVE_OK;
        }
        return MoveStatus.moveFailed("Expected " + newSpeed + ", found "
                + speed);
    }

    public MoveStatus doMove(World w, FreerailsPrincipal p) {
        MoveStatus status = tryDoMove(w, p);

        if (status.ok) {
            w.set(ITEM.GAME_SPEED, newSpeed);
        }

        return status;
    }

    public MoveStatus undoMove(World w, FreerailsPrincipal p) {
        MoveStatus status = tryUndoMove(w, p);

        if (status.isOk()) {
            w.set(ITEM.GAME_SPEED, oldSpeed);
        }

        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ChangeGameSpeedMove))
            return false;

        final ChangeGameSpeedMove changeGameSpeedMove = (ChangeGameSpeedMove) o;

        if (!newSpeed.equals(changeGameSpeedMove.newSpeed))
            return false;
        if (!oldSpeed.equals(changeGameSpeedMove.oldSpeed))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = oldSpeed.hashCode();
        result = 29 * result + newSpeed.hashCode();
        return result;
    }

    public int getNewSpeed() {
        return newSpeed.getSpeed();
    }

    @Override
    public String toString() {
        return "ChangeGameSpeedMove: " + oldSpeed + "=>" + newSpeed;
    }
}