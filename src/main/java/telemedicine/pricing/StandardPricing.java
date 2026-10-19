package telemedicine.pricing;

import telemedicine.model.ConsultationRequest;

import java.math.BigDecimal;

/** Rate of the specialty times the duration. */
public class StandardPricing implements PricingStrategy {

    @Override
    public BigDecimal price(ConsultationRequest request) {
        BigDecimal rate = RateCard.perMinute(request.specialty());
        return Money.round(rate.multiply(BigDecimal.valueOf(request.durationMinutes())));
    }
}
