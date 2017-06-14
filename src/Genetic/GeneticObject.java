package Genetic;

/**
 * Created by Stas on 06.06.2017.
 */

public interface GeneticObject<T> {
    double eval(T obj);

    T mutation(T obj);

    T crossover(T a, T b);

    T genRand();
}
