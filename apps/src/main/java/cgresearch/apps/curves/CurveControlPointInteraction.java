package cgresearch.apps.curves;

import java.util.HashMap;
import java.util.Map;

import cgresearch.graphics.datastructures.curves.ICurve;
import cgresearch.graphics.picking.CgApplicationPickable;
import cgresearch.graphics.picking.PickingItem;

public class CurveControlPointInteraction extends CgApplicationPickable {

  /**
   * Reference to the surface.
   */
  private final ICurve curve;

  /**
   * Mapping between a picking ID and a control point index (in 1D)
   */
  private Map<String, Integer> mapPickId2Index = new HashMap<String, Integer>();

  /**
   * Constructor
   */
  public CurveControlPointInteraction(ICurve curve) {
    this.curve = curve;
    for (int i = 0; i <= curve.getDegree(); i++) {
      PickingItem item = new PickingItem(curve.getControlPoint(i));
      mapPickId2Index.put(item.getId(), new Integer(i));
      addPickingItem(item);
    }
  }

  @Override
  public void itemPicked(String id) {
  }

  @Override
  public void itemMoved(String id) {
    int index = mapPickId2Index.get(id);
    PickingItem item = getItem(id);
    curve.setControlPoint(index, item.getPosition());
    curve.updateRenderStructures();
  }
}
