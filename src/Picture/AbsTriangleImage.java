package Picture;

import Geometry.Point;

/**
 * Created by Stas on 12.06.2017.
 */
public abstract class AbsTriangleImage {
    static int number = 0;
    int myNumber;

    public AbsTriangleImage() {
        myNumber = number++;
    }

    public abstract int[] getColor(Point point);

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


    public ImageWB getImageWB(int nh, int nw) {
        ImageWB result = new ImageWB(nh, nw);
        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nw; j++) {
                Point point = new Point((double) i / nh, (double) j / nw);
                result.set(i, j, getColor(point));
            }
        }
        return result;
    }

    public abstract int size();
}
