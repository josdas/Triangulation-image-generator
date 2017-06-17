package Picture.Triangular.Triangle;

import Geometry.Triangle;

import java.util.Random;

/**
 * Created by Stas on 11.06.2017.
 */
public class TrianColorRGBDepthTrans extends TrianColorRGBDepth {
    double trans;

    public TrianColorRGBDepthTrans(TrianColorRGBDepth a, double trans) {
        super(a);
        this.trans = trans;
    }

    public TrianColorRGBDepthTrans(TrianColorRGBDepthTrans a) {
        super(a);
        this.trans = a.getTrans();
    }

    public TrianColorRGBDepthTrans(Triangle b, double depth, double[] color, double trans) {
        super(b, depth, color);
        this.trans = trans;
    }

    public double getTrans() {
        return trans;
    }

    public void setTrans(double trans) {
        this.trans = trans;
    }

    public static TrianColorRGBDepthTrans getRand(Random random) {
        return new TrianColorRGBDepthTrans(
                TrianColorRGBDepth.getRand(random),
                random.nextDouble()
        );
    }
}
