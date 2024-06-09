package com.alerts.strategies;

import com.alerts.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class HeartRateStrategy implements StrategyPattern {
    AlertFactory factory;


    @Override
    public Alert checkAlert(Patient patient) {

        PatientRecord lastUploaded= patient.getPatientRecords().get(patient.getPatientRecords().size()-1);
        double currentValue=lastUploaded.getMeasurementValue();

        if (currentValue<0 || currentValue>100) {
            factory= new ECGAlertFactory();
            return factory.createAlert(String.valueOf(patient.getPatientId()),"Abnormal Heart Rate", lastUploaded.getTimestamp(),1);
        }

        int ECG_values=0;
        double anomaly=20;
        double average=0;

        List<PatientRecord> record= patient.getRecords(lastUploaded.getTimestamp()-6000,lastUploaded.getTimestamp());
        if (record.size()>5){
            for (int i = 0; i < record.size()-1; i++) {
                if (record.get(i).getRecordType().equals("ECG")){
                    ECG_values++;
                    average+=record.get(i).getMeasurementValue();
                }
            }
            if (ECG_values<4){
                if (Math.abs(currentValue-average/ECG_values)>anomaly) {
                    factory= new ECGAlertFactory();
                    return factory.createAlert(String.valueOf(patient.getPatientId()),"Irregular Heart Rate", lastUploaded.getTimestamp(), 0);
                }
            }
        }
        return null;
    }
}
