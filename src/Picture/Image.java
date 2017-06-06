package Picture;

/**
 * Created by Stas on 06.06.2017.
 */
public class Image {
    int h, w;
    double[][] data;

    public Image(int h, int w) {
        this.h = h;
        this.w = w;
        data = new double[h][w];
    }

    public void set(int x, int y, double depth) {
        data[x][y] = depth;
    }

    public double get(int x, int y) {
        return data[x][y];
    }

    public static double distance(Image a, Image b) {
        assert a.h == b.h && a.w == b.w;
        double result = 0;
        for (int i = 0; i < a.h; i++) {
            for (int j = 0; j < a.w; j++) {
                result += Math.pow(a.get(i, j) - b.get(i, j), 2);
            }
        }
        return result / a.h / a.w;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }
}
