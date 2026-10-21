package telemedicine.pricing;

import telemedicine.demand.DemandProvider;
import telemedicine.model.ConsultationRequest;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;


public class PriceCalculator {
    private final Map<PricingType, PricingStrategy> strategies = new EnumMap<>(PricingType.class);

    public PriceCalculator(DemandProvider demand) {
        strategies.put(PricingType.STANDARD, new StandardPricing());
        strategies.put(PricingType.INSURANCE, new InsurancePricing());
        strategies.put(PricingType.DEMAND, new DemandPricing(demand));
    }

    public BigDecimal calculate(PricingType type, ConsultationRequest request) {
        PricingStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown pricing type: " + type);
        }
        return new PricingContext(strategy).calculate(request);
    }
}
