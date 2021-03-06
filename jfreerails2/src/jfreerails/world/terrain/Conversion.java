/*
 * Created on 27-Apr-2003
 *
 */
package jfreerails.world.terrain;

import jfreerails.world.common.FreerailsSerializable;

/**
 * This class represents the conversion of one cargo type to another one a tile.
 * 
 * @author Luke
 * 
 */
public class Conversion implements FreerailsSerializable {
    private static final long serialVersionUID = 3546356219414853689L;

    private final int input;

    private final int output;

    public Conversion(int in, int out) {
        input = in;
        output = out;
    }

    public int getInput() {
        return input;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Conversion))
            return false;

        final Conversion conversion = (Conversion) o;

        if (input != conversion.input)
            return false;
        if (output != conversion.output)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = input;
        result = 29 * result + output;
        return result;
    }

    public int getOutput() {
        return output;
    }
}