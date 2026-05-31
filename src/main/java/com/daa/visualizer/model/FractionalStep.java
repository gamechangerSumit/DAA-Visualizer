package com.daa.visualizer.model;

public class FractionalStep {
    public double[] taken; // har item ka kitna weight liya
    public double totalValue; // ab tak ka total profit
    public double remainingCapacity; // bachi capacity
    public int currentItem; // konsa item check ho raha -1 = done
    public String message; // step ka explanation

    public FractionalStep(double[] taken, double totalValue, double remainingCapacity, int currentItem, String message) {
        this.taken = taken;
        this.totalValue = totalValue;
        this.remainingCapacity = remainingCapacity;
        this.currentItem = currentItem;
        this.message = message;
    }
}