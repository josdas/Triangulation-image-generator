package Geometry;

/**
 * Created by Stas on 06.06.2017.
 */
public class Point {
    static double EPS = 1e-3;
    
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    static double dotProduct(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    static double crossProduct(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    static Point sub(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    static double area(Point a, Point b, Point c) {
        Point sa = Point.sub(a, b);
        Point sc = Point.sub(c, b);
        return Math.abs(crossProduct(sc, sa));
    }

    static boolean isZero(double c) {
        return Math.abs(c) < EPS;
    }
}
