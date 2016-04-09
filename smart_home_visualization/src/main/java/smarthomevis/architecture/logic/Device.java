package smarthomevis.architecture.logic;

import org.mongodb.morphia.annotations.Entity;
import smarthomevis.architecture.config.Configuration;
import smarthomevis.architecture.data_access.BaseEntity;

import java.util.Date;
import java.util.LinkedHashMap;

@Entity("devices")
public class Device extends BaseEntity {

    private LinkedHashMap<String, String> entries;

    public Device() {
        entries = new LinkedHashMap<>();
    }

    public void addEntry(String data) {
        String timestamp = Configuration.getSimpleDateFormat().format(new Date());
        entries.put(timestamp, data);
    }

    public LinkedHashMap<String, String> getEntries() {
        return entries;
    }

    public void clearEntries() {
        entries.clear();
    }
}
