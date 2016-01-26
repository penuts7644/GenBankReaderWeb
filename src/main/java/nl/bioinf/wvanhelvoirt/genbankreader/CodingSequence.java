/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */

package nl.bioinf.wvanhelvoirt.genbankreader;

/**
 * @author Wout van Helvoirt [wout.van.helvoirt@gmail.com]
 * @version 1.0.0
 */
public class CodingSequence {

    /**
     * @param coordinates is a Coordinates object that contains start and stop coordinates.
     */
    private final Coordinates coordinates;

    /**
     * @param product is a string that contains the CDS name.
     */
    private final String product;

    /**
     * @param proteinID is a string that contains the protein id of the cds.
     */
    private final String proteinID;

    /**
     * @param translation is a string that contains the translated aminoacid sequence.
     */
    private final String translation;

    /**
     * @param direction is a SequenceOrientation object that contains the orientation of the gene sequence.
     */
    private final SequenceOrientation direction;

    /**
    * Constructor for a single CDS.
    * @param coordinates contains start and stop coordinates of gene.
    * @param product contains the cds product name.
    * @param proteinID contains the protein id.
    * @param translation contains the translated cds sequence.
    * @param direction contains the direction of the gene sequence.
    */
    public CodingSequence(final Coordinates coordinates,
            final String product,
            final String proteinID,
            final String translation,
            final SequenceOrientation direction) {

        this.coordinates = coordinates;
        this.product = product;
        this.proteinID = proteinID;
        this.translation = translation;
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
    * Get product name.
    * @return product.
    */
    public String getProduct() {
        return product;
    }

    /**
    * Get start and stop coordinates.
    * @return proteinID.
    */
    public String getProteinID() {
        return proteinID;
    }

    /**
    * Get translation sequence.
    * @return translation.
    */
    public String getTranslation() {
        return translation;
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
        return "Coordinates: " + getCoordinates() + ", Product: " + getProduct() + ", Protein id: " + getProteinID()
                + ", Translation: " + getTranslation() + ", Direction: " + getDirection();
    }
}
