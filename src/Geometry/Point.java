package Geometry;

import java.util.Random;

/**
 * Created by Stas on 06.06.2017.
 */
public class Point {
    static double EPS = 1e-3;

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public Point(Point b) {
        this.x = b.x;
        this.y = b.y;
    }

    public static double dotProduct(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double crossProduct(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    public static Point sub(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public static Point mul(Point a, double t) {
        return new Point(a.x * t, a.y * t);
    }

    public static Point center(Point a, Point b, double t) {
        return new Point(a.x * t + b.x * (1 - t), a.y * t + b.y * (1 - t));
    }

    public static double distance(Point a, Point b) {
        double x = a.x - b.x;
        double y = a.y - b.y;
        return Math.sqrt(x * x + y * y);
    }

    public static double area(Point a, Point b, Point c) {
        Point sa = Point.sub(a, b);
        Point sc = Point.sub(c, b);
        return Math.abs(crossProduct(sc, sa));
    }

    public static boolean isZero(double c) {
        return Math.abs(c) < EPS;
    }

    public static Point getRand(Random random) {
        return new Point(random.nextDouble(), random.nextDouble());
    }
}
