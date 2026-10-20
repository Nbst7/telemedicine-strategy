package telemedicine.pricing;

import org.junit.jupiter.api.Test;
import telemedicine.model.ConsultationRequest;
import telemedicine.model.Specialty;
import telemedicine.testsupport.FakeDemandProvider;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static telemedicine.testsupport.TestData.plainRequest;

class DemandPricingTest {

    private final ConsultationRequest request = plainRequest(Specialty.GENERAL, 20); // base 2000

    private BigDecimal priceAtLoad(double load) {
        return new DemandPricing(new FakeDemandProvider(load)).price(request);
    }

    @Test
    void lowLoad_noSurcharge() {
        assertEquals(new BigDecimal("2000.00"), priceAtLoad(0.2));
    }

    @Test
    void mediumLoad_plus25Percent() {
        assertEquals(new BigDecimal("2500.00"), priceAtLoad(0.7));
    }

    @Test
    void highLoad_plus50Percent() {
        assertEquals(new BigDecimal("3000.00"), priceAtLoad(1.5));
    }

    @Test
    void veryHighLoad_priceIsDoubled() {
        assertEquals(new BigDecimal("4000.00"), priceAtLoad(5.0));
    }

    @Test
    void boundaries_belongToTheUpperRange() {
        assertEquals(new BigDecimal("2500.00"), priceAtLoad(0.5));
        assertEquals(new BigDecimal("3000.00"), priceAtLoad(1.0));
        assertEquals(new BigDecimal("4000.00"), priceAtLoad(2.0));
    }

    @Test
    void readsTheLoadAtTheMomentOfCalculation() {
        double[] load = {0.0};
        DemandPricing strategy = new DemandPricing(() -> load[0]);
        assertEquals(new BigDecimal("2000.00"), strategy.price(request));
        load[0] = 3.0;
        assertEquals(new BigDecimal("4000.00"), strategy.price(request));
    }
}
