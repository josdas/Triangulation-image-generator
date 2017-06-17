package Picture.Triangular.RGB;

import Geometry.Point;
import Picture.Triangular.Triangle.TrianColorRGBDepth;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgRGBDepth extends TemplateTrnglImg<TrianColorRGBDepth> {
    public TrianImgRGBDepth(ArrayList<TrianColorRGBDepth> triangles) {
        super(triangles);
    }

    @Override
    protected double[] get(Point point) {
        double h = Double.NEGATIVE_INFINITY;
        double[] result = new double[3];
        for (TrianColorRGBDepth triangle : triangles) {
            if (triangle.inside(point)) {
                if (h < triangle.getDepth()) {
                    h = triangle.getDepth();
                    double[] temp = triangle.getColor();
                    for (int k = 0; k < 3; k++) {
                        result[k] = temp[k];
                    }
                }
            }
        }
        return result;
    }
}
