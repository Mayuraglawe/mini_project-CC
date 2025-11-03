package com.CC.LowLevel_Language.compiler.generator;


public class CompilationOptions {
    private String registerAllocationStrategy;
    private String targetArchitecture;

    public String getRegisterAllocationStrategy() { return registerAllocationStrategy; }
    public void setRegisterAllocationStrategy(String strategy) {
        this.registerAllocationStrategy = strategy;
    }

    public String getTargetArchitecture() { return targetArchitecture; }
    public void setTargetArchitecture(String arch) { this.targetArchitecture = arch; }
}

