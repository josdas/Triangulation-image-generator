package Picture.Triangular.WB;

import Geometry.Point;
import Geometry.Triangle;
import Picture.Triangular.AbsTriangleImage;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgTrans extends AbsTriangleImage {
    ArrayList<Triangle> triangles;
    ArrayList<Double> brightness;

    public TrianImgTrans(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
        this.brightness = new ArrayList<>();
        for (int i = 0; i < triangles.size(); i++) {
            brightness.add(1 / 5.0);
        }
    }

    public TrianImgTrans(ArrayList<Triangle> triangles, ArrayList<Double> brightness) {
        super();
        this.triangles = triangles;
        this.brightness = brightness;
    }

    @Override
    protected double[] get(Point point) {
        double result = 0;
        for (int i = 0; i < triangles.size(); i++) {
            if (triangles.get(i).inside(point)) {
                result += brightness.get(i) * 255;
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

    public Triangle getTriangle(int ind) {
        return triangles.get(ind);
    }

    public double getBrightness(int ind) {
        return brightness.get(ind);
    }
}
