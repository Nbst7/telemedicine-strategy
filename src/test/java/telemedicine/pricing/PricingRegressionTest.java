package telemedicine.pricing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import telemedicine.model.ConsultationRequest;
import telemedicine.model.Specialty;
import telemedicine.testsupport.FakeDemandProvider;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static telemedicine.testsupport.TestData.insured;
import static telemedicine.testsupport.TestData.plainRequest;
import static telemedicine.testsupport.TestData.request;
import static telemedicine.testsupport.TestData.uninsured;

class PricingRegressionTest {
    private static void assertPrice(String expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }

    private static PriceCalculator calculatorWithLoad(double load) {
        return new PriceCalculator(new FakeDemandProvider(load));
    }

    @Test
    void standard_isRatePerMinuteTimesDuration() {
        PriceCalculator calc = calculatorWithLoad(0.0);
        assertPrice("2000.00", calc.calculate(PricingType.STANDARD, plainRequest(Specialty.GENERAL, 20)));
        assertPrice("7500.00", calc.calculate(PricingType.STANDARD, plainRequest(Specialty.CARDIOLOGY, 30)));
    }

    @Test
    void insurance_partlyCovered_copayPlusUncoveredMinutes() {
        ConsultationRequest r = request(insured("ru", 15, "500"), Specialty.GENERAL, 20, false);
        assertPrice("1000.00", calculatorWithLoad(0.0).calculate(PricingType.INSURANCE, r));
    }

    @Test
    void insurance_fullyCovered_onlyCopay() {
        ConsultationRequest r = request(insured("ru", 30, "500"), Specialty.GENERAL, 20, false);
        assertPrice("500.00", calculatorWithLoad(0.0).calculate(PricingType.INSURANCE, r));
    }

    @Test
    void insurance_withoutPolicy_payFullPrice() {
        ConsultationRequest r = request(uninsured("ru"), Specialty.GENERAL, 20, false);
        assertPrice("2000.00", calculatorWithLoad(0.0).calculate(PricingType.INSURANCE, r));
    }

    @Test
    void demand_multiplierGrowsWithLoad() {
        ConsultationRequest r = plainRequest(Specialty.GENERAL, 20);
        assertPrice("2000.00", calculatorWithLoad(0.3).calculate(PricingType.DEMAND, r));
        assertPrice("2500.00", calculatorWithLoad(0.5).calculate(PricingType.DEMAND, r));
        assertPrice("3000.00", calculatorWithLoad(1.0).calculate(PricingType.DEMAND, r));
        assertPrice("3000.00", calculatorWithLoad(1.99).calculate(PricingType.DEMAND, r));
        assertPrice("4000.00", calculatorWithLoad(2.0).calculate(PricingType.DEMAND, r));
        assertPrice("4000.00", calculatorWithLoad(9.0).calculate(PricingType.DEMAND, r));
    }

    @Test
    void demand_cardiology_loadOnePointFive() {
        ConsultationRequest r = plainRequest(Specialty.CARDIOLOGY, 10);
        assertPrice("3750.00", calculatorWithLoad(1.5).calculate(PricingType.DEMAND, r));
    }

    @Test
    void demand_negativeLoadIsTreatedAsZero() {
        ConsultationRequest r = plainRequest(Specialty.GENERAL, 20);
        assertPrice("2000.00", calculatorWithLoad(-3.0).calculate(PricingType.DEMAND, r));
    }

    @ParameterizedTest
    @EnumSource(PricingType.class)
    void everyPricingType_neverReturnsNegativePrice(PricingType type) {
        ConsultationRequest r = request(insured("ru", 60, "0"), Specialty.PEDIATRICS, 5, true);
        assertTrue(calculatorWithLoad(1.5).calculate(type, r).signum() >= 0);
    }
}
