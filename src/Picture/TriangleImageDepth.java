package Picture;

import Geometry.Point;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TriangleImageDepth extends AbsTriangleImage {
    public ArrayList<TriangleColorDepth> getTriangles() {
        return triangles;
    }

    ArrayList<TriangleColorDepth> triangles;

    public TriangleImageDepth(ArrayList<TriangleColorDepth> triangles) {
        super();
        this.triangles = triangles;
    }

    double get(Point point) {
        double h = Double.NEGATIVE_INFINITY;
        double result = 0;
        for (TriangleColorDepth triangle : triangles) {
            if (triangle.inside(point)) {
                if (h < triangle.getDepth()) {
                    h = triangle.getDepth();
                    result = triangle.getColor() * 255;
                }
            }
        }
        return result;
    }

    public int size() {
        return triangles.size();
    }

    public TriangleColorDepth getTriangle(int ind) {
        return triangles.get(ind);
    }

    public int getNumber() {
        return myNumber;
    }

    @Override
    public int[] getColor(Point point) {
        int[] rgb = new int[3];
        int temp = (int) get(point);
        for (int k = 0; k < 3; k++) {
            rgb[k] = temp;
        }
        return rgb;
    }
}
