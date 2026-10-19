package telemedicine.testsupport;

import telemedicine.demand.DemandProvider;

/** Fake data source for tests: always returns the value given in the constructor. */
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
