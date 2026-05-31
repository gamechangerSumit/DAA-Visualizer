package com.daa.visualizer.algo;

import com.daa.visualizer.model.*;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class GraphAlgo {

    // ==================== BFS ====================
    public List<GraphStep> getBFSSteps(List<List<Integer>> adj, int start) {
        List<GraphStep> steps = new ArrayList<>();
        int n = adj.size();
        if (n == 0) return steps;

        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> visitedOrder = new ArrayList<>();

        visited[start] = true;
        queue.add(start);
        visitedOrder.add(start);
        steps.add(new GraphStep(new ArrayList<>(visitedOrder), new ArrayList<>(), start,
                "Start BFS from " + start, null));

        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                    visitedOrder.add(neighbor);
                    steps.add(new GraphStep(new ArrayList<>(visitedOrder),
                            List.of(new int[]{node, neighbor}), neighbor,
                            "Visited " + neighbor + " from " + node, null));
                }
            }
        }
        steps.add(new GraphStep(visitedOrder, new ArrayList<>(), -1, "BFS Complete!", null));
        return steps;
    }

    // ==================== DFS ====================
    public List<GraphStep> getDFSSteps(List<List<Integer>> adj, int start) {
        List<GraphStep> steps = new ArrayList<>();
        int n = adj.size();
        if (n == 0) return steps;

        boolean[] visited = new boolean[n];
        List<Integer> visitedOrder = new ArrayList<>();
        dfsHelper(adj, start, visited, visitedOrder, steps, -1);
        steps.add(new GraphStep(visitedOrder, new ArrayList<>(), -1, "DFS Complete!", null));
        return steps;
    }

    private void dfsHelper(List<List<Integer>> adj, int node, boolean[] visited,
                           List<Integer> visitedOrder, List<GraphStep> steps, int parent) {
        visited[node] = true;
        visitedOrder.add(node);
        steps.add(new GraphStep(new ArrayList<>(visitedOrder),
                parent!= -1? List.of(new int[]{parent, node}) : new ArrayList<>(),
                node, "Visited " + node, null));

        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfsHelper(adj, neighbor, visited, visitedOrder, steps, node);
            }
        }
    }

    // ==================== DIJKSTRA ====================
    public List<DijkstraStep> getDijkstraSteps(List<List<int[]>> adj, int start) {
        int n = adj.size();
        List<DijkstraStep> steps = new ArrayList<>();
        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE / 2);
        Arrays.fill(prev, -1);
        dist[start] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{start, 0});

        steps.add(new DijkstraStep(
                "Start: Source node " + start + " distance = 0",
                start, new int[]{}, dist.clone(), prev.clone(), null, null, 0
        ));

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];

            if (visited[u]) continue;
            visited[u] = true;

            int[] visitedList = getVisitedArray(visited, n);
            steps.add(new DijkstraStep(
                    "Visiting node " + u + " with distance " + dist[u],
                    u, visitedList, dist.clone(), prev.clone(), null, null, 0
            ));

            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int weight = edge[1];

                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                    pq.offer(new int[]{v, dist[v]});

                    steps.add(new DijkstraStep(
                            "Relaxed edge " + u + "->" + v + ", new distance = " + dist[v],
                            u, visitedList, dist.clone(), prev.clone(), new int[]{u, v}, null, 0
                    ));
                }
            }
        }

        int[] finalVisited = getVisitedArray(visited, n);

        // Target = sabse door wala reachable node
        int target = start;
        int maxDist = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] < Integer.MAX_VALUE / 2 && dist[i] > maxDist) {
                maxDist = dist[i];
                target = i;
            }
        }

        // Path reconstruct
        int[] path = reconstructPath(prev, start, target);

        steps.add(new DijkstraStep(
                "Done! Shortest path to node " + target + ": " + arrayToString(path),
                -1, finalVisited, dist.clone(), prev.clone(), null, path, dist[target]
        ));

        return steps;
    }

    private int[] reconstructPath(int[] prev, int start, int end) {
        List<Integer> path = new ArrayList<>();
        for (int at = end; at!= -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path.stream().mapToInt(i -> i).toArray();
    }

    private String arrayToString(int[] arr) {
        if (arr == null || arr.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(" → ");
        }
        return sb.toString();
    }

    private int[] getVisitedArray(boolean[] visited, int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) if (visited[i]) list.add(i);
        return list.stream().mapToInt(i -> i).toArray();
    }

    // ==================== PRIM'S MST ====================
    public static List<PrimStep> getPrimsSteps(int start, String edgesStr) {
        List<PrimStep> steps = new ArrayList<>();

        Map<Integer, List<int[]>> adj = new HashMap<>();
        int maxNode = 0;

        if (edgesStr == null || edgesStr.trim().isEmpty()) {
            return steps;
        }

        String[] edgeArr = edgesStr.split(",");
        for (String e : edgeArr) {
            String[] parts = e.trim().split("-");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int w = Integer.parseInt(parts[2]);

            maxNode = Math.max(maxNode, Math.max(u, v));

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(new int[]{v, w});
            adj.computeIfAbsent(v, k -> new ArrayList<>()).add(new int[]{u, w});
        }

        int n = maxNode + 1;
        boolean[] inMst = new boolean[n];
        int[] parent = new int[n];
        int[] key = new int[n];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        key[start] = 0;
        List<Integer> mstNodes = new ArrayList<>();
        List<int[]> mstEdges = new ArrayList<>();
        int totalWeight = 0;

        steps.add(new PrimStep(
                -1,
                new ArrayList<>(),
                null,
                new ArrayList<>(),
                0,
                "Starting Prim's from node " + start
        ));

        for (int count = 0; count < n; count++) {
            int u = -1;
            int minKey = Integer.MAX_VALUE;
            for (int v = 0; v < n; v++) {
                if (!inMst[v] && key[v] < minKey) {
                    minKey = key[v];
                    u = v;
                }
            }

            if (u == -1) break;

            inMst[u] = true;
            mstNodes.add(u);

            if (parent[u]!= -1) {
                mstEdges.add(new int[]{parent[u], u});
                totalWeight += key[u];
                steps.add(new PrimStep(
                        u,
                        new ArrayList<>(mstNodes),
                        new int[]{parent[u], u},
                        new ArrayList<>(mstEdges),
                        totalWeight,
                        "Added edge " + parent[u] + "-" + u + " with weight " + key[u] + " to MST"
                ));
            } else {
                steps.add(new PrimStep(
                        u,
                        new ArrayList<>(mstNodes),
                        null,
                        new ArrayList<>(mstEdges),
                        totalWeight,
                        "Started MST with node " + u
                ));
            }

            if (adj.containsKey(u)) {
                for (int[] neighbor : adj.get(u)) {
                    int v = neighbor[0];
                    int weight = neighbor[1];

                    if (!inMst[v] && weight < key[v]) {
                        steps.add(new PrimStep(
                                u,
                                new ArrayList<>(mstNodes),
                                new int[]{u, v},
                                new ArrayList<>(mstEdges),
                                totalWeight,
                                "Checking edge " + u + "-" + v + " with weight " + weight + ". Updating key of " + v
                        ));

                        parent[v] = u;
                        key[v] = weight;
                    }
                }
            }
        }

        steps.add(new PrimStep(
                -1,
                new ArrayList<>(mstNodes),
                null,
                new ArrayList<>(mstEdges),
                totalWeight,
                "MST Complete! Total weight: " + totalWeight
        ));

        return steps;
    }
}