package smarthomevis.architecture.data_access;

import org.mongodb.morphia.annotations.Entity;
import java.util.LinkedHashMap;

@Entity("devices")
public class Device extends BaseEntity {

    private String name;
    private LinkedHashMap<String, String> datedMeasurements;

    public Device() {
        datedMeasurements = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, String> getDatedMeasurements() {
        return datedMeasurements;
    }

    public void setDatedMeasurements(LinkedHashMap<String, String> datedMeasurements) {
        this.datedMeasurements = datedMeasurements;
    }
}
