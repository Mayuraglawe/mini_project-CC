package com.CC.LowLevel_Language.api.dto;


import com.CC.LowLevel_Language.compiler.result.ConversionResult;

public class CompilationResponse {
    private boolean success;
    private String message;
    private String assemblyCode;
    private int instructionCount;
    private int assemblyLineCount;

    public static CompilationResponse fromConversionResult(ConversionResult result) {
        CompilationResponse response = new CompilationResponse();
        response.setSuccess(result.isSuccess());
        response.setMessage(result.getMessage());
        response.setAssemblyCode(result.getAssemblyCode());
        response.setInstructionCount(result.getTacInstructions() != null ?
                result.getTacInstructions().size() : 0);
        response.setAssemblyLineCount(result.getAssemblyLines() != null ?
                result.getAssemblyLines().size() : 0);
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAssemblyCode() { return assemblyCode; }
    public void setAssemblyCode(String assemblyCode) { this.assemblyCode = assemblyCode; }

    public int getInstructionCount() { return instructionCount; }
    public void setInstructionCount(int instructionCount) { this.instructionCount = instructionCount; }

    public int getAssemblyLineCount() { return assemblyLineCount; }
    public void setAssemblyLineCount(int assemblyLineCount) { this.assemblyLineCount = assemblyLineCount; }
}
