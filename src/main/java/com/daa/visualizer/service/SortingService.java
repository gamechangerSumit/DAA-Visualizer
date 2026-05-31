package com.daa.visualizer.service;

import com.daa.visualizer.model.SortStep;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    public List<SortStep> getBubbleSortSteps(int[] arr) {
        List<SortStep> steps = new ArrayList<>();
        int n = arr.length;
        int[] tempArr = arr.clone(); // Original array ko change mat karo

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Step 1: Comparison dikhao
                steps.add(new SortStep(
                        tempArr.clone(),
                        j, j + 1,
                        false,
                        "Comparing " + tempArr[j] + " and " + tempArr[j + 1]
                ));

                if (tempArr[j] > tempArr[j + 1]) {
                    // Swap karo
                    int temp = tempArr[j];
                    tempArr[j] = tempArr[j + 1];
                    tempArr[j + 1] = temp;

                    // Step 2: Swap ke baad wala state
                    steps.add(new SortStep(
                            tempArr.clone(),
                            j, j + 1,
                            true,
                            "Swapped " + tempArr[j + 1] + " and " + tempArr[j]
                    ));
                }
            }
        }

        // Final sorted array
        steps.add(new SortStep(
                tempArr.clone(),
                -1, -1,
                false,
                "Array Sorted!"
        ));

        return steps;
    }

    // Selection Sort
    public List<SortStep> getSelectionSortSteps(int[] arr) {
        List<SortStep> steps = new ArrayList<>();
        int n = arr.length;
        int[] tempArr = arr.clone();

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            steps.add(new SortStep(tempArr.clone(), i, minIdx, false, "Finding minimum from index " + i));

            for (int j = i + 1; j < n; j++) {
                steps.add(new SortStep(tempArr.clone(), minIdx, j, false, "Comparing " + tempArr[minIdx] + " and " + tempArr[j]));
                if (tempArr[j] < tempArr[minIdx]) {
                    minIdx = j;
                    steps.add(new SortStep(tempArr.clone(), i, minIdx, false, "New minimum: " + tempArr[minIdx]));
                }
            }

            if (minIdx!= i) {
                int temp = tempArr[minIdx];
                tempArr[minIdx] = tempArr[i];
                tempArr[i] = temp;
                steps.add(new SortStep(tempArr.clone(), i, minIdx, true, "Swapped " + tempArr[i] + " with " + tempArr[minIdx]));
            }
        }
        steps.add(new SortStep(tempArr.clone(), -1, -1, false, "Array Sorted!"));
        return steps;
    }

    // Insertion Sort
    public List<SortStep> getInsertionSortSteps(int[] arr) {
        List<SortStep> steps = new ArrayList<>();
        int[] tempArr = arr.clone();

        for (int i = 1; i < tempArr.length; i++) {
            int key = tempArr[i];
            int j = i - 1;
            steps.add(new SortStep(tempArr.clone(), i, -1, false, "Key: " + key + " at index " + i));

            while (j >= 0 && tempArr[j] > key) {
                steps.add(new SortStep(tempArr.clone(), j, j + 1, false, tempArr[j] + " > " + key + ", shifting"));
                tempArr[j + 1] = tempArr[j];
                steps.add(new SortStep(tempArr.clone(), j, j + 1, true, "Shifted " + tempArr[j + 1]));
                j--;
            }
            tempArr[j + 1] = key;
            steps.add(new SortStep(tempArr.clone(), j + 1, -1, true, "Inserted " + key + " at index " + (j + 1)));
        }
        steps.add(new SortStep(tempArr.clone(), -1, -1, false, "Array Sorted!"));
        return steps;
    }

    // Quick Sort Helper
    private void quickSortHelper(int[] arr, int low, int high, List<SortStep> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSortHelper(arr, low, pi - 1, steps);
            quickSortHelper(arr, pi + 1, high, steps);
        }
    }

    private int partition(int[] arr, int low, int high, List<SortStep> steps) {
        int pivot = arr[high];
        steps.add(new SortStep(arr.clone(), high, -1, false, "Pivot: " + pivot));
        int i = low - 1;

        for (int j = low; j < high; j++) {
            steps.add(new SortStep(arr.clone(), j, high, false, "Comparing " + arr[j] + " with pivot " + pivot));
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                steps.add(new SortStep(arr.clone(), i, j, true, "Swapped " + arr[i] + " and " + arr[j]));
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        steps.add(new SortStep(arr.clone(), i + 1, high, true, "Placed pivot at index " + (i + 1)));
        return i + 1;
    }

    public List<SortStep> getQuickSortSteps(int[] arr) {
        List<SortStep> steps = new ArrayList<>();
        int[] tempArr = arr.clone();
        quickSortHelper(tempArr, 0, tempArr.length - 1, steps);
        steps.add(new SortStep(tempArr.clone(), -1, -1, false, "Array Sorted!"));
        return steps;
    }
}