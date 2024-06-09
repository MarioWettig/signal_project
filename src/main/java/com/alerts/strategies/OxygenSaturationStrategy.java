package com.alerts.strategies;

import com.alerts.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class OxygenSaturationStrategy implements StrategyPattern {
    AlertFactory factory;


    @Override
    public Alert checkAlert(Patient patient) {

        PatientRecord lastUploaded= patient.getPatientRecords().get(patient.getPatientRecords().size()-1);
        double currentValue=lastUploaded.getMeasurementValue();


        if (currentValue<92) {
            factory= new BloodOxygenAlertFactory();
            // if this is triggered, we do not investigate the saturation drop alert
            return factory.createAlert(String.valueOf(patient.getPatientId()),"Low Saturation", lastUploaded.getTimestamp(), 0);
        }


        // second priority alert
        List<PatientRecord> record= patient.getRecords(lastUploaded.getTimestamp()-6000,lastUploaded.getTimestamp());
        double max=0;
        boolean condition=false;
        for (int i = 0; i < record.size()-1; i++) {
            if (record.get(i).getRecordType().equals("Saturation")){
                if (record.get(i).getMeasurementValue()>max) {
                    condition=true;
                    max=record.get(i).getMeasurementValue();
                }
            }
        } if (condition && lastUploaded.getMeasurementValue()-max>5) {
            factory= new BloodOxygenAlertFactory();
            return factory.createAlert(String.valueOf(patient.getPatientId()),"Saturation drop", lastUploaded.getTimestamp(), 1);
        }
        return null;
    }
}
