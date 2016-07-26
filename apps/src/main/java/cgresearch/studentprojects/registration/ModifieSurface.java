package cgresearch.studentprojects.registration;

import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Noise;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.TriangleMeshSampler;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class ModifieSurface extends CgApplication{

	/**
	 * TODO: UPdate Dreate usw überarbeiten, damit das richtig funzt
	 */

	private final CgRootNode rootNode;
	CgNode basePointCloudNode;
	public IPointCloud pointCloud;

	public ModifieSurface(CgRootNode rootNode){
		this.rootNode = rootNode;

	}

	public void create(IPointCloud pointCloud){
		this.pointCloud = pointCloud;
		basePointCloudNode = new CgNode(pointCloud, "pointCloud");
		rootNode.addChild(basePointCloudNode);


	}
	
	public void deleteNode(IPointCloud pointCloud){
		basePointCloudNode.deleteNode();
		rootNode.update(rootNode, getCgRootNode());

	}
	
	public void changeColor(String color){
		pointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
		if(color.equals("green")){
			for (int i = 0; i < pointCloud.getNumberOfPoints(); i++) {
				pointCloud.getPoint(i).getColor().copy(Material.PALETTE1_COLOR3);
			}
		}
		if(color.equals("red")){
			for (int i = 0; i < pointCloud.getNumberOfPoints(); i++) {
				pointCloud.getPoint(i).getColor().copy(Material.PALETTE2_COLOR1);
			}
		}
		
	}

	public void update(){
		pointCloud.updateRenderStructures();

	}
	
	public void addNoise(double degree){
		Noise noise = new Noise();
		noise.addNoise(pointCloud, degree);
		this.update();
		
	}
	
	public void addOutliers(double outliers){
		int x=0,y=1,z = 2;
		double[] rndPoint = new double[3];
		
		for(int k = 0; k < outliers; k++){
			for(int i = 0; i< 3; i++){
				rndPoint[i] = (double) (Math.random()*(0.5-(-0.5))+(-0.5)); // Wert muss getestet werden, ob wirklich 2 geht oder shcon zu weit ist		

			}
			Vector vec = new Vector(rndPoint[x], rndPoint[y], rndPoint[z]);			
			Point point = new Point(vec);
			System.out.println("outliers: "+point.getPosition());
			pointCloud.addPoint(point);

		}
		this.changeColor("red");
		this.update();
	
	}
	
	public void addRotation(double rotation, ITriangleMesh cubeMeshData){
		double rotationAngle = rotation * Math.PI / 180;
		TriangleMeshTransformation.transform(cubeMeshData, MatrixFactory.createRotationMatrix(VectorFactory.createVector3(1, 1, 1).getNormalized(), rotationAngle));
		pointCloud = TriangleMeshSampler.sample(cubeMeshData, pointCloud.getNumberOfPoints());
		this.deleteNode(pointCloud);
		this.create(pointCloud);
		this.changeColor("red");
	}
	
	public void addTranslation(double directionX, double directionY, double directionZ, ITriangleMesh cubeMeshData){
		Vector translation = VectorFactory.createVector3(directionX, directionY, directionZ);
		TriangleMeshTransformation.translate(cubeMeshData, translation);
		pointCloud = TriangleMeshSampler.sample(cubeMeshData, pointCloud.getNumberOfPoints());
		this.deleteNode(pointCloud);
		this.create(pointCloud);
		this.changeColor("red");		
	}

	


}
