package telemedicine.pricing;

import telemedicine.model.ConsultationRequest;

import java.math.BigDecimal;

public class StandardPricing implements PricingStrategy {
    @Override
    public BigDecimal price(ConsultationRequest request) {
        BigDecimal rate = RateCard.perMinute(request.specialty());
        return Money.round(rate.multiply(BigDecimal.valueOf(request.durationMinutes())));
    }
}
