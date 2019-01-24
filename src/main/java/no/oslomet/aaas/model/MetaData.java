package no.oslomet.aaas.model;
import java.util.Map;

public class MetaData {

    private Map<String, String> sensitivityList;
    private Map<String, String> dataType;
    private Map<String, String> hierarchy;
    private Map<String, Map<String, String>> models;

    public Map<String, String> getSensitivityList() {
        return sensitivityList;
    }

    public void setSensitivityList(Map<String, String> sensitivityList) {
        this.sensitivityList = sensitivityList;
    }

    public Map<String, String> getDataType() {
        return dataType;
    }

    public void setDataType(Map<String, String> dataType) {
        this.dataType = dataType;
    }

    public Map<String, String> getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Map<String, String> hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Map<String, Map<String, String>> getModels() {
        return models;
    }

    public void setModels(Map<String, Map<String, String>> models) {
        this.models = models;
    }


}
