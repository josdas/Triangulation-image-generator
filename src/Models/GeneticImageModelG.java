package Models;

import Genetic.GeneticObject;
import Geometry.Point;
import Picture.AbsImage;
import Picture.ImageRGB;
import Picture.ImageWB;
import Picture.Triangular.RGB.TrianImgRGBDepthTransOrdered;
import Picture.Triangular.Triangle.TrianColorRGBTrans;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImageModelG extends ImageModel implements GeneticObject<TrianImgRGBDepthTransOrdered> {
    public int MUTATION_SIZE = 5;
    ImageWB imageCircuit;

    public GeneticImageModelG(AbsImage image) {
        super(image);
        imageCircuit = image.toCircuit();
    }

    private void mutationFirst(
            ArrayList<TrianColorRGBTrans> triangles,
            TrianImgRGBDepthTransOrdered obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorRGBTrans(obj.getTriangle(i)));
        }
        int numberMutations = random.nextInt(MUTATION_SIZE);

        double prob = random.nextDouble();

        for (int i = 0; i < numberMutations * 3; i++) {
            boolean temp = prob > random.nextDouble();
            if (triangles.size() > 0 && temp) {
                triangles.remove(random.nextInt(triangles.size()));
            } else {
                triangles.add(
                        random.nextInt(triangles.size() + 1),
                        TrianColorRGBTrans.getRand(random)
                );
            }
        }
    }

    private void mutationSecond(
            ArrayList<TrianColorRGBTrans> triangles,
            boolean[] index,
            TrianImgRGBDepthTransOrdered obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            triangles.add(new TrianColorRGBTrans(obj.getTriangle(i)));
            if (index[i]) {
                triangles.get(i).setTrans(
                        random.nextDouble() * 0.4 + 0.1
                );
                Collections.swap(
                        triangles,
                        random.nextInt(triangles.size()),
                        random.nextInt(triangles.size())
                );
            }
        }
    }

    private void mutationThird(
            ArrayList<TrianColorRGBTrans> triangles,
            boolean[] index,
            TrianImgRGBDepthTransOrdered obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TrianColorRGBTrans triangle = new TrianColorRGBTrans(obj.getTriangle(i));
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
            ArrayList<TrianColorRGBTrans> triangles,
            boolean[] index,
            TrianImgRGBDepthTransOrdered obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TrianColorRGBTrans triangle = new TrianColorRGBTrans(obj.getTriangle(i));
            if (index[i] && triangle.area() > 0.05) {
                Point center = Point.div(
                        Point.add(
                                Point.add(
                                        triangle.a,
                                        triangle.b
                                ),
                                triangle.c
                        ), 3);
                center = smallChange(center, MUTATION_COEF * 0.3);
                TrianColorRGBTrans A = new TrianColorRGBTrans(triangle);
                TrianColorRGBTrans B = new TrianColorRGBTrans(triangle);
                TrianColorRGBTrans C = new TrianColorRGBTrans(triangle);
                A.a = center;
                B.b = center;
                C.c = center;
                if (random.nextBoolean() && A.area() > 0.006) {
                    triangles.add(
                            random.nextInt(triangles.size() + 1),
                            A
                    );
                }
                if (random.nextBoolean() && B.area() > 0.006) {
                    triangles.add(
                            random.nextInt(triangles.size() + 1),
                            B
                    );
                }
                if (random.nextBoolean() && C.area() > 0.006) {
                    triangles.add(
                            random.nextInt(triangles.size() + 1),
                            C
                    );
                }
            } else {
                triangles.add(triangle);
            }
        }
    }


    public TrianImgRGBDepthTransOrdered mutationm(TrianImgRGBDepthTransOrdered obj) {
        ArrayList<TrianColorRGBTrans> triangles = new ArrayList<>();
        int type = random.nextInt(3);
        if (random.nextDouble() < 0.1) {
            type = 3;
        }
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
        return new TrianImgRGBDepthTransOrdered(triangles);
    }

    @Override
    public TrianImgRGBDepthTransOrdered mutation(TrianImgRGBDepthTransOrdered obj) {
        TrianImgRGBDepthTransOrdered result = mutationm(obj);
        while (random.nextDouble() < 0.2) {
            result = mutationm(result);
        }
        while (result.getTriangles().size() > MAX_SIZE) {
            result.getTriangles().remove(
                    random.nextInt(result.getTriangles().size())
            );
        }
        autoColor(result);
        return result;
    }

    public void autoColor(TrianImgRGBDepthTransOrdered a) {
        final int h = image.getH();
        final int w = image.getW();
        ArrayList<TrianColorRGBTrans> triangles = a.getTriangles();
        double[][] sumColor = new double[triangles.size()][3];
        double[] sumMass = new double[triangles.size()];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Point point = new Point((double) i / h, (double) j / w);
                double alpha = 1;
                for (int ind = 0; ind < triangles.size(); ind++) {
                    if (triangles.get(ind).inside(point)) {
                        final TrianColorRGBTrans triangle = triangles.get(ind);
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
        }
        double prob = Math.max(random.nextDouble(), random.nextDouble());
        prob = 2;
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
    public double eval(TrianImgRGBDepthTransOrdered obj) {
        if (evalStore.containsKey(obj.getNumber())) {
            return evalStore.get(obj.getNumber());
        }
        ImageRGB tempImage = obj.getImage(image.getH(), image.getW());
        double result = AbsImage.distance(
                tempImage,
                image
        );
        result += AbsImage.distance(
                imageCircuit,
                image.toCircuit()
        );
        for (int i = 0; i < obj.size(); i++) {
            double area = obj.getTriangle(i).area();
            if (area < 0.002) {
                result += 20;
            }
        }
        result += obj.size() * 2;
        evalStore.put(obj.getNumber(), result);
        return result;
    }

    @Override
    public TrianImgRGBDepthTransOrdered crossover(TrianImgRGBDepthTransOrdered a, TrianImgRGBDepthTransOrdered b) {
        ArrayList<TrianColorRGBTrans> triangles = new ArrayList<>();
        ArrayList<TrianColorRGBTrans> A = new ArrayList<>();
        ArrayList<TrianColorRGBTrans> B = new ArrayList<>();
        double k = random.nextDouble();
        for (int i = 0; i < a.size(); i++) {
            if (random.nextDouble() < k) {
                A.add(new TrianColorRGBTrans(a.getTriangle(i)));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (random.nextDouble() > k) {
                B.add(new TrianColorRGBTrans(b.getTriangle(i)));
            }
        }
        int indA = 0;
        int indB = 0;
        while (indA != A.size() && indB != B.size()) {
            boolean temp = random.nextBoolean();
            if (indA != A.size() && (temp || indB == B.size())) {
                triangles.add(A.get(indA));
                indA++;
            }
            if (indB != B.size() && (!temp || indA == A.size())) {
                triangles.add(B.get(indB));
                indB++;
            }
        }
        return new TrianImgRGBDepthTransOrdered(triangles);
    }

    @Override
    public TrianImgRGBDepthTransOrdered genRand() {
        ArrayList<TrianColorRGBTrans> triangles = new ArrayList<>();
        triangles.add(TrianColorRGBTrans.getRand(random, 0.005, 0.2));
        triangles.get(triangles.size() - 1).setTrans(random.nextDouble() * 0.4 + 0.1);
        return new TrianImgRGBDepthTransOrdered(triangles);
    }
}
