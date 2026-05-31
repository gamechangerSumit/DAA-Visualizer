package com.daa.visualizer.algo;

import com.daa.visualizer.model.FractionalStep;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class GreedyAlgo {

    public List<FractionalStep> getFractionalKnapsackSteps(int capacity, int[] weights, int[] values) {
        List<FractionalStep> steps = new ArrayList<>();
        int n = weights.length;

        // Item class for sorting
        class Item {
            double ratio;
            int idx;
            double w, v;

            Item(int i) {
                idx = i;
                w = weights[i];
                v = values[i];
                ratio = v / w;
            }
        }

        // Create and sort items by ratio descending
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            items.add(new Item(i));
        }
        items.sort((a, b) -> Double.compare(b.ratio, a.ratio));

        double[] taken = new double[n];
        double totalValue = 0;
        double remaining = capacity;

        steps.add(new FractionalStep(
                taken.clone(),
                0,
                capacity,
                -1,
                "Items sorted by value/weight ratio: " +
                        items.stream().map(i -> "Item" + i.idx + "(" + String.format("%.2f", i.ratio) + ")")
                                .reduce((a, b) -> a + ", " + b).orElse("")
        ));

        for (Item item : items) {
            if (remaining <= 0) break;

            steps.add(new FractionalStep(
                    taken.clone(),
                    totalValue,
                    remaining,
                    item.idx,
                    "Checking Item " + item.idx + " (w=" + item.w + ", v=" + item.v +
                            ", ratio=" + String.format("%.2f", item.ratio) + ")"
            ));

            if (item.w <= remaining) {
                // Take full item
                taken[item.idx] = item.w;
                totalValue += item.v;
                remaining -= item.w;
                steps.add(new FractionalStep(
                        taken.clone(),
                        totalValue,
                        remaining,
                        item.idx,
                        "Took FULL Item " + item.idx + " (weight=" + item.w +
                                ", value=" + item.v + "). Total = " + String.format("%.2f", totalValue)
                ));
            } else {
                // Take fraction
                double fraction = remaining / item.w;
                taken[item.idx] = remaining;
                double valueAdded = item.v * fraction;
                totalValue += valueAdded;
                steps.add(new FractionalStep(
                        taken.clone(),
                        totalValue,
                        0,
                        item.idx,
                        "Took " + String.format("%.2f", fraction * 100) + "% of Item " + item.idx +
                                " (weight=" + String.format("%.2f", remaining) +
                                ", value=" + String.format("%.2f", valueAdded) +
                                "). Bag FULL. Total = " + String.format("%.2f", totalValue)
                ));
                remaining = 0;
            }
        }

        steps.add(new FractionalStep(
                taken.clone(),
                totalValue,
                remaining,
                -1,
                "Algorithm complete! Max Profit = " + String.format("%.2f", totalValue)
        ));

        return steps;
    }
}