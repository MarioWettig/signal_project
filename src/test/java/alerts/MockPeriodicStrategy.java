package alerts;

import com.alerts.alerts.Alert;
import com.alerts.strategies.StrategyPattern;
import com.data_management.Patient;

public class MockPeriodicStrategy implements StrategyPattern {
    private boolean shouldGenerateAlert;

    public MockPeriodicStrategy(boolean shouldGenerateAlert) {
        this.shouldGenerateAlert = shouldGenerateAlert;
    }

    @Override
    public Alert checkAlert(Patient patient) {
        if (shouldGenerateAlert) {
            return new Alert(String.valueOf(patient.getPatientId()), "Mock Periodic Condition", System.currentTimeMillis(), 1);
        }
        return null;
    }
}
