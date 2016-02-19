package cgresearch.apps.hlsvis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cgresearch.apps.hlsvis.hls.City;
import cgresearch.apps.hlsvis.hls.HlsConstants;
import cgresearch.apps.hlsvis.hls.TransportEvent;
import cgresearch.apps.hlsvis.hls.TransportNetwork;
import cgresearch.apps.hlsvis.hls.TransportOrder;
import cgresearch.apps.hlsvis.rabbitmq.RabbitMqCommunication;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.ICgNodeContent;
import cgresearch.graphics.scenegraph.Transformation;

public class Package extends Movable {

	public enum Vehicle {
		TRUCK, AIRPLANE
	};

	/**
	 * Transport order
	 */
	private TransportOrder order;

	/**
	 * True, when the package has reached its destination
	 */
	private boolean destinationReached = false;

	/**
	 * Queue to send package events to
	 */
	private RabbitMqCommunication rabbitMqQueue;

	/**
	 * Temp vars to avoid instanciation
	 */
	private Vector v1 = VectorMatrixFactory.newVector(3),
			v2 = VectorMatrixFactory.newVector(3);
	private double[] vd = new double[] { 0, 0 };

	private Vehicle vehicle;

	/**
	 * Remember last time an on-way event was generated.
	 */
	private Date lastOnWayEvent;

	/**
	 * Default constructor
	 */
	public Package(Vehicle vehicle) {
		super();
		this.vehicle = vehicle;

		if (vehicle == Vehicle.TRUCK) {
			createPackageGeometry();
		} else if (vehicle == Vehicle.AIRPLANE) {
			createAirplaneGeometry();
		}
	}

	/**
	 * Create gometry for a package
	 */
	protected void createPackageGeometry() {
		double packageSize = 0.013;
		ObjFileReader reader = new ObjFileReader();
		List<ITriangleMesh> node = reader.readFile(ResourcesLocator
				.getInstance().getPathToResource("meshes/cube.obj"));
		ICgNodeContent content = node.get(0);
		ITriangleMesh packageMesh = (ITriangleMesh) content;
		packageMesh.getMaterial().setTextureId(DHL_TEXTURE_ID);
		packageMesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
		packageMesh.getMaterial().setRenderMode(Normals.PER_FACET);
		TriangleMeshTransformation.scale(packageMesh, VectorMatrixFactory
				.newVector(packageSize, packageSize, packageSize));
		TriangleMeshTransformation.translate(packageMesh,
				VectorMatrixFactory.newVector(0, packageSize / 2, 0));
		CgNode sphereNode = new CgNode(packageMesh, "mesh");
		addChild(sphereNode);
	}

	/**
	 * Create gometry for a package
	 */
	protected void createAirplaneGeometry() {
		double packageSize = 0.05;
		ObjFileReader reader = new ObjFileReader();
		List<ITriangleMesh> meshes = reader.readFile(ResourcesLocator
				.getInstance()
				.getPathToResource("meshes/quad45fg/quad45fg.obj"));
		CgNode node = new CgNode(null, "plane");
		for (ITriangleMesh mesh : meshes) {
			node.addChild(new CgNode(mesh, "mesh"));
		}

		// CgNode transformationNode = new

		// ICgNodeContent content = node.getChildNode(0).getContent();
		// ITriangleMesh packageMesh = (ITriangleMesh) content;
		// //packageMesh.getMaterial().setTextureId(DHL_TEXTURE_ID);
		// packageMesh.getMaterial().setShaderId(Material.SHADER_TEXTURE_PHONG);
		// packageMesh.getMaterial().setRenderMode(Normals.PER_FACET);
		// TriangleMeshTransformation.scale(packageMesh, VectorMatrixFactory
		// .newVector(packageSize, packageSize, packageSize));
		// TriangleMeshTransformation.translate(packageMesh,
		// VectorMatrixFactory.newVector(0, packageSize / 2, 0));
		// CgNode sphereNode = new CgNode(packageMesh, "mesh");

		BoundingBox bb = node.getBoundingBox();
		double scale = 1.0 / bb.getMaxExtend();
		Vector translation = bb.getCenter().multiply(-1);
		Matrix R = VectorMatrixFactory.getRotationMatrix(
				VectorMatrixFactory.newVector(0, 1, 0), -Math.PI / 2.0);
		for (int i = 0; i < node.getNumChildren(); i++) {
			ICgNodeContent content = node.getChildNode(i).getContent();
			ITriangleMesh packageMesh = (ITriangleMesh) content;
			TriangleMeshTransformation.translate(packageMesh, translation);
			TriangleMeshTransformation.scale(
					packageMesh,
					VectorMatrixFactory.newVector(scale * packageSize, scale
							* packageSize, scale * packageSize));
			TriangleMeshTransformation.multiply(packageMesh, R);
		}
		addChild(node);
	}

