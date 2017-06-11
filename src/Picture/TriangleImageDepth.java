package Picture;

import Geometry.Point;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TriangleImageDepth {
    static int number = 0;
    int myNumber;
    ArrayList<TriangleCD> triangles;

    public TriangleImageDepth(ArrayList<TriangleCD> triangles) {
        myNumber = number++;
        this.triangles = triangles;
    }

    double getColor(Point point) {
        double h = Double.NEGATIVE_INFINITY;
        double result = 0;
        for (TriangleCD triangle : triangles) {
            if (triangle.inside(point)) {
                if (h < triangle.getDepth()) {
                    h = triangle.getDepth();
                    result = triangle.getColor() * 255;
                }
            }
        }
        return result;
    }

    public Image getImage(int nh, int nw) {
        Image result = new Image(nh, nw);
        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nw; j++) {
                Point point = new Point((double) i / nh, (double) j / nw);
                result.set(i, j, getColor(point));
            }
        }
        return result;
    }

    public int size() {
        return triangles.size();
    }

    public TriangleCD getTriangle(int ind) {
        return triangles.get(ind);
    }

    public int getNumber() {
        return myNumber;
    }
}
