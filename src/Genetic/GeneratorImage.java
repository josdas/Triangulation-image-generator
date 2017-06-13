package Genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by Stas on 12.06.2017.
 */
public class GeneratorImage<T, E extends GeneticObject<T>> extends Generator<T, E> {
    final static double START_PROB = 0.3;
    final static double SCALE_PROB = 3;

    public GeneratorImage(E option, int generationSize, int numberMutation, int numberCross) {
        super(option, generationSize, numberMutation, numberCross);
    }

    @Override
    protected ArrayList<T> newGeneration(ArrayList<T> objects) {
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
            double c = (double) j / newObj.size() * SCALE_PROB - START_PROB;
            if (random.nextDouble() > c) {
                selection.add(newObj.get(j));
            }
        }

        for (T aSelection : selection) {
            option.clean(aSelection);
        }

        return selection;
    }
}
