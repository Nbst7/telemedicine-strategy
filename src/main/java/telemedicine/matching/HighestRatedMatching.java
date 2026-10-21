package telemedicine.matching;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HighestRatedMatching implements DoctorMatchingStrategy {
    static final Comparator<Doctor> BY_RATING = Comparator.comparingDouble(Doctor::rating);

    @Override
    public Optional<Doctor> choose(List<Doctor> doctors, ConsultationRequest request) {
        return CandidateFilter.candidates(doctors, request).stream().max(BY_RATING);
    }
}
