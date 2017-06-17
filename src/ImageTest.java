import Genetic.Generator;
import Genetic.GeneratorImage;
import Genetic.GeneticObject;
import Models.*;
import Picture.ImageRGB;
import Picture.Triangular.AbsTriangleImage;
import Picture.Triangular.RGB.TrianImgRGBDepth;
import Picture.Triangular.RGB.TrianImgRGBDepthTrans;
import Picture.Triangular.RGB.TrianImgRGBDepthTransOrdered;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// DONE сделай очистку "лишних" треугольников
// DONE сделай нормальный порядок рендера (отказаться от глубины)
// DONE нормальный код
// DONE RGB
// DONE авто цвет как мутация
// TODO UI
// DONE RGB TriangleImage
// TODO class rgb
// DONE нормальная иерархия в GeneticImageModel
// TODO temp файлы и сохренние результата в .txt
// DONE адаптивный коф. мутации
// DONE метрика через контур
// DONE глубина + прозрачность
// DONE мутация: деленеи на 3 части
// DONE треугольное поле

public class ImageTest {
    final static int MAX_TIME = 60 * 60 * 2;
    final static int NUMBER_OF_SECTION = 30;

    static void draw(Image image) {
        ImageFrame frame = new ImageFrame(image);
        frame.setVisible(true);
    }

    static BufferedImage getImageFromFile() {
        try {
            return ImageIO.read(new File("test_3.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void drawTriangleImage(AbsTriangleImage triangleImage, int h, int w) {
        draw(triangleImage.getImage(h, w).getImage());
    }

    public static void TestE(ImageRGB realImage,
                             ImageRGB image) {
        GeneticImageModelE geneticImage = new GeneticImageModelE(image);
        Generator<TrianImgRGBDepth, GeneticImageModelE> generator
                = new GeneratorImage<>(geneticImage, 10, 20, 10);

        AbsTest(geneticImage, generator, realImage, image);
    }

    public static void TestF(ImageRGB realImage,
                             ImageRGB image) {
        GeneticImageModelF geneticImage = new GeneticImageModelF(image);
        Generator<TrianImgRGBDepthTrans, GeneticImageModelF> generator
                = new GeneratorImage<>(geneticImage, 10, 30, 0);

        AbsTest(geneticImage, generator, realImage, image);
    }


    public static void TestG(ImageRGB realImage,
                             ImageRGB image) {
        GeneticImageModelG geneticImage = new GeneticImageModelG(image);
        Generator<TrianImgRGBDepthTransOrdered, GeneticImageModelG> generator
                = new GeneratorImage<>(geneticImage, 10, 10, 10);

        AbsTest(geneticImage, generator, realImage, image);
    }

    public static void TestH(ImageRGB realImage,
                             ImageRGB image) {
        GeneticImageModelH geneticImage = new GeneticImageModelH(image);
        Generator<TrianImgRGBDepthTransOrdered, GeneticImageModelH> generator
                = new GeneratorImage<>(geneticImage, 10, 20, 0);

        AbsTest(geneticImage, generator, realImage, image);
    }
    public static <T extends AbsTriangleImage, S extends ImageModel & GeneticObject<T>> void AbsTest(
            S geneticImage,
            Generator<T, S> generator,
            ImageRGB realImage,
            ImageRGB image) {

        long startTime = System.currentTimeMillis();
        int sectionIndex = 0;
        double lastResult = 0;
        while (true) {
            long timeSpent = (System.currentTimeMillis() - startTime) / 1000;
            if (timeSpent > MAX_TIME) {
                break;
            }
            if (timeSpent > MAX_TIME / NUMBER_OF_SECTION * sectionIndex) {
                sectionIndex++;
                geneticImage.MAX_SIZE += 5;
                System.out.println("Switch");
            }
            System.out.println("Time spent:" + timeSpent);
            generator.generation(10);
            double result = geneticImage.eval(generator.getBest());
            if (result < lastResult - 1) {
                geneticImage.MUTATION_COEF = 0.2;
            } else {
                geneticImage.MUTATION_COEF += 0.03;
                geneticImage.MUTATION_COEF = Math.min(geneticImage.MUTATION_COEF, 0.6);
                System.out.println("Changed coef: " + geneticImage.MUTATION_COEF);
            }
            System.out.println("Size of the best: " + generator.getBest().size());
            lastResult = result;
        }
        AbsTriangleImage triangleImage = generator.getBest();


        assert realImage != null;
        drawTriangleImage(triangleImage, realImage.getW(), realImage.getH());
        draw(triangleImage.getImage(realImage.getW(), realImage.getH()).toCircuit().getImage());
        draw(realImage.getImage());
        draw(realImage.toCircuit().getImage());
    }

    public static void main(String[] args) {
        BufferedImage ImageFile = getImageFromFile();
        ImageRGB image = new ImageRGB(ImageFile, 0.2);
        ImageRGB realImage = new ImageRGB(ImageFile, 1);

        TestG(realImage, image);
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