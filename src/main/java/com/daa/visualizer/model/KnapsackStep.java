package com.daa.visualizer.model;

public class KnapsackStep {
    private int[][] dp;
    private int item; // current item row
    private int capacity; // current capacity column
    private int prevValue; // pehle kya value thi
    private String message; // step ka explanation

    public KnapsackStep(int[][] dp, int item, int capacity, int prevValue, String message) {
        this.dp = dp;
        this.item = item;
        this.capacity = capacity;
        this.prevValue = prevValue;
        this.message = message;
    }

    // Getters - JSON ke liye zaroori
    public int[][] getDp() { return dp; }
    public int getItem() { return item; }
    public int getCapacity() { return capacity; }
    public int getPrevValue() { return prevValue; }
    public String getMessage() { return message; }
}