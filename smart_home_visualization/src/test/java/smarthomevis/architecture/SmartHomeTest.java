package smarthomevis.architecture;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.primitives.Cylinder;
import cgresearch.graphics.scenegraph.CgNode;
import org.junit.*;
import smarthomevis.architecture.logic.CgNodeLayer;
import smarthomevis.architecture.logic.DeviceLayer;
import smarthomevis.architecture.logic.SmartHome;
import smarthomevis.groundplan.GroundPlan;
import smarthomevis.groundplan.IGroundPlan;

public class SmartHomeTest {

    public static void main(String[] args) {
        ResourcesLocator.getInstance().parseIniFile("smart_home_visualization/resources.ini");
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();

        IGroundPlan gp = new GroundPlan();
        CgNode plan = gp.convertDXFPlanToCgNode("4H-HORA Projekt1");

        DeviceLayer deviceLayer = new DeviceLayer();
        deviceLayer.addDevice("device_id");

        SmartHome smartHome = new SmartHome();
        smartHome.createCgNodeLayer(plan);
        smartHome.addLayer(deviceLayer);

        appLauncher.create(smartHome);
        appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
    }
}
