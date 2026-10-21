package telemedicine.matching;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorMatchingStrategy {
    Optional<Doctor> choose(List<Doctor> doctors, ConsultationRequest request);

    default String name() {
        return getClass().getSimpleName();
    }
}
