package telemedicine.model;

public record Patient(String id, String name, String language, Insurance insurance) {

    public Patient {
        insurance = (insurance == null) ? Insurance.NONE : insurance;
    }
}
