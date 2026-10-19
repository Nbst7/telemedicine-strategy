package telemedicine.pricing;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Insurance;

import java.math.BigDecimal;

/** Fixed copay plus the full rate for every minute the policy does not cover. */
public class InsurancePricing implements PricingStrategy {

    @Override
    public BigDecimal price(ConsultationRequest request) {
        Insurance insurance = request.patient().insurance();
        int uncoveredMinutes = Math.max(0, request.durationMinutes() - insurance.coveredMinutes());
        BigDecimal uncoveredPart = RateCard.perMinute(request.specialty())
                .multiply(BigDecimal.valueOf(uncoveredMinutes));
        return Money.round(insurance.copay().add(uncoveredPart));
    }
}
