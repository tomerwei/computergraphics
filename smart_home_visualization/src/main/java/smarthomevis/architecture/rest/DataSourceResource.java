package smarthomevis.architecture.rest;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.logic.DataSource;
import smarthomevis.architecture.persistence.Connector;
import smarthomevis.architecture.persistence.Repository;

public class DataSourceResource extends ServerResource {

    private Connector connector;
    private Datastore datastore;
    private Repository<DataSource> dataSourceRepository;

    public DataSourceResource() {
        // TODO: Connector raus
        connector = new Connector();
        datastore = connector.connectToMongoDB("testDB");
        dataSourceRepository = new Repository<>(datastore, DataSource.class);
    }

    @Get
    public String getDataSource() {
        String objectId = getRequestAttributes().get("objectId").toString();
        DataSource dataSource = dataSourceRepository.get(DataSource.class, new ObjectId(objectId));
        return "Name: " + dataSource.getName() + " --- Data: " + dataSource.getData();
    }

}
