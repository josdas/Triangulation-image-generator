package Models;

import Genetic.GeneticObject;
import Geometry.Point;
import Geometry.Triangle;
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
public class GeneticImageModelH extends ImageModel implements GeneticObject<TrianImgRGBDepthTransOrdered> {
    public int MUTATION_SIZE = 5;
    ImageWB imageCircuit;

    public GeneticImageModelH(AbsImage image) {
        super(image);
        imageCircuit = image.toCircuit();
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
                        random.nextDouble() * 0.6 + 0.1
                );
                if (i > 0) {
                    Collections.swap(
                            triangles,
                            random.nextInt(i),
                            i
                    );
                }
            }
        }
    }

    private void mutationFourth(
            ArrayList<TrianColorRGBTrans> triangles,
            boolean[] index,
            TrianImgRGBDepthTransOrdered obj
    ) {
        for (int i = 0; i < obj.size(); i++) {
            TrianColorRGBTrans triangle = new TrianColorRGBTrans(obj.getTriangle(i));
            if (index[i] && triangle.area() > 0.03) {
                Point center = Point.div(
                        Point.add(
                                Point.add(
                                        triangle.a,
                                        triangle.b
                                ),
                                triangle.c
                        ), 3);
                center = smallChange(center, MUTATION_COEF * 0.3);
                for (int j = 0; j < 3; j++) {
                    TrianColorRGBTrans temp = new TrianColorRGBTrans(triangle);
                    temp.setPoint(j, center);
                    triangles.add(temp);
                }
            } else {
                triangles.add(triangle);
            }
        }
    }


    public TrianImgRGBDepthTransOrdered mutationm(TrianImgRGBDepthTransOrdered obj) {
        ArrayList<TrianColorRGBTrans> triangles = new ArrayList<>();
        int type = random.nextInt(2);
        int change = random.nextInt(MUTATION_SIZE) + 1;
        boolean[] index = new boolean[obj.size()];
        for (int i = 0; i < change; i++) {
            index[random.nextInt(obj.size())] = true;
        }
        switch (type) {
            case 0:
                mutationSecond(triangles, index, obj);
                break;
            case 1:
                mutationFourth(triangles, index, obj);
                break;
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
        for (int i = 0; i < a.size(); i++) {
            if (sumMass[i] > 0) {
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
        for (int i = 0; i < obj.size(); i++) {
            double area = obj.getTriangle(i).area();
            if (area < 0.002) {
                result += 20;
            }
        }
        result += obj.size();
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
            if (random.nextDouble() < k || i < 2) {
                A.add(new TrianColorRGBTrans(a.getTriangle(i)));
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (random.nextDouble() > k || i < 2) {
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


    double[] randomColor() {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            result[i] = random.nextDouble();
        }
        return result;
    }

    @Override
    public TrianImgRGBDepthTransOrdered genRand() {
        ArrayList<TrianColorRGBTrans> triangles = new ArrayList<>();

        Point[] corner = new Point[4];
        for (int i = 0, t = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++, t++) {
                corner[t] = new Point(i, j);
            }
        }
        for (int i = 0; i < 8; i++) {
            int n = i % 4;
            triangles.add(new TrianColorRGBTrans(
                    new Triangle(
                            corner[n],
                            corner[(n + 1) % 4],
                            corner[(n + 2) % 4]
                    ),
                    random.nextDouble() * 0.6 + 0.1,
                    randomColor()
            ));
        }
        return new TrianImgRGBDepthTransOrdered(triangles);
    }
}
