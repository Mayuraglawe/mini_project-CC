package com.CC.LowLevel_Language.compiler.generator;

import java.util.List;
import java.util.ArrayList;

public class AssemblyInstruction {
    private String mnemonic;
    private List<String> operands;
    private String label;
    private String comment;

    public AssemblyInstruction() {
        this.operands = new ArrayList<>();
    }

    public AssemblyInstruction(String mnemonic, List<String> operands) {
        this.mnemonic = mnemonic;
        this.operands = operands != null ? operands : new ArrayList<>();
    }

    public AssemblyInstruction(String mnemonic, List<String> operands, String label) {
        this(mnemonic, operands);
        this.label = label;
    }

    // Getters and Setters
    public String getMnemonic() { return mnemonic; }
    public void setMnemonic(String mnemonic) { this.mnemonic = mnemonic; }

    public List<String> getOperands() { return operands; }
    public void setOperands(List<String> operands) { this.operands = operands; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public void addOperand(String operand) {
        if (this.operands == null) {
            this.operands = new ArrayList<>();
        }
        this.operands.add(operand);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Add label if present
        if (label != null && !label.isEmpty()) {
            sb.append(label).append(":\n");
        }

        // Add instruction with proper indentation
        sb.append("\t").append(mnemonic);

        // Add operands
        if (!operands.isEmpty()) {
            sb.append(" ").append(String.join(", ", operands));
        }

        // Add comment if present
        if (comment != null && !comment.isEmpty()) {
            sb.append("\t\t; ").append(comment);
        }

        return sb.toString();
    }
}
