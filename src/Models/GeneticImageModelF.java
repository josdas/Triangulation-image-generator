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
public class GeneticImageModelF extends ImageModel implements GeneticObject<TrianImgRGBDepthTrans> {
    ImageWB imageCircuit;

    public GeneticImageModelF(AbsImage image) {
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

        for (int i = 0; i < numberMutations; i++) {
            boolean temp = prob > random.nextDouble();
            if (triangles.size() > 0 && temp) {
                triangles.remove(random.nextInt(triangles.size()));
            }
            if (triangles.size() == 0 || (triangles.size() + 1 < MAX_SIZE && !temp)) {
                triangles.add(TrianColorRGBDepthTrans.getRand(random));
            }
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
                final int type = random.nextInt(3);
                switch (type) {
                    case 0:
                        triangles.get(i).setDepth(
                                random.nextDouble()
                        );
                        break;
                    case 1:
                        triangles.get(i).setColor(
                                smallChange(triangles.get(i).getColor(), MUTATION_COEF)
                        );
                        break;
                    case 2:
                        triangles.get(i).setTrans(
                                smallChange(triangles.get(i).getTrans(), MUTATION_COEF)
                        );
                        break;

                }
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
                    triangle.a = smallChange(triangle.a, MUTATION_COEF);
                }
                if (random.nextBoolean()) {
                    triangle.b = smallChange(triangle.b, MUTATION_COEF);
                }
                if (random.nextBoolean()) {
                    triangle.c = smallChange(triangle.c, MUTATION_COEF);
                }
            }
            triangles.add(triangle);
        }
    }

    TrianImgRGBDepthTrans cloneImage(TrianImgRGBDepthTrans a) {
        ArrayList<TrianColorRGBDepthTrans> triangles = new ArrayList<>();
        for (TrianColorRGBDepthTrans triangle : a.getTriangles()) {
            triangles.add(new TrianColorRGBDepthTrans(triangle));
        }
        return new TrianImgRGBDepthTrans(triangles);
    }

    @Override
    public TrianImgRGBDepthTrans mutation(TrianImgRGBDepthTrans obj) {
        ArrayList<TrianColorRGBDepthTrans> triangles = new ArrayList<>();
        int type = random.nextInt(3);

        if (random.nextDouble() < 0.3) {
            type = 3;
        }

        if (obj.size() == 0 || type == 0) {
            mutationFirst(triangles, obj);
        } else if (type == 3) {
            TrianImgRGBDepthTrans temp = cloneImage(obj);
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
        return new TrianImgRGBDepthTrans(triangles);
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
    public double eval(TrianImgRGBDepthTrans obj) {
        if (evalStore.containsKey(obj.getNumber())) {
            return evalStore.get(obj.getNumber());
        }
        ImageRGB tempImage = obj.getImage(image.getH(), image.getW());
        double result = AbsImage.distance(
                tempImage,
                image
        );
        result += AbsImage.distance(
                tempImage.toCircuit(),
                imageCircuit
        ) * 0.5;
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
        for (int i = 0; i < 10; i++) {
            triangles.add(TrianColorRGBDepthTrans.getRand(random));
        }
        return new TrianImgRGBDepthTrans(triangles);
    }
}
