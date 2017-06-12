import Genetic.Generator;
import Picture.TriangleImageDepth;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageTest {
    final static int MAX_TIME = 60 * 60 * 11;
    final static int NUMBER_OF_SECTION = 10;

    static Random random = new Random();

    static void draw(Image image) {
        ImageFrame frame = new ImageFrame(image);
        frame.setVisible(true);
    }

    static BufferedImage getImageFromFile() {
        try {
            return ImageIO.read(new File("src/test_2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        BufferedImage imageFile = getImageFromFile();

        Picture.Image image = new Picture.Image(imageFile, 0.2);
        GeneticImageModelC geneticImage = new GeneticImageModelC(image);
        Generator<TriangleImageDepth, GeneticImageModelC> generator = new Generator<>(geneticImage, 10, 20, 10);

        long startTime = System.currentTimeMillis();
        int sectionIndex = 0;
        while(true) {
            long timeSpent = (System.currentTimeMillis() - startTime) / 1000;
            if(timeSpent > MAX_TIME) {
                break;
            }
            if (timeSpent > MAX_TIME / NUMBER_OF_SECTION * sectionIndex) {
                sectionIndex++;
                GeneticImageModelC.MAX_SIZE = 30 + sectionIndex * 25;
                System.out.println("Switch");
            }
            System.out.println("Time spent:" + timeSpent);
            generator.generation(10);
        }

        assert imageFile != null;
        draw(generator.getBest().getImage(imageFile.getWidth(), imageFile.getHeight()).getImage());
        draw(generator.getBest().getImage(image.getW(), image.getH()).getImage());
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