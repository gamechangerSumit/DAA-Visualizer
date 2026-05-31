package com.daa.visualizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step {
    private int[] array;
    private int[] comparing;
    private int[] sorted;
    private int pivot; // int hona chahiye, -1 = no pivot
    private String message;
}