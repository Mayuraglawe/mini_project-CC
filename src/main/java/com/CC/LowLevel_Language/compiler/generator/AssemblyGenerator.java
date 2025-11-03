package com.CC.LowLevel_Language.compiler.generator;

import com.CC.LowLevel_Language.compiler.analysis.RegisterManager;
import com.CC.LowLevel_Language.compiler.parser.TACInstruction;

import java.util.*;

public class AssemblyGenerator {
    private final RegisterManager registerManager;
    private final List<String> assemblyCode;

    public AssemblyGenerator() {
        this.registerManager = new RegisterManager();
        this.assemblyCode = new ArrayList<>();
    }

    public List<String> generateAssembly(List<TACInstruction> instructions) {
        assemblyCode.clear();
        registerManager.reset();

        assemblyCode.add("; Generated Assembly Code from TAC");
        assemblyCode.add(".section .data");
        assemblyCode.add(".section .text");
        assemblyCode.add(".global _start");
        assemblyCode.add("_start:");

        for (TACInstruction instruction : instructions) {
            generateInstructionAssembly(instruction);
        }

        // Add program termination
        assemblyCode.add("mov eax, 1     ; sys_exit");
        assemblyCode.add("int 0x80       ; call kernel");

        return new ArrayList<>(assemblyCode);
    }

    private void generateInstructionAssembly(TACInstruction instruction) {
        if (instruction.isLabel()) {
            assemblyCode.add(instruction.getLabel() + ":");
            return;
        }

        String operation = instruction.getOperation();

        switch (operation) {
            case "+":
                generateAddition(instruction);
                break;
            case "-":
                generateSubtraction(instruction);
                break;
            case "*":
                generateMultiplication(instruction);
                break;
            case "/":
                generateDivision(instruction);
                break;
            case "=":
                generateAssignment(instruction);
                break;
            case "return":
                generateReturn(instruction);
                break;
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "==":
            case "!=":
                generateComparison(instruction);
                break;
            default:
                assemblyCode.add("; Unsupported operation: " + operation);
        }
    }

    private void generateAddition(TACInstruction instruction) {
        String result = registerManager.formatOperand(instruction.getResult());
        String operand1 = registerManager.formatOperand(instruction.getOperand1());
        String operand2 = registerManager.formatOperand(instruction.getOperand2());

        assemblyCode.add("; " + instruction.getResult() + " = " +
                instruction.getOperand1() + " + " + instruction.getOperand2());

        // Move first operand to result register/memory
        if (!result.equals(operand1)) {
            assemblyCode.add("mov " + result + ", " + operand1);
        }

        // Add second operand
        assemblyCode.add("add " + result + ", " + operand2);
    }

    private void generateSubtraction(TACInstruction instruction) {
        String result = registerManager.formatOperand(instruction.getResult());
        String operand1 = registerManager.formatOperand(instruction.getOperand1());
        String operand2 = registerManager.formatOperand(instruction.getOperand2());

        assemblyCode.add("; " + instruction.getResult() + " = " +
                instruction.getOperand1() + " - " + instruction.getOperand2());

        // Move first operand to result register/memory
        if (!result.equals(operand1)) {
            assemblyCode.add("mov " + result + ", " + operand1);
        }

        // Subtract second operand
        assemblyCode.add("sub " + result + ", " + operand2);
    }

    private void generateMultiplication(TACInstruction instruction) {
        String result = registerManager.formatOperand(instruction.getResult());
        String operand1 = registerManager.formatOperand(instruction.getOperand1());
        String operand2 = registerManager.formatOperand(instruction.getOperand2());

        assemblyCode.add("; " + instruction.getResult() + " = " +
                instruction.getOperand1() + " * " + instruction.getOperand2());

        // Move first operand to result register/memory
        if (!result.equals(operand1)) {
            assemblyCode.add("mov " + result + ", " + operand1);
        }

        // Multiply by second operand
        assemblyCode.add("imul " + result + ", " + operand2);
    }

    private void generateDivision(TACInstruction instruction) {
        String result = registerManager.formatOperand(instruction.getResult());
        String operand1 = registerManager.formatOperand(instruction.getOperand1());
        String operand2 = registerManager.formatOperand(instruction.getOperand2());

        assemblyCode.add("; " + instruction.getResult() + " = " +
                instruction.getOperand1() + " / " + instruction.getOperand2());

        // Division requires eax and edx registers
        assemblyCode.add("mov eax, " + operand1);
        assemblyCode.add("cdq                 ; sign extend eax to edx:eax");
        assemblyCode.add("idiv " + operand2 + " ; divide edx:eax by operand2");
        assemblyCode.add("mov " + result + ", eax ; store quotient");
    }

    private void generateAssignment(TACInstruction instruction) {
        String result = registerManager.formatOperand(instruction.getResult());
        String operand1 = registerManager.formatOperand(instruction.getOperand1());

        assemblyCode.add("; " + instruction.getResult() + " = " + instruction.getOperand1());
        assemblyCode.add("mov " + result + ", " + operand1);
    }

    private void generateReturn(TACInstruction instruction) {
        if (instruction.getOperand1() != null) {
            String returnValue = registerManager.formatOperand(instruction.getOperand1());
            assemblyCode.add("; return " + instruction.getOperand1());
            assemblyCode.add("mov eax, " + returnValue + " ; prepare return value");
        } else {
            assemblyCode.add("; return");
        }
        assemblyCode.add("ret                     ; return from function");
    }

    private void generateComparison(TACInstruction instruction) {
        String operand1 = registerManager.formatOperand(instruction.getOperand1());
        String operand2 = registerManager.formatOperand(instruction.getOperand2());
        String label = instruction.getResult(); // The label to jump to
        String operation = instruction.getOperation();

        assemblyCode.add("; if " + instruction.getOperand1() + " " + operation +
                " " + instruction.getOperand2() + " goto " + label);

        assemblyCode.add("cmp " + operand1 + ", " + operand2);

        // Generate appropriate jump instruction
        switch (operation) {
            case ">":
                assemblyCode.add("jg " + label);
                break;
            case "<":
                assemblyCode.add("jl " + label);
                break;
            case ">=":
                assemblyCode.add("jge " + label);
                break;
            case "<=":
                assemblyCode.add("jle " + label);
                break;
            case "==":
                assemblyCode.add("je " + label);
                break;
            case "!=":
                assemblyCode.add("jne " + label);
                break;
            default:
                assemblyCode.add("jmp " + label + " ; unconditional jump");
        }
    }

    public void reset() {
        assemblyCode.clear();
        registerManager.reset();
    }
}
