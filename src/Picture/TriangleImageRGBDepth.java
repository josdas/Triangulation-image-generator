package Picture;

import Geometry.Point;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TriangleImageRGBDepth extends AbsTriangleImage {
    public ArrayList<TriangleColorRGBDepth> getTriangles() {
        return triangles;
    }

    ArrayList<TriangleColorRGBDepth> triangles;

    public TriangleImageRGBDepth(ArrayList<TriangleColorRGBDepth> triangles) {
        super();
        this.triangles = triangles;
    }

    double[] get(Point point) {
        double h = Double.NEGATIVE_INFINITY;
        double[] result = new double[3];
        for (TriangleColorRGBDepth triangle : triangles) {
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

    public TriangleColorRGBDepth getTriangle(int ind) {
        return triangles.get(ind);
    }

    public int getNumber() {
        return myNumber;
    }

    @Override
    public int[] getColor(Point point) {
        int[] rgb = new int[3];
        double[] temp = get(point);
        for (int k = 0; k < 3; k++) {
            rgb[k] = (int) (temp[k]);
        }
        return rgb;
    }
}
