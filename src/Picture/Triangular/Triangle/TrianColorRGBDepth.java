package Picture.Triangular.Triangle;

import Geometry.Triangle;

import java.util.Random;

/**
 * Created by Stas on 11.06.2017.
 */
public class TrianColorRGBDepth extends Triangle {
    double depth;
    double[] color;

    public TrianColorRGBDepth(Triangle b, double depth, double[] color) {
        super(b);
        this.depth = depth;
        this.color = color;
    }

    public TrianColorRGBDepth(TrianColorRGBDepth a) {
        super(a);
        this.color = a.color;
        this.depth = a.depth;
    }

    public double getDepth() {
        return depth;
    }

    public double[] getColor() {
        return color;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setColor(double[] color) {
        this.color = color;
    }

    public static TrianColorRGBDepth getRand(Random random) {
        double[] rgb = new double[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextDouble();
        }
        return new TrianColorRGBDepth(
                Triangle.getRand(random),
                random.nextDouble(),
                rgb
        );
    }

}
