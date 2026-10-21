package telemedicine.matching;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import telemedicine.model.Doctor;
import telemedicine.model.Specialty;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static telemedicine.testsupport.TestData.doctor;
import static telemedicine.testsupport.TestData.request;
import static telemedicine.testsupport.TestData.uninsured;

class MatchingStrategiesParameterizedTest {
    static Stream<DoctorMatchingStrategy> allStrategies() {
        return Stream.of(new HighestRatedMatching(), new LongestIdleMatching(), new LanguageFirstMatching());
    }

    private final List<Doctor> doctors = List.of(
            doctor("g-busy", Specialty.GENERAL, 5.0, Set.of("kk"), 500, false),
            doctor("cardio", Specialty.CARDIOLOGY, 5.0, Set.of("kk"), 500, true),
            doctor("g-1", Specialty.GENERAL, 4.0, Set.of("ru"), 20, true),
            doctor("g-2", Specialty.GENERAL, 4.5, Set.of("kk"), 60, true));

    @ParameterizedTest(name = "{0}: never returns an unavailable doctor or a wrong specialty")
    @MethodSource("allStrategies")
    void chosenDoctorIsAvailableAndHasTheRequestedSpecialty(DoctorMatchingStrategy strategy) {
        var r = request(uninsured("kk"), Specialty.GENERAL, 15, false);
        Doctor chosen = strategy.choose(doctors, r).orElseThrow();
        assertTrue(chosen.available());
        assertTrue(chosen.specialty() == Specialty.GENERAL);
    }

    @ParameterizedTest(name = "{0}: returns empty when no doctor fits")
    @MethodSource("allStrategies")
    void emptyWhenNobodyFits(DoctorMatchingStrategy strategy) {
        var r = request(uninsured("ru"), Specialty.PEDIATRICS, 15, false);
        assertTrue(strategy.choose(doctors, r).isEmpty());
    }

    @ParameterizedTest(name = "{0}: does not change the input list")
    @MethodSource("allStrategies")
    void doesNotModifyTheInput(DoctorMatchingStrategy strategy) {
        List<Doctor> copy = List.copyOf(doctors);
        strategy.choose(doctors, request(uninsured("kk"), Specialty.GENERAL, 15, false));
        assertTrue(copy.equals(doctors));
    }
}
