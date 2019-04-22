package no.oslomet.aaas.model.risk;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReIdentificationRisk {

    private final Map<String, String> measures;

    @JsonCreator
    public ReIdentificationRisk(Map<String, String> measures) {
        this.measures  = new HashMap<>(measures);
    }

    public Map<String, String> getMeasures() {
        return measures;
    }

    @Override
    public String toString() {
        return "ReIdentificationRisk{" +
                "measures=" + measures +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReIdentificationRisk that = (ReIdentificationRisk) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(measures);
    }
}