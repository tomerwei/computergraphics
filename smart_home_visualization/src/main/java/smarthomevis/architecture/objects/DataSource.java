package smarthomevis.architecture.objects;

import org.mongodb.morphia.annotations.*;

@Entity("datasources")
@Indexes(@Index(value = "date", fields = @Field("date") ))
public class DataSource {

  @Id
  private String id;
  private String date;

  public DataSource(String id) {
    this.id = id;
  }

  public void addData(String date) {
    this.date += date;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
