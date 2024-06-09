package com.cardio_generator.outputs;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;

public class ConsoleOutputStrategy implements OutputStrategy {

    DataStorage dataStorage= DataStorage.getDataStorageInstance();
    AlertGenerator alertGenerator= new AlertGenerator(dataStorage);
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);



        char[] value= data.toCharArray();
        StringBuilder value2= new StringBuilder(data);
        if (value[value.length-1]=='%') {
            value2 = new StringBuilder();
            for (int i = 0; i < value.length-1; i++) {
                value2.append(value[i]);
            }
        }


        dataStorage.addPatientData(patientId,Double.parseDouble(value2.toString()),label,timestamp);
        alertGenerator.evaluateData(dataStorage.getPatientMap().get(patientId));




    }
}
