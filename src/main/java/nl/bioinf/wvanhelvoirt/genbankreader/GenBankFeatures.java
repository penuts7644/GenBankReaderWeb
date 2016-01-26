/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */

package nl.bioinf.wvanhelvoirt.genbankreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Wout van Helvoirt [wout.van.helvoirt@gmail.com]
 * @version 1.0.0
 */
public class GenBankFeatures {

    /**
     * @param definition is a string that contains definition of the GenBank file.
     */
    private final String definition;

    /**
     * @param accession is a string that contains the accession of the GenBank file.
     */
    private final String accession;

    /**
     * @param organism is a string that contains the organism name.
     */
    private final String organism;

    /**
     * @param cdsElements is a list with CodingSequence elements.
     */
    private final List<CodingSequence> cdsElements;

    /**
     * @param geneElements is a list with Gene elements.
     */
    private final List<Gene> geneElements;

    /**
     * @param origin is a string with the GenBank sequence.
     */
    private final String origin;

    /**
    * Constructor for a single CDS.
    * @param definition contains definition of GenBank.
    * @param accession contains accession of GenBank.
    * @param organism contains organism name.
    * @param cdsElements contains CodingSequence object.
    * @param geneElements contains Gene object.
    * @param origin contains the sequence string.
    */
    public GenBankFeatures(final String definition,
            final String accession,
            final String organism,
            final List<CodingSequence> cdsElements,
            final List<Gene> geneElements,
            final String origin) {

        this.definition = definition;
        this.accession = accession;
        this.organism = organism;
        this.cdsElements = cdsElements;
        this.geneElements = geneElements;
        this.origin = origin;
    }

    /**
    * Get definition of GenBank file.
    * @return definition.
    */
    public String getDefinition() {
        return definition;
    }

    /**
    * Get accession of GenBank file.
    * @return accession.
    */
    public String getAccession() {
        return accession;
    }

    /**
    * Get organism name.
    * @return organism.
    */
    public String getOrganism() {
        return organism;
    }

    /**
    * Get list with CodingSequence objects.
    * @return cdsElements.
    */
    public List<CodingSequence> getCdsElements() {
        return cdsElements;
    }

    /**
    * Get list with Gene objects.
    * @return geneElements.
    */
    public List<Gene> getGeneElements() {
        return geneElements;
    }

