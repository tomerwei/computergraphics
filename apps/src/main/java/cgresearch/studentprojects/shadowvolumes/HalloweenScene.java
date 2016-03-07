package cgresearch.studentprojects.shadowvolumes;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
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
        // Remove existing light sources
        getCgRootNode().clearLights();

        // Load room
        CgNode node = loadRoom();
        // Add objects
        if (node != null) {
            loadPumpkin("Pumpkin", VectorFactory.createVector(3), 0, node);
        }

        // Set light source
        lightSource.setPosition(VectorFactory.createVector3(0, 2, 0));
        lightSource.setColor(VectorFactory.createVector3(1,1,0));
        getCgRootNode().addLight(lightSource);
        getCgRootNode().setAllowShadows(true);
        getCgRootNode().setUseBlending(true);
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
