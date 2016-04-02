package cgresearch.studentprojects.registration;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.apps.trianglemeshes.ObjTriangleMesh;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger.VerboseMode;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.points.PointCloudFactory;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;
import cgresearch.graphics.datastructures.points.TriangleMeshSampler;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;
import cgresearch.studentprojects.shapegrammar.gui.menu.BuilderMenu;

/**
 * Initial frame for the registration project (J�ckel)
 *
 */
public class RegistrationFrame extends CgApplication {

  public static IPointCloud Base = new PointCloud();
  public static IPointCloud Register = new PointCloud();
  public static IPointCloud basePointCloud = new PointCloud();
  public static IPointCloud registerPointCloud = new PointCloud();

  /**
   * Constructor
   */
  public RegistrationFrame() {
    // Testing code: load a triangle mesh and display it
    // ObjFileReader reader = new ObjFileReader();
    // List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
    // getCgRootNode().addChild(new CgNode(meshes.get(0), "mesh"));

    // Old version: Jäckel, few points
    // loadIPointCloud();

    // New version: Cubes (JNK)
    loadTestData();
  }
  
	public int[] nearestPoints(IPointCloud Base){
		
		int[] nearestPoints = new int[Base.getNumberOfPoints()];
		PointNeighborsQuery nearest = new PointNeighborsQuery(Base);
		
		for(int k = 0; k < Base.getNumberOfPoints(); k++){
			
			nearest.queryKnn(Base.getPoint(k).getPosition(), 1);
				
			
			nearestPoints[k] = nearest.getNeigbor(0);
			System.out.println("Platz in der RegisterCloud:" + nearestPoints[k] );
			
			System.out.println("Punkt: " + Base.getPoint(nearestPoints[k]).getPosition());
			
		}
		return nearestPoints;
	}
	

  private void loadTestData() {
	  int x =0, y =1, z=2;
	  
	 
	  Vector translation = VectorFactory.createVector3(1, 1, 1);

    // Load cube from file
	  ObjFileReader reader = new ObjFileReader();
		 ITriangleMesh cubeMesh = reader.readFile("meshes/skull.obj").get(0);
    // Created point cloud from cube
    basePointCloud = TriangleMeshSampler.sample(cubeMesh, 3000);
    basePointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
    // Set point color
    for (int i = 0; i < basePointCloud.getNumberOfPoints(); i++) {
      basePointCloud.getPoint(i).getColor().copy(Material.PALETTE2_COLOR0);
    }

    double maxY = 0;
    double maxYOld = 0;
    double minY = 0;
    double minYOld=0;
    for(int k = 0; k < basePointCloud.getNumberOfPoints()-1; k++){
    	minY = basePointCloud.getPoint(k).getPosition().get(y);
    	maxY = basePointCloud.getPoint(k).getPosition().get(y);
    	if(maxY > maxYOld)
    		maxYOld = basePointCloud.getPoint(k).getPosition().get(y);
    	if(minY < minYOld)
    		minYOld = basePointCloud.getPoint(k).getPosition().get(y);
    	
    	
    	}
    
    double middleBase = ((maxYOld - minYOld)/2) + minYOld;
    
    for(int i = 0; i < basePointCloud.getNumberOfPoints(); i++){
    	if(middleBase < basePointCloud.getPoint(i).getPosition().get(y))
    		Base.addPoint(basePointCloud.getPoint(i));
    }
    
//    this.greateSurface(basePointCloud);
    
    System.out.println("maxY: "+maxYOld+" minY: \n"+minYOld);
        
 
    // Transform mesh for second cube
    // Rotation of the second point cloud: 10 degrees in degrees - transformed
    // to radiens. Rotation axis: (1,1,1)
    double rotationAngle = 10 * Math.PI / 180;
    TriangleMeshTransformation.transform(cubeMesh, MatrixFactory.createRotationMatrix(VectorFactory.createVector3(1, 1, 1), rotationAngle));
    // Optional: translation
    TriangleMeshTransformation.translate(cubeMesh, translation);
    registerPointCloud = TriangleMeshSampler.sample(cubeMesh, 1000);
    registerPointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
    // Set point color
    for (int i = 0; i < registerPointCloud.getNumberOfPoints(); i++) {
      registerPointCloud.getPoint(i).getColor().copy(Material.PALETTE1_COLOR3);
    }
    double registermaxY = 0;
    double registermaxYOld = 0;
    double registerminY;
    double registerminYOld = registerPointCloud.getPoint(0).getPosition().get(y);;
    
    for(int k = 0; k < registerPointCloud.getNumberOfPoints(); k++){
    	registerminY = registerPointCloud.getPoint(k).getPosition().get(y);
    	registermaxY = registerPointCloud.getPoint(k).getPosition().get(y);
    	if(registermaxY > registermaxYOld)
    		registermaxYOld = registerPointCloud.getPoint(k).getPosition().get(y);
    	if(registerminY < registerminYOld)
    		registerminYOld = registerPointCloud.getPoint(k).getPosition().get(y);
    	
    	
    	}
    System.out.println("maxY: "+registermaxYOld+" minY: \n"+registerminYOld);
    double middleRegister = ((registermaxYOld - registerminYOld)/2) + registerminYOld;
    
    for(int i = 0; i < registerPointCloud.getNumberOfPoints(); i++){
    	if(middleRegister > registerPointCloud.getPoint(i).getPosition().get(y))
    		Register.addPoint(registerPointCloud.getPoint(i));
    }
    
    System.out.println("middle: "+middleRegister);
    
//    CgNode basePointCloudNode = new CgNode(Base, "pointCloud");
//    getCgRootNode().addChild(basePointCloudNode);
//    CgNode registerPointCloudNode = new CgNode(Register, "pointCloud2");
//    getCgRootNode().addChild(registerPointCloudNode);

  }
  
  public void greateSurface(IPointCloud test){
	  CgNode basePointCloudNode = new CgNode(test, "pointCloud");
		 getCgRootNode().addChild(basePointCloudNode);
//		 test.updateRenderStructures();
  }


  public static void main(String[] args) {
    // ResourcesLocator.getInstance().parseIniFile("resources.ini");
    // RegistrationFrame app = new RegistrationFrame();
    // JoglFrame frame = new JoglFrame(app);
    // new JoglSwingUserInterface(app, frame);
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    RegistrationFrame app = new RegistrationFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
//    RegistrationButton button = new RegistrationButton();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    // appLauncher.addCustomMenu(new RegistrationMenu());
    appLauncher.addCustomUi(new RegistrationButton(app.getCgRootNode()));

    // CgNode pointCloudNode = new CgNode(pointCloud, "point cloud");
    // getCgRootNode().addChild(pointCloudNode);

  }

}
