package smarthomevis.architecture;

import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.primitives.Cylinder;
import cgresearch.graphics.scenegraph.CgNode;
import org.junit.*;
import smarthomevis.architecture.logic.CgNodeLayer;

@Ignore
public class SmartHomeTest {

    @Test
    public void addLayer() {
        Cylinder cylinder = new Cylinder(
            VectorFactory.createVector3(0, 0, 0),
            VectorFactory.createVector3(0, 1, 0), 0.065 / (2.0));
        CgNode cylinderNode = new CgNode(cylinder, "cylinder");
        CgNodeLayer layer = new CgNodeLayer(cylinderNode);
    }

}
