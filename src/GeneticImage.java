import Genetic.GeneticObject;
import Geometry.Point;
import Geometry.Triangle;
import Picture.Image;
import Picture.TriangleImage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImage implements GeneticObject<TriangleImage> {
    static final int MAX_SIZE = 50;
    static final double SMALL_COEF = 0.3;
    static final int MUTATION_SIZE = 5;
    static final int CROSS_SIZE = 20;

    Random random;
    Image image;

    public GeneticImage(Image image) {
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

    private TriangleImage mutation_type_first(
            ArrayList<Triangle> triangles,
            ArrayList<Double> brightness,
            boolean[] index,
            TriangleImage obj) {
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
        return new TriangleImage(triangles, brightness);
    }

    private TriangleImage mutation_type_second(
            ArrayList<Triangle> triangles,
            ArrayList<Double> brightness,
            boolean[] index,
            TriangleImage obj) {
        for (int i = 0; i < MAX_SIZE; i++) {
            Triangle triangle = new Triangle(obj.getTriangle(i));
            double bright = obj.getBrightness(i);
            if (index[i]) {
                bright += (random.nextDouble() - 0.5) * SMALL_COEF;
            }
            triangles.add(triangle);
            brightness.add(bright);
        }
        return new TriangleImage(triangles, brightness);
    }

    @Override
    public TriangleImage mutation(TriangleImage obj) {
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
        return new TriangleImage(triangles, brightness);
    }

    @Override
    public double eval(TriangleImage obj) {
        return Image.distance(
                obj.getImage(image.getH(), image.getW()),
                image
        );
    }

    @Override
    public TriangleImage crossover(TriangleImage a, TriangleImage b) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Double> brightness = new ArrayList<>();

        boolean[] index = new boolean[MAX_SIZE];
        for (int i = 0; i < CROSS_SIZE; i++) {
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
        return new TriangleImage(triangles, brightness);
    }

    @Override
    public TriangleImage genRand() {
        ArrayList<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < MAX_SIZE; i++) {
            triangles.add(Triangle.getRand(random));
        }
        return new TriangleImage(triangles);
    }
}
