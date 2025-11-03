package com.CC.LowLevel_Language.compiler.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceAnalysisResult {
    private int totalInstructions;
    private List<String> registersUsed;
    private int registerCount;
    private Map<String, Integer> instructionTypes;
    private int memoryAccesses;
    private int arithmeticOperations;
    private int jumpInstructions;
    private int labelCount;
    private String controlFlowComplexity;
    private String memoryPressure;
    private String registerPressure;

    public ResourceAnalysisResult() {
        this.registersUsed = new ArrayList<>();
        this.instructionTypes = new HashMap<>();
    }

    // Getters and Setters
    public int getTotalInstructions() {
        return totalInstructions;
    }

    public void setTotalInstructions(int totalInstructions) {
        this.totalInstructions = totalInstructions;
    }

    public List<String> getRegistersUsed() {
        return registersUsed;
    }

    public void setRegistersUsed(List<String> registersUsed) {
        this.registersUsed = registersUsed;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }

    public Map<String, Integer> getInstructionTypes() {
        return instructionTypes;
    }

    public void setInstructionTypes(Map<String, Integer> instructionTypes) {
        this.instructionTypes = instructionTypes;
    }

    public int getMemoryAccesses() {
        return memoryAccesses;
    }

    public void setMemoryAccesses(int memoryAccesses) {
        this.memoryAccesses = memoryAccesses;
    }

    public int getArithmeticOperations() {
        return arithmeticOperations;
    }

    public void setArithmeticOperations(int arithmeticOperations) {
        this.arithmeticOperations = arithmeticOperations;
    }

    public int getJumpInstructions() {
        return jumpInstructions;
    }

    public void setJumpInstructions(int jumpInstructions) {
        this.jumpInstructions = jumpInstructions;
    }

    public int getLabelCount() {
        return labelCount;
    }

    public void setLabelCount(int labelCount) {
        this.labelCount = labelCount;
    }

    public String getControlFlowComplexity() {
        return controlFlowComplexity;
    }

    public void setControlFlowComplexity(String controlFlowComplexity) {
        this.controlFlowComplexity = controlFlowComplexity;
    }

    public String getMemoryPressure() {
        return memoryPressure;
    }

    public void setMemoryPressure(String memoryPressure) {
        this.memoryPressure = memoryPressure;
    }

    public String getRegisterPressure() {
        return registerPressure;
    }

    public void setRegisterPressure(String registerPressure) {
        this.registerPressure = registerPressure;
    }
}
