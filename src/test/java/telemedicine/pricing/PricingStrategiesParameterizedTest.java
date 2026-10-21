package telemedicine.pricing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import telemedicine.model.ConsultationRequest;
import telemedicine.model.Specialty;
import telemedicine.testsupport.FakeDemandProvider;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static telemedicine.testsupport.TestData.insured;
import static telemedicine.testsupport.TestData.request;

class PricingStrategiesParameterizedTest {
    static Stream<PricingStrategy> allStrategies() {
        return Stream.of(
                new StandardPricing(),
                new InsurancePricing(),
                new DemandPricing(new FakeDemandProvider(1.5)));
    }

    private static ConsultationRequest requestOf(Specialty specialty, int minutes) {
        return request(insured("ru", 10, "200"), specialty, minutes, false);
    }

    @ParameterizedTest(name = "{0}: price is never negative")
    @MethodSource("allStrategies")
    void priceIsNeverNegative(PricingStrategy strategy) {
        for (Specialty specialty : Specialty.values()) {
            for (int minutes : new int[]{1, 10, 60}) {
                assertTrue(strategy.price(requestOf(specialty, minutes)).signum() >= 0);
            }
        }
    }

    @ParameterizedTest(name = "{0}: price has two decimals")
    @MethodSource("allStrategies")
    void priceHasScaleTwo(PricingStrategy strategy) {
        assertEquals(2, strategy.price(requestOf(Specialty.PEDIATRICS, 7)).scale());
    }

    @ParameterizedTest(name = "{0}: longer consultation never costs less")
    @MethodSource("allStrategies")
    void longerConsultationNeverCostsLess(PricingStrategy strategy) {
        BigDecimal previous = BigDecimal.ZERO;
        for (int minutes = 1; minutes <= 60; minutes++) {
            BigDecimal current = strategy.price(requestOf(Specialty.CARDIOLOGY, minutes));
            assertTrue(current.compareTo(previous) >= 0, "price dropped at " + minutes + " minutes");
            previous = current;
        }
    }
}
