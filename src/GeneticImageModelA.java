import Genetic.GeneticObject;
import Geometry.Point;
import Geometry.Triangle;
import Picture.Image;
import Picture.TriangleImageTrans;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelA implements GeneticObject<TriangleImageTrans> {
    static final int MAX_SIZE = 100;
    static final double SMALL_COEF = 0.5;
    static final int MUTATION_SIZE = 10;
    static final int CROSS_SIZE = 30;

    Random random;
    Image image;

    public GeneticImageModelA(Image image) {
        this.image = image;
        this.random = new Random();
    }

    private double norm(double t) {
        t = Math.min(t, 1);
        t = Math.max(t, 0);
        return t;
    }

    private Point smallChange(Point a, double t) {
        Point c = Point.mul(Point.getRand(random), t);
        c = Point.add(a, c);
        c.x = norm(c.x);
        c.y = norm(c.y);
        return c;
    }

    private TriangleImageTrans mutation_type_first(
            ArrayList<Triangle> triangles,
            ArrayList<Double> brightness,
            boolean[] index,
            TriangleImageTrans obj) {
        for (int i = 0; i < MAX_SIZE; i++) {
            Triangle triangle = new Triangle(obj.getTriangle(i));
            double bright = obj.getBrightness(i);
            if (index[i]) {
                triangle.a = smallChange(triangle.a, SMALL_COEF);
                triangle.b = smallChange(triangle.b, SMALL_COEF);
                triangle.c = smallChange(triangle.c, SMALL_COEF);
            }
            triangles.add(triangle);
            brightness.add(bright);
        }
        return new TriangleImageTrans(triangles, brightness);
    }

    private TriangleImageTrans mutation_type_second(
            ArrayList<Triangle> triangles,
            ArrayList<Double> brightness,
            boolean[] index,
            TriangleImageTrans obj) {
        for (int i = 0; i < MAX_SIZE; i++) {
            Triangle triangle = new Triangle(obj.getTriangle(i));
            double bright = obj.getBrightness(i);
            if (index[i]) {
                bright += (random.nextDouble() - 0.5) * SMALL_COEF;
                bright = norm(bright);
            }
            triangles.add(triangle);
            brightness.add(bright);
        }
        return new TriangleImageTrans(triangles, brightness);
    }

    @Override
    public TriangleImageTrans mutation(TriangleImageTrans obj) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Double> brightness = new ArrayList<>();

        int change = random.nextInt(MUTATION_SIZE) + 1;
        boolean[] index = new boolean[MAX_SIZE];
        for (int i = 0; i < change; i++) {
            index[random.nextInt(MAX_SIZE)] = true;
        }
        if(random.nextBoolean()) {
            mutation_type_first(triangles, brightness, index, obj);
        }
        else {
            mutation_type_second(triangles, brightness, index, obj);
        }
        return new TriangleImageTrans(triangles, brightness);
    }

    @Override
    public double eval(TriangleImageTrans obj) {
        return Image.distance(
                obj.getImage(image.getH(), image.getW()),
                image
        );
    }

    @Override
    public TriangleImageTrans crossover(TriangleImageTrans a, TriangleImageTrans b) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Double> brightness = new ArrayList<>();

        int change = random.nextInt(CROSS_SIZE) + 1;
        boolean[] index = new boolean[MAX_SIZE];
        for (int i = 0; i < change; i++) {
            index[random.nextInt(MAX_SIZE)] = true;
        }
        for (int i = 0; i < MAX_SIZE; i++) {
            Triangle triangle;
            double bright = 0;
            if(index[i]) {
                double k = random.nextDouble();
                triangle = new Triangle(
                        Point.center(a.getTriangle(i).a, b.getTriangle(i).a, k),
                        Point.center(a.getTriangle(i).b, b.getTriangle(i).b, k),
                        Point.center(a.getTriangle(i).c, b.getTriangle(i).c, k)
                );
                bright = k * a.getBrightness(i) + (1 - k) * b.getBrightness(i);
            }
            else {
                triangle = new Triangle(a.getTriangle(i));
                bright = a.getBrightness(i);
            }
            triangles.add(triangle);
            brightness.add(bright);
        }
        return new TriangleImageTrans(triangles, brightness);
    }

    @Override
    public TriangleImageTrans genRand() {
        ArrayList<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < MAX_SIZE; i++) {
            triangles.add(Triangle.getRand(random));
        }
        return new TriangleImageTrans(triangles);
    }

    @Override
    public void clean(TriangleImageTrans a) {

    }

    public double distance(TriangleImageTrans a, TriangleImageTrans b) {
        double result = 0;
        for (int i = 0; i < MAX_SIZE; i++) {
            result += Math.pow(a.getBrightness(i) - b.getBrightness(i), 2);
            result += Math.pow(Point.distance(a.getTriangle(i).a, b.getTriangle(i).a), 2);
            result += Math.pow(Point.distance(a.getTriangle(i).b, b.getTriangle(i).b), 2);
            result += Math.pow(Point.distance(a.getTriangle(i).c, b.getTriangle(i).c), 2);
        }
        return result;
    }
}
