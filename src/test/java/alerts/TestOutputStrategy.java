package alerts;

import com.alerts.outputstrategy.AlertOutputStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestOutputStrategy implements AlertOutputStrategy {
    private final List<String> alerts = new ArrayList<>();
    private CountDownLatch latch;

    public TestOutputStrategy(int expectedAlertCount) {
        this.latch = new CountDownLatch(expectedAlertCount);
    }

    public void send(String message) {
        alerts.add(message);
        latch.countDown();
        //System.out.println("Alert sent: " + message);
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public boolean awaitAlerts(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public void resetLatch(int expectedAlertCount) {
        this.latch = new CountDownLatch(expectedAlertCount);
        System.out.println("Latch reset with count: " + expectedAlertCount);
    }
}
