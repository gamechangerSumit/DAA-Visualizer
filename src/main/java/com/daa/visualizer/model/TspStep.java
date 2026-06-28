package com.daa.visualizer.model;
import java.util.List;

public class TspStep {
    private List<Integer> visitedOrder;
    private double totalDistance;
    private String description;

    public TspStep(List<Integer> visitedOrder, double totalDistance, String description) {
        this.visitedOrder = visitedOrder;
        this.totalDistance = totalDistance;
        this.description = description;
    }

    public List<Integer> getVisitedOrder() { return visitedOrder; }
    public double getTotalDistance() { return totalDistance; }
    public String getDescription() { return description; }
}