package telemedicine.matching;

import telemedicine.model.ConsultationRequest;

public class MatchingStrategySelector {
    public static final String DEFAULT_LANGUAGE = "ru";

    private final DoctorMatchingStrategy highestRated = new HighestRatedMatching();
    private final DoctorMatchingStrategy longestIdle = new LongestIdleMatching();
    private final DoctorMatchingStrategy languageFirst = new LanguageFirstMatching();

    public DoctorMatchingStrategy select(ConsultationRequest request) {
        if (request.urgent()) {
            return longestIdle;
        }
        if (!DEFAULT_LANGUAGE.equals(request.patient().language())) {
            return languageFirst;
        }
        return highestRated;
    }
}
