package Models;

import Genetic.GeneticObject;
import Geometry.Point;
import Picture.AbsImage;
import Picture.ImageWB;
import Picture.Triangular.Triangle.TrianColorWBDepth;
import Picture.Triangular.WB.TrianImgDepth;

import java.util.ArrayList;


/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelD extends ImageModel implements GeneticObject<TrianImgDepth> {
    public GeneticImageModelD(AbsImage image) {
        super(image);
    }

    private double norm(double t) {
        t = Math.min(t, 1);
        t = Math.max(t, 0);
        return t;
    }

    private void mutationFirst(
            ArrayList<TrianColorWBDepth> triangles,
            TrianImgDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorWBDepth(obj.getTriangle(i)));
        }
        int numberMutations = random.nextInt(MUTATION_SIZE);

        double prob = random.nextDouble();

        for (int i = 0; i < numberMutations; i++) {
            boolean temp = prob > random.nextDouble();
            if (triangles.size() > 0 && temp) {
                triangles.remove(random.nextInt(triangles.size()));
            }
            if (triangles.size() == 0 || (triangles.size() + 1 < MAX_SIZE && !temp)) {
                triangles.add(TrianColorWBDepth.getRand(random));
            }
        }
    }

    private void mutationSecond(
            ArrayList<TrianColorWBDepth> triangles,
            boolean[] index,
            TrianImgDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorWBDepth(obj.getTriangle(i)));
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
            ArrayList<TrianColorWBDepth> triangles,
            boolean[] index,
            TrianImgDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TrianColorWBDepth triangle = new TrianColorWBDepth(obj.getTriangle(i));
            if (index[i]) {
                triangle.a = smallChange(triangle.a, MUTATION_COEF);
                triangle.b = smallChange(triangle.b, MUTATION_COEF);
                triangle.c = smallChange(triangle.c, MUTATION_COEF);
            }
            triangles.add(triangle);
        }
    }

    TrianImgDepth cloneImage(TrianImgDepth a) {
        ArrayList<TrianColorWBDepth> triangles = new ArrayList<>();
        for (TrianColorWBDepth triangle : a.getTriangles()) {
            triangles.add(new TrianColorWBDepth(triangle));
        }
        return new TrianImgDepth(triangles);
    }

    @Override
    public TrianImgDepth mutation(TrianImgDepth obj) {
        ArrayList<TrianColorWBDepth> triangles = new ArrayList<>();
        int type = random.nextInt(3);

        if (type == 3 && random.nextDouble() > 0.3) {
            type = random.nextInt(2);
        }

        if (obj.size() == 0 || type == 0) {
            mutationFirst(triangles, obj);
        } else if(type == 3) {
            TrianImgDepth temp = cloneImage(obj);
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
        return new TrianImgDepth(triangles);
    }

    public void autoColor(TrianImgDepth a) {
        final int h = image.getH();
        final int w = image.getW();
        ArrayList<TrianColorWBDepth> triangles = a.getTriangles();
        double[] sumColor = new double[triangles.size()];
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
                    sumColor[id] += image.getColor(i, j)[0] / 255.0;
                    numberPixels[id]++;
                }
            }
        }
        double prob = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if(random.nextDouble() < prob && numberPixels[i] > 0) {
                triangles.get(i).setColor(sumColor[i] / numberPixels[i]);
            }
        }
    }

    @Override
    public double eval(TrianImgDepth obj) {
        if (evalStore.containsKey(obj.getNumber())) {
            return evalStore.get(obj.getNumber());
        }
        double s = 0;
        double result = ImageWB.distance(
                obj.getImage(image.getH(), image.getW()),
                image
        );
        for (int i = 0; i < obj.size(); i++) {
            double t = obj.getTriangle(i).area();
            if (t < 0.003) {
                result += 10;
            }
            s += t;
        }
        evalStore.put(obj.getNumber(), result);
        return result;
    }

    @Override
    public TrianImgDepth crossover(TrianImgDepth a, TrianImgDepth b) {
        ArrayList<TrianColorWBDepth> triangles = new ArrayList<>();
        double k = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < k) {
                triangles.add(new TrianColorWBDepth(a.getTriangle(i)));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (random.nextDouble() >= k) {
                triangles.add(new TrianColorWBDepth(b.getTriangle(i)));
            }
        }
        while (triangles.size() > MAX_SIZE) {
            int i = random.nextInt(triangles.size());
            triangles.remove(i);
        }
        return new TrianImgDepth(triangles);
    }

    @Override
    public TrianImgDepth genRand() {
        ArrayList<TrianColorWBDepth> triangles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            triangles.add(TrianColorWBDepth.getRand(random));
        }
        return new TrianImgDepth(triangles);
    }
}
