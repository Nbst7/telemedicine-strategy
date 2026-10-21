package telemedicine.pricing;

import org.junit.jupiter.api.Test;
import telemedicine.model.Specialty;
import telemedicine.testsupport.FakeDemandProvider;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static telemedicine.testsupport.TestData.insured;
import static telemedicine.testsupport.TestData.request;
import static telemedicine.testsupport.TestData.uninsured;

class PricingStrategySelectorTest {
    private static PricingStrategySelector selectorWithLoad(double load) {
        return new PricingStrategySelector(new FakeDemandProvider(load));
    }

    @Test
    void rule1_insuredPatient_getsInsurancePricing() {
        var r = request(insured("ru", 20, "300"), Specialty.GENERAL, 15, false);
        assertInstanceOf(InsurancePricing.class, selectorWithLoad(0.1).select(r));
    }

    @Test
    void rule1_insuranceWinsOverUrgencyAndHighDemand() {
        var r = request(insured("ru", 20, "300"), Specialty.GENERAL, 15, true);
        assertInstanceOf(InsurancePricing.class, selectorWithLoad(3.0).select(r));
    }

    @Test
    void rule2_urgentRequest_getsDemandPricingEvenWhenLoadIsLow() {
        var r = request(uninsured("ru"), Specialty.GENERAL, 15, true);
        assertInstanceOf(DemandPricing.class, selectorWithLoad(0.1).select(r));
    }

    @Test
    void rule2_highDemand_getsDemandPricing() {
        var r = request(uninsured("ru"), Specialty.GENERAL, 15, false);
        assertInstanceOf(DemandPricing.class, selectorWithLoad(PricingStrategySelector.HIGH_DEMAND_THRESHOLD).select(r));
    }

    @Test
    void rule3_otherwise_standardPricing() {
        var r = request(uninsured("ru"), Specialty.GENERAL, 15, false);
        assertInstanceOf(StandardPricing.class, selectorWithLoad(0.99).select(r));
    }
}
