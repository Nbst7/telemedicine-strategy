package telemedicine.matching;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Picks the candidate who has waited for a patient the longest. */
public class LongestIdleMatching implements DoctorMatchingStrategy {

    @Override
    public Optional<Doctor> choose(List<Doctor> doctors, ConsultationRequest request) {
        return CandidateFilter.candidates(doctors, request).stream()
                .min(Comparator.comparing(Doctor::idleSince));
    }
}
