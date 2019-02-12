package no.OsloMET.aaas.utils;

import no.oslomet.aaas.model.AnonymizationPayload;
import no.oslomet.aaas.model.PrivacyModel;
import no.oslomet.aaas.model.SensitivityModel;
import no.oslomet.aaas.model.MetaData;
import no.oslomet.aaas.utils.ARXWrapper;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static no.oslomet.aaas.model.PrivacyModel.KANONYMITY;
import static no.oslomet.aaas.model.PrivacyModel.LDIVERSITY;
import static no.oslomet.aaas.model.SensitivityModel.IDENTIFYING;

public class ARXWrapperTest {
    ARXWrapper arxWrapper;
    String testValues = "age, gender, zipcode\n34, male, 81667\n35, female, 81668\n36, male, 81669";

    @Before
    public void initilze(){
        arxWrapper = new ARXWrapper();
    }

    @Test
    public void makedata() {

        String expectedResultValue1 = "34";
        String expectedResultValue2 = "female";
        String expectedResultValue3 = "81669";

        Data result = arxWrapper.setData(testValues);
        String resultValue1 = result.getHandle().getValue(0,0);
        String resultValue2 = result.getHandle().getValue(1,1);
        String resultValue3 = result.getHandle().getValue(2,2);

        Assert.assertEquals(resultValue1,expectedResultValue1);
        Assert.assertEquals(resultValue2,expectedResultValue2);
        Assert.assertEquals(resultValue3,expectedResultValue3 );
    }

    @Test
    public void setSuppression(){
        ARXConfiguration testData = ARXConfiguration.create();
        arxWrapper.setsuppressionlimit(testData);

        String result = String.valueOf(testData.getSuppressionLimit());

        String expectedResult = "0.02";

        Assert.assertEquals(result,expectedResult);
    }

    @Test
    public void setSensitivityModels() {

        Data testData = arxWrapper.setData(testValues);
        AnonymizationPayload testpayload = new AnonymizationPayload();
        MetaData testMetaData = new MetaData();
        Map<String,SensitivityModel> testMap = new HashMap<>();
        testMap.put("age",IDENTIFYING);
        testMetaData.setSensitivityList(testMap);
        testpayload.setMetaData(testMetaData);

        arxWrapper.setSensitivityModels(testData,testpayload);

        String result = String.valueOf(testData.getDefinition().getAttributeType("age"));
        String expectedResult = "IDENTIFYING_ATTRIBUTE";
        Assert.assertEquals(result,expectedResult);
    }

    @Test
    public void setPrivacyModelsKAnon(){

        ARXConfiguration testConfig = ARXConfiguration.create();
        AnonymizationPayload testpayload = new AnonymizationPayload();
        MetaData testMetaData = new MetaData();
        Map<PrivacyModel,Map<String,String>> testMap = new HashMap<>();
        Map<String,String> testMapValue = new HashMap<>();
        testMapValue.put("k","5");
        testMap.put(KANONYMITY,testMapValue);
        testMetaData.setModels(testMap);
        testpayload.setMetaData(testMetaData);

        arxWrapper.setPrivacyModels(testConfig,testpayload);

        String result = String.valueOf(testConfig.getPrivacyModels());

        String expectedResult = "[5-anonymity]";

        Assert.assertEquals(result,expectedResult);
    }

    @Test
    public void setPrivacyModelsLDiv(){

        Data testData = arxWrapper.setData(testValues);
        ARXConfiguration testConfig = ARXConfiguration.create();
        testData.getDefinition().setAttributeType("age", AttributeType.SENSITIVE_ATTRIBUTE);
        AnonymizationPayload testpayload = new AnonymizationPayload();
        MetaData testMetaData = new MetaData();
        Map<PrivacyModel,Map<String,String>> testMap = new HashMap<>();
        Map<String,String> testMapValue = new HashMap<>();
        testMapValue.put("l","5");
        testMapValue.put("column_name","age");
        testMapValue.put("variant","distinct");
        testMap.put(LDIVERSITY,testMapValue);
        testMetaData.setModels(testMap);
        testpayload.setMetaData(testMetaData);

        arxWrapper.setPrivacyModels(testConfig,testpayload);

        String result = String.valueOf(testConfig.getPrivacyModels());

        String expectedResult = "[distinct-5-diversity for attribute 'age']";

        Assert.assertEquals(result,expectedResult);
        }

    @Test
    public void setHierarchies(){

        Data testData = arxWrapper.setData(testValues);
        AnonymizationPayload testpayload = new AnonymizationPayload();
        MetaData testMetaData = new MetaData();
        Map<String ,String[][]> testMap = new HashMap<>();
        String [][] testHeirarchy = new String[][]{
                {"81667","81*67"},{"81668","8*668"},{"81669","8166*"}
        };
        testMap.put("zipcode",testHeirarchy);
        testMetaData.setHierarchy(testMap);
        testpayload.setMetaData(testMetaData);

        testData = arxWrapper.setHierarchies(testData,testpayload);

        String result = Arrays.deepToString(testData.getDefinition().getHierarchy("zipcode"));

        String expectedResult = "[[81667, 81*67], [81668, 8*668], [81669, 8166*]]";
        Assert.assertEquals(result,expectedResult);
    }

    @Test
    public void setAnonymizer() {
    }

    @Test
    public void anonymize() {
    }
}