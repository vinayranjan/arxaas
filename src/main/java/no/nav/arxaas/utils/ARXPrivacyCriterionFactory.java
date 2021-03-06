package no.nav.arxaas.utils;

import no.nav.arxaas.exception.AaaSRuntimeException;
import no.nav.arxaas.model.anonymity.PrivacyCriterionModel;
import org.deidentifier.arx.criteria.*;
import org.springframework.stereotype.Component;

import java.util.Map;

/***
 * Utility class for assigning a privacy model
 */
@Component
public class ARXPrivacyCriterionFactory {

    private static final String COLUMNNAME = "column_name";

    /**
     * Returns an Arx {@link PrivacyCriterion} object for the desired privacy object selected by the user.
     * @param model  enum representing the privacy model type we want created
     * @param params map containing parameters that defines which settings to be used to created the privacy model
     * @return an ARX {@link PrivacyCriterion} object created with the specified parameters
     */
    PrivacyCriterion create(PrivacyCriterionModel.PrivacyModel model, Map<String, String> params){
        switch(model){
            case KANONYMITY:
                return new KAnonymity(Integer.parseInt(params.get("k")));
            case LDIVERSITY_DISTINCT:
                validateColumnParam(params);
                return new DistinctLDiversity(params.get(COLUMNNAME),Integer.parseInt(params.get("l")));
            case LDIVERSITY_SHANNONENTROPY:
                validateColumnParam(params);
                return new EntropyLDiversity(params.get(COLUMNNAME),Integer.parseInt(params.get("l")),
                        EntropyLDiversity.EntropyEstimator.SHANNON);
            case LDIVERSITY_GRASSBERGERENTROPY:
                validateColumnParam(params);
                return new EntropyLDiversity(params.get(COLUMNNAME),Integer.parseInt(params.get("l")),
                        EntropyLDiversity.EntropyEstimator.GRASSBERGER);
            case LDIVERSITY_RECURSIVE:
                validateColumnParam(params);
                return new RecursiveCLDiversity(params.get(COLUMNNAME),Integer.parseInt(params.get("l")),
                        Integer.parseInt(params.get("c")));
            case TCLOSENESS_ORDERED_DISTANCE:
                validateColumnParam(params);
                return new OrderedDistanceTCloseness(params.get(COLUMNNAME),Double.parseDouble(params.get("t")));
            case TCLOSENESS_EQUAL_DISTANCE:
                validateColumnParam(params);
                return new EqualDistanceTCloseness(params.get(COLUMNNAME),Double.parseDouble(params.get("t")));
            default:
                throw new AaaSRuntimeException(model.getName() + " Privacy Model not supported");
        }
    }


    private static void validateColumnParam(Map<String, String> lDivParams){
        if (!lDivParams.containsKey(COLUMNNAME) || lDivParams.get(COLUMNNAME).isEmpty()){
            throw new IllegalArgumentException("Privacy Model must contain " + COLUMNNAME + " param");
        }
    }
}
