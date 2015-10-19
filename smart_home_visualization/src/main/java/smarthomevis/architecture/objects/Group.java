package smarthomevis.architecture.objects;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity("groups")
@Indexes(
        @Index(value = "datasources", fields = @Field("datasources"))
)
public class Group {

    @Id
    private ObjectId id;
    private List<String> datasources;

    public Group () {
        datasources = new ArrayList<>();
    }

    public void addDataSource(String datasource) {
        datasources.add(datasource);
    }
}
