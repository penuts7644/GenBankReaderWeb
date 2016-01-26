/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */

package nl.bioinf.wvanhelvoirt.genbankreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wout van Helvoirt [wout.van.helvoirt@gmail.com]
 * @version 1.0.0
 */
public class GeneBankFileParser implements Serializable {

    /**
     * inputFile is the path of the input file.
     */
    private final String inputFile;

    /**
     * The constructor which checks given input file path.
     * @param inputFile is needed, is input file path.
     * @throws FileNotFoundException when input is not a genbank file or is a directory.
     */
    public GeneBankFileParser(final String inputFile) throws FileNotFoundException {

        /* Check if given input is a file, not a directory and is GenBank. */
        File givenInputFile = new File(inputFile);
        if (givenInputFile.isFile() && !givenInputFile.isDirectory() && inputFile.matches(".*\\.gbk$|.*\\.gb$")) {
            this.inputFile = inputFile;
        } else {
            throw new FileNotFoundException("Given file does not exist, is a directory or is not a GenBank.");
        }
    }

    /**
     * Function for parsing GenBank file contents.
     * @return GenBankFeatures object that contains all GenBank data.
     */
    public GenBankFeatures ParseGenBankContent() {

        /* Initialize vaiables used for GenBank data. */
        Path path = Paths.get(getInputFile());
        String definition = "";
        String accession = "";
        String organism = "";
        List<CodingSequence> cdsElements = new LinkedList();
        List<Gene> geneElements = new LinkedList();
        String origin = "";

        /* Try to read input file for parsing. */
        try (BufferedReader reader = Files.newBufferedReader(path);
            Scanner scanner = new Scanner(reader);) {

            /* Find definition from GenBank. */
            scanner.findWithinHorizon(Pattern.compile("(?s)(?<=DEFINITION)(.+?)(?=ACCESSION)"), 0);
            definition = scanner.match().group(1).replaceAll("\\s+", " ").trim();

            /* Find accession from GenBank. */
            scanner.findWithinHorizon(Pattern.compile("(?s)(?<=ACCESSION)(.+?)(?=VERSION)"), 0);
            accession = scanner.match().group(1).replaceAll("\\s+", " ").trim();

            /* Find from organism to origin sequence from GenBank. */
            scanner.findWithinHorizon(Pattern.compile("(?s)source.+?/organism=\"(.+?)\"(.+?)(?=ORIGIN)"), 0);

            /* Find organism from previous scanner match. */
            organism = scanner.match().group(1);

            /* For each CDS found in prevoius scanner match retrieve data. */
            Matcher matchCDS = Pattern.compile("(?s)(?<=CDS)(.+?/translation=\".+?\")")
                    .matcher(scanner.match().group(2));
            while (matchCDS.find()) {

                /* Initialize vaiables used for each CDS. */
                String product = "";
                String proteinID = "";
                String translation = "";
                SequenceOrientation direction = SequenceOrientation.FORWARD;

                /* Check orientation of CDS. */
                String[] cds = matchCDS.group(1).replaceAll("[\\s\"]+", "").split("/");
                if (cds[0].contains("complement")) {
                    direction = SequenceOrientation.REVERSE;
                }

                /* Create coordinates object containing start and stop values. */
                String[] coordinates = cds[0].replaceAll("[^\\d\\.]+", "").split("\\.\\.");
                Coordinates cs = new Coordinates(Integer.parseInt(coordinates[0]),
                        Integer.parseInt(coordinates[coordinates.length - 1]));

                /* Retrieve all useful data for given CDS. */
                for (String i : cds) {
                    if (i.contains("product")) {
                        product = i.replace("product=", "");
                    } else if (i.contains("protein_id")) {
                        proteinID = i.replace("product_id=", "");
                    } else if (i.contains("translation")) {
                        translation = i.replace("translation=", "").toUpperCase();
                    }
                }

                /* Add CDS object to list with CDS(s) */
                CodingSequence cdsObject = new CodingSequence(cs, product, proteinID, translation, direction);
                cdsElements.add(cdsObject);
            }

            /* For each Gene found in prevoius scanner match retrieve data. */
            Matcher matchGene = Pattern.compile("((?s)(?<=[^/]gene)(.+?/gene=\".+?\")|"
                    + "(?s)(?<=[^/]gene)(.+?/db_xref=\".+?\"))").matcher(scanner.match().group(2));
            while (matchGene.find()) {

                /* Initialize vaiables used for each Gene. */
                String genename = "";
                SequenceOrientation direction = SequenceOrientation.FORWARD;

                /* Check orientation of Gene. */
                String[] gene = matchGene.group(1).replaceAll("[\\s\"]+", "").split("/");
                if (gene[0].contains("complement")) {
                    direction = SequenceOrientation.REVERSE;
                }

                /* Create coordinates object containing start and stop values. */
                String[] coordinates = gene[0].replaceAll("[^\\d\\.]+", "").split("\\.\\.");
                Coordinates cs = new Coordinates(Integer.parseInt(coordinates[0]),
                        Integer.parseInt(coordinates[coordinates.length - 1]));

                /* Retrieve all useful data for given Gene. */
                for (String i : gene) {
                    if (i.contains("locus_tag")) {
                        genename = i.replace("locus_tag=", "");
                    } else if (i.contains("gene")) {
                        genename = i.replace("gene=", "");
                    }
                }

                /* Add Gene object to list with Gene(s) */
                Gene geneObject = new Gene(cs, genename, direction);
                geneElements.add(geneObject);
            }

            /* Find from origin from GenBank. */
            scanner.findWithinHorizon(Pattern.compile("(?s)(?<=ORIGIN)(.+?)(?=//)"), 0);
            origin = scanner.match().group(1).replaceAll("\\s*\\d*", "").toUpperCase();

            /* Close scanner en return GenBankfeatures object. */
            scanner.close();
            return new GenBankFeatures(definition, accession, organism, cdsElements, geneElements, origin);

        } catch (IOException e) {
            System.out.println("A problem occured: " + e + "\n");
        }
        return null;
    }

    /**
    * Get inputFile value.
    * @return inputFile.
    */
    public String getInputFile() {
        return inputFile;
    }

    @Override
    public String toString() {
        return "Input file: " + getInputFile();
    }

    /**
    * Get inputFileName value.
    * @return inputFile.
    */
    public String getInputName() {
        Path path = Paths.get(getInputFile());
        return path.getFileName().toString();
    }
}
