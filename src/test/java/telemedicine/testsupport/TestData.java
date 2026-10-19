package telemedicine.testsupport;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;
import telemedicine.model.Insurance;
import telemedicine.model.Patient;
import telemedicine.model.Specialty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public final class TestData {

    public static final Instant NOON = Instant.parse("2026-10-19T12:00:00Z");

    private TestData() {
    }

    public static Patient uninsured(String language) {
        return new Patient("p-1", "Aigerim", language, null);
    }

    public static Patient insured(String language, int coveredMinutes, String copay) {
        return new Patient("p-2", "Daniyar", language,
                new Insurance("Freedom Health", coveredMinutes, new BigDecimal(copay)));
    }

    public static ConsultationRequest request(Patient patient, Specialty specialty, int minutes, boolean urgent) {
        return new ConsultationRequest(patient, specialty, minutes, urgent);
    }

    public static ConsultationRequest plainRequest(Specialty specialty, int minutes) {
        return request(uninsured("ru"), specialty, minutes, false);
    }

    public static Doctor doctor(String id, Specialty specialty, double rating,
                                Set<String> languages, int idleMinutesBeforeNoon, boolean available) {
        return new Doctor(id, "Dr. " + id, specialty, rating, languages,
                NOON.minusSeconds(idleMinutesBeforeNoon * 60L), available);
    }
}
