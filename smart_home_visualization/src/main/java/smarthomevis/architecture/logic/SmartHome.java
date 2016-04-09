package smarthomevis.architecture.logic;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import smarthomevis.architecture.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class SmartHome extends CgApplication {

    private List<CgNodeLayer> layers;

    public SmartHome() {
        super();
        layers = new ArrayList<>();
    }

    public static void main(String[] args) {
        initializeApplication();
    }

    public CgNodeLayer createCgNodeLayer(CgNode node) {
        CgNodeLayer layer = new CgNodeLayer(node);
        getCgRootNode().addChild(node);
        return layer;
    }

    public List<CgNodeLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<CgNodeLayer> layers) {
        this.layers = layers;
    }

    private static void initializeApplication() {
        ResourcesLocator.getInstance().parseIniFile(Configuration.getResourcesLocation());
        CgApplication smartHome = new SmartHome();
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
        appLauncher.create(smartHome);
        appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
    }
}
