package telemedicine.model;

import java.math.BigDecimal;

public record Insurance(String provider, int coveredMinutes, BigDecimal copay) {
    public static final Insurance NONE = new Insurance("NONE", 0, BigDecimal.ZERO);

    public Insurance {
        if (coveredMinutes < 0) {
            throw new IllegalArgumentException("coveredMinutes must not be negative");
        }
        if (copay.signum() < 0) {
            throw new IllegalArgumentException("copay must not be negative");
        }
    }

    public boolean isNone() {
        return this.equals(NONE);
    }
}
