package cgresearch.studentprojects.viewfrustum;

import java.util.ArrayList;
import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.rendering.jogl.misc.OctreeFactoryStrategyScene;

public class OctreeTestFrame extends CgApplication {
  
  public static CgRootNode rootNode;
  
  public OctreeTestFrame(){
    
    getCgRootNode().setUseBlending(true);
    
    ITriangleMesh cow = loadMesh("meshes/cow.obj");
    ITriangleMesh bunny = loadMesh("meshes/bunny.obj");
    ITriangleMesh bunnyDown = loadMesh("meshes/bunny.obj");
    ITriangleMesh bunnyUp = loadMesh("meshes/bunny.obj");
    ITriangleMesh bunnyUp1 = loadMesh("meshes/bunny.obj");
    ITriangleMesh fenja = loadMesh("meshes/fenja02.obj");
    ITriangleMesh pumpkin = loadMesh("meshes/pumpkin.obj");
    
   
    // // ############### Transformations ###############
//    System.out.println("BB BUNNDYDOWN VORHER = " + bunnyDown.getBoundingBox());
    TriangleMeshTransformation.scale(bunnyDown, 3.0);
    TriangleMeshTransformation.translate(bunnyDown,
    VectorFactory.createVector3(0.0, 0.0, 2.0));
//    System.out.println("BB BUNNDYDOWN NACHHER = " + bunnyDown.getBoundingBox());
//    System.out.println("BB COW VORHER = " + cow.getBoundingBox());
    TriangleMeshTransformation.transform(cow, new Matrix(Math.cos(100), -Math.sin(100),0 ,Math.sin(100), Math.cos(100), 0, 0, 0, 1));
    TriangleMeshTransformation.translate(cow,
    VectorFactory.createVector3(5.5, 5.5, -5.0));
//    System.out.println("BB COW NACHHER = " + cow.getBoundingBox());
//    System.out.println("BB BUNNY VORHER = " + bunny.getBoundingBox());
    TriangleMeshTransformation.scale(bunny, 3.0);
    TriangleMeshTransformation.translate(bunny,
    VectorFactory.createVector3(0.0, 4.0 /* 1.15 */, 9.0));
//    System.out.println("BB BUNNY NACHHER = " + bunny.getBoundingBox());
//    System.out.println("BB FENJA VORHER = " + fenja.getBoundingBox());
    TriangleMeshTransformation.translate(fenja,
    VectorFactory.createVector3(0.5, 0.0, 25.0));
    TriangleMeshTransformation.scale(fenja, 1.0/*1*/);
//    System.out.println("BB FENJA NACHHER = " + fenja.getBoundingBox());
//    System.out.println("BB BUNNYUP VORHER = " + bunnyUp.getBoundingBox());
    TriangleMeshTransformation.scale(bunnyUp, 3.0);
    TriangleMeshTransformation.translate(bunnyUp,
    VectorFactory.createVector3(0.0, 2.0, 0.5));
//    System.out.println("BB BUNNYUP NACHHER = " + bunnyUp.getBoundingBox());
//    System.out.println("BB PUMPKIN VORHER = " + pumpkin.getBoundingBox());
    TriangleMeshTransformation.scale(pumpkin,0.02);
    TriangleMeshTransformation.translate(pumpkin,
    VectorFactory.createVector3(0.0, 0.0, 23.5));
//    System.out.println("BB PUMPKIN NACHHER = " + pumpkin.getBoundingBox());
    // ############### Transformationen ###############
   
    getCgRootNode().addChild(new CgNode(bunnyUp, "bunnyUp"));
    getCgRootNode().addChild(new CgNode(bunny, "bunny"));
    getCgRootNode().addChild(new CgNode(pumpkin, "pumpkin"));
    getCgRootNode().addChild(new CgNode(bunnyDown, "bunnyDown"));
    getCgRootNode().addChild(new CgNode(bunnyUp1, "bunnyUp1"));
    getCgRootNode().addChild(new CgNode(cow, "cow")); 
    getCgRootNode().addChild(new CgNode(fenja, "fenja"));
    rootNode = getCgRootNode();
  }
  
  public ITriangleMesh loadMesh(String path) {
    String objFilename = path;
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return null;
    }
    meshes.get(0).getMaterial().setTransparency(0.5);
    meshes.get(0).getMaterial().setRenderMode(Normals.PER_FACET);
    meshes.get(0).getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    return meshes.get(0);
  }
  
  public OctreeNode<Integer> createSceneOctree(ArrayList<CgNode> objects) {
    return new OctreeFactory<Integer>(new OctreeFactoryStrategyScene(objects)).create(7, 2);
  }
  
  public static void main(String[] args){
    ArrayList<CgNode> meshes = new ArrayList<CgNode>();
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    
    CgApplication app  = new OctreeTestFrame();
    for(int i = 0; i < rootNode.getNumChildren(); ++i){
      meshes.add(rootNode.getChildNode(i));
    }
    OctreeNode<Integer> octreeScene = ((OctreeTestFrame)app).createSceneOctree(meshes);
    rootNode.addChild(new CgNode(octreeScene, "octreeScene"));
    
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }

}
