package cgresearch.studentprojects.autogenerator;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

public class Butterfly {

	private Body body;
	private LeftTopWing leftTopWing;
	private LeftBottomWing leftBottomWing;
	private RightTopWing rightTopWing;
	private RightBottomWing rightBottomWing;

	public Butterfly(double bh, double bl, int wingProzent, double wingMitteBreite, double twh, double twl, double bwh,
			double bwl) {

		double wings = 0;

		if (wingProzent != 50) {
			wings = bh * (wingProzent - 50) / 100;
		}

		this.leftBottomWing = generateLeftBottomWing(bl, wings, bwh, bwl, wingMitteBreite);
		this.leftTopWing = generateLeftTopWing(bl, wings, twh, twl, leftBottomWing.getB(), wingMitteBreite);
		this.rightBottomWing = generateRightBottomWing(bl, wings, bwh, bwl, wingMitteBreite);
		this.rightTopWing = generateRightTopWing(bl, wings, twh, twl, rightBottomWing.getC(), wingMitteBreite);
		this.body = generateBody(bh, bl, leftTopWing.getD(), rightTopWing.getA(), wingMitteBreite);
	}

	public Body generateBody(double bh, double bl, IVector3 topLeft, IVector3 topRight, double wingMitteBreite) {

		IVector3 c = VectorMatrixFactory.newIVector3(0, 0, 0);

		Body erg = new Body(c, bh, bl, topLeft, topRight, wingMitteBreite);

		return erg;
	}

	public LeftTopWing generateLeftTopWing(double bl, double wings, double twh, double twl, IVector3 tp,
			double wingMitteBreite) {

		double x = 0 - bl / 2 - twl / 2;

		double y = 0 + wings + twh / 2;

		IVector3 c = VectorMatrixFactory.newIVector3(x, y, 0);

		LeftTopWing erg = new LeftTopWing(c, twh, twl, tp, wingMitteBreite);

		return erg;
	}

	public LeftBottomWing generateLeftBottomWing(double bl, double wings, double bwh, double bwl,
			double wingMitteBreite) {

		double x = 0 - bl / 2 - bwl / 2;

		double y = 0 + wings - bwh / 2;

		IVector3 c = VectorMatrixFactory.newIVector3(x, y, 0);

		LeftBottomWing erg = new LeftBottomWing(c, bwh, bwl, wingMitteBreite);

		return erg;
	}

	public RightTopWing generateRightTopWing(double bl, double wings, double twh, double twl, IVector3 tp,
			double wingMitteBreite) {

		double x = 0 + bl / 2 + twl / 2;

		double y = 0 + wings + twh / 2;

		IVector3 c = VectorMatrixFactory.newIVector3(x, y, 0);

		RightTopWing erg = new RightTopWing(c, twh, twl, tp, wingMitteBreite);

		return erg;
	}

	public RightBottomWing generateRightBottomWing(double bl, double wings, double bwh, double bwl,
			double wingMitteBreite) {

		double x = 0 + bl / 2 + bwl / 2;

		double y = 0 + wings - bwh / 2;

		IVector3 c = VectorMatrixFactory.newIVector3(x, y, 0);

		RightBottomWing erg = new RightBottomWing(c, bwh, bwl, wingMitteBreite);

		return erg;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public LeftTopWing getLeftTopWing() {
		return leftTopWing;
	}

	public void setLeftTopWing(LeftTopWing leftTopWing) {
		this.leftTopWing = leftTopWing;
	}

	public LeftBottomWing getLeftBottomWing() {
		return leftBottomWing;
	}

	public void setLeftBottomWing(LeftBottomWing leftBottomWing) {
		this.leftBottomWing = leftBottomWing;
	}

	public RightTopWing getRightTopWing() {
		return rightTopWing;
	}

	public void setRightTopWing(RightTopWing rightTopWing) {
		this.rightTopWing = rightTopWing;
	}

	public RightBottomWing getRightBottomWing() {
		return rightBottomWing;
	}

	public void setRightBottomWing(RightBottomWing rightBottomWing) {
		this.rightBottomWing = rightBottomWing;
	}

}
