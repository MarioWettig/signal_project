package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This implementation is designed to write patient data into files stored in selected directory.
 * @author MarioWettig
 */
public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory; // The base directory where files will be stored

    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>(); // Mapping of labels to file paths

    /**
     * creates a FileOutputStrategy object with the specified directory.
     *
     * @param baseDirectory the base directory where files will be stored
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory; // Initialize the base directory
    }

    /**
     * it creates a file with the label if such file does not exist
     * it writes the data into the file
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp in which the data was collected
     * @param label     the label that describes the data
     * @param data      the patient data to be written into a file then stored in the directory
     *
     * @throws IOException if an I/O error occurs while creating directories or writing to the file
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) throws IOException {
        try {
            // Create the directory if it does not exist
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            throw e; // Re-throw the exception
        }

        // Set the file path based on the label
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
            throw e; // Re-throw the exception
        }
    }
}
