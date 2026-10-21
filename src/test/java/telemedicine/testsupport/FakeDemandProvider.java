package telemedicine.testsupport;

import telemedicine.demand.DemandProvider;

public class FakeDemandProvider implements DemandProvider {
    private final double load;

    public FakeDemandProvider(double load) {
        this.load = load;
    }

    @Override
    public double loadFactor() {
        return load;
    }
}
