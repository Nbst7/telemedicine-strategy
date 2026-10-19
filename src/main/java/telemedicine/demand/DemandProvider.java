package telemedicine.demand;

/**
 * Our own interface for the external "how busy is the platform" data.
 * Strategies depend on this interface, never on a concrete data source
 * (Clean Code ch. 10: isolating from change).
 */
public interface DemandProvider {

    /** Waiting patients per online doctor. 0.0 = nobody waits, 2.0 = two patients per doctor. */
    double loadFactor();
}
