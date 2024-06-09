import com.cardio_generator.HealthDataSimulator;
import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HealthSimulatorSingletonTest {
    private HealthDataSimulator healthDataSimulator;
    private ScheduledExecutorService mockScheduler;
    private OutputStrategy mockOutputStrategy;

    @BeforeEach
    void setUp() {
        mockScheduler = mock(ScheduledExecutorService.class);
        mockOutputStrategy = mock(OutputStrategy.class);
        healthDataSimulator = HealthDataSimulator.getHealthDataSimulatorInstance(50, mockScheduler, mockOutputStrategy);
    }

    @Test
    void testSingletonInstance() {
        HealthDataSimulator anotherInstance = HealthDataSimulator.getHealthDataSimulatorInstance(50, mockScheduler, mockOutputStrategy);
        assertSame(healthDataSimulator, anotherInstance, "Instances should be the same");
    }

    @Test
    void testResetInstance() {
        HealthDataSimulator.resetInstance();
        HealthDataSimulator newInstance = HealthDataSimulator.getHealthDataSimulatorInstance(50, mockScheduler, mockOutputStrategy);
        assertNotSame(healthDataSimulator, newInstance, "Instances should be different after reset");
    }
}
