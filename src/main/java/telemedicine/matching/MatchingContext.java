package telemedicine.matching;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MatchingContext {
    private DoctorMatchingStrategy strategy;

    public MatchingContext(DoctorMatchingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public void setStrategy(DoctorMatchingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public DoctorMatchingStrategy strategy() {
        return strategy;
    }

    public Optional<Doctor> match(List<Doctor> doctors, ConsultationRequest request) {
        return strategy.choose(doctors, request);
    }
}
