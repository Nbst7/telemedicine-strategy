package telemedicine.pricing;

import org.junit.jupiter.api.Test;
import telemedicine.model.ConsultationRequest;
import telemedicine.model.Specialty;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static telemedicine.testsupport.TestData.insured;
import static telemedicine.testsupport.TestData.request;
import static telemedicine.testsupport.TestData.uninsured;

class InsurancePricingTest {
    private final PricingStrategy strategy = new InsurancePricing();

    @Test
    void fullyCovered_patientPaysOnlyCopay() {
        ConsultationRequest r = request(insured("ru", 30, "500"), Specialty.GENERAL, 20, false);
        assertEquals(new BigDecimal("500.00"), strategy.price(r));
    }

    @Test
    void uncoveredMinutes_arePaidAtFullRate() {
        ConsultationRequest r = request(insured("ru", 15, "500"), Specialty.CARDIOLOGY, 20, false);
        assertEquals(new BigDecimal("1750.00"), strategy.price(r)); // 500 + 5 * 250
    }

    @Test
    void noInsurance_meansFullPrice() {
        ConsultationRequest r = request(uninsured("ru"), Specialty.GENERAL, 20, false);
        assertEquals(new BigDecimal("2000.00"), strategy.price(r));
    }
}
