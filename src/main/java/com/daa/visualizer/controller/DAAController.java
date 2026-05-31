package com.daa.visualizer.controller;

import com.daa.visualizer.model.FractionalStep;
import com.daa.visualizer.algo.*;
import com.daa.visualizer.model.*;
import com.daa.visualizer.service.DynamicAlgo;
import com.daa.visualizer.service.KnapsackAlgo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class DAAController {

    @Autowired
    private SortingAlgo sortingAlgo;

    @Autowired
    private GraphAlgo graphAlgo;

    @Autowired
    private DynamicAlgo dynamicAlgo;

    @Autowired
    private GreedyAlgo greedyAlgo;

    @Autowired
    private KnapsackAlgo knapsackAlgo; // 0/1 knapsack isme hoga

    // ========== PAGE ROUTES ==========
    @GetMapping("/") public String home() { return "index"; }
    @GetMapping("/sort") public String sortPage() { return "sorting"; }
    @GetMapping("/graph-traversal") public String traversalPage() { return "bfsdfs"; }
    @GetMapping("/graph-mst") public String mstPage() { return "prims"; }
    @GetMapping("/graph-path") public String pathPage() { return "dijkstra"; }
    @GetMapping("/dp") public String dpPage() { return "floyd"; }
    @GetMapping("/greedy") public String greedyPage() { return "knapsack"; }

    // ========== SORTING APIs ==========
    @GetMapping("/sort/{algo}")
    @ResponseBody
    public List<Step> sort(@PathVariable String algo, @RequestParam String arr) {
        int[] intArr = Arrays.stream(arr.split(",")).mapToInt(Integer::parseInt).toArray();
        return switch (algo.toLowerCase()) {
            case "selection" -> sortingAlgo.getSelectionSortSteps(intArr);
            case "insertion" -> sortingAlgo.getInsertionSortSteps(intArr);
            case "merge" -> sortingAlgo.getMergeSortSteps(intArr);
            case "quick" -> sortingAlgo.getQuickSortSteps(intArr);
            default -> sortingAlgo.getBubbleSortSteps(intArr);
        };
    }

    // ========== GRAPH TRAVERSAL APIs ==========
    @GetMapping("/graph/bfs")
    @ResponseBody
    public List<GraphStep> bfs(@RequestParam int start, @RequestParam String edges) {
        return graphAlgo.getBFSSteps(parseAdjList(edges), start);
    }

    @GetMapping("/graph/dfs")
    @ResponseBody
    public List<GraphStep> dfs(@RequestParam int start, @RequestParam String edges) {
        return graphAlgo.getDFSSteps(parseAdjList(edges), start);
    }

    // ========== SHORTEST PATH API - DIJKSTRA ==========
    @GetMapping("/graph/dijkstra")
    @ResponseBody
    public List<DijkstraStep> dijkstra(@RequestParam int start, @RequestParam String edges) {
        List<List<int[]>> adj = parseWeightedAdjList(edges);
        return graphAlgo.getDijkstraSteps(adj, start);
    }

    // ========== MST API ==========
    @GetMapping("/graph/prims")
    @ResponseBody
    public List<PrimStep> prims(@RequestParam int start, @RequestParam String edges) {
        return graphAlgo.getPrimsSteps(start, edges);
    }

    // ========== DP API - FLOYD WARSHALL ==========
    @PostMapping("/dp/floyd")
    @ResponseBody
    public List<FloydStep> floyd(@RequestBody int[][] matrix) {
        return dynamicAlgo.getFloydWarshallSteps(matrix);
    }

    // ========== KNAPSACK 0/1 API ==========
    @PostMapping("/knapsack")
    @ResponseBody
    public List<KnapsackStep> knapsack(@RequestParam int capacity,
                                       @RequestParam String weights,
                                       @RequestParam String values) {
        int[] w = Arrays.stream(weights.split(","))
                .map(s -> s.trim().replaceAll("\\s+", "")) // space sab hatao
                .filter(s ->!s.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray();

        int[] v = Arrays.stream(values.split(","))
                .map(s -> s.trim().replaceAll("\\s+", ""))
                .filter(s ->!s.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray();

        if (w.length!= v.length) {
            throw new IllegalArgumentException(
                    "Weights=" + w.length + " items, Values=" + v.length + " items. Count same karo!"
            );
        }

        return knapsackAlgo.getKnapsackSteps(capacity, w, v);
    }
    // ========== GREEDY API - FRACTIONAL KNAPSACK ==========
    @GetMapping("/greedy/knapsack")
    @ResponseBody
    public List<FractionalStep> fractionalKnapsack(@RequestParam int capacity,
                                                   @RequestParam String weights,
                                                   @RequestParam String values) {
        int[] w = Arrays.stream(weights.split(",")).mapToInt(Integer::parseInt).toArray();
        int[] v = Arrays.stream(values.split(",")).mapToInt(Integer::parseInt).toArray();
        return greedyAlgo.getFractionalKnapsackSteps(capacity, w, v);
    }

    // ========== HELPER METHODS ==========
    private List<List<Integer>> parseAdjList(String edges) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        if (edges == null || edges.trim().isEmpty()) return new ArrayList<>();

        for (String e : edges.split(",")) {
            String[] uv = e.trim().split("-");
            int u = Integer.parseInt(uv[0]);
            int v = Integer.parseInt(uv[1]);
            map.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            map.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        int max = map.keySet().stream().max(Integer::compare).orElse(0);
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= max; i++) {
            adj.add(map.getOrDefault(i, new ArrayList<>()));
        }
        return adj;
    }

    private List<List<int[]>> parseWeightedAdjList(String edges) {
        Map<Integer, List<int[]>> map = new HashMap<>();
        if (edges == null || edges.trim().isEmpty()) return new ArrayList<>();

        for (String e : edges.split(",")) {
            String[] uvw = e.trim().split("-");
            if (uvw.length < 3) continue;
            int u = Integer.parseInt(uvw[0]);
            int v = Integer.parseInt(uvw[1]);
            int w = Integer.parseInt(uvw[2]);
            map.computeIfAbsent(u, k -> new ArrayList<>()).add(new int[]{v, w});
            map.computeIfAbsent(v, k -> new ArrayList<>()).add(new int[]{u, w});
        }

        int max = map.keySet().stream().max(Integer::compare).orElse(0);
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i <= max; i++) {
            adj.add(map.getOrDefault(i, new ArrayList<>()));
        }
        return adj;
    }
}