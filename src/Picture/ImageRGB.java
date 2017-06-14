package Picture;

import java.awt.image.BufferedImage;

/**
 * Created by Stas on 06.06.2017.
 */
public class ImageRGB extends AbsImage {
    double[][][] data;

    public ImageRGB(int h, int w) {
        super(h, w);
        data = new double[h][w][3];
    }

    public ImageRGB(ImageWB[] imagesWB) {
        super(imagesWB[0].getH(), imagesWB[0].getW());
        assert imagesWB[0].getH() == imagesWB[1].getH()
                && imagesWB[0].getH() == imagesWB[2].getH()
                && imagesWB[0].getW() == imagesWB[1].getW()
                && imagesWB[0].getW() == imagesWB[2].getW();
        data = new double[h][w][3];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                for (int k = 0; k < 3; k++) {
                    data[i][j][k] = imagesWB[k].get(i, j);
                }
            }
        }
    }

    public ImageWB[] toImagesWB() {
        ImageWB[] imagesWB = new ImageWB[3];
        for (int i = 0; i < 3; i++) {
            imagesWB[i] = new ImageWB(h, w);
        }
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                for (int k = 0; k < 3; k++) {
                    imagesWB[k].set(i, j, data[i][j][k]);
                }
            }
        }
        return imagesWB;
    }

    public ImageRGB(BufferedImage image, double t) {
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
    public void set(int x, int y, int[] rgb) {
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
