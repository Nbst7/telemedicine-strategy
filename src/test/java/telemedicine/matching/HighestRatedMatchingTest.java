package telemedicine.matching;

import org.junit.jupiter.api.Test;
import telemedicine.model.Doctor;
import telemedicine.model.Specialty;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static telemedicine.testsupport.TestData.doctor;
import static telemedicine.testsupport.TestData.plainRequest;

class HighestRatedMatchingTest {

    private final DoctorMatchingStrategy strategy = new HighestRatedMatching();

    @Test
    void picksTheBestRatedAvailableDoctorOfTheSpecialty() {
        List<Doctor> doctors = List.of(
                doctor("a", Specialty.GENERAL, 4.2, Set.of("ru"), 10, true),
                doctor("b", Specialty.GENERAL, 4.9, Set.of("ru"), 10, true),
                doctor("c", Specialty.GENERAL, 4.5, Set.of("ru"), 10, true));
        assertEquals("b", strategy.choose(doctors, plainRequest(Specialty.GENERAL, 15)).orElseThrow().id());
    }

    @Test
    void ignoresBusyDoctorsAndOtherSpecialties() {
        List<Doctor> doctors = List.of(
                doctor("busy-star", Specialty.GENERAL, 5.0, Set.of("ru"), 10, false),
                doctor("cardio-star", Specialty.CARDIOLOGY, 5.0, Set.of("ru"), 10, true),
                doctor("ok", Specialty.GENERAL, 3.8, Set.of("ru"), 10, true));
        assertEquals("ok", strategy.choose(doctors, plainRequest(Specialty.GENERAL, 15)).orElseThrow().id());
    }

    @Test
    void emptyWhenNobodyMatches() {
        assertEquals(Optional.empty(), strategy.choose(List.of(), plainRequest(Specialty.GENERAL, 15)));
    }
}
