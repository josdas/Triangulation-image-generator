package Picture.Triangular.RGB;

import Geometry.Point;
import Picture.Triangular.Triangle.TrianColorRGBDepthTrans;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Stas on 06.06.2017.
 */
public class TrianImgRGBDepthTrans extends TemplateTrnglImg<TrianColorRGBDepthTrans> {
    public TrianImgRGBDepthTrans(ArrayList<TrianColorRGBDepthTrans> triangles) {
        super(triangles);
    }

    @Override
    protected double[] get(Point point) {
        double[] result = new double[3];
        ArrayList<TrianColorRGBDepthTrans> goodTriangles = triangles.stream()
                .filter(
                        triangle -> triangle.inside(point)
                ).collect(
                        Collectors.toCollection(ArrayList::new)
                );
        goodTriangles.sort((a, b) -> {
            double valueA = a.getDepth();
            double valueB = b.getDepth();
            if (valueA == valueB) {
                return 0;
            }
            if (valueA > valueB) {
                return -1;
            }
            return 1;
        });
        double alpha = 1;
        for (TrianColorRGBDepthTrans triangle : goodTriangles) {
            double[] temp = triangle.getColor();
            double k = alpha * triangle.getTrans();
            alpha *= 1 - triangle.getTrans();
            for (int i = 0; i < 3; i++) {
                result[i] += k * temp[i];
            }
        }
        return result;
    }
}
