package Picture;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Stas on 12.06.2017.
 */
public abstract class AbsImage {
    int h, w;

    public AbsImage(int h, int w) {
        this.h = h;
        this.w = w;
    }

    protected void build(BufferedImage image, double t) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int x = (int) (j / t);
                int y = (int) (i / t);
                int temp = image.getRGB(x, y);
                int rgb[] = new int[]{
                        (temp >> 16) & 0xff,    //red
                        (temp >> 8 ) & 0xff,    //green
                        (temp      ) & 0xff     //blue
                };
                set(i, j, rgb);
            }
        }
    }

    abstract void set(int x, int y, int[] rgb);

    public abstract int[] get_colors(int x, int y);

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
                int rgb[] = get_colors(j, i);
                pixels[j * w + i] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
            }
        }

        BufferedImage pixelImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        pixelImage.setRGB(0, 0, w, h, pixels, 0, w);
        return pixelImage;
    }


    public static double distance(AbsImage a, AbsImage b) {
        assert a.h == b.h && a.w == b.w;
        double result = 0;
        for (int i = 0; i < a.h; i++) {
            for (int j = 0; j < a.w; j++) {
                int[] f = a.get_colors(i, j);
                int[] s = b.get_colors(i, j);
                for (int k = 0; k < 3; k++) {
                    result += Math.pow(Math.abs(f[k] - s[k]), 2);
                }
            }
        }
        return result / a.h / a.w;
    }
}
