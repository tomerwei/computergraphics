package smarthomevis.architecture.entities;

import org.mongodb.morphia.annotations.Entity;
import smarthomevis.architecture.persistence.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;

@Entity("devices")
public class Device extends BaseEntity {

    private String name;
    private LinkedHashMap<Date, String> data;

    public Device() {
        data = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String insert(String input) {
        Date timestamp = new Date();
        data.put(timestamp, input);
        return timestamp.toString();
    }

    public LinkedHashMap<Date, String> getData() {
        return data;
    }

    public void setData(LinkedHashMap<Date, String> data) {
        this.data = data;
    }
}
