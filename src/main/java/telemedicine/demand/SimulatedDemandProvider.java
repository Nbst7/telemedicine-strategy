package telemedicine.demand;

import java.time.Clock;
import java.time.LocalTime;

/** Simulated source: the load depends on the hour of the day shown by the clock. */
public class SimulatedDemandProvider implements DemandProvider {

    private final Clock clock;

    public SimulatedDemandProvider(Clock clock) {
        this.clock = clock;
    }

    @Override
    public double loadFactor() {
        int hour = LocalTime.now(clock).getHour();
        if (hour >= 18 && hour < 22) {
            return 2.5;
        }
        if (hour >= 12 && hour < 18) {
            return 1.2;
        }
        if (hour >= 8 && hour < 12) {
            return 0.8;
        }
        return 0.3;
    }
}
