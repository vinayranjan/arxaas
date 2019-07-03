package no.oslomet.aaas.utils;

import no.oslomet.aaas.model.anonymity.PrivacyCriterionModel;
import org.deidentifier.arx.ARXConfiguration;

import java.util.List;

/**
 * Public contract to be fulfilled by data anonymization classes
 */
public interface ConfigurationFactory {

    /***
     * Returns an ARX {@link ARXConfiguration} object created from the provided payload
     * @param privacyModels Model object containing the parameters to use in anonymization
     * @param suppressionLimit a double containing the suppression limit value to be used in anonymization
     * @return ARX {@link ARXConfiguration} object containing the anonymization algorithm settings.
     */
    ARXConfiguration create(List<PrivacyCriterionModel> privacyModels, Double suppressionLimit);
}