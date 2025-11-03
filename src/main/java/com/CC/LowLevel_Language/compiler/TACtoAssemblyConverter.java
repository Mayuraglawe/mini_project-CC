package com.CC.LowLevel_Language.compiler;


import com.CC.LowLevel_Language.compiler.analysis.RegisterManager;
import com.CC.LowLevel_Language.compiler.analysis.SymbolTable;
import com.CC.LowLevel_Language.compiler.generator.AssemblyGenerator;
import com.CC.LowLevel_Language.compiler.parser.TACInstruction;
import com.CC.LowLevel_Language.compiler.parser.TACParser;
import com.CC.LowLevel_Language.compiler.result.ConversionResult;

import java.util.*;

public class TACtoAssemblyConverter {
    private final TACParser parser;
    private final AssemblyGenerator generator;
    private final RegisterManager registerManager;
    private final SymbolTable symbolTable;

    public TACtoAssemblyConverter() {
        this.parser = new TACParser();
        this.generator = new AssemblyGenerator();
        this.registerManager = new RegisterManager();
        this.symbolTable = new SymbolTable();
    }

    public String convertTACToAssembly(String tacCode) {
        try {
            List<TACInstruction> instructions = parser.parseCode(tacCode);
            List<String> assemblyLines = generator.generateAssembly(instructions);

            StringBuilder result = new StringBuilder();
            for (String line : assemblyLines) {
                result.append(line).append("\n");
            }
            return result.toString();

        } catch (Exception e) {
            return "Error during conversion: " + e.getMessage();
        }
    }

    public List<TACInstruction> parseTAC(String tacCode) {
        return parser.parseCode(tacCode);
    }

    public List<String> generateAssemblyFromInstructions(List<TACInstruction> instructions) {
        return generator.generateAssembly(instructions);
    }

    public ConversionResult convertWithDetails(String tacCode) {
        ConversionResult result = new ConversionResult();

        try {
            // Parse TAC
            List<TACInstruction> instructions = parser.parseCode(tacCode);
            result.setTacInstructions(instructions);

            // Generate Assembly
            List<String> assemblyLines = generator.generateAssembly(instructions);
            result.setAssemblyLines(assemblyLines);

            // Create formatted assembly string
            StringBuilder assemblyCode = new StringBuilder();
            for (String line : assemblyLines) {
                assemblyCode.append(line).append("\n");
            }
            result.setAssemblyCode(assemblyCode.toString());

            result.setSuccess(true);
            result.setMessage("Conversion successful");

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }
}
