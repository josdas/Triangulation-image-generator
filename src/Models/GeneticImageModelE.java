package Models;

import Genetic.GeneticObject;
import Geometry.Point;
import Picture.AbsImage;
import Picture.Image;
import Picture.TriangleColorRGBDepth;
import Picture.TriangleImageRGBDepth;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;


/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelE implements GeneticObject<TriangleImageRGBDepth> {
    public static int MAX_SIZE = 20;
    static final int MUTATION_SIZE = 10;
    public static double MUTATION_COEF = 0.2;

    Random random;
    AbsImage image;
    private TreeMap<Integer, Double> evalStore;

    public GeneticImageModelE(AbsImage image) {
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

    private double[] smallChange(double[] a, double t) {
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = norm(random.nextDouble() * t + a[i]);
        }
        return res;
    }

    private void mutationFirst(
            ArrayList<TriangleColorRGBDepth> triangles,
            TriangleImageRGBDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TriangleColorRGBDepth(obj.getTriangle(i)));
        }
        int numberMutations = random.nextInt(MUTATION_SIZE);

        double prob = random.nextDouble();

        for (int i = 0; i < numberMutations; i++) {
            boolean temp = prob > random.nextDouble();
            if (triangles.size() > 0 && temp) {
                triangles.remove(random.nextInt(triangles.size()));
            }
            if (triangles.size() == 0 || (triangles.size() + 1 < MAX_SIZE && !temp)) {
                triangles.add(TriangleColorRGBDepth.getRand(random));
            }
        }
    }

    private void mutationSecond(
            ArrayList<TriangleColorRGBDepth> triangles,
            boolean[] index,
            TriangleImageRGBDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TriangleColorRGBDepth(obj.getTriangle(i)));
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
            ArrayList<TriangleColorRGBDepth> triangles,
            boolean[] index,
            TriangleImageRGBDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TriangleColorRGBDepth triangle = new TriangleColorRGBDepth(obj.getTriangle(i));
            if (index[i]) {
                triangle.a = smallChange(triangle.a, MUTATION_COEF);
                triangle.b = smallChange(triangle.b, MUTATION_COEF);
                triangle.c = smallChange(triangle.c, MUTATION_COEF);
            }
            triangles.add(triangle);
        }
    }

    TriangleImageRGBDepth cloneImage(TriangleImageRGBDepth a) {
        ArrayList<TriangleColorRGBDepth> triangles = new ArrayList<>();
        for (TriangleColorRGBDepth triangle : a.getTriangles()) {
            triangles.add(new TriangleColorRGBDepth(triangle));
        }
        return new TriangleImageRGBDepth(triangles);
    }

    @Override
    public TriangleImageRGBDepth mutation(TriangleImageRGBDepth obj) {
        ArrayList<TriangleColorRGBDepth> triangles = new ArrayList<>();
        int type = random.nextInt(4);

        if (obj.size() == 0 || type == 0) {
            mutationFirst(triangles, obj);
        } else if(type == 3) {
            TriangleImageRGBDepth temp = cloneImage(obj);
            autoColor(temp);
            triangles = temp.getTriangles();
        } else {
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
        return new TriangleImageRGBDepth(triangles);
    }

    public void autoColor(TriangleImageRGBDepth a) {
        final int h = image.getH();
        final int w = image.getW();
        ArrayList<TriangleColorRGBDepth> triangles = a.getTriangles();
        double[][] sumColor = new double[triangles.size()][3];
        int[] numberPixels = new  int[triangles.size()];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int id = -1;
                for (int k = 0; k < triangles.size(); k++) {
                    Point temp = new Point((double)i / h, (double)j / w);
                    double depth = Double.NEGATIVE_INFINITY;
                    if (triangles.get(k).inside(temp)) {
                        if (depth < triangles.get(k).getDepth()) {
                            id = k;
                        }
                    }
                }
                if (id >= 0) {
                    int[] temp = image.get_colors(i, j);
                    for (int k = 0; k < 3; k++) {
                        sumColor[id][k] += temp[k] / 255.0;
                    }
                    numberPixels[id]++;
                }
            }
        }
        double prob = Math.max(random.nextDouble(), random.nextDouble());
        for (int i = 0; i < a.size(); i++) {
            if(random.nextDouble() < prob && numberPixels[i] > 0) {
                double[] rgb = new double[3];
                for (int k = 0; k < 3; k++) {
                    rgb[k] = sumColor[i][k] / numberPixels[i];
                }
                triangles.get(i).setColor(rgb);
            }
        }
    }

    @Override
    public double eval(TriangleImageRGBDepth obj) {
        if (evalStore.containsKey(obj.getNumber())) {
            return evalStore.get(obj.getNumber());
        }
        double s = 0;
        Image tempImage = obj.getImage(image.getH(), image.getW());
        double result = AbsImage.distance(
                tempImage,
                image
        );
        for (int i = 0; i < obj.size(); i++) {
            double t = obj.getTriangle(i).area();
            if (t < 0.002) {
                result += 20;
            }
            s += t;
        }
        evalStore.put(obj.getNumber(), result);
        return result;
    }

    @Override
    public TriangleImageRGBDepth crossover(TriangleImageRGBDepth a, TriangleImageRGBDepth b) {
        ArrayList<TriangleColorRGBDepth> triangles = new ArrayList<>();
        double k = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < k) {
                triangles.add(new TriangleColorRGBDepth(a.getTriangle(i)));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (random.nextDouble() >= k) {
                triangles.add(new TriangleColorRGBDepth(b.getTriangle(i)));
            }
        }
        while (triangles.size() > MAX_SIZE) {
            int i = random.nextInt(triangles.size());
            triangles.remove(i);
        }
        return new TriangleImageRGBDepth(triangles);
    }

    @Override
    public void clean(TriangleImageRGBDepth a) {
    }

    @Override
    public TriangleImageRGBDepth genRand() {
        ArrayList<TriangleColorRGBDepth> triangles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            triangles.add(TriangleColorRGBDepth.getRand(random));
        }
        return new TriangleImageRGBDepth(triangles);
    }
}
