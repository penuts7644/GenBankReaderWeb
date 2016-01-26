/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */

package nl.bioinf.wvanhelvoirt.genbankreader;

/**
 * @author Wout van Helvoirt [wout.van.helvoirt@gmail.com]
 * @version 1.0.0
 */
public class Coordinates {

    /**
     * @param first is a integer start coordinate.
     */
    private final long first;

    /**
     * @param last is a integer stop coordinate.
     */
    private final long last;

    /**
    * Constructor creating the start and stop coordinates.
    * @param first contains start coordinate.
    * @param last contains stop coordinate.
    */
    public Coordinates(final long first,
            final long last) {

        this.first = first;
        this.last = last;
    }

    /**
    * Get start coordinate value.
    * @return first.
    */
    public long getFirst() {
        return first;
    }

    /**
    * Get stop coordinate value.
    * @return stop.
    */
    public long getLast() {
        return last;
    }

    @Override
    public String toString() {
        return "Start: " + getFirst() + ", Stop: " + getLast();
    }
}
