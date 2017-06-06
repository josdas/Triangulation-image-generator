package Geometry;

import java.util.Random;

/**
 * Created by Stas on 06.06.2017.
 */
public class Triangle {
    public Point a, b, c;

    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle(Triangle b) {
        this.a = new Point(b.a);
        this.b = new Point(b.b);
        this.c = new Point(b.c);
    }

    public double area() {
        return Math.abs(0.5 * Point.area(a, b, c));
    }

    public boolean inside(Point v) {
        double s = Point.area(a, b, c);
        s -= Point.area(a, b, v);
        s -= Point.area(a, c, v);
        s -= Point.area(c, b, v);

        return Point.isZero(s);
    }

    public static Triangle getRand(Random random) {
        return new Triangle(
            Point.getRand(random),
            Point.getRand(random),
            Point.getRand(random)
        );
    }
}
