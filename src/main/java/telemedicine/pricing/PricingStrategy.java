package telemedicine.pricing;

import telemedicine.model.ConsultationRequest;

import java.math.BigDecimal;

/** One way to calculate the price of a consultation. */
public interface PricingStrategy {

    /** Price in tenge, never negative, scale 2. */
    BigDecimal price(ConsultationRequest request);

    default String name() {
        return getClass().getSimpleName();
    }
}
