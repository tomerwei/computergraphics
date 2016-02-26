package homeautomation.data;

import java.util.Date;

public class DoubleDataItem {

  /**
   * Value of the item.
   */
  private double value;

  /**
   * Date of the aquisition.
   */
  private Date date;

  /**
   * Constructor.
   * 
   * @param value
   *          Initial value.
   * @param date
   *          Initial date.
   */
  public DoubleDataItem(double value, Date date) {
    this.value = value;
    this.date = date;
  }

  /**
   * Getter.
   * 
   * @return value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Getter.
   * 
   * @return Date.
   */
  public Date getDate() {
    return date;
  }
}
