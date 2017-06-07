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
public class GeneticImageModelB implements GeneticObject<TriangleImage> {
    static final int MAX_SIZE = 150;
    static final int MUTATION_SIZE = 10;
    static final double MUTATION_COEF = 0.5;

    Random random;
    Image image;

    public GeneticImageModelB(Image image) {
        this.image = image;
        this.random = new Random();
    }

    private void mutationFirst(
            ArrayList<Triangle> triangles,
            ArrayList<Double> brightness,
            boolean[] index,
            TriangleImage obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            if(!index[i]) {
                triangles.add(new Triangle(obj.getTriangle(i)));
                brightness.add(obj.getBrightness(i));
            }
        }
        for (int i = 0; i < obj.size(); i++) {
            if(index[i]) {
                if(triangles.size() + 1 < MAX_SIZE && random.nextBoolean()) {
                    triangles.add(Triangle.getRand(random));
                    brightness.add(random.nextDouble());
                }
            }
        }
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

    private void mutationSecond(
            ArrayList<Triangle> triangles,
            ArrayList<Double> brightness,
            boolean[] index,
            TriangleImage obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            if(index[i]) {
                brightness.add(norm(obj.getBrightness(i) + (random.nextDouble() * 2 - 1) * MUTATION_COEF));
            }
            else {
                brightness.add(obj.getBrightness(i));
            }
            triangles.add(new Triangle(obj.getTriangle(i)));
        }
    }

    private void mutationThird(
            ArrayList<Triangle> triangles,
            ArrayList<Double> brightness,
            boolean[] index,
            TriangleImage obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            Triangle temp = new Triangle(obj.getTriangle(i));
            if(index[i]) {
                temp.a = smallChange(temp.a, MUTATION_COEF);
                temp.b = smallChange(temp.b, MUTATION_COEF);
                temp.c = smallChange(temp.c, MUTATION_COEF);
            }
            brightness.add(obj.getBrightness(i));
            triangles.add(temp);
        }
    }
    @Override
    public TriangleImage mutation(TriangleImage obj) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Double> brightness = new ArrayList<>();

        int change = random.nextInt(MUTATION_SIZE) + 1;
        boolean[] index = new boolean[obj.size()];
        for (int i = 0; i < change; i++) {
            index[random.nextInt(obj.size())] = true;
        }
        switch (random.nextInt(3)) {
            case 0:
                mutationFirst(triangles, brightness, index, obj);
                break;
            case 1:
                mutationSecond(triangles, brightness, index, obj);
                break;
            case 2:
                mutationThird(triangles, brightness, index, obj);
                break;
        }
        return new TriangleImage(triangles, brightness);
    }

    @Override
    public double eval(TriangleImage obj) {
        double s = 0;
        double result = Image.distance(
                obj.getImage(image.getH(), image.getW()),
                image
        );
        for (int i = 0; i < obj.size(); i++) {
            double t = obj.getTriangle(i).area();
            s += t;
        }
        return result;
    }

    @Override
    public TriangleImage crossover(TriangleImage a, TriangleImage b) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Double> brightness = new ArrayList<>();

        double k = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if(random.nextDouble() < k) {
                triangles.add(new Triangle(a.getTriangle(i)));
                brightness.add(a.getBrightness(i));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if(random.nextDouble() >= k) {
                triangles.add(new Triangle(b.getTriangle(i)));
                brightness.add(b.getBrightness(i));
            }
        }
        while(triangles.size() > MAX_SIZE) {
            int i = random.nextInt(triangles.size());
            triangles.remove(i);
            brightness.remove(i);
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

    @Override
    public double distance(TriangleImage a, TriangleImage b) {
        return 10;
    }
}
