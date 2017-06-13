import Genetic.Generator;
import Genetic.GeneratorImage;
import Picture.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

// DONE сделай очистку "лишних" треугольников
// TODO сделай нормальный порядок рендера (отказаться от глубины)
// DONE нормальный код
// DONE RGB
// DONE авто цвет
// TODO нормальный интерфейс
// DONE RGB TriangleImage
// TODO class rgb

public class ImageTest {
    final static int MAX_TIME = 60 * 20;
    final static int MAX_TIME_FOR_ONE_COLOR = MAX_TIME / 3;
    final static int NUMBER_OF_SECTION = 7;

    static Random random = new Random();

    static void draw(Image image) {
        ImageFrame frame = new ImageFrame(image);
        frame.setVisible(true);
    }

    static BufferedImage getImageFromFile() {
        try {
            return ImageIO.read(new File("src/test_3.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void drawTriangleImage(AbsTriangleImage[] triangleImage, int h, int w) {
        ImageWB[] imagesWB = new ImageWB[3];
        for (int i = 0; i < 3; i++) {
            imagesWB[i] = triangleImage[i].getImageWB(h, w);
        }
        Picture.Image resultImage = new Picture.Image(imagesWB);
        draw(resultImage.getImage());
    }

    public static void drawTriangleImage(AbsTriangleImage triangleImage, int h, int w) {
        draw(triangleImage.getImage(h, w).getImage());
    }

    public static void TestD() {
        BufferedImage imageFile = getImageFromFile();

        Picture.Image image = new Picture.Image(imageFile, 0.3);

        ImageWB[] imagesWB = image.toImagesWB();
        TriangleImageDepth[] triangleImage = new TriangleImageDepth[3];
        double[] results = new double[3];

        for (int i = 0; i < 3; i++) {
            GeneticImageModelD geneticImage = new GeneticImageModelD(imagesWB[i]);
            Generator<TriangleImageDepth, GeneticImageModelD> generator = new GeneratorImage<>(geneticImage, 10, 20, 10);
            long startTime = System.currentTimeMillis();
            int sectionIndex = 0;
            if(i == 0) while (true) {
                long timeSpent = (System.currentTimeMillis() - startTime) / 1000;
                if (timeSpent > MAX_TIME_FOR_ONE_COLOR) {
                    break;
                }
                if (timeSpent > MAX_TIME_FOR_ONE_COLOR / NUMBER_OF_SECTION * sectionIndex) {
                    sectionIndex++;
                    GeneticImageModelD.MAX_SIZE += 25;
                    System.out.println("Switch");
                }
                System.out.println("Time spent:" + timeSpent);
                generator.generation(10);
            }
            triangleImage[i] = generator.getBest();
            results[i] = geneticImage.eval(triangleImage[i]);
        }

        for (int i = 0; i < 3; i++) {
            System.out.println("The best with color " + i + " has result: " + results[i]);
        }

        assert imageFile != null;
        drawTriangleImage(triangleImage, imageFile.getWidth(), imageFile.getHeight());
        drawTriangleImage(triangleImage, image.getW(), image.getH());
        draw(image.getImage());
        draw(imageFile);
    }

    public static void main(String[] args) {
        BufferedImage imageFile = getImageFromFile();
        Picture.Image image = new Picture.Image(imageFile, 0.3);

        GeneticImageModelE geneticImage = new GeneticImageModelE(image);
        Generator<TriangleImageRGBDepth, GeneticImageModelE> generator = new GeneratorImage<>(geneticImage, 10, 20, 10);
        long startTime = System.currentTimeMillis();
        int sectionIndex = 0;
        while (true) {
            long timeSpent = (System.currentTimeMillis() - startTime) / 1000;
            if (timeSpent > MAX_TIME) {
                break;
            }
            if (timeSpent > MAX_TIME / NUMBER_OF_SECTION * sectionIndex) {
                sectionIndex++;
                GeneticImageModelE.MAX_SIZE += 25;
                System.out.println("Switch");
            }
            System.out.println("Time spent:" + timeSpent);
            generator.generation(10);
        }
        TriangleImageRGBDepth triangleImage = generator.getBest();


        assert imageFile != null;
        drawTriangleImage(triangleImage, imageFile.getWidth(), imageFile.getHeight());
        drawTriangleImage(triangleImage, image.getW(), image.getH());
        draw(image.getImage());
        draw(imageFile);
    }

}

class ImageFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 700;
    public static final int DEFAULT_HEIGHT = 700;

    public ImageFrame(Image image) {
        setTitle("Triangulation");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        ImageComponent component = new ImageComponent(image);
        add(component);
    }
}

class ImageComponent extends JComponent {
    private Image image;

    public ImageComponent(Image image) {
        this.image = image;
    }

    public void paintComponent(Graphics g) {
        if (image == null) {
            return;
        }
        g.drawImage(image, 0, 0, 500, 500, null);
    }
}