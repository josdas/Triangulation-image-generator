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

    public Image(BufferedImage image, double t) {
        this.h = (int)(image.getHeight() * t);
        this.w = (int)(image.getWidth() * t);
        this.data = new double[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int x = (int)(j / t);
                int y = (int)(i / t);
                int temp = image.getRGB(x, y);
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
                double f = a.get(i, j);
                double s = b.get(i, j);
                if(f > s) {
                    result += Math.pow(Math.abs(a.get(i, j) - b.get(i, j)), 1);
                }
                else {
                    result += Math.pow(Math.abs(a.get(i, j) - b.get(i, j)), 1);
                }
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
                if(e < 0) {
                    e = 0;
                }
                pixels[j * w + i] = new Color(e, e, e).getRGB();
            }
        }

        BufferedImage pixelImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        pixelImage.setRGB(0, 0, w, h, pixels, 0, w);
        return pixelImage;
    }
}
