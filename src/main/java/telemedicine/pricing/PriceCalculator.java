package telemedicine.pricing;

import telemedicine.demand.DemandProvider;
import telemedicine.model.ConsultationRequest;
import telemedicine.model.Insurance;

import java.math.BigDecimal;

/**
 * BEFORE refactoring: all pricing variants live in one class and are chosen with a switch.
 * Adding a new variant means editing this class (violates the Open/Closed Principle).
 */
public class PriceCalculator {

    private final DemandProvider demand;

    public PriceCalculator(DemandProvider demand) {
        this.demand = demand;
    }

    public BigDecimal calculate(PricingType type, ConsultationRequest request) {
        BigDecimal rate = RateCard.perMinute(request.specialty());
        BigDecimal base = rate.multiply(BigDecimal.valueOf(request.durationMinutes()));

        switch (type) {
            case STANDARD:
                return Money.round(base);

            case INSURANCE: {
                Insurance insurance = request.patient().insurance();
                int uncovered = Math.max(0, request.durationMinutes() - insurance.coveredMinutes());
                BigDecimal extra = rate.multiply(BigDecimal.valueOf(uncovered));
                return Money.round(insurance.copay().add(extra));
            }

            case DEMAND: {
                double load = Math.max(0.0, demand.loadFactor());
                BigDecimal multiplier;
                if (load >= 2.0) {
                    multiplier = new BigDecimal("2.00");
                } else if (load >= 1.0) {
                    multiplier = new BigDecimal("1.50");
                } else if (load >= 0.5) {
                    multiplier = new BigDecimal("1.25");
                } else {
                    multiplier = new BigDecimal("1.00");
                }
                return Money.round(base.multiply(multiplier));
            }

            default:
                throw new IllegalArgumentException("Unknown pricing type: " + type);
        }
    }
}
