package com.data_management.readers;

import com.data_management.DataStorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextFileReader implements DataReader {

    private String filePath;

    public TextFileReader(){}

    public TextFileReader(String filePath){this.filePath = filePath;}

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void readData(DataStorage dataStorage) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Patient ID:")) {
                    String[] parts = line.split(", ");
                    int patientId = Integer.parseInt(parts[0].split(": ")[1]);
                    long timestamp = Long.parseLong(parts[1].split(": ")[1]);
                    String label = parts[2].split(": ")[1];
                    double data = Double.parseDouble(parts[3].split(": ")[1]);

                    dataStorage.addPatientData(patientId, data, label, timestamp);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw e;
        }
    }

    public void readString(String message) throws IOException {

    }

}
