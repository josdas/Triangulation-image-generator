package Picture.Triangular.RGB;

import Geometry.Point;
import Picture.Triangular.Triangle.TrianColorRGBTrans;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgRGBTrans extends TemplateTrnglImg<TrianColorRGBTrans> {
    public TrianImgRGBTrans(ArrayList<TrianColorRGBTrans> triangles) {
        super(triangles);
    }

    @Override
    protected double[] get(Point point) {
        double[] result = new double[3];
        for (TrianColorRGBTrans triangle : triangles) {
            if (triangle.inside(point)) {
                double[] temp = triangle.getColor();
                double alpha = triangle.getTrans();
                for (int k = 0; k < 3; k++) {
                    result[k] += temp[k] * alpha;
                }
            }
        }
        return result;
    }
}
