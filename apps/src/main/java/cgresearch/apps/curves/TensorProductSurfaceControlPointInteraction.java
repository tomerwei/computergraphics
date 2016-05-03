package cgresearch.apps.curves;

import java.util.HashMap;
import java.util.Map;

import cgresearch.graphics.datastructures.curves.TensorProductSurface;
import cgresearch.graphics.picking.CgApplicationPickable;
import cgresearch.graphics.picking.PickingItem;

public class TensorProductSurfaceControlPointInteraction
    extends CgApplicationPickable {

  /**
   * Reference to the surface.
   */
  private final TensorProductSurface surface;

  /**
   * Mapping between a picking ID and a control point index (in 1D)
   */
  private Map<String, Integer> mapPickId2Index = new HashMap<String, Integer>();

  /**
   * Constructor
   */
  public TensorProductSurfaceControlPointInteraction(
      TensorProductSurface surface) {
    this.surface = surface;
    for (int i = 0; i <= surface.getDegreeU(); i++) {
      for (int j = 0; j <= surface.getDegreeV(); j++) {
        PickingItem item = new PickingItem(surface.getControlPoint(i, j));
        int controlPointIndex = j * (surface.getDegreeU() + 1) + i;
        mapPickId2Index.put(item.getId(), new Integer(controlPointIndex));
        addPickingItem(item);
      }
    }
  }

  @Override
  public void itemPicked(String id) {
  }

  @Override
  public void itemMoved(String id) {
    int index1D = mapPickId2Index.get(id);
    int j = index1D / (surface.getDegreeU() + 1);
    int i = index1D - j * (surface.getDegreeU() + 1);
    PickingItem item = getItem(id);
    surface.setControlPoint(i, j, item.getPosition());
  }
}
