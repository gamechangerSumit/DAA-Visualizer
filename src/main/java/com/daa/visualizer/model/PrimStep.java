package com.daa.visualizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrimStep {
    private int currentNode;
    private List<Integer> mstNodes;
    private int[] currentEdge;
    private List<int[]> mstEdges;
    private int totalWeight;
    private String message;
}