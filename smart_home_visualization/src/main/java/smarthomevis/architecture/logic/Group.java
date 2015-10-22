package smarthomevis.architecture.logic;

import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity("groups")
public class Group extends BaseEntity {

    private List<DataSource> dataSources;
    private String name;

    public Group (String name) {
        this.name = name;
        dataSources = new ArrayList<>();
    }


}
