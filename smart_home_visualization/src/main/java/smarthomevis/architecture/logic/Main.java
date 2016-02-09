package smarthomevis.architecture.logic;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.primitives.Cylinder;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

import java.util.List;

public class Main extends CgApplication {

    public static void main(String[] args) {
        ResourcesLocator.getInstance().parseIniFile("smart_home_visualization/resources.ini");
        CgApplication main = new Main();
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
        appLauncher.create(main);
        appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);

        Connector.getInstance().connectToMongoDB("smarthome");
        Connector.getInstance().setCgRootNode(main.getCgRootNode());

        ObjFileReader reader = new ObjFileReader();
        List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
        ITriangleMesh bunny = meshes.get(0);
        CgNode bunnyNode = new CgNode(bunny, "bunny");
        bunny.fitToUnitBox();
        TriangleMeshTransformation.scale(bunny, 0.5);
        TriangleMeshTransformation.translate(bunny, VectorMatrixFactory.newIVector3(-1, 0.26, -1));
        bunny.computeTriangleNormals();
        bunny.computeVertexNormals();
        bunny.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
        bunny.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR4);
        bunny.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0, 0, 0));
        bunny.getMaterial().setTransparency(0.5);

        LayerController layerController = new LayerController();
        String layerId = layerController.save("BunnyLayer");
        layerController.addNodeToLayer(bunnyNode, layerId);
    }
}
