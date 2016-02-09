/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.camerapath;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.fileio.PlyFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.Transformation;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

/**
 * Camera path in a virtual scene.
 * 
 * @author Philipp Jenke
 * 
 */
public class CameraPathFrame extends CgApplication {

  /**
   * Constructor.
   */
  public CameraPathFrame() {
    AnimationTimer timer = AnimationTimer.getInstance();
    timer.setMaxValue(200);
    timer.startTimer(100);
    createScene();
  }

  /**
   * Create a dummy scene.
   */
  private void createScene() {
    String[] filenames = { "meshes/bunny.obj", "meshes/cow.obj", "meshes/teddy.obj", "meshes/big_porsche.ply",
        "meshes/apple.ply", "meshes/dragon_vrip_res4.ply" };
    List<ITriangleMesh> meshes = new ArrayList<ITriangleMesh>();
    ObjFileReader objReader = new ObjFileReader();
    PlyFileReader plyReader = new PlyFileReader();
    int RESOLUTION = 7;
    for (String modelFilename : filenames) {
      ITriangleMesh mesh = null;
      if (modelFilename.toUpperCase().endsWith("OBJ")) {
        mesh = objReader.readFile(modelFilename).get(0);
      } else if (modelFilename.toUpperCase().endsWith("PLY")) {
        mesh = plyReader.readFile(modelFilename);
      }
      mesh.fitToUnitBox();
      TriangleMeshTransformation.scale(mesh, 1.0f / (float) RESOLUTION);
      meshes.add(mesh);
    }

    for (int x = 0; x < RESOLUTION; x++) {
      float xPos = (float) x / RESOLUTION - 0.5f;
      for (int z = 0; z < RESOLUTION; z++) {
        float zPos = (float) z / RESOLUTION - 0.5f;
        // Select random node
        ITriangleMesh mesh = new TriangleMesh(meshes.get((int) (Math.random() * meshes.size())));
        mesh.getMaterial()
            .setReflectionDiffuse(VectorMatrixFactory.newIVector3(Math.random(), Math.random(), Math.random()));
        mesh.getMaterial().setRenderMode(Material.Normals.PER_VERTEX);

        mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
        TriangleMeshTransformation.multiply(mesh, VectorMatrixFactory
            .getRotationMatrix(VectorMatrixFactory.newIVector3(0, 1, 0), Math.random() * 2 * Math.PI));
        CgNode node = new CgNode(mesh, "mesh (" + x + ", " + z + ")");
        Transformation transformation = new Transformation();
        CgNode transformationNode = new CgNode(transformation, "transformation");
        transformation.addTranslation(VectorMatrixFactory.newIVector3(xPos, 0, zPos));
        transformationNode.addChild(node);
        getCgRootNode().addChild(transformationNode);
      }
    }

  }

  /**
   * Save an image as JPEG file.
   */
  public void saveImageAsJpeg(BufferedImage image, String filename) {
    try {
      FileOutputStream fos = new FileOutputStream(new File(filename));
      JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
      ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
      imageWriter.setOutput(ios);

      // and metadata
      IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);

      // new Compression
      JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
      jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
      jpegParams.setCompressionQuality(0.5f);

      imageWriter.write(imageMetaData, new IIOImage(image, null, null), null);
      ios.close();
      imageWriter.dispose();
    } catch (IOException e) {
      Logger.getInstance().error("Failed to write JPEG image " + filename);
    }
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(new CameraPathFrame());
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}
