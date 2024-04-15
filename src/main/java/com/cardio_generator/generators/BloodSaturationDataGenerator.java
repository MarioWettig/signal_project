package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

import java.util.Random;

/**
 *
 * It simulates blood saturation values and outputs them using the provided OutputStrategy.
 *
 * @author MarioWettig
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    private static final Random random = new Random();
    private int[] lastSaturationValues; // Array to store the last saturation values for each patient

    /**
     * Constructs a BloodSaturationDataGenerator object with the specified number of patients.
     *
     * @param patientCount the number of patients for which to generate blood saturation data
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1]; // Initialize the array with the specified number of patients

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates blood saturation data for the specified patient and outputs it using the provided OutputStrategy.
     *
     * @param patientId      the ID of the patient
     * @param outputStrategy strategy used to output the generated data
     * */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;

            // Output the blood saturation data
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            // Handle any exceptions that may occur during data generation
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // Print the stack trace to help identify where the error occurred
        }
    }
}

