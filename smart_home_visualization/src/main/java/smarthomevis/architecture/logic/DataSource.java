package smarthomevis.architecture.logic;

import org.mongodb.morphia.annotations.*;

@Entity("datasources")
public class DataSource extends BaseEntity {

  private String name;
  private String data;

  public DataSource() {
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass())
      return false;

    DataSource that = (DataSource) other;

    return name.equals(that.name) && data.equals(that.data);

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (data != null ? data.hashCode() : 0);
    return result;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
