package telemedicine.pricing;

import telemedicine.model.ConsultationRequest;

import java.math.BigDecimal;
import java.util.Objects;

/** Context of the pricing family: holds one strategy and delegates the work to it. */
public class PricingContext {

    private PricingStrategy strategy;

    public PricingContext(PricingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public void setStrategy(PricingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public PricingStrategy strategy() {
        return strategy;
    }

    public BigDecimal calculate(ConsultationRequest request) {
        return strategy.price(request);
    }
}
