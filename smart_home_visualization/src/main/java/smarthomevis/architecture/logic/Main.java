package smarthomevis.architecture.logic;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;

import java.util.ArrayList;
import java.util.List;

public class Main extends CgApplication {

    private static List<Layer> layers;

    public static void main(String[] args) {
        layers = new ArrayList<>();

        ResourcesLocator.getInstance().parseIniFile("smart_home_visualization/resources.ini");
        CgApplication main = new Main();
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
        appLauncher.create(main);
        appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
    }
}
