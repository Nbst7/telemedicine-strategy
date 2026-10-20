package telemedicine.demo;

import telemedicine.demand.DemandProvider;
import telemedicine.demand.SimulatedDemandProvider;
import telemedicine.matching.DoctorMatchingStrategy;
import telemedicine.matching.HighestRatedMatching;
import telemedicine.matching.LanguageFirstMatching;
import telemedicine.matching.LongestIdleMatching;
import telemedicine.matching.MatchingContext;
import telemedicine.matching.MatchingStrategySelector;
import telemedicine.model.ConsultationRequest;
import telemedicine.model.Doctor;
import telemedicine.model.Insurance;
import telemedicine.model.Patient;
import telemedicine.model.Specialty;
import telemedicine.pricing.DemandPricing;
import telemedicine.pricing.InsurancePricing;
import telemedicine.pricing.PricingContext;
import telemedicine.pricing.PricingStrategy;
import telemedicine.pricing.PricingStrategySelector;
import telemedicine.pricing.StandardPricing;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

/** Console demo of both Strategy families. Deterministic: the clocks are fixed. */
public class Main {

    private static final Instant NOW = Instant.parse("2026-10-19T12:00:00Z");

    public static void main(String[] args) {
        DemandProvider morning = new SimulatedDemandProvider(clockAt("09:00"));
        DemandProvider evening = new SimulatedDemandProvider(clockAt("19:00"));

        Patient aigerim = new Patient("p-1", "Aigerim", "ru", null);
        Patient daniyar = new Patient("p-2", "Daniyar", "kk",
                new Insurance("Freedom Health", 15, new BigDecimal("500")));

        pricingDemo(morning, evening, aigerim, daniyar);
        matchingDemo(aigerim, daniyar);
    }

    // ------------------------------------------------------------------ pricing family

    private static void pricingDemo(DemandProvider morning, DemandProvider evening,
                                    Patient aigerim, Patient daniyar) {
        ConsultationRequest request = new ConsultationRequest(daniyar, Specialty.CARDIOLOGY, 20, false);

        title("1. FAMILY 1 (pricing): one request, every strategy chosen by hand");
        System.out.println("Request: CARDIOLOGY, 20 min, patient Daniyar (insured: 15 min covered, copay 500)");
        PricingContext context = new PricingContext(new StandardPricing());
        printPrice(context, request);
        context.setStrategy(new InsurancePricing());
        printPrice(context, request);
        context.setStrategy(new DemandPricing(morning));
        printPrice(context, request);
        context.setStrategy(new DemandPricing(evening));
        System.out.print("(evening load) ");
        printPrice(context, request);

        title("2. Pricing: strategy chosen automatically by the Selector");
        PricingStrategySelector morningSelector = new PricingStrategySelector(morning);
        PricingStrategySelector eveningSelector = new PricingStrategySelector(evening);

        ConsultationRequest plain = new ConsultationRequest(aigerim, Specialty.GENERAL, 20, false);
        ConsultationRequest urgent = new ConsultationRequest(aigerim, Specialty.GENERAL, 20, true);

        printSelected("Aigerim, plain,   09:00", morningSelector, plain);
        printSelected("Aigerim, plain,   19:00", eveningSelector, plain);
        printSelected("Aigerim, urgent,  09:00", morningSelector, urgent);
        printSelected("Daniyar, insured, 19:00", eveningSelector, request);

        title("3. Pricing: switching the strategy of ONE context at runtime");
        PricingContext live = new PricingContext(morningSelector.select(plain));
        printPrice(live, plain);
        live.setStrategy(eveningSelector.select(plain));
        System.out.print("(evening rush, same context object) ");
        printPrice(live, plain);
    }

    private static void printPrice(PricingContext context, ConsultationRequest request) {
        System.out.printf("  %-18s -> %10s KZT%n", context.strategy().name(), context.calculate(request));
    }

