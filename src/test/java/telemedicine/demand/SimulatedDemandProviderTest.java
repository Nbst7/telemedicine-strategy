package telemedicine.demand;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimulatedDemandProviderTest {

    @ParameterizedTest
    @CsvSource({
            "03:00, 0.3",
            "09:30, 0.8",
            "14:00, 1.2",
            "19:00, 2.5",
            "22:00, 0.3"
    })
    void loadDependsOnTheHour(String time, double expected) {
        Clock clock = Clock.fixed(Instant.parse("2026-10-19T" + time + ":00Z"), ZoneOffset.UTC);
        assertEquals(expected, new SimulatedDemandProvider(clock).loadFactor());
    }
}
