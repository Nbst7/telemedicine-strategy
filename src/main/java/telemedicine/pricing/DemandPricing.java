package telemedicine.pricing;

import telemedicine.demand.DemandProvider;
import telemedicine.model.ConsultationRequest;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class DemandPricing implements PricingStrategy {
    private static final TreeMap<Double, BigDecimal> MULTIPLIERS = new TreeMap<>(Map.of(
            0.0, new BigDecimal("1.00"),
            0.5, new BigDecimal("1.25"),
            1.0, new BigDecimal("1.50"),
            2.0, new BigDecimal("2.00")));

    private final DemandProvider demand;
    private final PricingStrategy basePrice = new StandardPricing();

    public DemandPricing(DemandProvider demand) {
        this.demand = demand;
    }

    @Override
    public BigDecimal price(ConsultationRequest request) {
        double load = Math.max(0.0, demand.loadFactor());
        BigDecimal multiplier = MULTIPLIERS.floorEntry(load).getValue();
        return Money.round(basePrice.price(request).multiply(multiplier));
    }
}
