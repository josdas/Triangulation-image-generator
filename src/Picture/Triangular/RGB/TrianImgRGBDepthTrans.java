package Picture.Triangular.RGB;

import Geometry.Point;
import Picture.Triangular.AbsTriangleImage;
import Picture.Triangular.Triangle.TrianColorRGBDepthTrans;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgRGBDepthTrans extends AbsTriangleImage {
    public ArrayList<TrianColorRGBDepthTrans> getTriangles() {
        return triangles;
    }

    ArrayList<TrianColorRGBDepthTrans> triangles;

    public TrianImgRGBDepthTrans(ArrayList<TrianColorRGBDepthTrans> triangles) {
        super();
        this.triangles = triangles;
    }

    protected double[] get(Point point) {
        double[] result = new double[3];
        ArrayList<TrianColorRGBDepthTrans> goodTriangles = new ArrayList<>();
        for (TrianColorRGBDepthTrans triangle : triangles) {
            if (triangle.inside(point)) {
                goodTriangles.add(triangle);
            }
        }
        goodTriangles.sort((a, b) -> {
            double valueA = a.getDepth();
            double valueB = b.getDepth();
            if (valueA == valueB) {
                return 0;
            }
            if (valueA > valueB) {
                return -1;
            }
            return 1;
        });
        double alpha = 1;
        for (TrianColorRGBDepthTrans triangle : goodTriangles) {
            double[] temp = triangle.getColor();
            double k = alpha * triangle.getTrans();
            alpha *= 1 - triangle.getTrans();
            for (int i = 0; i < 3; i++) {
                result[i] += k * temp[i] * 255;
            }
        }
        return result;
    }

    public int size() {
        return triangles.size();
    }

    public TrianColorRGBDepthTrans getTriangle(int ind) {
        return triangles.get(ind);
    }
}
