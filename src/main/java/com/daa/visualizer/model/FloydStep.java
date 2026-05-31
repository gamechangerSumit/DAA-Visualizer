package com.daa.visualizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloydStep {
    private int k; // intermediate node
    private int i; // source
    private int j; // destination
    private int[][] dist; // current distance matrix
    private boolean updated; // kya update hua
    private String message;
}