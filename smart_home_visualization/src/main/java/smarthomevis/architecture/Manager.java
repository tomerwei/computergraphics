package smarthomevis.architecture;

import cgresearch.graphics.bricks.CgApplication;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import smarthomevis.architecture.objects.DataSource;

import java.util.ArrayList;
import java.util.List;

public class Manager extends CgApplication {

  private List<String> datasources;
  private List<String> groups;

  Datastore datastore;

  public Manager() {
    datasources = new ArrayList<>();
    groups = new ArrayList<>();
  }

  public void connectToMongoDB(String databaseName) {
    MongoClient mongoClient = new MongoClient();
    // MongoDatabase mongoDB = mongoClient.getDatabase(databaseName);

    final Morphia morphia = new Morphia();
    morphia.mapPackage("cg.smart_home_visualization.src.main.java.smarthomevis.architecture");
    datastore = morphia.createDatastore(mongoClient, databaseName);
    datastore.ensureIndexes();
  }

  public void addDatasource() {
    DataSource datasource = new DataSource("myId1");
    datasource.addData("1999-12-31");

    datastore.save(datasource);
  }

}
