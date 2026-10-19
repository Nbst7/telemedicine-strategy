package telemedicine.model;

import java.time.Instant;
import java.util.Set;

public record Doctor(String id,
                     String name,
                     Specialty specialty,
                     double rating,
                     Set<String> languages,
                     Instant idleSince,
                     boolean available) {

    public boolean speaks(String language) {
        return languages.contains(language);
    }
}
