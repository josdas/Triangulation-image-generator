package Models;

import Geometry.Point;
import Picture.AbsImage;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Stas on 14.06.2017.
 */
public class ImageModel {
    public int MAX_SIZE = 20;
    public int MUTATION_SIZE = 10;
    public double MUTATION_COEF = 0.2;

    Random random;
    AbsImage image;
    HashMap<Integer, Double> evalStore;

    public ImageModel(AbsImage image) {
        random = new Random();
        this.image = image;
        evalStore = new HashMap<>();
    }

    private double norm(double t) {
        t = Math.min(t, 1);
        t = Math.max(t, 0);
        return t;
    }

    protected Point smallChange(Point a, double t) {
        Point c = Point.mul(Point.getRand(random), t);
        c = Point.add(a, c);
        c.x = norm(c.x);
        c.y = norm(c.y);
        return c;
    }

    protected double smallChange(double a, double t) {
        double c = random.nextDouble() * t;
        return norm(c + a);
    }

    protected double[] smallChange(double[] a, double t) {
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = norm(random.nextDouble() * t + a[i]);
        }
        return res;
    }
}
