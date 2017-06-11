import Genetic.GeneticObject;
import Geometry.Point;
import Picture.Image;
import Picture.TriangleCD;
import Picture.TriangleImageDepth;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelC implements GeneticObject<TriangleImageDepth> {
    public static int MAX_SIZE = 30;
    static final int MUTATION_SIZE = 5;
    static final double MUTATION_COEF = 0.3;

    Random random;
    Image image;
    private TreeMap<Integer, Double> evalStore;

    public GeneticImageModelC(Image image) {
        this.image = image;
        this.random = new Random();
        this.evalStore = new TreeMap<>();
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

    private double smallChange(double a, double t) {
        double c = random.nextDouble() * t;
        return norm(c + a);
    }

    private void mutationFirst(
            ArrayList<TriangleCD> triangles,
            TriangleImageDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TriangleCD(obj.getTriangle(i)));
        }
        int numberMutations = random.nextInt(MUTATION_SIZE);
        for (int i = 0; i < numberMutations; i++) {
            boolean temp = random.nextBoolean();
            if (triangles.size() > 0 && temp) {
                triangles.remove(random.nextInt(triangles.size()));
            }
            if (triangles.size() + 1 < MAX_SIZE && !temp) {
                triangles.add(TriangleCD.getRand(random));
            }
        }
    }

    private void mutationSecond(
            ArrayList<TriangleCD> triangles,
            boolean[] index,
            TriangleImageDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TriangleCD(obj.getTriangle(i)));
            if (index[i]) {
                if (random.nextBoolean()) {
                    triangles.get(i).setDepth(
                            random.nextDouble()
                    );
                } else {
                    triangles.get(i).setColor(
                            smallChange(triangles.get(i).getColor(), MUTATION_COEF)
                    );
                }
            }
        }
    }

    private void mutationThird(
            ArrayList<TriangleCD> triangles,
            boolean[] index,
            TriangleImageDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TriangleCD triangle = new TriangleCD(obj.getTriangle(i));
            if (index[i]) {
                triangle.a = smallChange(triangle.a, MUTATION_COEF);
                triangle.b = smallChange(triangle.b, MUTATION_COEF);
                triangle.c = smallChange(triangle.c, MUTATION_COEF);
            }
            triangles.add(triangle);
        }
    }

    @Override
    public TriangleImageDepth mutation(TriangleImageDepth obj) {
        ArrayList<TriangleCD> triangles = new ArrayList<>();
        final int type = random.nextInt(3);

        if(obj.size() == 0 || type == 0) {
            mutationFirst(triangles, obj);
        }
        else {
            int change = random.nextInt(MUTATION_SIZE) + 1;
            boolean[] index = new boolean[obj.size()];
            for (int i = 0; i < change; i++) {
                index[random.nextInt(obj.size())] = true;
            }
            switch (type) {
                case 1:
                    mutationThird(triangles, index, obj);
                    break;
                case 2:
                    mutationSecond(triangles, index, obj);
                    break;
            }
        }
        return new TriangleImageDepth(triangles);
    }


    @Override
    public double eval(TriangleImageDepth obj) {
        if (evalStore.containsKey(obj.getNumber())) {
            return evalStore.get(obj.getNumber());
        }
        double s = 0;
        double result = Image.distance(
                obj.getImage(image.getH(), image.getW()),
                image
        );
        for (int i = 0; i < obj.size(); i++) {
            double t = obj.getTriangle(i).area();
            s += t;
        }
        //result += s * 20;
        evalStore.put(obj.getNumber(), result);
        return result;
    }

    @Override
    public TriangleImageDepth crossover(TriangleImageDepth a, TriangleImageDepth b) {
        ArrayList<TriangleCD> triangles = new ArrayList<>();
        double k = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < k) {
                triangles.add(new TriangleCD(a.getTriangle(i)));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (random.nextDouble() >= k) {
                triangles.add(new TriangleCD(b.getTriangle(i)));
            }
        }
        while (triangles.size() > MAX_SIZE) {
            int i = random.nextInt(triangles.size());
            triangles.remove(i);
        }
        return new TriangleImageDepth(triangles);
    }

    @Override
    public TriangleImageDepth genRand() {
        ArrayList<TriangleCD> triangles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            triangles.add(TriangleCD.getRand(random));
        }
        return new TriangleImageDepth(triangles);
    }

    @Override
    public double distance(TriangleImageDepth a, TriangleImageDepth b) {
        return 10;
    }
}