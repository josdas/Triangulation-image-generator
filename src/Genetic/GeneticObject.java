package Genetic;

/**
 * Created by Stas on 06.06.2017.
 */

public interface GeneticObject<T> {
    T mutation(T obj);

    double eval(T obj);

    T crossover(T a, T b);

    T genRand();

    double distance(T a, T b);
}
