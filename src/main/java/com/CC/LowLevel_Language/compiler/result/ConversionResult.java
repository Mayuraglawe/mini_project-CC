package com.CC.LowLevel_Language.compiler.result;



import com.CC.LowLevel_Language.compiler.analysis.ConversionStatistics;
import com.CC.LowLevel_Language.compiler.generator.AssemblyInstruction;
import com.CC.LowLevel_Language.compiler.parser.TACInstruction;

import java.util.*;

public class ConversionResult {
    private boolean success;
    private String message;
    private List<TACInstruction> tacInstructions;
    private List<String> assemblyLines;
    private List<AssemblyInstruction> assemblyInstructions;
    private String assemblyCode;
    private Map<Integer, List<String>> registerStates;
    private ConversionStatistics stats;

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<TACInstruction> getTacInstructions() { return tacInstructions; }
    public void setTacInstructions(List<TACInstruction> instructions) {
        this.tacInstructions = instructions;
    }

    public List<String> getAssemblyLines() { return assemblyLines; }
    public void setAssemblyLines(List<String> lines) {
        this.assemblyLines = lines;
    }

    public List<AssemblyInstruction> getAssemblyInstructions() { return assemblyInstructions; }
    public void setAssemblyInstructions(List<AssemblyInstruction> instructions) {
        this.assemblyInstructions = instructions;
    }

    public String getAssemblyCode() { return assemblyCode; }
    public void setAssemblyCode(String code) { this.assemblyCode = code; }

    public Map<Integer, List<String>> getRegisterStates() { return registerStates; }
    public void setRegisterStates(Map<Integer, List<String>> states) {
        this.registerStates = states;
    }

    public ConversionStatistics getStats() { return stats; }
    public void setStats(ConversionStatistics stats) { this.stats = stats; }
}
