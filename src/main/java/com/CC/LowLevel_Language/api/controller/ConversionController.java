package com.CC.LowLevel_Language.api.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CC.LowLevel_Language.api.dto.CompilationRequest;
import com.CC.LowLevel_Language.api.dto.CompilationResponse;
import com.CC.LowLevel_Language.api.dto.HealthResponse;
import com.CC.LowLevel_Language.api.dto.ValidationResponse;
import com.CC.LowLevel_Language.api.dto.Version2Response;
import com.CC.LowLevel_Language.compiler.TACtoAssemblyConverter;
import com.CC.LowLevel_Language.compiler.analysis.ResourceAnalysisResult;
import com.CC.LowLevel_Language.compiler.analysis.ResourceAnalyzer;
import com.CC.LowLevel_Language.compiler.optimizer.AssemblyOptimizer;
import com.CC.LowLevel_Language.compiler.optimizer.OptimizationResult;
import com.CC.LowLevel_Language.compiler.result.ConversionResult;

@RestController
@RequestMapping("/api/compile")
@CrossOrigin(origins = "*")
public class ConversionController {
    private final TACtoAssemblyConverter converter;
    private final ResourceAnalyzer resourceAnalyzer;
    private final AssemblyOptimizer optimizer;

    public ConversionController() {
        this.converter = new TACtoAssemblyConverter();
        this.resourceAnalyzer = new ResourceAnalyzer();
        this.optimizer = new AssemblyOptimizer();
    }

    // ========== VERSION 1 ENDPOINTS ==========
    
    @PostMapping("/tac")
    public CompilationResponse convertTACToAssembly(@RequestBody CompilationRequest request) {
        try {
            ConversionResult result = converter.convertWithDetails(request.getTacCode());
            return CompilationResponse.fromConversionResult(result);

        } catch (Exception e) {
            CompilationResponse errorResponse = new CompilationResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Server error: " + e.getMessage());
            return errorResponse;
        }
    }

    @GetMapping("/health")
    public HealthResponse healthCheck() {
        return new HealthResponse("OK", "TAC to Assembly Converter API is running", System.currentTimeMillis());
    }

    @PostMapping("/validate")
    public ValidationResponse validateTAC(@RequestBody CompilationRequest request) {
        try {
            converter.parseTAC(request.getTacCode());
            return new ValidationResponse(true, "TAC syntax is valid");
        } catch (Exception e) {
            return new ValidationResponse(false, "TAC syntax error: " + e.getMessage());
        }
    }

    // ========== VERSION 2 ENDPOINTS ==========

    /**
     * Version 2 - Complete Pipeline: Assembly Conversion + Resource Analysis + Optimization
     */
    @PostMapping("/v2/complete")
    public Version2Response completeV2Pipeline(@RequestBody CompilationRequest request) {
        Version2Response response = new Version2Response();
        
        try {
            // Step 1: Convert TAC to Assembly
            ConversionResult conversionResult = converter.convertWithDetails(request.getTacCode());
            
            if (!conversionResult.isSuccess()) {
                response.setSuccess(false);
                response.setMessage(conversionResult.getMessage());
                return response;
            }
            
            response.setAssemblyCode(conversionResult.getAssemblyCode());
            response.setAssemblyLines(conversionResult.getAssemblyLines());
            
            // Step 2: Analyze Resources
            ResourceAnalysisResult resourceResult = resourceAnalyzer.analyze(conversionResult.getAssemblyLines());
            response.setResourceAnalysis(resourceResult);
            
            // Step 3: Optimize Assembly
            OptimizationResult optimizationResult = optimizer.optimize(conversionResult.getAssemblyLines());
            
            Version2Response.OptimizationData optData = new Version2Response.OptimizationData();
            optData.setOriginalCode(String.join("\n", optimizationResult.getOriginalCode()));
            optData.setOptimizedCode(String.join("\n", optimizationResult.getOptimizedCode()));
            optData.setOptimizationLogs(optimizationResult.getOptimizationLogs());
            optData.setOptimizationsApplied(optimizationResult.getOptimizationsApplied());
            optData.setImprovementPercentage(optimizationResult.getImprovementPercentage());
            optData.setOriginalInstructionCount(optimizationResult.getOriginalInstructionCount());
            optData.setOptimizedInstructionCount(optimizationResult.getOptimizedInstructionCount());
            
            response.setOptimization(optData);
            response.setSuccess(true);
            response.setMessage("V2 Pipeline completed successfully");
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("V2 Pipeline error: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Version 2 - Step 1: Assembly Conversion Only
     */
    @PostMapping("/v2/assembly")
    public Version2Response assemblyConversionOnly(@RequestBody CompilationRequest request) {
        Version2Response response = new Version2Response();
        
        try {
            ConversionResult result = converter.convertWithDetails(request.getTacCode());
            response.setSuccess(result.isSuccess());
            response.setMessage(result.getMessage());
            response.setAssemblyCode(result.getAssemblyCode());
            response.setAssemblyLines(result.getAssemblyLines());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Assembly conversion error: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Version 2 - Step 2: Resource Analysis Only
     */
    @PostMapping("/v2/resources")
    public Version2Response resourceAnalysisOnly(@RequestBody CompilationRequest request) {
        Version2Response response = new Version2Response();
        
        try {
            // First convert to assembly
            ConversionResult result = converter.convertWithDetails(request.getTacCode());
            
            if (!result.isSuccess()) {
                response.setSuccess(false);
                response.setMessage(result.getMessage());
                return response;
            }
            
            // Then analyze resources
            ResourceAnalysisResult resourceResult = resourceAnalyzer.analyze(result.getAssemblyLines());
            response.setResourceAnalysis(resourceResult);
            response.setAssemblyCode(result.getAssemblyCode());
            response.setSuccess(true);
            response.setMessage("Resource analysis completed");
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Resource analysis error: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Version 2 - Step 3: Optimization Only
     */
    @PostMapping("/v2/optimize")
    public Version2Response optimizationOnly(@RequestBody CompilationRequest request) {
        Version2Response response = new Version2Response();
        
        try {
            // First convert to assembly
            ConversionResult result = converter.convertWithDetails(request.getTacCode());
            
            if (!result.isSuccess()) {
                response.setSuccess(false);
                response.setMessage(result.getMessage());
                return response;
            }
            
            // Then optimize
            OptimizationResult optimizationResult = optimizer.optimize(result.getAssemblyLines());
            
            Version2Response.OptimizationData optData = new Version2Response.OptimizationData();
            optData.setOriginalCode(String.join("\n", optimizationResult.getOriginalCode()));
            optData.setOptimizedCode(String.join("\n", optimizationResult.getOptimizedCode()));
            optData.setOptimizationLogs(optimizationResult.getOptimizationLogs());
            optData.setOptimizationsApplied(optimizationResult.getOptimizationsApplied());
            optData.setImprovementPercentage(optimizationResult.getImprovementPercentage());
            optData.setOriginalInstructionCount(optimizationResult.getOriginalInstructionCount());
            optData.setOptimizedInstructionCount(optimizationResult.getOptimizedInstructionCount());
            
            response.setOptimization(optData);
            response.setSuccess(true);
            response.setMessage("Optimization completed");
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Optimization error: " + e.getMessage());
        }
        
        return response;
    }
}
