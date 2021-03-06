package no.nav.arxaas.model.anonymity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.arxaas.model.risk.RiskProfile;


/***
 * Model class for the response object from anonymisation process.
 */
public class AnonymizationResultPayload {

    private final AnonymizeResult anonymizeResult;
    private final RiskProfile riskProfile;

    /***
     * Setter method for the response object from the anonymisation and analysation process.
     * @param anonymizeResult model {@link AnonymizeResult} containing the anonymized dataset and the metadata
     *                        used for the arxaas
     * @param riskProfile contains the analysis data from the anonymized dataset.
     */
    @JsonCreator
    public AnonymizationResultPayload(@JsonProperty("anonymizeResult") AnonymizeResult anonymizeResult,
                                      @JsonProperty("riskProfile") RiskProfile riskProfile) {
        this.anonymizeResult = anonymizeResult;
        this.riskProfile = riskProfile;

    }

    /***
     * Getter method for the {@link AnonymizeResult} model class containing the anonymized dataset and the metadata used
     * for the arxaas.
     * @return Object of {@link AnonymizeResult} containing the dataset and metadata after the arxaas process
     */
    public AnonymizeResult getAnonymizeResult() {
        return anonymizeResult;
    }



    /***
     * Getter method for the analysis of the anonymized dataset.
     * @return RiskProfile containing the analysis data of the anonymized dataset
     */
    public RiskProfile getRiskProfile() {
        return riskProfile;
    }



}
