package telemedicine.pricing;

import telemedicine.model.ConsultationRequest;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal price(ConsultationRequest request);

    default String name() {
        return getClass().getSimpleName();
    }
}
