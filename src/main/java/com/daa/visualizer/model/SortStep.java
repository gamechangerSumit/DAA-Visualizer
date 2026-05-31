package com.daa.visualizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortStep {
    private int[] array; // Array ka current state
    private int comparingIndex1; // Konsa 2 element compare ho rahe
    private int comparingIndex2;
    private boolean swapped; // Swap hua ya nahi
    private String message; // "Comparing 5 and 3" jaisa text
}