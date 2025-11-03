package com.CC.LowLevel_Language.compiler.parser;

public class TACInstruction {
    private String operation;
    private String result;
    private String operand1;
    private String operand2;
    private String label;
    private boolean isLabel;

    public TACInstruction() {}

    public TACInstruction(String operation, String result, String operand1, String operand2) {
        this.operation = operation;
        this.result = result;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.isLabel = false;
    }

    public TACInstruction(String label) {
        this.label = label;
        this.isLabel = true;
    }

    // Getters and Setters
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getOperand1() { return operand1; }
    public void setOperand1(String operand1) { this.operand1 = operand1; }

    public String getOperand2() { return operand2; }
    public void setOperand2(String operand2) { this.operand2 = operand2; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isLabel() { return isLabel; }
    public void setLabel(boolean isLabel) { this.isLabel = isLabel; }

    @Override
    public String toString() {
        if (isLabel) {
            return label + ":";
        }
        return result + " = " + operand1 + " " + operation + " " + operand2;
    }
}
