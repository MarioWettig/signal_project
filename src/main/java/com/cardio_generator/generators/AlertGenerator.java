package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * this class implements PatientDataGenerator and therefore it is a generator
 * It simulates alerts being triggered and resolved, and outputs them using the provided OutputStrategy.
 *
 * @author MarioWettig
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();

    private boolean[] alertStates; // Indicates whether an alert is currently triggered for each patient

    /**
     * Constructs an AlertGenerator object with the specified number of patients.
     *
     * @param patientCount number of patients for which to run the simulation
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1]; // Initialize the alert states for each patient
    }

    /**
     * Generates alerts for the patients and outputs them using the provided OutputStrategy.
     *
     * @param patientId      ID of the patient
     * @param outputStrategy output strategy used to output the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                // If an alert is currently triggered
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false; // solves the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // If no alert is currently triggered
                double lambda = 0.1; // Average rate , adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;
                if (alertTriggered) {
                    alertStates[patientId] = true; // triggers the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during data generation
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace(); // Print the stack trace to help identify where the error occurred
        }
    }
}
