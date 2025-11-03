package com.CC.LowLevel_Language.compiler.analysis;

import java.util.*;

public class RegisterManager {
    private final Map<String, String> variableToRegister;
    private final Set<String> usedRegisters;
    private final Queue<String> availableRegisters;

    public RegisterManager() {
        variableToRegister = new HashMap<>();
        usedRegisters = new HashSet<>();
        availableRegisters = new LinkedList<>();

        // Initialize available x86 registers
        availableRegisters.offer("eax");
        availableRegisters.offer("ebx");
        availableRegisters.offer("ecx");
        availableRegisters.offer("edx");
        availableRegisters.offer("esi");
        availableRegisters.offer("edi");
    }

    public String getRegisterFor(String variable) {
        // If variable already has a register assigned
        if (variableToRegister.containsKey(variable)) {
            return variableToRegister.get(variable);
        }

        // Assign a new register
        String register = allocateRegister();
        if (register != null) {
            variableToRegister.put(variable, register);
            return register;
        }

        // If no registers available, use memory reference
        return "[" + variable + "]";
    }

    public String allocateRegister() {
        if (!availableRegisters.isEmpty()) {
            String register = availableRegisters.poll();
            usedRegisters.add(register);
            return register;
        }
        return null;
    }

    public String allocateRegister(String variable) {
        String register = allocateRegister();
        if (register != null) {
            variableToRegister.put(variable, register);
        }
        return register;
    }

    public void freeRegister(String variable) {
        String register = variableToRegister.get(variable);
        if (register != null && !register.startsWith("[")) {
            variableToRegister.remove(variable);
            usedRegisters.remove(register);
            availableRegisters.offer(register);
        }
    }

    public Set<String> getRegisters() {
        return Collections.unmodifiableSet(usedRegisters);
    }

    public boolean isRegister(String operand) {
        return operand != null &&
                (operand.equals("eax") || operand.equals("ebx") ||
                        operand.equals("ecx") || operand.equals("edx") ||
                        operand.equals("esi") || operand.equals("edi"));
    }

    public boolean isMemoryReference(String operand) {
        return operand != null && operand.startsWith("[") && operand.endsWith("]");
    }

    public boolean isConstant(String operand) {
        if (operand == null) return false;
        try {
            Integer.parseInt(operand);
            return true;  // Fixed: just return true if parsing succeeds
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String formatOperand(String operand) {
        if (operand == null) return "";

        // If it's a constant, return as is
        if (isConstant(operand)) {
            return operand;
        }

        // If it's already a register or memory reference, return as is
        if (isRegister(operand) || isMemoryReference(operand)) {
            return operand;
        }

        // Otherwise, get the register for this variable
        return getRegisterFor(operand);
    }

    public void reset() {
        variableToRegister.clear();
        usedRegisters.clear();
        availableRegisters.clear();

        // Re-initialize available registers
        availableRegisters.offer("eax");
        availableRegisters.offer("ebx");
        availableRegisters.offer("ecx");
        availableRegisters.offer("edx");
        availableRegisters.offer("esi");
        availableRegisters.offer("edi");
    }

    public Map<String, String> getVariableToRegisterMap() {
        return new HashMap<>(variableToRegister);
    }

    public boolean tryAllocateRegister(String variable, Set<String> interferingVars) {
        // If variable already has a register assigned
        if (variableToRegister.containsKey(variable)) {
            return true;
        }

        // Find a register that's not used by any interfering variables
        for (String register : availableRegisters) {
            boolean canUseRegister = true;
            for (String interferingVar : interferingVars) {
                String interferingReg = variableToRegister.get(interferingVar);
                if (register.equals(interferingReg)) {
                    canUseRegister = false;
                    break;
                }
            }
            if (canUseRegister) {
                availableRegisters.remove(register);
                usedRegisters.add(register);
                variableToRegister.put(variable, register);
                return true;
            }
        }

        // No suitable register found
        return false;
    }
}
