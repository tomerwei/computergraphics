package cgresearch.studentprojects.scanner.scanner;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.fileio.AsciiPointFormat;
import cgresearch.graphics.fileio.AsciiPointsReader;
import cgresearch.graphics.fileio.AsciiPointsWriter;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.studentprojects.scanner.calibration.ScannerMeasurement;
import cgresearch.studentprojects.scanner.calibration.ScannerRegistrationInformation;
import cgresearch.studentprojects.scanner.laser.ILaser;
import cgresearch.studentprojects.scanner.laser.LaserOWTC1;
import cgresearch.studentprojects.scanner.laser.SimulatorLaser;
import cgresearch.studentprojects.scanner.motors.IStack;
import cgresearch.studentprojects.scanner.motors.SimulatorStack;
import cgresearch.studentprojects.scanner.motors.StepperMotor;
import cgresearch.studentprojects.scanner.motors.TinkerforgeStack;

public class Scanner {
	
	private static final float MIN_DISTANCE = 0.25f;
	private static final float RESOLUTION = 0.001f;		//Resolution in m
	
	private static Scanner scanner;
	
	private ILaser laser;
	private IStack tinkerforgeStack;

	private int rotCounter;
	private int objectHeight;			//in mm
	
	private boolean debugMode;
	private int pointsPerLine;
	private int angle;
	private IPointCloud lastPointCloud;
	private float currentDistance;
	private double currentRotation;
	
	
	private List<MeasuringItem> measuring;
	private ScannerRegistrationInformation scannerRegistrationInformation;
	
	/**
	 * The measuring Item is used for debug printing of the real data from scanner
	 * before the calibration runs.
	 * @author ailab
	 *
	 */
	class MeasuringItem {
		private float distance;		//Distance in mmm
		private float rotationDeg;	//Rotation in deg
		private float shift;			//Shift in mm
		
		public MeasuringItem(float distance, float rotationDeg, float shift) {
			this.distance = distance;
			this.rotationDeg = rotationDeg;
			this.shift = shift;
		}
		
		public String toString() {
			return distance + " " + rotationDeg + " " + shift;
		}
	}
	
	/**
	 * initialize the new instance of the Scanner
	 * 
	 * @param portName the serial port name of the scanner port
	 * @param debugMode if true the application uses the simulator classes and working without hardware
	 */
	private Scanner(String portName, int pointsPerLine, int angle, boolean debugMode) {
		this.debugMode = debugMode;
		this.pointsPerLine = pointsPerLine;
		this.angle = angle;
		this.measuring = new ArrayList<Scanner.MeasuringItem>();
		this.objectHeight = 50;
		laser = (debugMode ? new SimulatorLaser() : new LaserOWTC1(portName));
		tinkerforgeStack = (debugMode ? new SimulatorStack() : new TinkerforgeStack());
		tinkerforgeStack.connect();
		
		//Setting Up the Motos
		tinkerforgeStack.getMotor1().setup();
		tinkerforgeStack.getMotor2().setup();
		
		rotCounter = 0;
		
		//New ScannerRegistrationInformation for calibrating the point
		scannerRegistrationInformation = new ScannerRegistrationInformation();
	}
	
	public static Scanner getInstance(String portName, int pointsPerLine, int angle) {
		if(scanner == null) {
			scanner = new Scanner(portName, pointsPerLine, angle, false);
		}
		return scanner;
	}
	
	public void setPort(String port) {
		laser.setPort(port);
	}
	
	public IStack getIStack() {
		return tinkerforgeStack;
	}
	
	/**
	 * moves the scanner up for testing purpose
	 */
	public void fireTest() {
		tinkerforgeStack.getMotor1().enable();
		
		tinkerforgeStack.getMotor1().moveBackward();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tinkerforgeStack.getMotor1().stop();
		
		tinkerforgeStack.getMotor1().disable();
	}
	
	/**
	 * Let the scanner set a zero point.
	 */
	public void initStartPosition() {
		if(tinkerforgeStack.isConnected()) {
			tinkerforgeStack.getMotor1().disable();
			tinkerforgeStack.getMotor2().disable();
		}
		if(laser.isConnected() && tinkerforgeStack.isConnected()) {
			tinkerforgeStack.getMotor1().enable();
			
			//reset laser first
			laser.resetDistance();
			
			laser.startScan();
			
			if(laser.waitForDistance() < MIN_DISTANCE) {
				tinkerforgeStack.getMotor1().setStartPosition();
				System.out.println("Startpunkt ist schon erreicht (" + laser.getDistance() + ")"); 
			}else {
				
				tinkerforgeStack.getMotor1().moveForward();
				while(laser.getDistance() >= MIN_DISTANCE) {
					
					System.out.println(laser.getDistance() + "<" + MIN_DISTANCE);
					if(laser.getDistance() < MIN_DISTANCE) {
						tinkerforgeStack.getMotor1().setStartPosition();
						tinkerforgeStack.getMotor1().stop();
						
						System.out.println("Startpunkt gefunden (" + laser.getDistance() + ")"); 
					}
				}
			}
			
			laser.endScan();
			tinkerforgeStack.getMotor1().disable();
		}
	}
	
