package Picture;

import Geometry.Point;
import Geometry.Triangle;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TriangleImageTrans extends AbsTriangleImage {
    ArrayList<Triangle> triangles;
    ArrayList<Double> brightness;

    public TriangleImageTrans(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
        this.brightness = new ArrayList<>();
        for (int i = 0; i < triangles.size(); i++) {
            brightness.add((255.0 / triangles.size() * 4 + 10) / 255);
        }
    }

    public TriangleImageTrans(ArrayList<Triangle> triangles, ArrayList<Double> brightness) {
        super();
        this.triangles = triangles;
        this.brightness = brightness;
    }

    double get(Point point) {
        double result = 0;
        for (int i = 0; i < triangles.size(); i++) {
            if (triangles.get(i).inside(point)) {
                result += brightness.get(i) * 255;
            }
        }
        return result;
    }

    public int size() {
        return triangles.size();
    }

    public Triangle getTriangle(int ind) {
        return triangles.get(ind);
    }

    public double getBrightness(int ind) {
        return brightness.get(ind);
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
