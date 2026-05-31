package com.daa.visualizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphStep {
    private List<Integer> visited; // Visited nodes
    private List<int[]> currentEdge; // Current edge highlight karne ke liye [u,v]
    private int currentNode; // Current node
    private String message; // Step description
    private Object extra; // Extra data - null rakh
}