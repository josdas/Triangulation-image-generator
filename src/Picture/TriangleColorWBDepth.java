package Picture;

import Geometry.Triangle;

import java.util.Random;

/**
 * Created by Stas on 11.06.2017.
 */
public class TriangleColorWBDepth extends Triangle {
    public TriangleColorWBDepth(Triangle b, double depth, double color) {
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

    public TriangleColorWBDepth(TriangleColorWBDepth a) {
        super(a);
        this.color = a.color;
        this.depth = a.depth;
    }

    public static TriangleColorWBDepth getRand(Random random) {
        return new TriangleColorWBDepth(
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
