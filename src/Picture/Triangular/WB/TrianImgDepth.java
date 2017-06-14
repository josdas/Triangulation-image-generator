package Picture.Triangular.WB;

import Geometry.Point;
import Picture.Triangular.AbsTriangleImage;
import Picture.Triangular.Triangle.TrianColorWBDepth;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgDepth extends AbsTriangleImage {
    public ArrayList<TrianColorWBDepth> getTriangles() {
        return triangles;
    }

    ArrayList<TrianColorWBDepth> triangles;

    public TrianImgDepth(ArrayList<TrianColorWBDepth> triangles) {
        super();
        this.triangles = triangles;
    }

    protected double[] get(Point point) {
        double h = Double.NEGATIVE_INFINITY;
        double result = 0;
        for (TrianColorWBDepth triangle : triangles) {
            if (triangle.inside(point)) {
                if (h < triangle.getDepth()) {
                    h = triangle.getDepth();
                    result = triangle.getColor() * 255;
                }
            }
        }
        double[] temp = new double[3];
        for (int i = 0; i < 3; i++) {
            temp[i] = result;
        }
        return temp;
    }

    public int size() {
        return triangles.size();
    }

    public TrianColorWBDepth getTriangle(int ind) {
        return triangles.get(ind);
    }
}
