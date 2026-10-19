package telemedicine.matching;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;

import java.util.List;

/** Keeps only available doctors of the requested specialty. */
final class CandidateFilter {

    private CandidateFilter() {
    }

    static List<Doctor> candidates(List<Doctor> doctors, ConsultationRequest request) {
        return doctors.stream()
                .filter(Doctor::available)
                .filter(d -> d.specialty() == request.specialty())
                .toList();
    }
}
