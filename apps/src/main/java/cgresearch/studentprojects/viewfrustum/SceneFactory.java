package cgresearch.studentprojects.viewfrustum;

import java.util.List;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;

public class SceneFactory extends CgApplication {
    
    public SceneFactory(){
        
      ITriangleMesh cow = loadMesh("meshes/cow.obj");
      ITriangleMesh bunny = loadMesh("meshes/bunny.obj");
      ITriangleMesh fenja = loadMesh("meshes/fenja02.obj");
      ITriangleMesh fenjaDown = loadMesh("meshes/fenja02.obj");
      ITriangleMesh fenjaUp = loadMesh("meshes/fenja02.obj");
      ITriangleMesh pumpkin = loadMesh("meshes/pumpkin.obj");
      
      // ############### Transformation ###############
      TriangleMeshTransformation.translate(cow,
              VectorMatrixFactory.newVector(1.0, 0.0, -9.0));
//            VectorMatrixFactory.newVector(0.0, 0.0, -9.0));
      TriangleMeshTransformation.translate(bunny,
              VectorMatrixFactory.newVector(0.0, 1.15, -9.0));
      TriangleMeshTransformation.scale(bunny, 3.0);
      TriangleMeshTransformation.scale(fenja, 0.1);
      TriangleMeshTransformation.translate(fenja,
              VectorMatrixFactory.newVector(0.5, -1.0, -8.0));
      TriangleMeshTransformation.scale(fenjaDown, 0.1);
      TriangleMeshTransformation.translate(fenjaDown,
              VectorMatrixFactory.newVector(2.0, -8.0, -8.0));
      TriangleMeshTransformation.scale(fenjaUp, 0.1);
      TriangleMeshTransformation.translate(fenjaUp,
              VectorMatrixFactory.newVector(-1.0, 2.0, -0.5));
      TriangleMeshTransformation.scale(pumpkin, 0.02);
      TriangleMeshTransformation.translate(pumpkin,
              VectorMatrixFactory.newVector(0.0, 0.0, -20.5));
   // ############### Transformation ###############
        
      getCgRootNode().setUseBlending(true);
      getCgRootNode().addChild(new CgNode(cow, "cow"));
      getCgRootNode().addChild(new CgNode(bunny, "bunny"));
      getCgRootNode().addChild(new CgNode(fenja, "fenja"));
      getCgRootNode().addChild(new CgNode(fenjaDown, "fenjaDown"));
      getCgRootNode().addChild(new CgNode(fenjaUp, "fenjaUp"));
      getCgRootNode().addChild(new CgNode(pumpkin, "pumpkin"));
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
      meshes.get(0).getMaterial().setRenderMode(Normals.PER_FACET);
      meshes.get(0).getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      return meshes.get(0);
    }

}
