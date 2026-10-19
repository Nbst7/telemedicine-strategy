package telemedicine.matching;

import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;

import java.util.List;
import java.util.Optional;

/** One way to pick a doctor for a consultation request. */
public interface DoctorMatchingStrategy {

    /** Returns an available doctor of the requested specialty, or empty when there is none. */
    Optional<Doctor> choose(List<Doctor> doctors, ConsultationRequest request);

    default String name() {
        return getClass().getSimpleName();
    }
}
