package no.nav.arxaas.controller;

import no.nav.arxaas.GenerateTestData;
import no.nav.arxaas.model.risk.DistributionOfRisk;
import no.nav.arxaas.model.risk.ReIdentificationRisk;
import no.nav.arxaas.model.risk.RiskProfile;
import no.nav.arxaas.model.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnalyzationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Request testPayload;

    @BeforeEach
    void setUp() {
        testPayload = GenerateTestData.zipcodeRequestPayload2Quasi();
    }


    @Test
    void getPayloadAnalyze() {

        ResponseEntity<RiskProfile> responseEntity = restTemplate.postForEntity("/api/analyze",testPayload, RiskProfile.class);
        assertNotNull(responseEntity);
        assertSame(HttpStatus.OK , responseEntity.getStatusCode());
        var resultData = responseEntity.getBody();
        assert resultData != null;
        assertNotNull(resultData.getReIdentificationRisk().getMeasures().get("records_affected_by_highest_prosecutor_risk"));
        assertEquals("[50,100]", resultData.getDistributionOfRisk().getRiskIntervalList().get(0).getInterval());
        assertEquals(1.0,resultData.getDistributionOfRisk().getRiskIntervalList().get(0).getRecordsWithRiskWithinInterval());
        assertEquals(1.0,resultData.getDistributionOfRisk().getRiskIntervalList().get(0).getRecordsWithMaximalRiskWithinInterval());
        assertNotNull(resultData.getDistributionOfRisk());

    }

    @Test
    void getPayloadAnalyze__check_for_re_identification_risk_values(){
        ResponseEntity<RiskProfile> responseEntity = restTemplate.postForEntity("/api/analyze",testPayload, RiskProfile.class);
        assertNotNull(responseEntity);
        assertSame(HttpStatus.OK , responseEntity.getStatusCode());
        ReIdentificationRisk actual = Objects.requireNonNull(responseEntity.getBody()).getReIdentificationRisk();
        assert actual != null;
        ReIdentificationRisk expected = GenerateTestData.ageGenderZipcodeReIndenticationRisk();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getPayloadAnalyze__check_for_distribution_of_risk_values(){
        ResponseEntity<RiskProfile> responseEntity = restTemplate.postForEntity("/api/analyze",testPayload, RiskProfile.class);
        assertNotNull(responseEntity);
        assertSame(HttpStatus.OK , responseEntity.getStatusCode());
        DistributionOfRisk actual = Objects.requireNonNull(responseEntity.getBody()).getDistributionOfRisk();
        assert actual != null;
        DistributionOfRisk expected = GenerateTestData.ageGenderZipcodeDistributionOfRisk();
        Assertions.assertEquals(expected,actual);
    }
}