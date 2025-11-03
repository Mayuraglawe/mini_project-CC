package com.CC.LowLevel_Language.compiler.optimizer;

import java.util.List;

public class OptimizationResult {
    private final List<String> originalCode;
    private final List<String> optimizedCode;
    private final List<String> optimizationLogs;
    private final int optimizationsApplied;
    private final double improvementPercentage;

    public OptimizationResult(List<String> originalCode, 
                            List<String> optimizedCode,
                            List<String> optimizationLogs,
                            int optimizationsApplied,
                            double improvementPercentage) {
        this.originalCode = originalCode;
        this.optimizedCode = optimizedCode;
        this.optimizationLogs = optimizationLogs;
        this.optimizationsApplied = optimizationsApplied;
        this.improvementPercentage = improvementPercentage;
    }

    public List<String> getOriginalCode() {
        return originalCode;
    }

    public List<String> getOptimizedCode() {
        return optimizedCode;
    }

    public List<String> getOptimizationLogs() {
        return optimizationLogs;
    }

    public int getOptimizationsApplied() {
        return optimizationsApplied;
    }

    public double getImprovementPercentage() {
        return improvementPercentage;
    }

    public int getOriginalInstructionCount() {
        return countActualInstructions(originalCode);
    }

    public int getOptimizedInstructionCount() {
        return countActualInstructions(optimizedCode);
    }

    private int countActualInstructions(List<String> code) {
        int count = 0;
        for (String line : code) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && 
                !trimmed.startsWith(";") && 
                !trimmed.startsWith(".") && 
                !trimmed.endsWith(":")) {
                count++;
            }
        }
        return count;
    }
}
