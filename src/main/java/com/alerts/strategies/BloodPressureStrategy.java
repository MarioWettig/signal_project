package com.alerts.strategies;

import com.alerts.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodPressureStrategy implements StrategyPattern {
    AlertFactory factory;
    @Override
    public Alert checkAlert(Patient patient) {
        PatientRecord lastUploaded= patient.getPatientRecords().get(patient.getPatientRecords().size()-1);
        double currentValue=lastUploaded.getMeasurementValue();

        if (( lastUploaded.getRecordType().equals("SystolicPressure") && (currentValue > 180 || currentValue < 90)) ||  (lastUploaded.getRecordType().equals("DiastolicPressure") && ((currentValue > 120 || currentValue < 60)))){
                factory= new BloodPressureAlertFactory();
                return factory.createAlert(String.valueOf(patient.getPatientId()),"Critical Threshold Alert pressure", lastUploaded.getTimestamp());
        }

        if (trendAlert(patient.getRecords(System.currentTimeMillis()-6000,System.currentTimeMillis()),lastUploaded)){
            factory= new BloodPressureAlertFactory();
            return factory.createAlert(String.valueOf(patient.getPatientId()),"Trend Alert", lastUploaded.getTimestamp());
        }

        return null;
    }

    private boolean trendAlert(List<PatientRecord> list, PatientRecord last) {
        if (list.size()<3) return false;
        double[] values= new double[]{last.getMeasurementValue(),0,0};
        int countValues=1;
        for (int i = list.size()-2; i >=0 ; i--) {
            if (countValues>2) break;
            if (list.get(i).getRecordType().equals(last.getRecordType())){
                values[countValues]=list.get(i).getMeasurementValue();
                countValues++;
            }
        }
        if (countValues>2){
            return ((values[0]-values[1]>10 && values[1]-values[2]>10) || (values[0]-values[1]<-10 && values[1]-values[2]<-10));
        } else
            return false;
    }
}
