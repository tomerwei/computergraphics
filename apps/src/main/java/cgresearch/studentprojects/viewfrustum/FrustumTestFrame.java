package cgresearch.studentprojects.viewfrustum;

/**
 * Testframe fuer View Frustum Culling
 */

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.fileio.PlyFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.rendering.jogl.misc.OctreeFactoryStrategyScene;
import cgresearch.rendering.jogl.misc.ViewFrustumCulling;

public class FrustumTestFrame extends CgApplication {

  public static OctreeFactoryStrategyScene scene;

  public static final double objectsTransparency = 0.5;
  public static CgRootNode rootNode;
  public static boolean viewFrustumCullingTest = true;

  public FrustumTestFrame() {
    

    getCgRootNode().setUseBlending(true);
    getCgRootNode().setUseViewFrustumCulling(false);
    getCgRootNode().setShowFps(true);

    PlyFileReader reader = new PlyFileReader();
    ITriangleMesh dragon = reader.readFile("meshes/dragon_vrip_res3.ply");
    
     ITriangleMesh cow = loadMesh("meshes/cow.obj");
     ITriangleMesh bunny = loadMesh("meshes/bunny.obj");
     ITriangleMesh bunnyDown = loadMesh("meshes/bunny.obj");
     ITriangleMesh bunnyUp = loadMesh("meshes/bunny.obj");
     ITriangleMesh bunnyUp1 = loadMesh("meshes/bunny.obj");
     ITriangleMesh fenja = loadMesh("meshes/fenja02.obj");
     ITriangleMesh pumpkin = loadMesh("meshes/pumpkin.obj");
     
    
     // // ############### Transformations ###############
//     System.out.println("BB BUNNDYDOWN VORHER = " + bunnyDown.getBoundingBox());
     TriangleMeshTransformation.scale(bunnyDown, 3.0);
     TriangleMeshTransformation.translate(bunnyDown,
     VectorFactory.createVector3(0.0, 0.0, 2.0));
//     System.out.println("BB BUNNDYDOWN NACHHER = " + bunnyDown.getBoundingBox());
//     System.out.println("BB COW VORHER = " + cow.getBoundingBox());
     TriangleMeshTransformation.transform(cow, new Matrix(Math.cos(100), -Math.sin(100),0 ,Math.sin(100), Math.cos(100), 0, 0, 0, 1));
     TriangleMeshTransformation.translate(cow,
     VectorFactory.createVector3(5.5, 5.5, -5.0));
//     System.out.println("BB COW NACHHER = " + cow.getBoundingBox());
//     System.out.println("BB BUNNY VORHER = " + bunny.getBoundingBox());
     TriangleMeshTransformation.scale(bunny, 3.0);
     TriangleMeshTransformation.translate(bunny,
     VectorFactory.createVector3(0.0, 4.0 /* 1.15 */, 9.0));
//     System.out.println("BB BUNNY NACHHER = " + bunny.getBoundingBox());
//     System.out.println("BB FENJA VORHER = " + fenja.getBoundingBox());
     TriangleMeshTransformation.scale(fenja, 1.0/*1*/);
     TriangleMeshTransformation.translate(fenja,
     VectorFactory.createVector3(0.5, 0.0, 25.0));
//     System.out.println("BB FENJA NACHHER = " + fenja.getBoundingBox());
//     System.out.println("BB BUNNYUP VORHER = " + bunnyUp.getBoundingBox());
     TriangleMeshTransformation.scale(bunnyUp, 3.0);
     TriangleMeshTransformation.translate(bunnyUp,
     VectorFactory.createVector3(0.0, 2.0, 0.5));
//     System.out.println("BB BUNNYUP NACHHER = " + bunnyUp.getBoundingBox());
//     System.out.println("BB PUMPKIN VORHER = " + pumpkin.getBoundingBox());
     TriangleMeshTransformation.scale(pumpkin,0.02);
     TriangleMeshTransformation.translate(pumpkin,
     VectorFactory.createVector3(0.0, 0.0, 23.5));
//     System.out.println("BB PUMPKIN NACHHER = " + pumpkin.getBoundingBox());
     // ############### Transformationen ###############
    
     getCgRootNode().addChild(new CgNode(bunny, "bunny"));
     getCgRootNode().addChild(new CgNode(bunnyUp, "bunnyUp"));
     getCgRootNode().addChild(new CgNode(pumpkin, "pumpkin"));
     getCgRootNode().addChild(new CgNode(bunnyDown, "bunnyDown"));
     getCgRootNode().addChild(new CgNode(bunnyUp1, "bunnyUp1"));
     getCgRootNode().addChild(new CgNode(cow, "cow")); 
     getCgRootNode().addChild(new CgNode(dragon, "dragon"));
     getCgRootNode().addChild(new CgNode(fenja, "fenja"));
     

    // ######################################################################################################################################
//     UrbanSceneGenerator usg = new UrbanSceneGenerator();
//    // CgNode usNode = usg.buildScene(-4, -4);
//     CgNode usNode = usg.buildScene(2, 2);
//     getCgRootNode().addChild(usNode);

    // ######################################################################################################################################
    ITriangleMesh bunny1 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny1, 3.0);
    TriangleMeshTransformation.translate(bunny1, VectorFactory.createVector3(0.0, 0.0, 0.0));
    ITriangleMesh bunny2 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny2, 3.0);
    TriangleMeshTransformation.translate(bunny2, VectorFactory.createVector3(0.0, 0.0, -1.0));
    ITriangleMesh bunny3 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny3, 3.0);
    TriangleMeshTransformation.translate(bunny3, VectorFactory.createVector3(0.0, 0.0, 1.0));
    ITriangleMesh bunny4 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny4, 3.0);
    TriangleMeshTransformation.translate(bunny4, VectorFactory.createVector3(-1.0, 0.0, 0));
    ITriangleMesh bunny5 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny5, 3.0);
    TriangleMeshTransformation.translate(bunny5, VectorFactory.createVector3(1.0, 0.0, 0));
    ITriangleMesh bunny6 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny6, 3.0);
    TriangleMeshTransformation.translate(bunny6, VectorFactory.createVector3(0.0, -1.0, 0));
    ITriangleMesh bunny7 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny7, 3.0);
    TriangleMeshTransformation.translate(bunny7, VectorFactory.createVector3(0.0, 1.0, 0));

    ITriangleMesh bunny8 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny8, 3.0);
    TriangleMeshTransformation.translate(bunny8, VectorFactory.createVector3(0.0, 0.0, -2));
    ITriangleMesh bunny9 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny9, 3.0);
    TriangleMeshTransformation.translate(bunny9, VectorFactory.createVector3(0.0, 0.0, 2));
    ITriangleMesh bunny10 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny10, 3.0);
    TriangleMeshTransformation.translate(bunny10, VectorFactory.createVector3(0.0, 2.0, 0));
    ITriangleMesh bunny11 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny11, 3.0);
    TriangleMeshTransformation.translate(bunny11, VectorFactory.createVector3(0, -2.0, 0));
    ITriangleMesh bunny12 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny12, 3.0);
    TriangleMeshTransformation.translate(bunny12, VectorFactory.createVector3(2.0, 0.0, 0));
    ITriangleMesh bunny13 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny13, 3.0);
    TriangleMeshTransformation.translate(bunny13, VectorFactory.createVector3(-2.0, 0.0, 0));
    
    ITriangleMesh bunny14 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny14, 3.0);
    TriangleMeshTransformation.translate(bunny14, VectorFactory.createVector3(0.0, 0.0, -3));
    ITriangleMesh bunny15 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny15, 3.0);
    TriangleMeshTransformation.translate(bunny15, VectorFactory.createVector3(0.0, 0.0, 3));
    ITriangleMesh bunny16 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny16, 3.0);
    TriangleMeshTransformation.translate(bunny16, VectorFactory.createVector3(0.0, 3.0, 0));
    ITriangleMesh bunny17 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny17, 3.0);
    TriangleMeshTransformation.translate(bunny17, VectorFactory.createVector3(0, -3.0, 0));
    ITriangleMesh bunny18 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny18, 3.0);
    TriangleMeshTransformation.translate(bunny18, VectorFactory.createVector3(3.0, 0.0, 0));
    ITriangleMesh bunny19 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny19, 3.0);
    TriangleMeshTransformation.translate(bunny19, VectorFactory.createVector3(-3.0, 0.0, 0));

    ITriangleMesh bunny20 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny20, 3.0);
    TriangleMeshTransformation.translate(bunny20, VectorFactory.createVector3(0.0, 0.0, -4));
    ITriangleMesh bunny21 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny21, 3.0);
    TriangleMeshTransformation.translate(bunny21, VectorFactory.createVector3(0.0, 0.0, 4));
    ITriangleMesh bunny22 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny22, 3.0);
    TriangleMeshTransformation.translate(bunny22, VectorFactory.createVector3(0.0, 4.0, 0));
    ITriangleMesh bunny23 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny23, 3.0);
    TriangleMeshTransformation.translate(bunny23, VectorFactory.createVector3(0, -4.0, 0));
    ITriangleMesh bunny24 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny24, 3.0);
    TriangleMeshTransformation.translate(bunny24, VectorFactory.createVector3(4.0, 0.0, 0));
    ITriangleMesh bunny25 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny25, 3.0);
    TriangleMeshTransformation.translate(bunny25, VectorFactory.createVector3(-4.0, 0.0, 0));
    
    ITriangleMesh bunny26 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny26, 3.0);
    TriangleMeshTransformation.translate(bunny26, VectorFactory.createVector3(0.0, 5.0, 0));
    ITriangleMesh bunny27 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny27, 3.0);
    TriangleMeshTransformation.translate(bunny27, VectorFactory.createVector3(0.0, 6.0, 0));
    ITriangleMesh bunny28 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny28, 3.0);
    TriangleMeshTransformation.translate(bunny28, VectorFactory.createVector3(0.0, 7.0, 0));
    ITriangleMesh bunny29 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.scale(bunny29, 3.0);
    TriangleMeshTransformation.translate(bunny29, VectorFactory.createVector3(0.0, 10.0, 0));
    
    getCgRootNode().addChild(new CgNode(bunny1, "bunny1"));
    getCgRootNode().addChild(new CgNode(bunny2, "bunny2"));
    getCgRootNode().addChild(new CgNode(bunny3, "bunny3"));
    getCgRootNode().addChild(new CgNode(bunny4, "bunny4"));
    getCgRootNode().addChild(new CgNode(bunny5, "bunny5"));
    getCgRootNode().addChild(new CgNode(bunny6, "bunny6"));
    getCgRootNode().addChild(new CgNode(bunny7, "bunny7"));
    getCgRootNode().addChild(new CgNode(bunny8, "bunny8"));
    getCgRootNode().addChild(new CgNode(bunny9, "bunny9"));
    getCgRootNode().addChild(new CgNode(bunny10, "bunny10"));
    getCgRootNode().addChild(new CgNode(bunny11, "bunny11"));
    getCgRootNode().addChild(new CgNode(bunny12, "bunny12"));
    getCgRootNode().addChild(new CgNode(bunny13, "bunny13"));
    getCgRootNode().addChild(new CgNode(bunny14, "bunny14"));
    getCgRootNode().addChild(new CgNode(bunny15, "bunny15"));
    getCgRootNode().addChild(new CgNode(bunny16, "bunny16"));
    getCgRootNode().addChild(new CgNode(bunny17, "bunny17"));
    getCgRootNode().addChild(new CgNode(bunny18, "bunny18"));
    getCgRootNode().addChild(new CgNode(bunny19, "bunny19"));
    getCgRootNode().addChild(new CgNode(bunny20, "bunny20"));
    getCgRootNode().addChild(new CgNode(bunny21, "bunny21"));
    getCgRootNode().addChild(new CgNode(bunny22, "bunny22"));
    getCgRootNode().addChild(new CgNode(bunny23, "bunny23"));
    getCgRootNode().addChild(new CgNode(bunny24, "bunny24"));
    getCgRootNode().addChild(new CgNode(bunny25, "bunny25"));
    getCgRootNode().addChild(new CgNode(bunny26, "bunny26"));
    getCgRootNode().addChild(new CgNode(bunny27, "bunny27"));
    getCgRootNode().addChild(new CgNode(bunny28, "bunny28"));
    getCgRootNode().addChild(new CgNode(bunny29, "bunny29"));

    rootNode = getCgRootNode();
  }

  /**
   * erzeugt ein TriangleMesh aus der angegebenen Datei
   */
  public ITriangleMesh loadMesh(String path) {
    String objFilename = path;
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return null;
    }
    meshes.get(0).getMaterial().setTransparency(objectsTransparency);
    meshes.get(0).getMaterial().setRenderMode(Normals.PER_FACET);
    meshes.get(0).getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    return meshes.get(0);
  }

  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");

    CgApplication app = new FrustumTestFrame();
    if ((!rootNode.useViewFrustumCulling()) && viewFrustumCullingTest) {
      ViewFrustumCulling vfc = new ViewFrustumCulling(rootNode);
      vfc.computeVisibleScenePart(app.getCgRootNode());
    }
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}
