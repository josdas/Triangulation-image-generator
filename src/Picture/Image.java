package Picture;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    public Image(BufferedImage image) {
        this.h = image.getHeight();
        this.w = image.getWidth();
        this.data = new double[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int temp = image.getRGB(j, i);
                int rgb[] = new int[]{
                        (temp >> 16) & 0xff, //red
                        (temp >> 8) & 0xff, //green
                        (temp) & 0xff  //blue
                };
                data[i][j] = (rgb[0] + rgb[1] + rgb[2]) / 3.0;
            }
        }
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

    public java.awt.Image getImage() {
        int[] pixels = new int[w * h];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int e = (int) data[j][i];
                if (e > 255) {
                    e = 255;
                }
                pixels[j * w + i] = new Color(e, e, e).getRGB();
            }
        }

        BufferedImage pixelImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        pixelImage.setRGB(0, 0, w, h, pixels, 0, w);
        return pixelImage;
    }
}