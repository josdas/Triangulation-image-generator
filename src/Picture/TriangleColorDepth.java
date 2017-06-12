package Picture;

import Geometry.Triangle;

import java.util.Random;

/**
 * Created by Stas on 11.06.2017.
 */
public class TriangleColorDepth extends Triangle {
    public TriangleColorDepth(Triangle b, double depth, double color) {
        super(b);
        this.depth = depth;
        this.color = color;
    }

    public double getDepth() {
        return depth;
    }

    public double getColor() {
        return color;
    }

    double depth;
    double color;

    public TriangleColorDepth(TriangleColorDepth a) {
        super(a);
        this.color = a.color;
        this.depth = a.depth;
    }

    public static TriangleColorDepth getRand(Random random) {
        return new TriangleColorDepth(
                Triangle.getRand(random),
                random.nextDouble(),
                random.nextDouble()
        );
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setColor(double color) {
        this.color = color;
    }
}
