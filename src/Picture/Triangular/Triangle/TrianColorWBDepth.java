package Picture.Triangular.Triangle;

import Geometry.Triangle;

import java.util.Random;

/**
 * Created by Stas on 11.06.2017.
 */
public class TrianColorWBDepth extends Triangle {
    double depth;
    double color;

    public TrianColorWBDepth(Triangle b, double depth, double color) {
        super(b);
        this.depth = depth;
        this.color = color;
    }

    public TrianColorWBDepth(TrianColorWBDepth a) {
        super(a);
        this.color = a.color;
        this.depth = a.depth;
    }

    public double getDepth() {
        return depth;
    }

    public double getColor() {
        return color;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setColor(double color) {
        this.color = color;
    }

    public static TrianColorWBDepth getRand(Random random) {
        return new TrianColorWBDepth(
                Triangle.getRand(random),
                random.nextDouble(),
                random.nextDouble()
        );
    }
}
