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
    static final int MAX_SIZE = 20;
    static final double SMALL_COEF = 0.2;
    static final int MUTATION_SIZE = 5;
    static final int CROSS_SIZE = 5;

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

    @Override
    public TriangleImage mutation(TriangleImage obj) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        boolean[] index = new boolean[MAX_SIZE];
        for (int i = 0; i < MUTATION_SIZE; i++) {
            index[random.nextInt(MAX_SIZE)] = true;
        }
        for (int i = 0; i < MAX_SIZE; i++) {
            Triangle triangle = new Triangle(obj.get(i));
            if (index[i]) {
                triangle.a = smallChange(triangle.a, SMALL_COEF);
                triangle.b = smallChange(triangle.b, SMALL_COEF);
                triangle.c = smallChange(triangle.c, SMALL_COEF);
            }
            triangles.add(triangle);
        }
        return new TriangleImage(triangles);
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
        boolean[] index = new boolean[MAX_SIZE];
        for (int i = 0; i < CROSS_SIZE; i++) {
            index[random.nextInt(MAX_SIZE)] = true;
        }
        for (int i = 0; i < MAX_SIZE; i++) {
            Triangle triangle;
            if(index[i]) {
                double k = random.nextDouble();
                triangle = new Triangle(
                        Point.center(a.get(i).a, b.get(i).a, k),
                        Point.center(a.get(i).b, b.get(i).b, k),
                        Point.center(a.get(i).c, b.get(i).c, k)
                );
            }
            else {
                triangle = new Triangle(a.get(i));
            }
            triangles.add(triangle);
        }
        return new TriangleImage(triangles);
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
