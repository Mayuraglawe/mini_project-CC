package com.CC.LowLevel_Language.api.dto;

import java.util.List;

import com.CC.LowLevel_Language.compiler.analysis.ResourceAnalysisResult;

public class Version2Response {
    private boolean success;
    private String message;
    
    // Assembly Conversion
    private String assemblyCode;
    private List<String> assemblyLines;
    
    // Resource Analysis
    private ResourceAnalysisResult resourceAnalysis;
    
    // Optimization
    private OptimizationData optimization;

    public Version2Response() {
    }

    public Version2Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAssemblyCode() {
        return assemblyCode;
    }

    public void setAssemblyCode(String assemblyCode) {
        this.assemblyCode = assemblyCode;
    }

    public List<String> getAssemblyLines() {
        return assemblyLines;
    }

    public void setAssemblyLines(List<String> assemblyLines) {
        this.assemblyLines = assemblyLines;
    }

    public ResourceAnalysisResult getResourceAnalysis() {
        return resourceAnalysis;
    }

    public void setResourceAnalysis(ResourceAnalysisResult resourceAnalysis) {
        this.resourceAnalysis = resourceAnalysis;
    }

    public OptimizationData getOptimization() {
        return optimization;
    }

    public void setOptimization(OptimizationData optimization) {
        this.optimization = optimization;
    }

    // Inner class for optimization data
    public static class OptimizationData {
        private String originalCode;
        private String optimizedCode;
        private List<String> optimizationLogs;
        private int optimizationsApplied;
        private double improvementPercentage;
        private int originalInstructionCount;
        private int optimizedInstructionCount;

        public OptimizationData() {
        }

        // Getters and Setters
        public String getOriginalCode() {
            return originalCode;
        }

        public void setOriginalCode(String originalCode) {
            this.originalCode = originalCode;
        }

        public String getOptimizedCode() {
            return optimizedCode;
        }

        public void setOptimizedCode(String optimizedCode) {
            this.optimizedCode = optimizedCode;
        }

        public List<String> getOptimizationLogs() {
            return optimizationLogs;
        }

        public void setOptimizationLogs(List<String> optimizationLogs) {
            this.optimizationLogs = optimizationLogs;
        }

        public int getOptimizationsApplied() {
            return optimizationsApplied;
        }

        public void setOptimizationsApplied(int optimizationsApplied) {
            this.optimizationsApplied = optimizationsApplied;
        }

        public double getImprovementPercentage() {
            return improvementPercentage;
        }

        public void setImprovementPercentage(double improvementPercentage) {
            this.improvementPercentage = improvementPercentage;
        }

        public int getOriginalInstructionCount() {
            return originalInstructionCount;
        }

        public void setOriginalInstructionCount(int originalInstructionCount) {
            this.originalInstructionCount = originalInstructionCount;
        }

        public int getOptimizedInstructionCount() {
            return optimizedInstructionCount;
        }

        public void setOptimizedInstructionCount(int optimizedInstructionCount) {
            this.optimizedInstructionCount = optimizedInstructionCount;
        }
    }
}
