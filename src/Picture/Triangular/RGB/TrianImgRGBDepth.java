package Picture.Triangular.RGB;

import Geometry.Point;
import Picture.Triangular.AbsTriangleImage;
import Picture.Triangular.Triangle.TrianColorRGBDepth;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgRGBDepth extends AbsTriangleImage {
    public ArrayList<TrianColorRGBDepth> getTriangles() {
        return triangles;
    }

    ArrayList<TrianColorRGBDepth> triangles;

    public TrianImgRGBDepth(ArrayList<TrianColorRGBDepth> triangles) {
        super();
        this.triangles = triangles;
    }

    protected double[] get(Point point) {
        double h = Double.NEGATIVE_INFINITY;
        double[] result = new double[3];
        for (TrianColorRGBDepth triangle : triangles) {
            if (triangle.inside(point)) {
                if (h < triangle.getDepth()) {
                    h = triangle.getDepth();
                    double[] temp = triangle.getColor();
                    for (int k = 0; k < 3; k++) {
                        result[k] = temp[k] * 255;
                    }
                }
            }
        }
        return result;
    }

    public int size() {
        return triangles.size();
    }

    public TrianColorRGBDepth getTriangle(int ind) {
        return triangles.get(ind);
    }
}
