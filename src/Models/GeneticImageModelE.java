package Models;

import Genetic.GeneticObject;
import Geometry.Point;
import Picture.AbsImage;
import Picture.ImageRGB;
import Picture.Triangular.RGB.TrianImgRGBDepth;
import Picture.Triangular.Triangle.TrianColorRGBDepth;

import java.util.ArrayList;


/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelE extends ImageModel implements GeneticObject<TrianImgRGBDepth> {

    public GeneticImageModelE(AbsImage image) {
        super(image);
    }

    private void mutationFirst(
            ArrayList<TrianColorRGBDepth> triangles,
            TrianImgRGBDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorRGBDepth(obj.getTriangle(i)));
        }
        int numberMutations = random.nextInt(MUTATION_SIZE);

        double prob = random.nextDouble();

        for (int i = 0; i < numberMutations; i++) {
            boolean temp = prob > random.nextDouble();
            if (triangles.size() > 0 && temp) {
                triangles.remove(random.nextInt(triangles.size()));
            }
            if (triangles.size() == 0 || (triangles.size() + 1 < MAX_SIZE && !temp)) {
                triangles.add(TrianColorRGBDepth.getRand(random));
            }
        }
    }

    private void mutationSecond(
            ArrayList<TrianColorRGBDepth> triangles,
            boolean[] index,
            TrianImgRGBDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorRGBDepth(obj.getTriangle(i)));
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
            ArrayList<TrianColorRGBDepth> triangles,
            boolean[] index,
            TrianImgRGBDepth obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TrianColorRGBDepth triangle = new TrianColorRGBDepth(obj.getTriangle(i));
            if (index[i]) {
                triangle.a = smallChange(triangle.a, MUTATION_COEF);
                triangle.b = smallChange(triangle.b, MUTATION_COEF);
                triangle.c = smallChange(triangle.c, MUTATION_COEF);
            }
            triangles.add(triangle);
        }
    }

    TrianImgRGBDepth cloneImage(TrianImgRGBDepth a) {
        ArrayList<TrianColorRGBDepth> triangles = new ArrayList<>();
        for (TrianColorRGBDepth triangle : a.getTriangles()) {
            triangles.add(new TrianColorRGBDepth(triangle));
        }
        return new TrianImgRGBDepth(triangles);
    }

    @Override
    public TrianImgRGBDepth mutation(TrianImgRGBDepth obj) {
        ArrayList<TrianColorRGBDepth> triangles = new ArrayList<>();
        int type = random.nextInt(3);

        if(random.nextDouble() < 0.07) {
            type = 3;
        }

        if (obj.size() == 0 || type == 0) {
            mutationFirst(triangles, obj);
        } else if(type == 3) {
            TrianImgRGBDepth temp = cloneImage(obj);
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
        return new TrianImgRGBDepth(triangles);
    }

    public void autoColor(TrianImgRGBDepth a) {
        final int h = image.getH();
        final int w = image.getW();
        ArrayList<TrianColorRGBDepth> triangles = a.getTriangles();
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
                    int[] temp = image.getColor(i, j);
                    for (int k = 0; k < 3; k++) {
                        sumColor[id][k] += temp[k] / 255.0;
                    }
                    numberPixels[id]++;
                }
            }
        }
        double prob = Math.min(random.nextDouble(), random.nextDouble());
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
    public double eval(TrianImgRGBDepth obj) {
        if (evalStore.containsKey(obj.getNumber())) {
            return evalStore.get(obj.getNumber());
        }
        double s = 0;
        ImageRGB tempImage = obj.getImage(image.getH(), image.getW());
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
    public TrianImgRGBDepth crossover(TrianImgRGBDepth a, TrianImgRGBDepth b) {
        ArrayList<TrianColorRGBDepth> triangles = new ArrayList<>();
        double k = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < k) {
                triangles.add(new TrianColorRGBDepth(a.getTriangle(i)));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (random.nextDouble() >= k) {
                triangles.add(new TrianColorRGBDepth(b.getTriangle(i)));
            }
        }
        while (triangles.size() > MAX_SIZE) {
            int i = random.nextInt(triangles.size());
            triangles.remove(i);
        }
        return new TrianImgRGBDepth(triangles);
    }

    @Override
    public TrianImgRGBDepth genRand() {
        ArrayList<TrianColorRGBDepth> triangles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            triangles.add(TrianColorRGBDepth.getRand(random));
        }
        return new TrianImgRGBDepth(triangles);
    }
}
