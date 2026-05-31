package com.daa.visualizer.service;

import com.daa.visualizer.model.KnapsackStep;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class KnapsackAlgo {

    public List<KnapsackStep> getKnapsackSteps(int W, int[] wt, int[] val) {
        if (wt.length!= val.length) {
            throw new IllegalArgumentException("Weights=" + wt.length + ", Values=" + val.length);
        }

        int n = wt.length;
        int[][] dp = new int[n + 1][W + 1];
        List<KnapsackStep> steps = new ArrayList<>();
        steps.add(new KnapsackStep(cloneArray(dp), 0, 0, -1, "Start"));

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                int prevValue = dp[i][w];

                if (wt[i - 1] <= w) {
                    // IMPORTANT: w-wt[i-1] yahi safe hai kyunki condition check kar li
                    int include = val[i - 1] + dp[i - 1][w - wt[i - 1]];
                    int exclude = dp[i - 1][w];
                    dp[i][w] = Math.max(include, exclude);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }

                if (dp[i][w]!= prevValue) {
                    String msg = String.format("Item %d, W=%d = %d", i, w, dp[i][w]);
                    steps.add(new KnapsackStep(cloneArray(dp), i, w, prevValue, msg));
                }
            }
        }

        steps.add(new KnapsackStep(cloneArray(dp), n, W, dp[n][W], "Max = " + dp[n][W]));
        return steps;
    }

    private int[][] cloneArray(int[][] arr) {
        int[][] clone = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) clone[i] = arr[i].clone();
        return clone;
    }
}