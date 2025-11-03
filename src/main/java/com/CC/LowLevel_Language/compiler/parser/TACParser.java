package com.CC.LowLevel_Language.compiler.parser;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class TACParser {

    public List<TACInstruction> parseCode(String tacCode) {
        List<TACInstruction> instructions = new ArrayList<>();
        String[] lines = tacCode.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }

            TACInstruction instruction = parseLine(line);
            if (instruction != null) {
                instructions.add(instruction);
            }
        }

        return instructions;
    }

    private TACInstruction parseLine(String line) {
        // Check if it's a label
        if (line.endsWith(":")) {
            String label = line.substring(0, line.length() - 1);
            return new TACInstruction(label);
        }

        // Check if it's a return statement
        if (line.startsWith("return")) {
            return parseReturn(line);
        }

        // Check if it's a conditional jump
        if (line.contains("if") && line.contains("goto")) {
            return parseConditionalJump(line);
        }

        // Check if it's an assignment
        if (line.contains("=")) {
            return parseAssignment(line);
        }

        // Check if it's a function call
        if (line.contains("call")) {
            return parseFunctionCall(line);
        }

        return null;
    }

    private TACInstruction parseReturn(String line) {
        if (line.equals("return")) {
            return new TACInstruction("return", null, null, null);
        } else {
            // Extract the return value
            String returnValue = line.substring(6).trim(); // Remove "return"
            return new TACInstruction("return", null, returnValue, null);
        }
    }

    private TACInstruction parseAssignment(String line) {
        int equalsIndex = line.indexOf('=');
        if (equalsIndex == -1) return null;

        String result = line.substring(0, equalsIndex).trim();
        String expression = line.substring(equalsIndex + 1).trim();

        // Check for addition
        if (expression.contains("+")) {
            int opIndex = expression.indexOf('+');
            String operand1 = expression.substring(0, opIndex).trim();
            String operand2 = expression.substring(opIndex + 1).trim();
            return new TACInstruction("+", result, operand1, operand2);
        }

        // Check for subtraction
        if (expression.contains("-")) {
            int opIndex = expression.indexOf('-');
            String operand1 = expression.substring(0, opIndex).trim();
            String operand2 = expression.substring(opIndex + 1).trim();
            return new TACInstruction("-", result, operand1, operand2);
        }

        // Check for multiplication
        if (expression.contains("*")) {
            int opIndex = expression.indexOf('*');
            String operand1 = expression.substring(0, opIndex).trim();
            String operand2 = expression.substring(opIndex + 1).trim();
            return new TACInstruction("*", result, operand1, operand2);
        }

        // Check for division
        if (expression.contains("/")) {
            int opIndex = expression.indexOf('/');
            String operand1 = expression.substring(0, opIndex).trim();
            String operand2 = expression.substring(opIndex + 1).trim();
            return new TACInstruction("/", result, operand1, operand2);
        }

        // Check for modulo
        if (expression.contains("%")) {
            int opIndex = expression.indexOf('%');
            String operand1 = expression.substring(0, opIndex).trim();
            String operand2 = expression.substring(opIndex + 1).trim();
            return new TACInstruction("%", result, operand1, operand2);
        }

        // Simple assignment
        return new TACInstruction("=", result, expression, null);
    }

    private TACInstruction parseConditionalJump(String line) {
        // Format: if t1 > 10 goto L1
        // Parse manually without using split()

        // Find positions of key words
        int ifIndex = line.indexOf("if");
        int gotoIndex = line.indexOf("goto");

        if (ifIndex == -1 || gotoIndex == -1) return null;

        // Extract the condition part between "if" and "goto"
        String conditionPart = line.substring(ifIndex + 2, gotoIndex).trim();

        // Extract the label part after "goto"
        String label = line.substring(gotoIndex + 4).trim();

        // Parse the condition part to find operator and operands
        String operand1 = "";
        String operator = "";
        String operand2 = "";

        // Check for different operators
        if (conditionPart.contains(">=")) {
            int opIndex = conditionPart.indexOf(">=");
            operand1 = conditionPart.substring(0, opIndex).trim();
            operator = ">=";
            operand2 = conditionPart.substring(opIndex + 2).trim();
        } else if (conditionPart.contains("<=")) {
            int opIndex = conditionPart.indexOf("<=");
            operand1 = conditionPart.substring(0, opIndex).trim();
            operator = "<=";
            operand2 = conditionPart.substring(opIndex + 2).trim();
        } else if (conditionPart.contains("==")) {
            int opIndex = conditionPart.indexOf("==");
            operand1 = conditionPart.substring(0, opIndex).trim();
            operator = "==";
            operand2 = conditionPart.substring(opIndex + 2).trim();
        } else if (conditionPart.contains("!=")) {
            int opIndex = conditionPart.indexOf("!=");
            operand1 = conditionPart.substring(0, opIndex).trim();
            operator = "!=";
            operand2 = conditionPart.substring(opIndex + 2).trim();
        } else if (conditionPart.contains(">")) {
            int opIndex = conditionPart.indexOf(">");
            operand1 = conditionPart.substring(0, opIndex).trim();
            operator = ">";
            operand2 = conditionPart.substring(opIndex + 1).trim();
        } else if (conditionPart.contains("<")) {
            int opIndex = conditionPart.indexOf("<");
            operand1 = conditionPart.substring(0, opIndex).trim();
            operator = "<";
            operand2 = conditionPart.substring(opIndex + 1).trim();
        }

        if (!operand1.isEmpty() && !operator.isEmpty() && !operand2.isEmpty() && !label.isEmpty()) {
            TACInstruction instruction = new TACInstruction("if", label, operand1, operand2);
            instruction.setOperation(operator);
            return instruction;
        }

        return null;
    }

    private TACInstruction parseFunctionCall(String line) {
        // Remove "call" from the beginning
        String withoutCall = line.substring(4).trim(); // Remove "call"

        if (withoutCall.contains(",")) {
            // Has arguments: call func, arg
            int commaIndex = withoutCall.indexOf(',');
            String funcName = withoutCall.substring(0, commaIndex).trim();
            String args = withoutCall.substring(commaIndex + 1).trim();
            return new TACInstruction("call", null, funcName, args);
        } else {
            // No arguments: call func
            return new TACInstruction("call", null, withoutCall, null);
        }
    }
}
