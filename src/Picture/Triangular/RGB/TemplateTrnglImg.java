package Picture.Triangular.RGB;

import Geometry.Point;
import Picture.Triangular.AbsTriangleImage;

import java.util.ArrayList;

/**
 * Created by Stas on 17.06.2017.
 */
public abstract class TemplateTrnglImg<T> extends AbsTriangleImage {
    ArrayList<T> triangles;

    public TemplateTrnglImg(ArrayList<T> triangles) {
        super();
        this.triangles = triangles;
    }

    public ArrayList<T> getTriangles() {
        return triangles;
    }

    public T getTriangle(int ind) {
        return triangles.get(ind);
    }

    @Override
    public int size() {
        return triangles.size();
    }

    @Override
    protected abstract double[] get(Point point);
}
