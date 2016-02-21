package cgresearch.studentprojects.autogenerator;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

public class Butterfly {

	private Body body;
	private LeftTopWing leftTopWing;
	private LeftBottomWing leftBottomWing;
	private RightTopWing rightTopWing;
	private RightBottomWing rightBottomWing;

	public Butterfly(double bh, double bl, int wingProzent, double wingMitteBreite, double twh, double twl, double bwh,
			double bwl, double twth, double twtv, double twlrh, double twlrv, double twtph, double twtpv, double twbh,
			double twbv, double bwlrh, double bwlrv, double bwbh, double bwbv, double bwtph, double bwtpv, double bwb2h,
			double bwb2v, double bwlr2h, double bwlr2v) {

		double wings = 0;

		if (wingProzent != 50) {
			wings = bh * (wingProzent - 50) / 100;
		}

		this.leftBottomWing = generateLeftBottomWing(bl, wings, bwh, bwl, wingMitteBreite, bwlrh, bwlrv, bwbh, bwbv,
				bwtph, bwtpv, bwb2h, bwb2v, bwlr2h, bwlr2v);
		this.leftTopWing = generateLeftTopWing(bl, wings, twh, twl, leftBottomWing.getB(), wingMitteBreite, twth, twtv,
				twlrh, twlrv, twtph, twtpv, twbh, twbv);
		this.rightBottomWing = generateRightBottomWing(bl, wings, bwh, bwl, wingMitteBreite, bwlrh, bwlrv, bwbh, bwbv,
				bwtph, bwtpv, bwb2h, bwb2v, bwlr2h, bwlr2v);
		this.rightTopWing = generateRightTopWing(bl, wings, twh, twl, rightBottomWing.getC(), wingMitteBreite, twth,
				twtv, twlrh, twlrv, twtph, twtpv, twbh, twbv);
		this.body = generateBody(bh, bl, leftTopWing.getD(), rightTopWing.getA(), wingMitteBreite);
	}

	public Body generateBody(double bh, double bl, Vector topLeft, Vector topRight, double wingMitteBreite) {

		Vector c = VectorFactory.createVector3(0, 0, 0);

		Body erg = new Body(c, bh, bl, topLeft, topRight, wingMitteBreite);

		return erg;
	}

	public LeftTopWing generateLeftTopWing(double bl, double wings, double twh, double twl, Vector tp,
			double wingMitteBreite, double twth, double twtv, double twlrh, double twlrv, double twtph, double twtpv,
			double twbh, double twbv) {

		double x = 0 - bl / 2 - twl / 2;

		double y = 0 + wings + twh / 2;

		Vector c = VectorFactory.createVector3(x, y, 0);

		LeftTopWing erg = new LeftTopWing(c, twh, twl, tp, wingMitteBreite, twth, twtv, twlrh, twlrv, twtph, twtpv,
				twbh, twbv);

		return erg;
	}

	public LeftBottomWing generateLeftBottomWing(double bl, double wings, double bwh, double bwl,
			double wingMitteBreite, double bwlrh, double bwlrv, double bwbh, double bwbv, double bwtph, double bwtpv,
			double bwb2h, double bwb2v, double bwlr2h, double bwlr2v) {

		double x = 0 - bl / 2 - bwl / 2;

		double y = 0 + wings - bwh / 2;

		Vector c = VectorFactory.createVector3(x, y, 0);

		LeftBottomWing erg = new LeftBottomWing(c, bwh, bwl, wingMitteBreite, bwlrh, bwlrv, bwbh, bwbv, bwtph, bwtpv,
				bwb2h, bwb2v, bwlr2h, bwlr2v);

		return erg;
	}

	public RightTopWing generateRightTopWing(double bl, double wings, double twh, double twl, Vector tp,
			double wingMitteBreite, double twth, double twtv, double twlrh, double twlrv, double twtph, double twtpv,
			double twbh, double twbv) {

		double x = 0 + bl / 2 + twl / 2;

		double y = 0 + wings + twh / 2;

		Vector c = VectorFactory.createVector3(x, y, 0);

		RightTopWing erg = new RightTopWing(c, twh, twl, tp, wingMitteBreite, twth, twtv, twlrh, twlrv, twtph, twtpv,
				twbh, twbv);

		return erg;
	}

	public RightBottomWing generateRightBottomWing(double bl, double wings, double bwh, double bwl,
			double wingMitteBreite, double bwlrh, double bwlrv, double bwbh, double bwbv, double bwtph, double bwtpv,
			double bwb2h, double bwb2v, double bwlr2h, double bwlr2v) {

		double x = 0 + bl / 2 + bwl / 2;

		double y = 0 + wings - bwh / 2;

		Vector c = VectorFactory.createVector3(x, y, 0);

		RightBottomWing erg = new RightBottomWing(c, bwh, bwl, wingMitteBreite, bwlrh, bwlrv, bwbh, bwbv, bwtph, bwtpv,
				bwb2h, bwb2v, bwlr2h, bwlr2v);

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