    /**
    * Get sequence of GenBank.
    * @return origin.
    */
    public String getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return "Definition: " + getDefinition() + ", Acceccion: " + getAccession() + ", Organism: " + getOrganism()
                + ", CDS Elements: " + getCdsElements() + ", Gene Elements: " + getGeneElements() + ", Origin: "
                + getOrigin();
    }

    /**
    * Get a summary of GanBank file.
    * @param fileName is a string of GenBank file name.
    * @return string containing summary information.
    */
    public ArrayList getSummary(final String fileName) {

        ArrayList<String> summaryList = new ArrayList<String>();

        /* Create counter and count total forward oriented gene(s). */
        int countForward = 0;
        for (Gene i : getGeneElements()) {
            if (i.getDirection() == SequenceOrientation.FORWARD) {
                countForward++;
            }
        }

        summaryList.add("file: " + fileName);
        summaryList.add("organism: " + getOrganism());
        summaryList.add("accession: " + getAccession());
        summaryList.add("sequence length: " + getOrigin().length() + " bp");
        summaryList.add("number of genes: " + getGeneElements().size());
        summaryList.add("gene F/R balance: " + (float) countForward / getGeneElements().size());
        summaryList.add("number of CDS's: " + getCdsElements().size());
        /* Return List summary. */
        return summaryList;
    }

    /**
    * Get all sequences from genes that match gene regex pattern.
    * @param gene is a regex string of gene name.
    * @return string containing all found genes in fasta format.
    */
    public ArrayList fetchGene(final String gene) {

        /* Initialize arraylist for all found genes. */
        ArrayList<String> geneList = new ArrayList<String>();
        Pattern patternGene = Pattern.compile(".");

        try {
            patternGene = Pattern.compile(gene);
        } catch (PatternSyntaxException e) {
            geneList.add("Gene pattern '" + gene + "' is not valid pattern, '.' was used instead.");
        } finally {
            /* Iterate gene elements, create pattern and append first line to sb. */
            for (Gene i : getGeneElements()) {

                Matcher matchGene = patternGene.matcher(i.getGene());
                if (matchGene.find()) {
                    geneList.add(">gene ".concat(i.getGene() + " sequence"));

                    /* Get sequence of matched gene, add newlines and append to sb. */
                    int lineLength = 80;
                    StringBuilder geneSequence = new StringBuilder(
                            getOrigin().subSequence((int) i.getCoordinates().getFirst() - 1,
                            (int) i.getCoordinates().getLast()));

                    geneList.add(geneSequence.toString());
                }
            }
        }

        /* When no matches, return 'nothing found' message. */
        if (geneList.isEmpty()) {
            geneList.add("Gene pattern '" + gene + "' was not found in the given GenBank.");
            return geneList;
        } else {
            return geneList;
        }
    }

    /**
    * Get all translation sequences from CDSs that match cds regex pattern.
    * @param cds is a regex string of CDS name.
    * @return string containing all found CDSs in fasta format.
    */
    public ArrayList fetchCds(final String cds) {

        /* Initialize arraylist for all found CDSs. */
        ArrayList<String> cdsList = new ArrayList<String>();
        Pattern patternCDS = Pattern.compile(".");

        try {
            patternCDS = Pattern.compile(cds);
        } catch (PatternSyntaxException e) {
            cdsList.add("CDS pattern '" + cds + "' is not valid pattern, '.' was used instead.");
        } finally {
            /* Iterate CDS elements, create pattern and append first line to sb. */
            for (CodingSequence i : getCdsElements()) {

                Matcher matchCDS = patternCDS.matcher(i.getProduct());
                if (matchCDS.find()) {
                    cdsList.add(""
                            + ">CDS ".concat(i.getProduct() + " sequence"));

                    /* Get translated sequence of matched CDS, add newlines and append to sb. */
                    int lineLength = 80;
                    StringBuilder cdsSequence = new StringBuilder(i.getTranslation());

                    cdsList.add(cdsSequence.toString());
                }
            }
        }

        /* When no matches, return 'nothing found' message. */
        if (cdsList.isEmpty()) {
            cdsList.add("CDS pattern '" + cds + "' was not found in the given GenBank.");
            return cdsList;
        } else {
            return cdsList;
        }
    }

    /**
    * Get features that lay between min and max coordinates.
    * @param maxCoordinates is a string with min and max coordinates.
    * @return string with al found features.
    * @throws NumberFormatException when maxCoordinates does not have correct format.
    */
    public ArrayList fetchFeatures(final String maxCoordinates) {

        ArrayList<String> featuresList = new ArrayList<String>();

        try {
            /* Split coordinates and create Coordinates object. */
            String[] coordinates = maxCoordinates.replaceAll("\\s", "").split("\\,");
            Coordinates cs = new Coordinates(Long.parseLong(coordinates[0]),
                    Long.parseLong(coordinates[coordinates.length - 1]));

            /* For each gene element add gene features to sb if coordinates are whithin given coordinates. */
            for (Gene g : getGeneElements()) {
                if (g.getCoordinates().getFirst() > cs.getFirst()
                        && g.getCoordinates().getLast() < cs.getLast()) {

                    featuresList.add(g.getGene() + ";gene;" + g.getCoordinates().getFirst() + ";"
                            + g.getCoordinates().getLast() + ";" + g.getDirection().getType());

                    /* If gene has been added, check for coresponding CDS element and add it to sb. */
                    for (CodingSequence c : getCdsElements()) {
                        if (g.getCoordinates().getFirst() == c.getCoordinates().getFirst()
                                && g.getCoordinates().getLast() == c.getCoordinates().getLast()) {

                            featuresList.add(c.getProduct() + ";CDS;" + c.getCoordinates().getFirst() + ";"
                                    + c.getCoordinates().getLast() + ";" + g.getDirection().getType());
                        }
                    }
                }
            }

            /* When no matches, return 'nothing found' message. */
            if (featuresList.isEmpty()) {
                featuresList.add("No gene(s) or CDS(s) was/were found between '" + maxCoordinates + "' in the given GenBank.");
                return featuresList;
            } else {
                featuresList.add(0, "FEATURE;TYPE;START;STOP;ORIENTATION");
                return featuresList;
            }

        /* If maxCoordinates does not have right format, return error string. */
        } catch (NumberFormatException e) {
            featuresList.add("A problem occured: '" + maxCoordinates + "' should be two numbers seperated by a comma."
                    + "The second number should have a max length of 9223372036854775807."
                    + "Only the first (before first comma) and last (after last comma) numbers will be used.");
            return featuresList;
        }
    }

    /**
    * Get start position sites were pattern matched.
    * @param pattern is a iupac sequence string.
    * @return string with al found sites and corresponding genes.
    */
    public ArrayList findSites(final String pattern) {

        ArrayList<String> sitesList = new ArrayList<String>();
        
        /* Initialize HashMap and add iupac codes to them. */
        Map<String, String> iupacCodes = new HashMap<>();
        iupacCodes.put("R", "[AG]");
        iupacCodes.put("Y", "[CT]");
        iupacCodes.put("S", "[GC]");
        iupacCodes.put("W", "[AT]");
        iupacCodes.put("K", "[GT]");
        iupacCodes.put("M", "[AC]");
        iupacCodes.put("B", "[CGT]");
        iupacCodes.put("D", "[AGT]");
        iupacCodes.put("H", "[ACT]");
        iupacCodes.put("V", "[ACG]");
        iupacCodes.put("N", "[ACGT]");

        /* Remove regex related charecters from pattern. */
        String filteredPattern = pattern.replaceAll("[^\\w]*[0-9]*", "").toUpperCase();
        String regexPattern = "";

        if (filteredPattern.equals("")) {
            sitesList.add("The site pattern '" + pattern + "' is not a valid pattern."
                    + "Pattern should only contain IUPAC codes.");
            return sitesList;
        } else {
            /* Build own regex pattern by using the previously initialized HashMap */
            for (int i = 0; i < filteredPattern.length(); i++) {
                if (iupacCodes.containsKey(filteredPattern.substring(i, i + 1))) {
                    regexPattern += iupacCodes.get(filteredPattern.substring(i, i + 1));
                } else {
                    regexPattern += filteredPattern.substring(i, i + 1);
                }
            }

            /* Compile regex pattern and match pattern to origin sequence. */
            Matcher matchSequence = Pattern.compile(regexPattern).matcher(getOrigin());

            while (matchSequence.find()) {
                boolean match = false;
                int startPosition = matchSequence.start();

                /* For each gene element check if match within gene and append. */
                for (Gene i : getGeneElements()) {
                    if (i.getCoordinates().getFirst() < startPosition
                            && i.getCoordinates().getLast() > startPosition) {

                        match = true;
                        sitesList.add((startPosition + 1) + ";" + matchSequence.group() + ";" + i.getGene());
                    }
                }

                /* When match not within gene position, append match as intergenic. */
                if (!match) {
                    sitesList.add((startPosition + 1) + ";" + matchSequence.group() + ";INTERGENIC");
                }
            }

            /* When no matches, return 'nothing found' message. */
            if (sitesList.isEmpty()) {
                sitesList.add("The pattern '" + filteredPattern + "' did not result in matches within the given GenBank.");
                return sitesList;
            } else {
                sitesList.add(0, "site search: " + filteredPattern + " (regex: " + regexPattern + ")");
                sitesList.add(1, "POSITION;SEQUENCE;GENE");
                return sitesList;
            }
        }
    }
}
