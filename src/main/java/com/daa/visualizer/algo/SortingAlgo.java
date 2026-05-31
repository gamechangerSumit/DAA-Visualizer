package com.daa.visualizer.algo;

import com.daa.visualizer.model.Step;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class SortingAlgo {

    public List<Step> getBubbleSortSteps(int[] arr) {
        List<Step> steps = new ArrayList<>();
        int n = arr.length;
        int[] tempArr = arr.clone();
        int[] sorted = new int[0];

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                steps.add(new Step(tempArr.clone(), new int[]{j, j + 1}, sorted.clone(), -1,
                        "Comparing " + tempArr[j] + " and " + tempArr[j + 1]));

                if (tempArr[j] > tempArr[j + 1]) {
                    int temp = tempArr[j];
                    tempArr[j] = tempArr[j + 1];
                    tempArr[j + 1] = temp;
                    steps.add(new Step(tempArr.clone(), new int[]{j, j + 1}, sorted.clone(), -1,
                            "Swapped " + tempArr[j] + " and " + tempArr[j + 1]));
                }
            }
            sorted = addToSorted(sorted, n - i - 1);
        }
        sorted = addToSorted(sorted, 0);
        steps.add(new Step(tempArr.clone(), new int[]{-1}, sorted, -1, "Array Sorted!"));
        return steps;
    }

    public List<Step> getSelectionSortSteps(int[] arr) {
        List<Step> steps = new ArrayList<>();
        int n = arr.length;
        int[] tempArr = arr.clone();
        int[] sorted = new int[0];

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            steps.add(new Step(tempArr.clone(), new int[]{i}, sorted.clone(), -1,
                    "Finding minimum from index " + i));

            for (int j = i + 1; j < n; j++) {
                steps.add(new Step(tempArr.clone(), new int[]{minIdx, j}, sorted.clone(), -1,
                        "Comparing " + tempArr[minIdx] + " and " + tempArr[j]));

                if (tempArr[j] < tempArr[minIdx]) {
                    minIdx = j;
                    steps.add(new Step(tempArr.clone(), new int[]{minIdx}, sorted.clone(), -1,
                            "New minimum: " + tempArr[minIdx]));
                }
            }

            if (minIdx!= i) {
                int temp = tempArr[minIdx];
                tempArr[minIdx] = tempArr[i];
                tempArr[i] = temp;
                steps.add(new Step(tempArr.clone(), new int[]{i, minIdx}, sorted.clone(), -1,
                        "Swapped " + tempArr[i] + " with " + tempArr[minIdx]));
            }
            sorted = addToSorted(sorted, i);
        }
        sorted = addToSorted(sorted, n - 1);
        steps.add(new Step(tempArr.clone(), new int[]{-1}, sorted, -1, "Array Sorted!"));
        return steps;
    }

    public List<Step> getInsertionSortSteps(int[] arr) {
        List<Step> steps = new ArrayList<>();
        int[] tempArr = arr.clone();
        int[] sorted = new int[]{0};

        for (int i = 1; i < tempArr.length; i++) {
            int key = tempArr[i];
            int j = i - 1;
            steps.add(new Step(tempArr.clone(), new int[]{i}, sorted.clone(), -1,
                    "Key: " + key + " at index " + i));

            while (j >= 0 && tempArr[j] > key) {
                steps.add(new Step(tempArr.clone(), new int[]{j, j + 1}, sorted.clone(), -1,
                        tempArr[j] + " > " + key + ", shifting"));
                tempArr[j + 1] = tempArr[j];
                steps.add(new Step(tempArr.clone(), new int[]{j, j + 1}, sorted.clone(), -1,
                        "Shifted " + tempArr[j + 1]));
                j--;
            }
            tempArr[j + 1] = key;
            sorted = updateSortedInsertion(sorted, i);
            steps.add(new Step(tempArr.clone(), new int[]{j + 1}, sorted.clone(), -1,
                    "Inserted " + key + " at index " + (j + 1)));
        }
        steps.add(new Step(tempArr.clone(), new int[]{-1}, sorted, -1, "Array Sorted!"));
        return steps;
    }

    public List<Step> getMergeSortSteps(int[] arr) {
        List<Step> steps = new ArrayList<>();
        int[] tempArr = arr.clone();
        mergeSortHelper(tempArr, 0, tempArr.length - 1, steps);
        steps.add(new Step(tempArr.clone(), new int[]{-1}, getAllIndices(tempArr.length), -1, "Array Sorted!"));
        return steps;
    }

    private void mergeSortHelper(int[] arr, int left, int right, List<Step> steps) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            steps.add(new Step(arr.clone(), getRange(left, right), new int[0], -1,
                    "Divide: " + left + "-" + mid + " | " + (mid + 1) + "-" + right));
            mergeSortHelper(arr, left, mid, steps);
            mergeSortHelper(arr, mid + 1, right, steps);
            merge(arr, left, mid, right, steps);
        }
    }

    private void merge(int[] arr, int left, int mid, int right, List<Step> steps) {
        int n1 = mid - left + 1, n2 = right - mid;
        int[] L = new int[n1], R = new int[n2];
        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        steps.add(new Step(arr.clone(), getRange(left, right), new int[0], -1, "Merging subarrays"));
        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            steps.add(new Step(arr.clone(), new int[]{left + i, mid + 1 + j}, new int[0], -1,
                    "Comparing " + L[i] + " and " + R[j]));
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
            steps.add(new Step(arr.clone(), new int[]{k - 1}, new int[0], -1, "Placed " + arr[k - 1]));
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    public List<Step> getQuickSortSteps(int[] arr) {
        List<Step> steps = new ArrayList<>();
        int[] tempArr = arr.clone();
        quickSortHelper(tempArr, 0, tempArr.length - 1, steps);
        steps.add(new Step(tempArr.clone(), new int[]{-1}, getAllIndices(tempArr.length), -1, "Array Sorted!"));
        return steps;
    }

    private void quickSortHelper(int[] arr, int low, int high, List<Step> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSortHelper(arr, low, pi - 1, steps);
            quickSortHelper(arr, pi + 1, high, steps);
        }
    }

    private int partition(int[] arr, int low, int high, List<Step> steps) {
        int pivot = arr[high];
        steps.add(new Step(arr.clone(), new int[]{high}, new int[0], high, "Pivot: " + pivot));
        int i = low - 1;

        for (int j = low; j < high; j++) {
            steps.add(new Step(arr.clone(), new int[]{j, high}, new int[0], high,
                    "Comparing " + arr[j] + " with pivot"));
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                steps.add(new Step(arr.clone(), new int[]{i, j}, new int[0], high,
                        "Swapped " + arr[j] + " and " + arr[i]));
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        steps.add(new Step(arr.clone(), new int[]{i + 1, high}, new int[0], i + 1,
                "Placed pivot at " + (i + 1)));
        return i + 1;
    }

    // Helper methods
    private int[] addToSorted(int[] sorted, int idx) {
        int[] newSorted = new int[sorted.length + 1];
        System.arraycopy(sorted, 0, newSorted, 0, sorted.length);
        newSorted[sorted.length] = idx;
        return newSorted;
    }

    private int[] updateSortedInsertion(int[] sorted, int idx) {
        int[] newSorted = new int[idx + 1];
        for (int i = 0; i <= idx; i++) newSorted[i] = i;
        return newSorted;
    }

    private int[] getRange(int start, int end) {
        int[] range = new int[end - start + 1];
        for (int i = 0; i < range.length; i++) range[i] = start + i;
        return range;
    }

    private int[] getAllIndices(int n) {
        int[] all = new int[n];
        for (int i = 0; i < n; i++) all[i] = i;
        return all;
    }
}