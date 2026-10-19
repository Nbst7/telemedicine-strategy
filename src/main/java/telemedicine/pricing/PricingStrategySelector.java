package telemedicine.pricing;

import telemedicine.demand.DemandProvider;
import telemedicine.model.ConsultationRequest;

/** Chooses a pricing strategy from the insurance, the urgency and the current load. */
public class PricingStrategySelector {

    public static final double HIGH_DEMAND_THRESHOLD = 1.0;

    private final DemandProvider demand;
    private final PricingStrategy standard = new StandardPricing();
    private final PricingStrategy insurance = new InsurancePricing();
    private final PricingStrategy demandBased;

    public PricingStrategySelector(DemandProvider demand) {
        this.demand = demand;
        this.demandBased = new DemandPricing(demand);
    }

    public PricingStrategy select(ConsultationRequest request) {
        if (!request.patient().insurance().isNone()) {
            return insurance;
        }
        if (request.urgent() || demand.loadFactor() >= HIGH_DEMAND_THRESHOLD) {
            return demandBased;
        }
        return standard;
    }
}
