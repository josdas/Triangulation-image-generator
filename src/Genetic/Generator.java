package Genetic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stas on 06.06.2017.
 */
public abstract class Generator<T, E extends GeneticObject<T>> implements GeneticGenerator<T, E> {
    private ArrayList<T> objects;

    E option;
    Random random;
    int numberMutation;
    int numberCross;
    int generationSize;
    int numberGeneration = 0;

    public Generator(E option, int generationSize, int numberMutation, int numberCross) {
        this.option = option;
        random = new Random();
        this.numberMutation = numberMutation;
        this.numberCross = numberCross;
        this.generationSize = generationSize;
        objects = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            objects.add(option.genRand());
        }
    }

    @Override
    public T getBest() {
        ArrayList<T> temp = getAllObject();
        double maxValue = Double.POSITIVE_INFINITY;
        T result = null;
        for (T obj : temp) {
            double val = option.eval(obj);
            if (val < maxValue) {
                maxValue = val;
                result = obj;
            }
        }
        return result;
    }

    protected abstract ArrayList<T> newGeneration(ArrayList<T> cur);

    @Override
    public void generation(int n) {
        System.out.println("Start #" + numberGeneration);
        for (int ind = 0; ind < n; numberGeneration++, ind++) {
            objects = newGeneration(objects);
        }
        System.out.println("Finish generation #" + numberGeneration
                + " with result:" + option.eval(getBest())
                + " Size: " + objects.size());
    }

    @Override
    public ArrayList<T> getAllObject() {
        return objects;
    }
}