	/**
	 * uses a number which can be devided with 360. 1� = 12,5 Steps
	 * 
	 * @param deg
	 * @return
	 */
	private int calculateAngle(int deg) {
		//10� = 125 Steps
		return (int) ((deg * 12.5f)/2);
	}
	
	private int calculateSteps(float meter) {
		return (int) ((meter*100)*StepperMotor.STEPS_FOR_CM);
	}
	
	/**
	 * activate the laser without scanning
	 */
	public void laserOn() {
		laser.laserOn();
	}
	
	/**
	 * shut the laser without scanning off
	 */
	public void laserOff() {
		laser.laserOff();
	}
	
	public void saveLastPointCloud(String filename) {
		//Save the measuring data in another file
		float center = 0.2554f;
		try {
			PrintWriter measureingWriter = new PrintWriter("measuring.txt", "UTF-8");
			measureingWriter.println("# Measuring Scanner Data");
			measureingWriter.println("# ");
			measureingWriter.println("# Format Information:");
			measureingWriter.println("# <distance> <rotation> <shift>");
			measureingWriter.println("# Distance to Center: " + center);
			measureingWriter.println("");
			for(MeasuringItem item : measuring) {
				measureingWriter.println(item.toString());
			}
			measureingWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Save real Point Cloud
		AsciiPointFormat format = new AsciiPointFormat().setPosition(0, 1, 2).setNormal(3, 4, 5).setSeparator("; �");
		AsciiPointsWriter writer = new AsciiPointsWriter();
		writer.writeToFile(filename, lastPointCloud, format);
	}
	
	public void loadLastPointCloud(CgNode root, String filename) {
		AsciiPointFormat format = new AsciiPointFormat().setPosition(0, 1, 2).setNormal(3, 4, 5).setSeparator("; �");
		AsciiPointsReader reader = new AsciiPointsReader();
		IPointCloud pointCloud = reader.readFromFile(filename, format);
		CgNode loadedPointCloud = new CgNode(pointCloud, filename);
		root.setVisible(false);
		root.addChild(loadedPointCloud);
		root.setVisible(true);
	}
	
	/**
	 * start an complete scan
	 * 
	 * @param cloud the point cloud created by the scan
	 */
	public void startScan(IPointCloud cloud, int objectHeight, int angle) {
		this.objectHeight = (int) (objectHeight/(RESOLUTION*1000));
		this.angle = angle;
		Logger.getInstance().message("Start the 3D scan");
		if(laser.isConnected()) {
			laser.startScan();
			tinkerforgeStack.getMotor1().enable();
			tinkerforgeStack.getMotor2().enable();
			
			rotCounter = 0;
			int degSteps = calculateAngle(this.angle);
			for(int i = 0; i < 360/this.angle ; i++) { //~4500 Steps = 360� - 450 * 10 Steps / 225 = 18�
				if(this.pointsPerLine > 1) {
					scanLineUpContinues(cloud);
					tinkerforgeStack.getMotor2().moveStepsForward(-degSteps);
					tinkerforgeStack.getMotor2().waitForStepper();
					rotCounter+=(this.angle/2);
					scanLineDownContinues(cloud);
					tinkerforgeStack.getMotor2().moveStepsForward(-degSteps);
					tinkerforgeStack.getMotor2().waitForStepper();
					rotCounter+=(this.angle/2);
				}else{
					scanPoint(cloud, 0);
					tinkerforgeStack.getMotor2().moveStepsForward(-degSteps);
					tinkerforgeStack.getMotor2().waitForStepper();
					rotCounter+=(this.angle/2);
					scanPoint(cloud, 0);
					tinkerforgeStack.getMotor2().moveStepsForward(-degSteps);
					tinkerforgeStack.getMotor2().waitForStepper();
					rotCounter+=(this.angle/2);
				}
			}
			
			tinkerforgeStack.getMotor2().disable();
			tinkerforgeStack.getMotor1().disable();
			laser.endScan();
			lastPointCloud = cloud;
			Logger.getInstance().message("Finished 3D scan");
		}else{
			Logger.getInstance().error("Can't connecto to Laser");
		}
	}
	
	public void moveHeight(float mm) {
		if(!tinkerforgeStack.isConnected()) {
			Logger.getInstance().error("Tinkerforge Stack is not connected");
		}else{
			tinkerforgeStack.getMotor1().enable();
			tinkerforgeStack.getMotor1().moveCm(mm/10);
			tinkerforgeStack.getMotor1().waitForStepper();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tinkerforgeStack.getMotor1().disable();
		}
	}
	
	public void rotateAngle(int deg) {
		if(!tinkerforgeStack.isConnected()) {
			Logger.getInstance().error("Tinkerforge Stack is not connected");
		}else{
			tinkerforgeStack.getMotor2().enable();
			tinkerforgeStack.getMotor2().moveStepsForward(calculateAngle(deg));
			tinkerforgeStack.getMotor2().waitForStepper();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tinkerforgeStack.getMotor2().disable();
		}
	}
	
	/**
	 * scans one point and add them to the pointcloud
	 * 
	 * @param cloud the cloud created by the scan
	 * @param vertical number of point get
	 */
	private void scanPoint(IPointCloud cloud, int vertical)  {
		currentDistance = laser.getRawDistance();
		currentRotation = Math.toRadians(rotCounter);
		
		//Scanning measuring data
		measuring.add(new MeasuringItem(currentDistance, rotCounter, RESOLUTION*vertical));

		//Scanning the real point
		Point point;
		Vector pointVector = scannerRegistrationInformation.convert2WorldCoordinates(new ScannerMeasurement(currentDistance, currentRotation, RESOLUTION*vertical));
		point = new Point(pointVector);
		cloud.addPoint(point);
		cloud.updateRenderStructures();
		
		System.out.println("Scanning Point " + cloud.getNumberOfPoints());
		System.out.println("Adding Point at:" + point.getPosition().toString());
	}
	
	public void scanLineUpContinues(IPointCloud cloud) {
		int steps = -calculateSteps(objectHeight*RESOLUTION);
		int lastStep = Integer.MIN_VALUE;
		int stepCounter = 0;
		int currentStep = Integer.MIN_VALUE;
		int targetStep = tinkerforgeStack.getMotor1().getStepCounter() + steps;
		tinkerforgeStack.getMotor1().moveStepsForward(steps);
		System.out.println("INFO--");
		System.out.println(steps + "/" + targetStep);
		while(tinkerforgeStack.getMotor1().getStepCounter() != targetStep){
			currentStep = tinkerforgeStack.getMotor1().getStepCounter();
			if(currentStep%calculateSteps(RESOLUTION) == 0 && currentStep != lastStep) {
				lastStep = currentStep;
				scanPoint(cloud, stepCounter);
				stepCounter++;
			}
		}
	}
	
	public void scanLineDownContinues(IPointCloud cloud) {
		int steps = calculateSteps(objectHeight*RESOLUTION);
		int lastStep = Integer.MIN_VALUE;
		int stepCounter = objectHeight;
		int currentStep = Integer.MIN_VALUE;
		int targetStep = tinkerforgeStack.getMotor1().getStepCounter() + steps;
		tinkerforgeStack.getMotor1().moveStepsForward(steps);
		while(tinkerforgeStack.getMotor1().getStepCounter() != targetStep){
			currentStep = tinkerforgeStack.getMotor1().getStepCounter();
			if(currentStep%calculateSteps(RESOLUTION) == 0 && currentStep != lastStep) {
				lastStep = currentStep;
				scanPoint(cloud, stepCounter);
				stepCounter--;
			}
		}
	}
	
	/**
	 * scan one line up
	 * 
	 * @param cloud the cloud created by the scan
	 */
	public void scanLineUp(IPointCloud cloud) {	
		
		for(int i = 0; i < pointsPerLine; i++) {
			scanPoint(cloud, i);
			
			tinkerforgeStack.getMotor1().moveStepsForward(-250);
			
			if(debugMode) {
				tinkerforgeStack.getMotor1().waitForStepper();
			}else{
				while(tinkerforgeStack.getMotor1().getStepCounter() > (-(i+1)*250)){
					//waiting for the hardware - active sleep :(
				}
			}
		}
	}
	
	/**
	 * scan one line down
	 * 
	 * @param cloud the cloud created by the scan
	 */
	public void scanLineDown(IPointCloud cloud) {
			for(int i = pointsPerLine-1; i >= 0; i--) {
				scanPoint(cloud, i);
				
				tinkerforgeStack.getMotor1().moveStepsForward(250);
				
				if(debugMode) {
					tinkerforgeStack.getMotor1().waitForStepper();
				}else{
					while(tinkerforgeStack.getMotor1().getStepCounter() < -(i*250)){
						//wating for the hardware - active sleep :(
					}
				}
			}
	}
}
