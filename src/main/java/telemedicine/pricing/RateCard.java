package telemedicine.pricing;

import telemedicine.model.Specialty;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/** Base price per minute of a consultation, in tenge. */
public final class RateCard {

    private static final Map<Specialty, BigDecimal> PER_MINUTE = new EnumMap<>(Specialty.class);

    static {
        PER_MINUTE.put(Specialty.GENERAL, BigDecimal.valueOf(100));
        PER_MINUTE.put(Specialty.CARDIOLOGY, BigDecimal.valueOf(250));
        PER_MINUTE.put(Specialty.DERMATOLOGY, BigDecimal.valueOf(180));
        PER_MINUTE.put(Specialty.PEDIATRICS, BigDecimal.valueOf(150));
    }

    private RateCard() {
    }

    public static BigDecimal perMinute(Specialty specialty) {
        return PER_MINUTE.get(specialty);
    }
}
