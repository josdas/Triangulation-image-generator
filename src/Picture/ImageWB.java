package Picture;

import java.awt.image.BufferedImage;

/**
 * Created by Stas on 06.06.2017.
 */
public class ImageWB extends AbsImage {
    double[][] data;

    public ImageWB(int h, int w) {
        super(h, w);
        data = new double[h][w];
    }

    public ImageWB(BufferedImage image, double t) {
        super((int) (image.getHeight() * t), (int) (image.getWidth() * t));
        data = new double[h][w];
        build(image, t);
    }

    public void set(int x, int y, double depth) {
        if (depth < 0) {
            depth = 0;
        }
        if (depth > 255) {
            depth = 255;
        }
        data[x][y] = depth;
    }

    @Override
    public void set(int x, int y, int[] rgb) {
        data[x][y] = (rgb[0] + rgb[1] + rgb[2]) / 3.0;
    }

    @Override
    public int[] get_colors(int x, int y) {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = (int) data[x][y];
        }
        return rgb;
    }

    public double get(int x, int y) {
        return data[x][y];
    }
}
