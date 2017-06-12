package Picture;

import java.awt.image.BufferedImage;

/**
 * Created by Stas on 06.06.2017.
 */
public class Image extends AbsImage {
    double[][][] data;

    public Image(int h, int w) {
        super(h, w);
        data = new double[h][w][3];
    }

    public Image(BufferedImage image, double t) {
        super((int) (image.getHeight() * t), (int) (image.getWidth() * t));
        data = new double[h][w][3];
        build(image, t);
    }

    public void set(int x, int y, int color, double val) {
        if (val < 0) {
            val = 0;
        }
        if (val > 255) {
            val = 255;
        }
        data[x][y][color] = val;
    }

    public double get(int x, int y, int color) {
        return data[x][y][color];
    }

    @Override
    void set(int x, int y, int[] rgb) {
        for (int i = 0; i < 3; i++) {
            data[x][y][i] = (double) rgb[i];
        }
    }

    @Override
    public int[] get_colors(int x, int y) {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = (int) data[x][y][i];
        }
        return rgb;
    }
}
