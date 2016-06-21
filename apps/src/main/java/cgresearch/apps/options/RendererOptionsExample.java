package cgresearch.apps.options;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.bricks.CgApplication;


/**
 * Sample app that showcases the renderer options interface.
 * All this app does is paint an empty scene with a black background instead of
 * the default white one.
 * Created by christian on 18.06.16.
 */
public class RendererOptionsExample extends CgApplication {

    public RendererOptionsExample() {
        //set the background to black

        //set up the necessary renderer options
        //set them in the root node of the scene graph so that the renderer
        // can access them
        getCgRootNode().getRendererOptions().
                setClearColor(VectorFactory.
                        createVector3(0,0,0)); // default is (1,1,1)
    }

    public static void main(String[] args) {
        ResourcesLocator.getInstance().parseIniFile("resources.ini");
        CgApplication app = new RendererOptionsExample();
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
        appLauncher.create(app);
        appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
    }

}
