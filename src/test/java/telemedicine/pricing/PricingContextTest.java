package telemedicine.pricing;

import org.junit.jupiter.api.Test;
import telemedicine.model.ConsultationRequest;
import telemedicine.model.Specialty;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static telemedicine.testsupport.TestData.plainRequest;

class PricingContextTest {
    private final ConsultationRequest request = plainRequest(Specialty.GENERAL, 10);

    @Test
    void delegatesToTheStrategyFromTheConstructor() {
        PricingContext context = new PricingContext(r -> new BigDecimal("42.00"));
        assertEquals(new BigDecimal("42.00"), context.calculate(request));
    }

    @Test
    void setStrategy_replacesTheAlgorithmAtRuntime() {
        PricingContext context = new PricingContext(r -> new BigDecimal("1.00"));
        context.setStrategy(r -> new BigDecimal("2.00"));
        assertEquals(new BigDecimal("2.00"), context.calculate(request));
    }

    @Test
    void exposesTheCurrentStrategy() {
        PricingStrategy standard = new StandardPricing();
        assertSame(standard, new PricingContext(standard).strategy());
    }

    @Test
    void rejectsNullStrategy() {
        assertThrows(NullPointerException.class, () -> new PricingContext(null));
        PricingContext context = new PricingContext(new StandardPricing());
        assertThrows(NullPointerException.class, () -> context.setStrategy(null));
    }
}
