package Picture;

import Geometry.Triangle;

import java.util.Random;

/**
 * Created by Stas on 11.06.2017.
 */
public class TriangleColorRGBDepth extends Triangle {
    public TriangleColorRGBDepth(Triangle b, double depth, double[] color) {
        super(b);
        this.depth = depth;
        this.color = color;
    }

    public double getDepth() {
        return depth;
    }

    public double[] getColor() {
        return color;
    }

    double depth;
    double[] color;

    public TriangleColorRGBDepth(TriangleColorRGBDepth a) {
        super(a);
        this.color = a.color;
        this.depth = a.depth;
    }

    public static TriangleColorRGBDepth getRand(Random random) {
        double[] rgb = new double[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextDouble();
        }
        return new TriangleColorRGBDepth(
                Triangle.getRand(random),
                random.nextDouble(),
                rgb
        );
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setColor(double[] color) {
        this.color = color;
    }
}
