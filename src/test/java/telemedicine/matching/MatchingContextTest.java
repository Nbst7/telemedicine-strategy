package telemedicine.matching;

import org.junit.jupiter.api.Test;
import telemedicine.model.Doctor;
import telemedicine.model.Specialty;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static telemedicine.testsupport.TestData.doctor;
import static telemedicine.testsupport.TestData.plainRequest;

class MatchingContextTest {
    private final List<Doctor> doctors = List.of(
            doctor("rated", Specialty.GENERAL, 4.9, Set.of("ru"), 5, true),
            doctor("idle", Specialty.GENERAL, 3.5, Set.of("ru"), 120, true));

    @Test
    void delegatesToTheStrategyFromTheConstructor() {
        MatchingContext context = new MatchingContext(new HighestRatedMatching());
        assertEquals("rated", context.match(doctors, plainRequest(Specialty.GENERAL, 15)).orElseThrow().id());
    }

    @Test
    void setStrategy_changesTheResultAtRuntime() {
        MatchingContext context = new MatchingContext(new HighestRatedMatching());
        context.setStrategy(new LongestIdleMatching());
        assertEquals("idle", context.match(doctors, plainRequest(Specialty.GENERAL, 15)).orElseThrow().id());
    }

    @Test
    void usesAnyImplementationOfTheInterface() {
        MatchingContext context = new MatchingContext((list, request) -> Optional.of(list.get(1)));
        assertEquals("idle", context.match(doctors, plainRequest(Specialty.GENERAL, 15)).orElseThrow().id());
    }

    @Test
    void rejectsNullStrategy() {
        assertThrows(NullPointerException.class, () -> new MatchingContext(null));
    }
}
