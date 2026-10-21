package telemedicine.pricing;

import org.junit.jupiter.api.Test;
import telemedicine.model.Specialty;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static telemedicine.testsupport.TestData.plainRequest;

class StandardPricingTest {
    private final PricingStrategy strategy = new StandardPricing();

    @Test
    void multipliesRateByDuration() {
        assertEquals(new BigDecimal("2000.00"), strategy.price(plainRequest(Specialty.GENERAL, 20)));
    }

    @Test
    void usesRateOfTheRequestedSpecialty() {
        assertEquals(new BigDecimal("4500.00"), strategy.price(plainRequest(Specialty.DERMATOLOGY, 25)));
    }
}
