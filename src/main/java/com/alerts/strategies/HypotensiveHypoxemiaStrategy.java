package com.alerts.strategies;

import com.alerts.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.HypotensiveHypoxemiaAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HypotensiveHypoxemiaStrategy implements StrateyPattern{
    AlertFactory factory;

    @Override
    public Alert checkAlert(Patient patient) {

        PatientRecord lastUploaded= patient.getPatientRecords().get(patient.getPatientRecords().size()-1);

        List<PatientRecord> record= patient.getRecords(lastUploaded.getTimestamp()-6000,lastUploaded.getTimestamp());

        if (record.size()<2) return null;

        double currentValue=lastUploaded.getMeasurementValue();

        boolean foundSaturation=false;
        boolean foundSystolic=false;

        double saturation=0;
        double systolic=0;

        if (patient.getPatientRecords().get(patient.getPatientRecords().size()-1).getRecordType().equals("Saturation")){
            foundSaturation=true;
            saturation=currentValue;
        } else {
            foundSystolic=true;
            systolic=currentValue;
        }

        for (int i = record.size()-2; i >=0 ; i--) {
            if (record.get(i).getRecordType().equals("Saturation") && foundSystolic){
                foundSaturation=true;
                saturation=record.get(i).getMeasurementValue();
                break;
            }
            if (record.get(i).getRecordType().equals("SystolicPressure") && foundSaturation){
                foundSystolic=true;
                systolic=record.get(i).getMeasurementValue();
                break;
            }

        }

        if (foundSystolic && foundSaturation){
            if (saturation<92 && systolic<90) {
                factory= new HypotensiveHypoxemiaAlertFactory();
                return factory.createAlert(String.valueOf(patient.getPatientId()),"Hypotensive Hypoxemia Alert", lastUploaded.getTimestamp());
            }
        }

        return null;
    }
}
