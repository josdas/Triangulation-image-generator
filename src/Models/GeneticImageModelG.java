package Models;

import Genetic.GeneticObject;
import Geometry.Point;
import Picture.AbsImage;
import Picture.ImageRGB;
import Picture.ImageWB;
import Picture.Triangular.RGB.TrianImgRGBDepthTrans;
import Picture.Triangular.Triangle.TrianColorRGBDepthTrans;

import java.util.ArrayList;


/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelG extends ImageModel implements GeneticObject<TrianImgRGBDepthTrans> {
    public int MUTATION_SIZE = 6;
    ImageWB imageCircuit;

    public GeneticImageModelG(AbsImage image) {
        super(image);
        imageCircuit = image.toCircuit();
    }

    private void mutationFirst(
            ArrayList<TrianColorRGBDepthTrans> triangles,
            TrianImgRGBDepthTrans obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorRGBDepthTrans(obj.getTriangle(i)));
        }
        int numberMutations = random.nextInt(MUTATION_SIZE);

        double prob = random.nextDouble();

        for (int i = 0; i < numberMutations * 3; i++) {
            boolean temp = prob > random.nextDouble();
            if (triangles.size() > 0 && temp) {
                triangles.remove(random.nextInt(triangles.size()));
            } else {
                triangles.add(TrianColorRGBDepthTrans.getRand(random));
            }
        }
        while (triangles.size() > MAX_SIZE) {
            triangles.remove(random.nextInt(triangles.size()));
        }
    }

    private void mutationSecond(
            ArrayList<TrianColorRGBDepthTrans> triangles,
            boolean[] index,
            TrianImgRGBDepthTrans obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorRGBDepthTrans(obj.getTriangle(i)));
            if (index[i]) {
                triangles.get(i).setDepth(
                        smallChange(triangles.get(i).getDepth(), MUTATION_COEF)
                );
                triangles.get(i).setTrans(
                        random.nextDouble() * 0.3 + 0.3
                );
            }
        }
    }

    private void mutationThird(
            ArrayList<TrianColorRGBDepthTrans> triangles,
            boolean[] index,
            TrianImgRGBDepthTrans obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TrianColorRGBDepthTrans triangle = new TrianColorRGBDepthTrans(obj.getTriangle(i));
            if (index[i]) {
                if (random.nextBoolean()) {
                    triangle.a = smallChange(triangle.a, MUTATION_COEF * 0.3);
                }
                if (random.nextBoolean()) {
                    triangle.b = smallChange(triangle.b, MUTATION_COEF * 0.3);
                }
                if (random.nextBoolean()) {
                    triangle.c = smallChange(triangle.c, MUTATION_COEF * 0.3);
                }
            }
            triangles.add(triangle);
        }
    }

    private void mutationFourth(
            ArrayList<TrianColorRGBDepthTrans> triangles,
            boolean[] index,
            TrianImgRGBDepthTrans obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TrianColorRGBDepthTrans triangle = new TrianColorRGBDepthTrans(obj.getTriangle(i));
            if (index[i] && triangle.area() > 0.1) {
                Point center = Point.div(
                        Point.add(
                                Point.add(
                                        triangle.a,
                                        triangle.b
                                ),
                                triangle.c
                        ), 3);

                TrianColorRGBDepthTrans A = new TrianColorRGBDepthTrans(triangle);
                TrianColorRGBDepthTrans B = new TrianColorRGBDepthTrans(triangle);
                TrianColorRGBDepthTrans C = new TrianColorRGBDepthTrans(triangle);
                A.a = center;
                B.b = center;
                C.c = center;
                if (random.nextBoolean() && A.area() > 0.006) {
                    triangles.add(A);
                }
                if (random.nextBoolean() && B.area() > 0.006) {
                    triangles.add(B);
                }
                if (random.nextBoolean() && C.area() > 0.006) {
                    triangles.add(C);
                }
            } else {
                triangles.add(triangle);
            }
        }
    }


    TrianImgRGBDepthTrans cloneImage(TrianImgRGBDepthTrans a) {
        ArrayList<TrianColorRGBDepthTrans> triangles = new ArrayList<>();
        for (TrianColorRGBDepthTrans triangle : a.getTriangles()) {
            triangles.add(new TrianColorRGBDepthTrans(triangle));
        }
        return new TrianImgRGBDepthTrans(triangles);
    }


    public TrianImgRGBDepthTrans mutationm(TrianImgRGBDepthTrans obj) {
        ArrayList<TrianColorRGBDepthTrans> triangles = new ArrayList<>();
        int type = random.nextInt(4);

        if (obj.size() == 0 || type == 0) {
            mutationFirst(triangles, obj);
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
                case 3:
                    mutationFourth(triangles, index, obj);
                    break;
            }
        }
        return new TrianImgRGBDepthTrans(triangles);
    }

    @Override
    public TrianImgRGBDepthTrans mutation(TrianImgRGBDepthTrans obj) {
        TrianImgRGBDepthTrans result = mutationm(obj);
        autoColor(result);
        return result;
    }

    public void autoColor(TrianImgRGBDepthTrans a) {
        final int h = image.getH();
        final int w = image.getW();
        ArrayList<TrianColorRGBDepthTrans> triangles = a.getTriangles();
        double[][] sumColor = new double[triangles.size()][3];
        double[] sumMass = new double[triangles.size()];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Point point = new Point((double) i / h, (double) j / w);
                ArrayList<Integer> goodTriangles = new ArrayList<>();

                for (int k = 0; k < triangles.size(); k++) {
                    if (triangles.get(k).inside(point)) {
                        goodTriangles.add(k);
                    }
                }
                goodTriangles.sort((q, e) -> {
                    double valueA = triangles.get(q).getDepth();
                    double valueB = triangles.get(e).getDepth();
                    if (valueA == valueB) {
                        return 0;
                    }
                    if (valueA > valueB) {
                        return -1;
                    }
                    return 1;
                });
                double alpha = 1;
                for (int ind : goodTriangles) {
                    final TrianColorRGBDepthTrans triangle = triangles.get(ind);
                    int[] temp = image.getColor(i, j);
                    double k = alpha * triangle.getTrans();
                    alpha *= 1 - triangle.getTrans();
                    for (int l = 0; l < 3; l++) {
                        sumColor[ind][l] += k * temp[l] / 255.0;
                    }
                    sumMass[ind] += k;
                }
            }
        }
        double prob = Math.max(random.nextDouble(), random.nextDouble());
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < prob && sumMass[i] > 0) {
                double[] rgb = new double[3];
                for (int k = 0; k < 3; k++) {
                    rgb[k] = sumColor[i][k] / sumMass[i];
                }
                triangles.get(i).setColor(rgb);
            }
        }
    }

    @Override
    public double eval(TrianImgRGBDepthTrans obj) {
        if (evalStore.containsKey(obj.getNumber())) {
            return evalStore.get(obj.getNumber());
        }
        ImageRGB tempImage = obj.getImage(image.getH(), image.getW());
        double result = AbsImage.distance(
                tempImage,
                image
        );
        for (int i = 0; i < obj.size(); i++) {
            double area = obj.getTriangle(i).area();
            if (area < 0.002) {
                result += 20;
            }
        }
        evalStore.put(obj.getNumber(), result);
        return result;
    }

    @Override
    public TrianImgRGBDepthTrans crossover(TrianImgRGBDepthTrans a, TrianImgRGBDepthTrans b) {
        ArrayList<TrianColorRGBDepthTrans> triangles = new ArrayList<>();
        double k = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < k) {
                triangles.add(new TrianColorRGBDepthTrans(a.getTriangle(i)));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (random.nextDouble() > k) {
                triangles.add(new TrianColorRGBDepthTrans(b.getTriangle(i)));
            }
        }
        while (triangles.size() > MAX_SIZE) {
            int i = random.nextInt(triangles.size());
            triangles.remove(i);
        }
        return new TrianImgRGBDepthTrans(triangles);
    }

    @Override
    public TrianImgRGBDepthTrans genRand() {
        ArrayList<TrianColorRGBDepthTrans> triangles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            triangles.add(TrianColorRGBDepthTrans.getRand(random));
            triangles.get(i).setTrans(random.nextDouble() * 0.3 + 0.3);
        }
        return new TrianImgRGBDepthTrans(triangles);
    }
}
