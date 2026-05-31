package com.daa.visualizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DijkstraStep {
    private String message; // Step ka message
    private int currentNode; // Current visited node
    private int[] visitedNodes; // Visited nodes list
    private int[] distances; // Har node ka distance
    private int[] previous; // Path banane ke liye
    private int[] currentEdge; // [u,v] edge highlight
    private int[] path; // Final path: [0,2,1,3]
    private int totalCost; // Total distance
}