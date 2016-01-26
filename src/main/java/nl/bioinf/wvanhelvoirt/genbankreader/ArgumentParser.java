/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */

package nl.bioinf.wvanhelvoirt.genbankreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;

/**
 * @author Wout van Helvoirt [wout.van.helvoirt@gmail.com]
 * @version 1.0.0
 */
public class ArgumentParser {

    /**
     * @param args default arguments.
     */
    private final String[] args;

    /**
     * @param options will contain the main command line options (help, infile).
     */
    private final Options allOptions;

    /**
     * @param subOptions is group of options that contains all optional options
     * (summary, fetch_gene, fetch_cds, fetch_features, find_sites).
     */
    private final OptionGroup subOptions;

    /**
    * Constructor creating all the options for the command line parser.
    * @param args contains user's command line input.
    */
    public ArgumentParser(final String[] args) {
        this.args = args;
        this.allOptions = new Options();
        this.subOptions = new OptionGroup();

        /* Make main options, infile is required. */
        Option help = Option.builder("h")
                .longOpt("help")
                .desc("Display help for this program.")
                .required(false)
                .build();
        Option infile = Option.builder("i")
                .argName("INFILE")
                .hasArg()
                .longOpt("infile")
                .required(true)
                .desc("Input Genbank file to proces.")
                .build();

        /* Make optional options, one option is required and only one optional option can be given at a time. */
        subOptions.setRequired(true);
        Option summary = Option.builder("s")
                .longOpt("summary")
                .desc("Creates a textual summary of the parsed Genbank file.")
                .build();
        Option fetchGene = Option.builder("G")
                .argName("GENE NAME (-PATTERN)")
                .hasArg()
                .longOpt("fetch_gene")
                .desc("Returns nucleotide sequences of the genes that match the gene name regex pattern, in Fasta format.")
                .build();
        Option fetchCds = Option.builder("C")
                .argName("PRODUCT NAME (-PATTERN)")
                .hasArg()
                .longOpt("fetch_cds")
                .desc("Returns the amino acid sequences of the CDSs that match the product"
                        + " name regex pattern, in Fasta format.")
                .build();
        Option fetchFeatures = Option.builder("F")
                .argName("COORDINATES")
                .hasArg()
                .longOpt("fetch_features")
                .desc("Returns all features with name, type, start, stop and orientation between the given coordinates."
                        + "Only features that are completely covered within the given region are listed.")
                .build();
        Option findSites = Option.builder("S")
                .argName("DNA SEQ WITH IUPAC CODES")
                .hasArg()
                .longOpt("find_sites")
                .desc("Lists the locations of all the sites where the DNA pattern is found: position, actual sequence"
                        + " and (if relevant) the gene in which it resides.")
                .build();

        /* Add optional options to subOptions and add main options (incl subOptions Group) to all available options. */
        subOptions.addOption(summary);
        subOptions.addOption(fetchGene);
        subOptions.addOption(fetchCds);
        subOptions.addOption(fetchFeatures);
        subOptions.addOption(findSites);
        allOptions.addOption(help);
        allOptions.addOption(infile);
        allOptions.addOptionGroup(subOptions);
    }

    /**
    * Function for retrieving user's command line input.
    * @return Option
    */
    public final List parseArguments() {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmdArguments;
        List parsedArguments = new ArrayList();

        /* Try to parse arguments, otherwise give error message and print help. */
        try {
            cmdArguments = parser.parse(getAllOptions(), getArgs());

            /* If help option is given, print help message. */
            if (cmdArguments.hasOption("h")) {
                help();
            } else {
                parsedArguments.add(0, cmdArguments.getOptionValue("i"));
                if (cmdArguments.hasOption("s")) {
                    parsedArguments.add(1, "summary");
                } else if (cmdArguments.hasOption("G")) {
                    parsedArguments.add(1, "fetch_gene");
                    parsedArguments.add(2, cmdArguments.getOptionValue("G"));
                } else if (cmdArguments.hasOption("C")) {
                    parsedArguments.add(1, "fetch_cds");
                    parsedArguments.add(2, cmdArguments.getOptionValue("C"));
                } else if (cmdArguments.hasOption("F")) {
                    parsedArguments.add(1, "fetch_features");
                    parsedArguments.add(2, cmdArguments.getOptionValue("F"));
                } else if (cmdArguments.hasOption("S")) {
                    parsedArguments.add(1, "find_sites");
                    parsedArguments.add(2, cmdArguments.getOptionValue("S"));
                }
                return parsedArguments;
            }

        } catch (ParseException e) {
            System.out.println("A problem occured: " + e.getMessage() + "\n");
            help();
        }
        return null;
    }

    /**
    * Help function that prints usage and available parameters.
    * It uses the main class it's name for displaying usage.
    */
    public final void help() {

        /* Get the correct thread that is equal to the name of program */
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement main = stack[stack.length - 1];
        String mainClassName = main.getClassName().replaceAll(".*\\.", "");

        /* Use formatter to create correct help output text. */
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(100,
                "java -jar " + mainClassName + ".jar",
                "\nThis commandline java program is able to parse a Genbank file.\n"
                        + "It uses only the first GenBank if a multi-record GenBank file is given as input.\n\n",
                getAllOptions(),
                "\nCopyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com]",
                true);
        System.exit(0);
    }

    /**
    * Get arguments values.
    * @return args.
    */
    public final String[] getArgs() {
        return args;
    }

    /**
    * Get all option values.
    * @return allOptions.
    */
    public final Options getAllOptions() {
        return allOptions;
    }

    /**
    * Get sub option values.
    * @return subOptions.
    */
    public final OptionGroup getSubOptions() {
        return subOptions;
    }

    @Override
    public String toString() {
        return "User arguments: " + Arrays.toString(getArgs()) + ", Available options: " + getAllOptions();
    }
}
