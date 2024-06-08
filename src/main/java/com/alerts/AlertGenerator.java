package com.alerts;

import com.alerts.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        PatientRecord current= patient.getPatientRecords().get(patient.getPatientRecords().size()-1);
            switch (current.getRecordType()) {
                case ("SystolicPressure"):
                    evaluateSystolicPressure(patient, current);
                    break;
                case ("DiastolicPressure"):
                    evaluateDiastolicPressure(patient, current);
                    break;
                case ("Saturation"):
                    evaluateSaturation(patient, current);
                    break;
                case ("ECG"):
                    // not implemented yet
            }
    }

    public void evaluateSystolicPressure(Patient patient, PatientRecord lastUploaded){
        // check trendAlert
        trendAlert(patient,lastUploaded.getRecordType());
        // checking critical value
        double currentValue= lastUploaded.getMeasurementValue();;
        if ((currentValue > 180 || currentValue < 90)) triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Threshold Alert pressure", lastUploaded.getTimestamp()));

        // Hypotensive Hypoxemia check
        if (currentValue<90) {
            List<PatientRecord> record = patient.getPatientRecords();
            boolean found = false;
            for (int i = record.size() - 1; i >= 0; i--) {
                if (record.get(i).getRecordType().equals("Saturation")) {
                    if (record.get(i).getMeasurementValue() < 92) found = true;
                    break;
                }
            }
            if (found)
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Hypotensive Hypoxemia Alert", lastUploaded.getTimestamp()));
        }
    }
    public void evaluateDiastolicPressure(Patient patient, PatientRecord lastUploaded) {
        // check trendAlert
        trendAlert(patient, lastUploaded.getRecordType());
        // checking critical value
        double currentValue = lastUploaded.getMeasurementValue();
        ;
        if ((currentValue > 120 || currentValue < 60))
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Threshold Alert pressure", lastUploaded.getTimestamp()));
    }
    public void evaluateSaturation(Patient patient, PatientRecord lastUploaded) {
        double currentValue=lastUploaded.getMeasurementValue();

        if (currentValue<92) triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Low Saturation Alert", lastUploaded.getTimestamp()));

        // Hypotensive Hypoxemia check
        if (currentValue<92) {
            List<PatientRecord> record = patient.getPatientRecords();
            boolean found = false;
            for (int i = record.size() - 1; i >= 0; i--) {
                if (record.get(i).getRecordType().equals("SystolicPressure")) {
                    if (record.get(i).getMeasurementValue() < 90) found = true;
                    break;
                }
            }
            if (found)
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Hypotensive Hypoxemia Alert", lastUploaded.getTimestamp()));
        }

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
        } if (condition && lastUploaded.getMeasurementValue()-max>5) triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Saturation drop Alert", lastUploaded.getTimestamp()));
    }

    public void trendAlert(Patient patient, String typeValue){
        int counter=1; //value counted
        List<PatientRecord> record= patient.getPatientRecords();
        if (record.size()<3) return;
        double [] values= new double[]{0,0,record.get(record.size()-1).getMeasurementValue()};
        for (int i = record.size()-2;i>=0 ; i--) {
            if (record.get(i).getRecordType().equals(typeValue)) {
                values[counter] = record.get(i).getMeasurementValue();
                counter--;
            }
            if (counter==-1) break;
        }
        if (counter!=-1) return;
        if (values[1]-values[0]>10 && values[2]-values[1]>10) triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Trend Alert",record.get(record.size()-1).getTimestamp() ));
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        System.out.println("alert for patient "+alert.getPatientId()+": "+alert.getCondition()+" at time: "+alert.getTimestamp()+" ---------------------------ALERT");
    }

    public void oldEvaluateData(Patient patient) {
        long startingEvaluation = 6000;  //ten minutes
        List<PatientRecord> list = patient.getRecords(System.currentTimeMillis() - startingEvaluation, System.currentTimeMillis());  // do not know which one to select
        int patientID = patient.getPatientId();

        double anomaly=10;
        double averageECG=0;

        boolean lowSystolicPressure=false;
        boolean lowSaturation= false;

        double max = 0;

// first value= previous systolic value    second value=consecutive times in which current value differs from previous one for a value bigger than 10;
        double[] systolicCheck= new double[]{0,0};

// first value= previous diastolic value    second value=consecutive times in which current value differs from previous one for a value bigger than 10;
        double[] diastolicCheck= new double[]{0,0};

        for (int i = 0; i < list.size(); i++) {
            PatientRecord current = list.get(i);  // current patient record being evaluated
            double currentValue = current.getMeasurementValue();  // current patient record being evaluated
            long timestamp = current.getTimestamp();

            switch (current.getRecordType()) {
                case ("SystolicPressure"):
                    //trend alter check
                    if (i==0) systolicCheck[0]=currentValue;  // first iteration, just update previous value
                    if (Math.abs(currentValue-systolicCheck[0])>10) systolicCheck[1]++;  // if difference >10 add 1 to the value of the consecutive days
                    else systolicCheck[1]=0;  // if not just set series to 0
                    systolicCheck[0]=currentValue; // update previous value
                    if (systolicCheck[1]>2 && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Trend Alert", timestamp));


                    if ((currentValue > 180 || currentValue < 90)  && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Critical Threshold Alert pressure", timestamp));
                    lowSystolicPressure= currentValue < 90;
                    if (lowSaturation && lowSystolicPressure  && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Hypotensive Hypoxemia Alert", timestamp));
                    break;
                case ("DiastolicPressure"):

                    //trend alter check
                    if (i==0) diastolicCheck[0]=currentValue;  // first iteration, just update previous value
                    if (Math.abs(currentValue-diastolicCheck[0])>10) diastolicCheck[1]++;  // if difference >10 add 1 to the value of the consecutive days
                    else diastolicCheck[1]=0;  // if not just set series to 0
                    diastolicCheck[0]=currentValue; // update previous value
                    if (diastolicCheck[1]>2  && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Trend Alert", timestamp));

                    if ((currentValue > 120 || currentValue < 60) && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Critical Threshold Alert pressure", timestamp));
                    // implement trend alert
                    break;
                case ("Saturation"):
                    if (currentValue<92.0  && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Low Saturation Alert", timestamp));
                    lowSaturation= currentValue<92;
                    if (lowSaturation && lowSystolicPressure  && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Hypotensive Hypoxemia Alert", timestamp));
                    if (currentValue>max) max=currentValue;
                    if (max-currentValue>5 && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Rapid drop Alert", timestamp));
                    break;
                case ("ECG"):
                    if (averageECG!=0 && Math.abs(averageECG-currentValue)>anomaly  && i==list.size()-1) triggerAlert(new Alert(String.valueOf(patientID), "Abnormal data", timestamp));
                    averageECG=(averageECG*i+currentValue)/(i+1);
            }
        }


    }

}
