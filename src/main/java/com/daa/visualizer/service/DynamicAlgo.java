package com.daa.visualizer.service;

import com.daa.visualizer.model.FloydStep;
import org.springframework.stereotype.Service; // ye import zaroori
import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicAlgo {
    private static final int INF = 999;

    public List<FloydStep> getFloydWarshallSteps(int[][] graph) {
        int n = graph.length;
        int[][] dist = new int[n][n];
        List<FloydStep> steps = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        steps.add(new FloydStep(-1, -1, -1, cloneMatrix(dist), false, "Initial distance matrix"));

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    boolean updated = false;
                    String msg = "Checking " + i + " → " + j + " via " + k;

                    if (dist[i][k] < INF && dist[k][j] < INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        updated = true;
                        msg = "Updated: dist[" + i + "][" + j + "] = " + dist[i][j] + " via " + k;
                    }

                    steps.add(new FloydStep(k, i, j, cloneMatrix(dist), updated, msg));
                }
            }
        }

        // Yaha pe final summary bana diya
        StringBuilder summary = new StringBuilder("Shortest distances from each source node:\n");
        for (int i = 0; i < n; i++) {
            summary.append("From node ").append(i).append(": ");
            for (int j = 0; j < n; j++) {
                if (dist[i][j] >= INF) {
                    summary.append("INF ");
                } else {
                    summary.append(dist[i][j]).append(" ");
                }
            }
            summary.append("\n");
        }

        steps.add(new FloydStep(-1, -1, -1, cloneMatrix(dist), false, summary.toString()));
        return steps;
    }

    private int[][] cloneMatrix(int[][] mat) {
        int n = mat.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(mat[i], 0, copy[i], 0, n);
        }
        return copy;
    }
}