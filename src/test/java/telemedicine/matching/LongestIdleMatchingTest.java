package telemedicine.matching;

import org.junit.jupiter.api.Test;
import telemedicine.model.Doctor;
import telemedicine.model.Specialty;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static telemedicine.testsupport.TestData.doctor;
import static telemedicine.testsupport.TestData.plainRequest;

class LongestIdleMatchingTest {

    private final DoctorMatchingStrategy strategy = new LongestIdleMatching();

    @Test
    void picksTheDoctorWhoWaitedTheLongest() {
        List<Doctor> doctors = List.of(
                doctor("a", Specialty.GENERAL, 5.0, Set.of("ru"), 5, true),
                doctor("b", Specialty.GENERAL, 3.0, Set.of("ru"), 90, true),
                doctor("c", Specialty.GENERAL, 4.0, Set.of("ru"), 30, true));
        assertEquals("b", strategy.choose(doctors, plainRequest(Specialty.GENERAL, 15)).orElseThrow().id());
    }

    @Test
    void ignoresUnavailableDoctors() {
        List<Doctor> doctors = List.of(
                doctor("away", Specialty.GENERAL, 5.0, Set.of("ru"), 300, false),
                doctor("ready", Specialty.GENERAL, 4.0, Set.of("ru"), 10, true));
        assertEquals("ready", strategy.choose(doctors, plainRequest(Specialty.GENERAL, 15)).orElseThrow().id());
    }

    @Test
    void emptyWhenNoDoctorHasTheSpecialty() {
        List<Doctor> doctors = List.of(doctor("a", Specialty.PEDIATRICS, 5.0, Set.of("ru"), 10, true));
        assertTrue(strategy.choose(doctors, plainRequest(Specialty.GENERAL, 15)).isEmpty());
    }
}