    private static void printSelected(String label, PricingStrategySelector selector, ConsultationRequest request) {
        PricingStrategy chosen = selector.select(request);
        System.out.printf("  %-24s -> %-17s %10s KZT%n", label, chosen.name(),
                new PricingContext(chosen).calculate(request));
    }

    // ------------------------------------------------------------------ matching family

    private static void matchingDemo(Patient aigerim, Patient daniyar) {
        List<Doctor> doctors = List.of(
                doctor("Dr. Ivanova", 4.9, Set.of("ru"), 10, true),
                doctor("Dr. Serikov", 4.8, Set.of("kk", "ru"), 45, true),
                doctor("Dr. Bekova", 4.6, Set.of("kk", "ru", "en"), 120, true),
                doctor("Dr. Petrov", 5.0, Set.of("ru"), 300, false));

        title("4. FAMILY 2 (matching): same doctors, every strategy chosen by hand");
        System.out.println("Doctors (GENERAL): Ivanova 4.9 idle 10 min | Serikov 4.8 idle 45 min (kk) | "
                + "Bekova 4.6 idle 120 min (kk, en) | Petrov 5.0 unavailable");
        ConsultationRequest kazakhSpeaker = new ConsultationRequest(daniyar, Specialty.GENERAL, 15, false);
        MatchingContext context = new MatchingContext(new HighestRatedMatching());
        printMatch(context, doctors, kazakhSpeaker);
        context.setStrategy(new LongestIdleMatching());
        printMatch(context, doctors, kazakhSpeaker);
        context.setStrategy(new LanguageFirstMatching());
        printMatch(context, doctors, kazakhSpeaker);

        title("5. Matching: strategy chosen automatically by the Selector");
        MatchingStrategySelector selector = new MatchingStrategySelector();
        printSelectedMatch("Aigerim (ru), plain ", selector, doctors,
                new ConsultationRequest(aigerim, Specialty.GENERAL, 15, false));
        printSelectedMatch("Daniyar (kk), plain ", selector, doctors, kazakhSpeaker);
        printSelectedMatch("Aigerim (ru), urgent", selector, doctors,
                new ConsultationRequest(aigerim, Specialty.GENERAL, 15, true));
        printSelectedMatch("No pediatrician     ", selector, doctors,
                new ConsultationRequest(aigerim, Specialty.PEDIATRICS, 15, false));

        title("6. Matching: switching the strategy of ONE context at runtime");
        ConsultationRequest request = new ConsultationRequest(aigerim, Specialty.GENERAL, 15, false);
        MatchingContext live = new MatchingContext(selector.select(request));
        printMatch(live, doctors, request);
        ConsultationRequest nowUrgent = new ConsultationRequest(aigerim, Specialty.GENERAL, 15, true);
        live.setStrategy(selector.select(nowUrgent));
        System.out.print("(patient became urgent, same context object) ");
        printMatch(live, doctors, nowUrgent);
    }

    private static void printMatch(MatchingContext context, List<Doctor> doctors, ConsultationRequest request) {
        System.out.printf("  %-22s -> %s%n", context.strategy().name(),
                context.match(doctors, request).map(Doctor::name).orElse("(nobody available)"));
    }

    private static void printSelectedMatch(String label, MatchingStrategySelector selector,
                                           List<Doctor> doctors, ConsultationRequest request) {
        DoctorMatchingStrategy chosen = selector.select(request);
        System.out.printf("  %-21s -> %-22s %s%n", label, chosen.name(),
                chosen.choose(doctors, request).map(Doctor::name).orElse("(nobody available)"));
    }

    // ------------------------------------------------------------------ helpers

    private static Doctor doctor(String name, double rating, Set<String> languages,
                                 int idleMinutes, boolean available) {
        return new Doctor(name, name, Specialty.GENERAL, rating, languages,
                NOW.minusSeconds(idleMinutes * 60L), available);
    }

    private static Clock clockAt(String time) {
        return Clock.fixed(Instant.parse("2026-10-19T" + time + ":00Z"), ZoneOffset.UTC);
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
