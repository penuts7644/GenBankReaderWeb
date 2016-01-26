/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */

package nl.bioinf.wvanhelvoirt.genbankreader;

/**
 * @author Wout van Helvoirt [wout.van.helvoirt@gmail.com]
 * @version 1.0.0
 */
public class Gene {

    /**
     * @param coordinates is a Coordinates object that contains start and stop coordinates.
     */
    private final Coordinates coordinates;

    /**
     * @param gene is string with the name of the gene.
     */
    private final String gene;

    /**
     * @param direction is a SequenceOrientation object that contains the orientation of the gene sequence.
     */
    private final SequenceOrientation direction;

    /**
    * Constructor for a single Gene.
    * @param coordinates contains start and stop coordinates of gene.
    * @param gene contains the gene name.
    * @param direction contains the direction of the gene sequence.
    */
    public Gene(final Coordinates coordinates,
            final String gene,
            final SequenceOrientation direction) {

        this.coordinates = coordinates;
        this.gene = gene;
        this.direction = direction;
    }

    /**
    * Get start and stop coordinates.
    * @return coordinates.
    */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
    * Get gene name.
    * @return gene.
    */
    public String getGene() {
        return gene;
    }

    /**
    * Get the sequence direction.
    * @return direction.
    */
    public SequenceOrientation getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Coordinates: " + getCoordinates() + ", Gene: " + getGene() + ", Direction: " + getDirection();
    }
}
