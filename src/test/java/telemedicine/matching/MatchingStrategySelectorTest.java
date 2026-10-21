package telemedicine.matching;

import org.junit.jupiter.api.Test;
import telemedicine.model.Specialty;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static telemedicine.testsupport.TestData.request;
import static telemedicine.testsupport.TestData.uninsured;

class MatchingStrategySelectorTest {
    private final MatchingStrategySelector selector = new MatchingStrategySelector();

    @Test
    void rule1_urgentRequest_longestIdle() {
        var r = request(uninsured("ru"), Specialty.GENERAL, 15, true);
        assertInstanceOf(LongestIdleMatching.class, selector.select(r));
    }

    @Test
    void rule1_urgencyWinsOverLanguage() {
        var r = request(uninsured("kk"), Specialty.GENERAL, 15, true);
        assertInstanceOf(LongestIdleMatching.class, selector.select(r));
    }

    @Test
    void rule2_nonDefaultLanguage_languageFirst() {
        var r = request(uninsured("kk"), Specialty.GENERAL, 15, false);
        assertInstanceOf(LanguageFirstMatching.class, selector.select(r));
    }

    @Test
    void rule3_otherwise_highestRated() {
        var r = request(uninsured(MatchingStrategySelector.DEFAULT_LANGUAGE), Specialty.GENERAL, 15, false);
        assertInstanceOf(HighestRatedMatching.class, selector.select(r));
    }
}
