package cgresearch.studentprojects.shadowvolumes;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.LightSource;

/**
 * Created by Marcel on 3/7/2016.
 */
public class HalloweenScene extends AbstractShadowVolumeDemo {

    private LightSource lightSource = new LightSource(LightSource.Type.POINT, LightSource.ShadowType.HARD, -1);

    /**
     * Constructor
     */
    public HalloweenScene() {
        loadScene();
    }

    private void loadScene() {
        Vector halloweenPumpkinPos = VectorFactory.createVector3(8.8, 0, 8.7);
        Vector lightPos = VectorFactory.createVector(3);
        lightPos.copy(halloweenPumpkinPos);
        lightPos.set(1, 2);

        // Remove existing light sources
        getCgRootNode().clearLights();

        // Load room
        CgNode node = loadHouseEntrance();

        // Add objects
        if (node != null) {
            loadPumpkinHalloween("Halloween Pumpkin",halloweenPumpkinPos, 105, node);
            loadPumpkinOrange("Small Pumpkin 1", VectorFactory.createVector3(11.3, 0, 2.9), 0,node);
            loadPumpkinRed("Small Pumpkin 2", VectorFactory.createVector3(6.8, 0, 3.5), 0,node);
            loadPumpkinLightGreen("Small Pumpkin 3", VectorFactory.createVector3(10.8, 0, 13.1), 0,node);
            loadPumpkinFlatRed("Small Pumpkin 4", VectorFactory.createVector3(6.9, 0, 13.4), 0,node);
            loadPumpkinGreen("Small Pumpkin 5", VectorFactory.createVector3(5.6, 0, 11.3), 0,node);
            loadPumpkinFlatGreen("Small Pumpkin 6", VectorFactory.createVector3(5.5, 0, 6.4), 0,node);
        }

        // Set light source
        lightSource.setPosition(lightPos);
        lightSource.setColor(VectorFactory.createVector3(1,0.8,0));
        getCgRootNode().addLight(lightSource);
        getCgRootNode().setAllowShadows(true);
    }

    /**
     * Program entry point.
     */
    public static void main(String[] args) {
        ResourcesLocator.getInstance().parseIniFile("resources.ini");
        CgApplication app = new HalloweenScene();
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
        appLauncher.create(app);
        appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
    }
}
