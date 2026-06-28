package com.daa.visualizer.service;
import com.daa.visualizer.algo.TspAlgo;
import com.daa.visualizer.model.TspStep;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TspService {
    public List<TspStep> solveTSP(int[][] cities) {
        return TspAlgo.nearestNeighbor(cities);
    }
}