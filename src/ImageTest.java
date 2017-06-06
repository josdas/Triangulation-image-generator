import Genetic.Generator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageTest {
    static Random random = new Random();

    static void draw(Image image) {
        ImageFrame frame = new ImageFrame(image);
        frame.setVisible(true);
    }

    static BufferedImage getImageFromFile() {
        try {
            return ImageIO.read(new File("src/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Picture.Image image = new Picture.Image(getImageFromFile());
        GeneticImage geneticImage = new GeneticImage(image);
        Generator<Picture.TriangleImage, GeneticImage> generator = new Generator<>(geneticImage, 10, 3, 2);

        generator.generation(10);
        draw(generator.getBest().getImage(image.getH(), image.getW()).getImage());
        draw(image.getImage());
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
        g.drawImage(image, 0, 0, null);
    }
}