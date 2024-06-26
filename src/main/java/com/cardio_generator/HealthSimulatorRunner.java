package com.cardio_generator;

import com.alerts.outputstrategy.ConsoleOutput;
import com.cardio_generator.outputs.FileOutputStrategy;
import com.cardio_generator.outputs.OutputStrategy;
import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.readers.DataReader;
import com.data_management.readers.TextFileReader;
import com.data_management.readers.WebSocketReader;
import com.alerts.health_processor.HealthDataProcessor;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class HealthSimulatorRunner {

    /**
     * using this class to run the project, the HealthDataProcessor encapsulates the health evaluation and alert management system
     * using websocket as output
     *
     * @param args
     */


    public static void main(String[] args) {
        try {
            int patientCount = 50;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(patientCount * 4);
            OutputStrategy outputStrategy = new WebSocketOutputStrategy(8080);
            DataStorage dataStorage = DataStorage.getDataStorageInstance();
            DataReader reader = new WebSocketReader("ws://localhost:8080");

            HealthDataProcessor processor = new HealthDataProcessor(new ConsoleOutput(), null);
            dataStorage.addDataListener(processor);

            HealthDataSimulator simulator = HealthDataSimulator.getHealthDataSimulatorInstance(patientCount, scheduler, outputStrategy);

            simulator.start();
            reader.readData(dataStorage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
