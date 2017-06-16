package Picture.Triangular.RGB;

import Geometry.Point;
import Picture.Triangular.AbsTriangleImage;
import Picture.Triangular.Triangle.TrianColorRGBTrans;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgRGBDepthTransOrdered extends AbsTriangleImage {
    public ArrayList<TrianColorRGBTrans> getTriangles() {
        return triangles;
    }

    ArrayList<TrianColorRGBTrans> triangles;

    public TrianImgRGBDepthTransOrdered(ArrayList<TrianColorRGBTrans> triangles) {
        super();
        this.triangles = triangles;
    }

    protected double[] get(Point point) {
        double[] result = new double[3];
        double alpha = 1;
        for (TrianColorRGBTrans triangle : triangles) {
            if (triangle.inside(point)) {
                double[] temp = triangle.getColor();
                double k = alpha * triangle.getTrans();
                alpha *= 1 - triangle.getTrans();
                for (int i = 0; i < 3; i++) {
                    result[i] += k * temp[i] * 255;
                }
            }
        }
        return result;
    }

    public int size() {
        return triangles.size();
    }

    public TrianColorRGBTrans getTriangle(int ind) {
        return triangles.get(ind);
    }
}
