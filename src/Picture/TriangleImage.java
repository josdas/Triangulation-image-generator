package Picture;

import Geometry.Point;
import Geometry.Triangle;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TriangleImage {
    ArrayList<Triangle> triangles;

    public TriangleImage(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
    }

    double getDepth(Point point) {
        double result = 0;
        for (Triangle triangle: triangles) {
            if(triangle.inside(point)) {
                result += 255.0 / triangles.size() * 4;
            }
        }
        return result;
    }

    public Image getImage(int nh, int nw) {
        Image result = new Image(nh, nw);
        for(int i = 0; i < nh; i++) {
            for(int j = 0; j < nw; j++) {
                Point point = new Point((double) i / nh, (double) j / nw);
                result.set(i, j, getDepth(point));
            }
        }
        return result;
    }

    public Triangle get(int ind) {
        return triangles.get(ind);
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }
}