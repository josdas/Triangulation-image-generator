package Genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Stas on 06.06.2017.
 */
public class Generator<T, E extends GeneticObject<T>> implements GeneticGenerator<T, E> {
    E option;
    Random random;

    ArrayList<T> objects;
    int numberMutation;
    int numberCross;
    int generationSize;
    int i = 0;

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

    @Override
    public void generation(int n) {
        for (int ind = 0; ind < n; i++, ind++) {
            System.out.println("Start #" + i);

            ArrayList<T> newObj = new ArrayList<>();
            for (int j = 0; j < numberMutation; j++) {
                newObj.add(option.mutation(
                        objects.get(random.nextInt(objects.size()))
                ));
            }
            int numberPairs = objects.size() * objects.size();
            ArrayList<Integer> pairs = new ArrayList<>();
            for (int j = 0; j < numberPairs; j++) {
                pairs.add(j);
            }
            Collections.shuffle(pairs);

            for (int j = 0; j < numberCross; j++) {
                newObj.add(option.mutation(
                        option.crossover(
                            objects.get(pairs.get(j) % objects.size()),
                            objects.get(pairs.get(j) / objects.size())
                        )
                ));
            }
            newObj.addAll(objects.stream().collect(Collectors.toList()));

            newObj.sort((a, b) -> {
                double valueA = option.eval(a);
                double valueB = option.eval(b);
                if (valueA == valueB) {
                    return 0;
                }
                if (valueA < valueB) {
                    return -1;
                }
                return 1;
            });

            ArrayList<T> selection = new ArrayList<>();
            for (int j = 0; j < newObj.size(); j++) {
                double c = (double) j / newObj.size() * 3 - 0.3;
                if (random.nextDouble() > c) {
                    selection.add(newObj.get(j));
                }
            }

            objects = selection;

            System.out.println("Finish generation #" + i + " with result:" + option.eval(getBest()) + " Size: " + objects.size());
        }
    }

    @Override
    public ArrayList<T> getAllObject() {
        return objects;
    }
}
