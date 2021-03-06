package Picture.Triangular;

import Geometry.Point;
import Picture.ImageRGB;

/**
 * Created by Stas on 12.06.2017.
 */
public abstract class AbsTriangleImage {
    static int number = 0;
    int myNumber;

    public AbsTriangleImage() {
        myNumber = number++;
    }

    public int getNumber() {
        return myNumber;
    }

    protected abstract double[] get(Point point);

    public abstract int size();

    public int[] getColor(Point point) {
        int[] rgb = new int[3];
        double[] temp = get(point);
        for (int k = 0; k < 3; k++) {
            int t = (int) (temp[k] * 255);
            if (t < 0) {
                t = 0;
            }
            if (t > 255) {
                t = 255;
            }
            rgb[k] = t;
        }
        return rgb;
    }

    public ImageRGB getImage(int nh, int nw) {
        ImageRGB result = new ImageRGB(nh, nw);
        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nw; j++) {
                Point point = new Point((double) i / nh, (double) j / nw);
                result.set(i, j, getColor(point));
            }
        }
        return result;
    }
}
