package Picture.Triangular.Triangle;

import Geometry.Triangle;

import java.util.Random;

/**
 * Created by Stas on 11.06.2017.
 */
public class TrianColorRGBTrans extends Triangle {
    double trans;
    double[] color;

    public TrianColorRGBTrans(Triangle b, double trans, double[] color) {
        super(b);
        this.trans = trans;
        this.color = color;
    }

    public TrianColorRGBTrans(TrianColorRGBTrans a) {
        super(a);
        this.color = a.color;
        this.trans = a.trans;
    }

    public double getTrans() {
        return trans;
    }

    public double[] getColor() {
        return color;
    }

    public void setTrans(double depth) {
        this.trans = trans;
    }

    public void setColor(double[] color) {
        this.color = color;
    }

    public static TrianColorRGBTrans getRand(Random random, double minArea, double maxArea) {
        double[] rgb = new double[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextDouble();
        }
        Triangle triangle = Triangle.getRand(random);
        while (triangle.area() > maxArea || triangle.area() < minArea) {
            triangle = Triangle.getRand(random);
        }
        return new TrianColorRGBTrans(
                triangle,
                random.nextDouble(),
                rgb
        );
    }

    public static TrianColorRGBTrans getRand(Random random) {
        return getRand(random, 0, Double.POSITIVE_INFINITY);
    }
}
