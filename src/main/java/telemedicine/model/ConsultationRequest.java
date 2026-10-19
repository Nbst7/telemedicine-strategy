package telemedicine.model;

import java.util.Objects;

public record ConsultationRequest(Patient patient,
                                  Specialty specialty,
                                  int durationMinutes,
                                  boolean urgent) {

    public ConsultationRequest {
        Objects.requireNonNull(patient, "patient");
        Objects.requireNonNull(specialty, "specialty");
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("durationMinutes must be positive");
        }
    }
}
