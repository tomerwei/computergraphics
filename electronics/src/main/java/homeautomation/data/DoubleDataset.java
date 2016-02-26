package homeautomation.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Observable;

public class DoubleDataset extends Observable {

  /**
   * Data values.
   */
  private List<DoubleDataItem> items = new ArrayList<DoubleDataItem>();

  /**
   * Constructor.
   */
  public DoubleDataset() {
  }

  /**
   * Add new item.
   * 
   * @param item Item
   *          to be added.
   */
  public void addDataItem(DoubleDataItem item) {
    items.add(item);

    setChanged();
    notifyObservers();
  }

  /**
   * Getter.
   * 
   * @return Number of items in dataset.
   */
  public int getNumberOfItems() {
    return items.size();
  }

  /**
   * Getter.
   * 
   * @param index
   *          Index of the item.
   * @return Item at the index.
   */
  public DoubleDataItem getItem(int index) {
    if (index < 0 || index >= items.size()) {
      System.out.println("Invalid index");
      return null;
    }
    return items.get(index);
  }

  /**
   * Getter.
   * 
   * @return Max value in the dataset.
   */
  public double getMaxValue() {
    double maxValue = Double.NEGATIVE_INFINITY;
    for (DoubleDataItem item : items) {
      if (item.getValue() > maxValue) {
        maxValue = item.getValue();
      }
    }
    return maxValue;
  }

  /**
   * Getter.
   * 
   * @return Max value in the dataset.
   */
  public double getMinValue() {
    double minValue = Double.POSITIVE_INFINITY;
    for (DoubleDataItem item : items) {
      if (item.getValue() < minValue) {
        minValue = item.getValue();
      }
    }
    return minValue;
  }
}
