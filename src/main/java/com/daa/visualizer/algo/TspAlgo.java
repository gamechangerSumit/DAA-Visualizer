package com.daa.visualizer.algo;
import com.daa.visualizer.model.TspStep;
import java.util.*;

public class TspAlgo {
    public static List<TspStep> nearestNeighbor(int[][] dist) {
        List<TspStep> steps = new ArrayList<>();
        int n = dist.length;
        if(n < 2) return steps;

        boolean[] visited = new boolean[n];
        List<Integer> path = new ArrayList<>();
        double totalDist = 0;

        int curr = 0;
        visited[curr] = true; // <-- yaha fix
        path.add(curr);
        steps.add(new TspStep(new ArrayList<>(path), 0.0, "Start at City 0"));

        for(int i = 1; i < n; i++) {
            int next = -1;
            int minDist = Integer.MAX_VALUE;

            for(int j = 0; j < n; j++) {
                if(!visited[j] && dist[curr][j] < minDist) {
                    minDist = dist[curr][j];
                    next = j;
                }
            }

            if(next == -1) break; // safety

            visited[next] = true; // <-- yaha fix
            path.add(next);
            totalDist += minDist;

            steps.add(new TspStep(
                    new ArrayList<>(path),
                    totalDist,
                    "City " + curr + " -> City " + next + " | Cost: " + minDist
            ));
            curr = next;
        }

        // Return to start
        totalDist += dist[curr][0];
        path.add(0);
        steps.add(new TspStep(
                new ArrayList<>(path),
                totalDist,
                "City " + curr + " -> City 0 | Return | Total Cost: " + (int)totalDist
        ));

        return steps;
    }
}
