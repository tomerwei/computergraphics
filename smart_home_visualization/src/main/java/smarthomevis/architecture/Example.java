package smarthomevis.architecture;

import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import smarthomevis.groundplan.GroundPlan;
import smarthomevis.groundplan.IGroundPlan;

import static cgresearch.AppLauncher.*;

public class Example {

    public static void main(String[] args) {
        ResourcesLocator.getInstance().parseIniFile("smart_home_visualization/resources.ini");
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();

        SmartHome smartHome = new SmartHome();
        IGroundPlan gp = new GroundPlan();
        //CgNode plan = gp.convertDXFPlanToCgNode("4H-HORA Projekt1");
        //smartHome.getCgRootNode().addChild(plan);

        appLauncher.create(smartHome);
        appLauncher.setRenderSystem(RenderSystem.JOGL);
        appLauncher.setUiSystem(UI.JOGL_SWING);
    }
}
