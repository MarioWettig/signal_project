package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.OutputStrategy;
import com.data_management.DataStorage;

public class BloodLevelsDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private final double[] baselineCholesterol;
    private final double[] baselineWhiteCells;
    private final double[] baselineRedCells;

    private DataStorage storage;

    public BloodLevelsDataGenerator(int patientCount, DataStorage storage) {
        // Initialize arrays to store baseline values for each patient
        baselineCholesterol = new double[patientCount + 1];
        baselineWhiteCells = new double[patientCount + 1];
        baselineRedCells = new double[patientCount + 1];

        // Generate baseline values for each patient
        for (int i = 1; i <= patientCount; i++) {
            baselineCholesterol[i] = 150 + random.nextDouble() * 50; // Initial random baseline
            baselineWhiteCells[i] = 4 + random.nextDouble() * 6; // Initial random baseline
            baselineRedCells[i] = 4.5 + random.nextDouble() * 1.5; // Initial random baseline
        }

        this.storage = storage;
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Generate values around the baseline for realism
            double cholesterol = baselineCholesterol[patientId] + (random.nextDouble() - 0.5) * 10; // Small variation
            double whiteCells = baselineWhiteCells[patientId] + (random.nextDouble() - 0.5) * 1; // Small variation
            double redCells = baselineRedCells[patientId] + (random.nextDouble() - 0.5) * 0.2; // Small variation

            long time =  System.currentTimeMillis();

            if( outputStrategy instanceof ConsoleOutputStrategy) {
                storage.addPatientData(patientId, cholesterol, "Cholesterol", time);
                storage.addPatientData(patientId, whiteCells, "WhiteBloodCells", time);
                storage.addPatientData(patientId, redCells, "RedBloodCells", time);
            }


            // Output the generated values
            outputStrategy.output(patientId, time, "Cholesterol", Double.toString(cholesterol));
            outputStrategy.output(patientId, time, "WhiteBloodCells",
                    Double.toString(whiteCells));
            outputStrategy.output(patientId, time, "RedBloodCells", Double.toString(redCells));

        } catch (Exception e) {
            System.err.println("An error occurred while generating blood levels data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
