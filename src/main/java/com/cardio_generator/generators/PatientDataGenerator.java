package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The PatientDataGenerator interface outlines a standard approach for generating patient-specific data.
 * It ensures that all data generators have a consistent method of generating
 */
public interface PatientDataGenerator {

    /**
     * Generates data for a specified patient.
     *
     * This method is expected to be implemented by any class that generates specific types of data,
     * such as ECG readings, blood pressure levels, etc., and needs to output this data in a flexible manner.
     *
     * @param patientId The identifier for the patient for whom data is to be generated.
     *
     * @param outputStrategy The strategy to use for outputting the generated data.
     *
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}

