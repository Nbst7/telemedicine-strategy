package telemedicine.matching;

import org.junit.jupiter.api.Test;
import telemedicine.model.Doctor;
import telemedicine.model.Specialty;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static telemedicine.testsupport.TestData.doctor;
import static telemedicine.testsupport.TestData.request;
import static telemedicine.testsupport.TestData.uninsured;

class LanguageFirstMatchingTest {

    private final DoctorMatchingStrategy strategy = new LanguageFirstMatching();

    @Test
    void prefersDoctorWhoSpeaksThePatientLanguageEvenWithLowerRating() {
        List<Doctor> doctors = List.of(
                doctor("ru-star", Specialty.GENERAL, 4.9, Set.of("ru"), 10, true),
                doctor("kk-ok", Specialty.GENERAL, 4.0, Set.of("kk", "ru"), 10, true));
        var r = request(uninsured("kk"), Specialty.GENERAL, 15, false);
        assertEquals("kk-ok", strategy.choose(doctors, r).orElseThrow().id());
    }

    @Test
    void amongSameLanguageDoctors_bestRatingWins() {
        List<Doctor> doctors = List.of(
                doctor("kk-1", Specialty.GENERAL, 4.1, Set.of("kk"), 10, true),
                doctor("kk-2", Specialty.GENERAL, 4.7, Set.of("kk"), 10, true));
        var r = request(uninsured("kk"), Specialty.GENERAL, 15, false);
        assertEquals("kk-2", strategy.choose(doctors, r).orElseThrow().id());
    }

    @Test
    void fallsBackToBestRatedWhenNobodySpeaksTheLanguage() {
        List<Doctor> doctors = List.of(
                doctor("a", Specialty.GENERAL, 4.1, Set.of("ru"), 10, true),
                doctor("b", Specialty.GENERAL, 4.6, Set.of("ru"), 10, true));
        var r = request(uninsured("en"), Specialty.GENERAL, 15, false);
        assertEquals("b", strategy.choose(doctors, r).orElseThrow().id());
    }
}
