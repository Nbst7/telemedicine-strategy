package telemedicine.matching;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;

import java.util.List;
import java.util.Optional;

/** Prefers candidates who speak the patient's language, then takes the best rated one. */
public class LanguageFirstMatching implements DoctorMatchingStrategy {

    @Override
    public Optional<Doctor> choose(List<Doctor> doctors, ConsultationRequest request) {
        List<Doctor> candidates = CandidateFilter.candidates(doctors, request);
        List<Doctor> sameLanguage = candidates.stream()
                .filter(d -> d.speaks(request.patient().language()))
                .toList();
        List<Doctor> pool = sameLanguage.isEmpty() ? candidates : sameLanguage;
        return pool.stream().max(HighestRatedMatching.BY_RATING);
    }
}
