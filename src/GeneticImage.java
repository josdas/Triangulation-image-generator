import Genetic.GeneticObject;
import Picture.Image;
import Picture.TriangleImage;

/**
 * Created by Stas on 06.06.2017.
 */
public class GeneticImage implements GeneticObject<TriangleImage> {
    Image image;

    @Override
    public TriangleImage mutation(TriangleImage obj) {
        return null;
    }

    @Override
    public double eval(TriangleImage obj) {
        return Image.distance(
                obj.getImage(image.getH(), image.getW()),
                image
        );
    }

    @Override
    public TriangleImage crossover(TriangleImage a, TriangleImage b) {
        return null;
    }

    @Override
    public TriangleImage genRand() {
        return null;
    }
}
