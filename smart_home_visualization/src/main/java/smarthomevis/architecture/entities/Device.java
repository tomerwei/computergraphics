package smarthomevis.architecture.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;
import smarthomevis.architecture.config.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

@Entity("devices")
public class Device extends BaseEntity {

    private LinkedHashMap<String, String> entries;

    @Transient
    private transient SimpleDateFormat simpleDateFormat;

    public Device() {
        entries = new LinkedHashMap<>();
        simpleDateFormat = new SimpleDateFormat(Configuration.getDateFormat());
    }

    public void addEntry(String data) {
        String timestamp = simpleDateFormat.format(new Date());
        entries.put(timestamp, data);
    }

    public LinkedHashMap<String, String> getEntries() {
        return entries;
    }

    public void clearEntries() {
        entries.clear();
    }
}
