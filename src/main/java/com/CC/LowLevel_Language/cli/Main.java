package com.CC.LowLevel_Language.cli;

import com.CC.LowLevel_Language.compiler.TACtoAssemblyConverter;
import com.CC.LowLevel_Language.compiler.result.ConversionResult;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {
    private static TACtoAssemblyConverter converter;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            converter = new TACtoAssemblyConverter();

            if (args.length > 0) {
                // File input mode
                String inputFile = args[0];
                // Replace the problematic line with explicit if-else:
                String outputFile;
                if (args.length > 1) {
                    outputFile = args[1];
                } else {
                    outputFile = "output.asm";
                }

                processFile(inputFile, outputFile);
            } else {
                // Interactive mode
                runInteractiveMode();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fatal error in main: ", e);
            System.exit(1);
        }
    }

    private static void processFile(String inputFile, String outputFile) {
        try {
            LOGGER.entering(Main.class.getName(), "processFile", new Object[]{inputFile, outputFile});
            LOGGER.info("Processing file: " + inputFile);

            if (!Files.exists(Paths.get(inputFile))) {
                throw new FileNotFoundException("Input file not found: " + inputFile);
            }

            // Read TAC code from file
            String tacCode = Files.readString(Paths.get(inputFile));
            if (tacCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Input file is empty");
            }

            // Convert TAC to Assembly with default options
            ConversionResult result = converter.convertWithDetails(tacCode);

            handleConversionResult(result, outputFile);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File operation failed", e);
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Conversion failed", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleConversionResult(ConversionResult result, String outputFile) {
        try {
            if (result.isSuccess()) {
                // Create parent directories if they don't exist
                Path outputPath = Paths.get(outputFile);
                if (outputPath.getParent() != null) {
                    Files.createDirectories(outputPath.getParent());
                }

                // Write assembly code to output file
                Files.writeString(outputPath, result.getAssemblyCode());

                System.out.println("Conversion successful!");
                System.out.println("TAC Instructions parsed: " +
                        (result.getTacInstructions() != null ? result.getTacInstructions().size() : 0));
                System.out.println("Assembly lines generated: " +
                        (result.getAssemblyLines() != null ? result.getAssemblyLines().size() : 0));
                System.out.println("\nGenerated Assembly Code:");
                System.out.println("========================");
                System.out.println(result.getAssemblyCode());

            } else {
                LOGGER.warning("Conversion failed: " + result.getMessage());
                System.err.println("Conversion failed: " + result.getMessage());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write output file", e);
            System.err.println("Failed to write output file: " + e.getMessage());
        }
    }

    private static void runInteractiveMode() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("TAC to Assembly Converter - Interactive Mode");
            System.out.println("===========================================");
            System.out.println("Enter TAC code (type 'END' on a new line to finish):");

            StringBuilder tacCode = new StringBuilder();
            String line;

            while (!(line = scanner.nextLine()).equals("END")) {
                tacCode.append(line).append("\n");
            }

            if (tacCode.length() > 0) {
                processInteractiveInput(tacCode.toString(), scanner);
            } else {
                System.out.println("No TAC code provided.");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in interactive mode", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void processInteractiveInput(String tacCode, Scanner scanner) {
        try {
            ConversionResult result = converter.convertWithDetails(tacCode);

            System.out.println("\n" + "=".repeat(50));

            if (result.isSuccess()) {
                System.out.println("Conversion Result:");
                System.out.println("=================");
                System.out.println(result.getAssemblyCode());

                handleInteractiveSave(result, scanner);
            } else {
                System.out.println("Conversion failed: " + result.getMessage());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing interactive input", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleInteractiveSave(ConversionResult result, Scanner scanner) {
        System.out.print("Save to file? (y/n): ");
        String saveChoice = scanner.nextLine();

        if (saveChoice.toLowerCase().startsWith("y")) {
            System.out.print("Enter filename (default: output.asm): ");
            String filename = scanner.nextLine();
            if (filename.trim().isEmpty()) {
                filename = "output.asm";
            }

            try {
                Files.writeString(Paths.get(filename), result.getAssemblyCode());
                System.out.println("Assembly code saved to: " + filename);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error saving file", e);
                System.err.println("Error saving file: " + e.getMessage());
            }
        }
    }
}
