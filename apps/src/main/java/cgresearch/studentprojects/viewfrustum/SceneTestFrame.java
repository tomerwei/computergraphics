package cgresearch.studentprojects.viewfrustum;

import cgresearch.JoglAppLauncher;
import cgresearch.apps.urbanscene.UrbanScene;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

public class SceneTestFrame extends CgApplication{
    
    public static void main(String[] args){
        ResourcesLocator.getInstance().parseIniFile("resources.ini");

        UrbanScene us = new UrbanScene();
        
        CgApplication app =  new FrustumTestFrame(new UrbanScene().getCgRootNode(),  -5.0, 230.0);
        
//        CgApplication app =  new FrustumTestFrame(new SceneFactory().getCgRootNode(), -8.0, -2.0);
        
        

        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
//        CgApplication app =  us;

        appLauncher.create(app);
        appLauncher.setRenderSystem(RenderSystem.JOGL);
        appLauncher.setUiSystem(UI.JOGL_SWING);
        
//        Camera.getInstance().setEye(VectorMatrixFactory.newIVector3(0.0, 0.0, 10.0));
    
    }
}
