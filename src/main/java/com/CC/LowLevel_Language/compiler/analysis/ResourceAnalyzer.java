package com.CC.LowLevel_Language.compiler.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResourceAnalyzer {
    
    public ResourceAnalysisResult analyze(List<String> assemblyCode) {
        ResourceAnalysisResult result = new ResourceAnalysisResult();
        
        // Track various resources
        Set<String> registersUsed = new HashSet<>();
        Map<String, Integer> instructionTypes = new HashMap<>();
        int memoryAccesses = 0;
        int arithmeticOps = 0;
        int jumpInstructions = 0;
        int totalInstructions = 0;
        int labels = 0;
        
        for (String line : assemblyCode) {
            String trimmed = line.trim();
            
            // Skip empty lines and directives
            if (trimmed.isEmpty() || trimmed.startsWith(".")) {
                continue;
            }
            
            // Count labels
            if (trimmed.endsWith(":")) {
                labels++;
                continue;
            }
            
            // Skip comments
            if (trimmed.startsWith(";")) {
                continue;
            }
            
            // Count as instruction
            totalInstructions++;
            
            // Extract instruction type
            String[] parts = trimmed.split("\\s+");
            if (parts.length > 0) {
                String instruction = parts[0].toLowerCase();
                instructionTypes.put(instruction, instructionTypes.getOrDefault(instruction, 0) + 1);
                
                // Categorize instruction types
                if (isArithmeticOp(instruction)) {
                    arithmeticOps++;
                }
                
                if (isJumpInstruction(instruction)) {
                    jumpInstructions++;
                }
                
                // Extract registers used
                extractRegisters(trimmed, registersUsed);
                
                // Count memory accesses
                if (trimmed.contains("[") || instruction.equals("push") || instruction.equals("pop")) {
                    memoryAccesses++;
                }
            }
        }
        
        // Calculate statistics
        result.setTotalInstructions(totalInstructions);
        result.setRegistersUsed(new ArrayList<>(registersUsed));
        result.setRegisterCount(registersUsed.size());
        result.setInstructionTypes(instructionTypes);
        result.setMemoryAccesses(memoryAccesses);
        result.setArithmeticOperations(arithmeticOps);
        result.setJumpInstructions(jumpInstructions);
        result.setLabelCount(labels);
        
        // Calculate complexity metrics
        result.setControlFlowComplexity(calculateComplexity(jumpInstructions, labels));
        result.setMemoryPressure(calculateMemoryPressure(memoryAccesses, totalInstructions));
        result.setRegisterPressure(calculateRegisterPressure(registersUsed.size()));
        
        return result;
    }
    
    private boolean isArithmeticOp(String instruction) {
        return instruction.equals("add") || instruction.equals("sub") || 
               instruction.equals("imul") || instruction.equals("idiv") ||
               instruction.equals("mul") || instruction.equals("div") ||
               instruction.equals("inc") || instruction.equals("dec") ||
               instruction.equals("shl") || instruction.equals("shr") ||
               instruction.equals("sal") || instruction.equals("sar");
    }
    
    private boolean isJumpInstruction(String instruction) {
        return instruction.equals("jmp") || instruction.equals("je") ||
               instruction.equals("jne") || instruction.equals("jg") ||
               instruction.equals("jl") || instruction.equals("jge") ||
               instruction.equals("jle") || instruction.equals("call") ||
               instruction.equals("ret");
    }
    
    private void extractRegisters(String instruction, Set<String> registers) {
        String[] x86Registers = {"eax", "ebx", "ecx", "edx", "esi", "edi", "ebp", "esp",
                                "ax", "bx", "cx", "dx", "si", "di", "bp", "sp",
                                "al", "ah", "bl", "bh", "cl", "ch", "dl", "dh"};
        
        for (String reg : x86Registers) {
            if (instruction.toLowerCase().contains(reg)) {
                // Add base register (32-bit version)
                if (reg.length() == 2 && !reg.equals("sp") && !reg.equals("bp")) {
                    registers.add("e" + reg);
                } else if (reg.length() == 3 && reg.startsWith("e")) {
                    registers.add(reg);
                } else if (reg.equals("sp")) {
                    registers.add("esp");
                } else if (reg.equals("bp")) {
                    registers.add("ebp");
                }
            }
        }
    }
    
    private String calculateComplexity(int jumps, int labels) {
        int complexity = jumps + labels;
        if (complexity <= 2) return "Low";
        if (complexity <= 5) return "Medium";
        return "High";
    }
    
    private String calculateMemoryPressure(int memAccesses, int totalInstructions) {
        if (totalInstructions == 0) return "None";
        double ratio = (double) memAccesses / totalInstructions;
        if (ratio < 0.2) return "Low";
        if (ratio < 0.5) return "Medium";
        return "High";
    }
    
    private String calculateRegisterPressure(int regCount) {
        if (regCount <= 3) return "Low";
        if (regCount <= 5) return "Medium";
        return "High";
    }
}