	/**
	 * Construktor.
	 */
	public void init(TransportOrder order, HeightField heightField,
			RabbitMqCommunication rabbitMqQueue) {
		setName("Package " + order.getDeliveryNumber());
		setHeightField(heightField);
		this.order = order;
		this.rabbitMqQueue = rabbitMqQueue;
		destinationReached = false;

		// Setup path
		path.clear();
		City startCity = TransportNetwork.getCity(order.getStartLocation());
		City destinationCity = TransportNetwork.getCity(order
				.getTargetLocation());
		path.add(VectorMatrixFactory.newVector(startCity.getCoords()[0], 0,
				startCity.getCoords()[1]));
		path.add(VectorMatrixFactory.newVector(
				destinationCity.getCoords()[0], 0,
				destinationCity.getCoords()[1]));

		// Set the orientation
		Matrix rotation = getOrientation();
		getTransformation().addTransformation(rotation);

		int dauer = (int) ((order.getDeliveryTime().getTime() - order
				.getStartTime().getTime()) / (1000 * 60));
		Logger.getInstance().debug(
				"New " + this + " created (" + dauer + " minutes).");
		destinationReached = false;

		lastOnWayEvent = new Date(order.getStartTime().getTime());

		// Event: Started
		TransportEvent event = new TransportEvent(order.getDeliveryNumber(),
				order.getOrderNumber(), order.getStartTime(),
				TransportEvent.EventType.ABGEFAHREN,
				getCoordinates(order.getStartTime()));
		rabbitMqQueue.sendMessage(event.toJson());
	}

	/**
	 * Setter.
	 */
	private void setHeightField(HeightField heightField) {
		this.heightField = heightField;
	}

	/**
	 * Compute the current coordinates on the map based on the time.
	 */
	private double[] getCoordinates(Date currentTime) {
		if (path.size() != 2) {
			Logger.getInstance()
					.error("Package path size must be of length 2!");
			destinationReached = true;
			return null;
		}

		if (currentTime.getTime() < order.getStartTime().getTime()
				|| currentTime.getTime() > order.getDeliveryTime().getTime()) {
			Logger.getInstance()
					.error("Invalid current time - must be within start and end time of the order.");

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String stringCurrentTime = formatter.format(currentTime);
			String stringStartTime = formatter.format(order.getStartTime());
			String stringDeliveryTime = formatter.format(order
					.getDeliveryTime());

			Logger.getInstance().debug(
					"currentTime: " + stringCurrentTime + ", startTimer: "
							+ stringStartTime + ", endTime: "
							+ stringDeliveryTime);

			destinationReached = true;
			return null;
		}

		double alpha = getPathFraction(currentTime);

		v1.copy(path.get(1));
		v1.multiplySelf(alpha);
		v2.copy(path.get(0));
		v2.multiplySelf(1.0 - alpha);
		vd[0] = v1.get(0) + v2.get(0);
		vd[1] = v1.get(2) + v2.get(2);
		return vd;
	}

	private double getPathFraction(Date currentTime) {
		double alpha = (float) (currentTime.getTime() - order.getStartTime()
				.getTime())
				/ (float) (order.getDeliveryTime().getTime() - order
						.getStartTime().getTime());
		return alpha;
	}

	@Override
	public void moveToNextPathPoint() {
		// Should not be used - package is done, when the destination is
		// reached.
	}

	@Override
	public boolean destinationReached() {
		return destinationReached;
	}

	@Override
	public String toString() {
		return "Package (" + order.getDeliveryNumber() + ") "
				+ order.getStartLocation() + " -> " + order.getTargetLocation();
	}

	@Override
	public void tick(Date currentDate) {

		if (currentDate.after(order.getDeliveryTime())) {
			// Event: destination reached
			TransportEvent event = new TransportEvent(
					order.getDeliveryNumber(), order.getOrderNumber(),
					order.getStartTime(), TransportEvent.EventType.ANGEKOMMEN,
					getCoordinates(order.getDeliveryTime()));
			rabbitMqQueue.sendMessage(event.toJson());

			Logger.getInstance().debug(this + " has reached its destination.");
			destinationReached = true;
		} else {
			// Event: on the way
			double[] coords = getCoordinates(currentDate);

			long timeToNexOrder = (lastOnWayEvent.getTime()
					+ HlsConstants.ON_WAY_EVENT_INTERVAL * 60 * 1000 - currentDate
						.getTime()) / (60 * 1000);

			if (coords != null && timeToNexOrder < 0) {
				TransportEvent event = new TransportEvent(
						order.getDeliveryNumber(), order.getOrderNumber(),
						order.getStartTime(),
						TransportEvent.EventType.UNTERWEGS, coords);
				rabbitMqQueue.sendMessage(event.toJson());
				lastOnWayEvent.setTime(currentDate.getTime());
			}

			updateTranslation(currentDate);
		}
	}

	/**
	 * Update the translation and rotation.
	 */
	protected void updateTranslation(Date currentTime) {
		Transformation transformation = getTransformation();
		transformation.reset();

		if (path.size() >= 2) {
			double[] coords = getCoordinates(currentTime);
			if (coords != null) {

				if (vehicle == Vehicle.TRUCK) {
					transformation
							.addTranslation(VectorMatrixFactory.newVector(
									coords[0],
									heightField.getHeight(coords[0], coords[1]),
									coords[1]));
				} else if (vehicle == Vehicle.AIRPLANE) {
					transformation.addTranslation(VectorMatrixFactory
							.newVector(
									coords[0],
									computePlaneHeight(
											getPathFraction(currentTime),
											coords), coords[1]));
				}
			}
		}
	}

	/**
	 * Compute height value for plane.
	 */
	private double computePlaneHeight(double alpha, double[] coords) {
		City startCity = TransportNetwork.getCity(order.getStartLocation());
		City destinationCity = TransportNetwork.getCity(order
				.getTargetLocation());
		double x = 2 * alpha - 1;
		return (1 - MathHelpers.sqr(x))
				* heightField.getMaxHeight()
				* 1.0
				+ (1 - alpha)
				* heightField.getHeight(startCity.getCoords()[0],
						startCity.getCoords()[1])
				+ alpha
				* (heightField.getHeight(destinationCity.getCoords()[0],
						destinationCity.getCoords()[1]));
	}
}
