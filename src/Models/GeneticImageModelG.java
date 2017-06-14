package Models;

import Genetic.GeneticObject;
import Geometry.Point;
import Picture.AbsImage;
import Picture.ImageRGB;
import Picture.Triangular.Triangle.TrianColorRGBDepth;
import Picture.Triangular.RGB.TrianImgRGBTrans;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;


/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelG extends ImageModel implements GeneticObject<TrianImgRGBTrans> {
    public GeneticImageModelG(AbsImage image) {
        this.image = image;
        this.random = new Random();
        this.evalStore = new TreeMap<>();
    }

    private void mutationFirst(
            ArrayList<TrianColorRGBDepth> triangles,
            TrianImgRGBTrans obj
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
            TrianImgRGBTrans obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorRGBDepth(obj.getTriangle(i)));
            if (index[i]) {
                triangles.get(i).setColor(
                        smallChange(triangles.get(i).getColor(), MUTATION_COEF)
                );
            }
        }
    }

    private void mutationThird(
            ArrayList<TrianColorRGBDepth> triangles,
            boolean[] index,
            TrianImgRGBTrans obj
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

    TrianImgRGBTrans cloneImage(TrianImgRGBTrans a) {
        ArrayList<TrianColorRGBDepth> triangles = new ArrayList<>();
        for (TrianColorRGBDepth triangle : a.getTriangles()) {
            triangles.add(new TrianColorRGBDepth(triangle));
        }
        return new TrianImgRGBTrans(triangles);
    }

    @Override
    public TrianImgRGBTrans mutation(TrianImgRGBTrans obj) {
        ArrayList<TrianColorRGBDepth> triangles = new ArrayList<>();
        int type = random.nextInt(3);

        if (random.nextDouble() < 0.7) {
            type = 3;
        }

        if (obj.size() == 0 || type == 0) {
            mutationFirst(triangles, obj);
        } else if (type == 3) {
            TrianImgRGBTrans temp = cloneImage(obj);
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
        return new TrianImgRGBTrans(triangles);
    }

    public void autoColor(TrianImgRGBTrans a) {
        final int h = image.getH();
        final int w = image.getW();
        ArrayList<TrianColorRGBDepth> triangles = a.getTriangles();
        double[][] sumColor = new double[triangles.size()][3];
        int[] numberPixels = new int[triangles.size()];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Point point = new Point((double) i / h, (double) j / w);

                for (int k = 0; k < triangles.size(); k++) {
                    final TrianColorRGBDepth triangle = triangles.get(k);
                    if (triangle.inside(point)) {
                        double[] temp = triangle.getColor();
                        for (int l = 0; l < 3; l++) {
                            sumColor[k][l] += temp[l] / 5.0;
                        }
                        numberPixels[k]++;
                    }
                }
            }
        }
        double prob = Math.min(random.nextDouble(), random.nextDouble());
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < prob && numberPixels[i] > 0) {
                double[] rgb = new double[3];
                for (int k = 0; k < 3; k++) {
                    rgb[k] = sumColor[i][k] / numberPixels[i];
                }
                triangles.get(i).setColor(rgb);
            }
        }
    }

    @Override
    public double eval(TrianImgRGBTrans obj) {
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
    public TrianImgRGBTrans crossover(TrianImgRGBTrans a, TrianImgRGBTrans b) {
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
        return new TrianImgRGBTrans(triangles);
    }

    @Override
    public TrianImgRGBTrans genRand() {
        ArrayList<TrianColorRGBDepth> triangles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            triangles.add(TrianColorRGBDepth.getRand(random));
        }
        return new TrianImgRGBTrans(triangles);
    }
}
