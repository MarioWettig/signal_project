package com.cardio_generator;

import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.OutputStrategy;
import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.readers.DataReader;
import com.data_management.readers.WebSocketReader;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class HealthSimulatorRunner {
    public static void main(String[] args) {
        try {
            int patientCount = 50;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(patientCount * 4);
            OutputStrategy outputStrategy = new WebSocketOutputStrategy(8080);
            DataStorage dataStorage = DataStorage.getDataStorageInstance();
            DataReader reader = new WebSocketReader("ws://localhost:8080");

            HealthDataSimulator simulator = HealthDataSimulator.getHealthDataSimulatorInstance(patientCount, scheduler, outputStrategy);

            simulator.start();
            reader.readData(dataStorage);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
