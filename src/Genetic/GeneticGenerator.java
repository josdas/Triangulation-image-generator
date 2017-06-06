package Genetic;

import java.util.ArrayList;

/**
 * Created by Stas on 06.06.2017.
 */
public interface GeneticGenerator<T, E extends GeneticObject<T>> {
    T getBest();

    void generation(int n);

    ArrayList<T> getAllObject();
}
