/*
 * Created on 04-Jul-2005
 *
 */
package jfreerails.world.common;

import java.awt.Point;

import jfreerails.util.Immutable;

/**
 * An immutable point.
 * 
 * @author Luke
 * 
 */
@Immutable
public final class ImPoint implements FreerailsSerializable,
        Comparable<ImPoint> {

    private static final long serialVersionUID = -3053020239886388576L;

    public final int x, y;

    public ImPoint() {
        x = 0;
        y = 0;
    }

    public ImPoint(Point p) {
        x = p.x;
        y = p.y;
    }

    public ImPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ImPoint))
            return false;

        final ImPoint imPoint = (ImPoint) o;

        if (x != imPoint.x)
            return false;
        if (y != imPoint.y)
            return false;

        return true;
    }

    public Point toPoint() {
        return new Point(x, y);
    }

    @Override
    public int hashCode() {
        return x * 1000 + y;
    }

    @Override
    public String toString() {
        return "ImPoint{" + x + ", " + y + "}";
    }

    public int compareTo(ImPoint o) {
        if (o.y != y)
            return y - o.y;
        else
            return x - o.x;
    }
}
