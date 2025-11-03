package com.CC.LowLevel_Language.compiler.analysis;

public class ConversionStatistics {
    private final int totalInstructions;
    private final int registerCount;
    private final long spilledVariables;
    private final int variableCount;

    public ConversionStatistics(int totalInstructions, int registerCount,
                                long spilledVariables, int variableCount) {
        this.totalInstructions = totalInstructions;
        this.registerCount = registerCount;
        this.spilledVariables = spilledVariables;
        this.variableCount = variableCount;
    }

    public int getTotalInstructions() { return totalInstructions; }
    public int getRegisterCount() { return registerCount; }
    public long getSpilledVariables() { return spilledVariables; }
    public int getVariableCount() { return variableCount; }
}
