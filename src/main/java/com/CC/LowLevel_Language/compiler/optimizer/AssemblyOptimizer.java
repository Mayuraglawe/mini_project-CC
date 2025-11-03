package com.CC.LowLevel_Language.compiler.optimizer;

import java.util.ArrayList;
import java.util.List;

public class AssemblyOptimizer {
    private List<String> optimizationLogs;
    private int optimizationsApplied;

    public AssemblyOptimizer() {
        this.optimizationLogs = new ArrayList<>();
        this.optimizationsApplied = 0;
    }

    public OptimizationResult optimize(List<String> assemblyCode) {
        optimizationLogs.clear();
        optimizationsApplied = 0;

        List<String> originalCode = new ArrayList<>(assemblyCode);
        List<String> optimizedCode = new ArrayList<>(assemblyCode);

        // Apply various optimization techniques
        optimizedCode = applyPeepholeOptimizations(optimizedCode);
        optimizedCode = removeRedundantMoves(optimizedCode);
        optimizedCode = constantFolding(optimizedCode);
        optimizedCode = removeDeadCode(optimizedCode);
        optimizedCode = strengthReduction(optimizedCode);

        return new OptimizationResult(
            originalCode,
            optimizedCode,
            optimizationLogs,
            optimizationsApplied,
            calculateImprovement(originalCode.size(), optimizedCode.size())
        );
    }

    /**
     * Peephole Optimization: Look for patterns in a small window of instructions
     */
    private List<String> applyPeepholeOptimizations(List<String> code) {
        List<String> optimized = new ArrayList<>();
        
        for (int i = 0; i < code.size(); i++) {
            String current = code.get(i).trim();
            
            // Skip empty lines and comments
            if (current.isEmpty() || current.startsWith(";") || 
                current.startsWith(".") || current.endsWith(":")) {
                optimized.add(code.get(i));
                continue;
            }

            // Pattern 1: mov reg, reg (same register) - Remove self-assignment
            if (isSelfMove(current)) {
                logOptimization("Removed redundant self-move: " + current);
                continue; // Skip this instruction
            }

            // Pattern 2: add/sub reg, 0 - Remove addition/subtraction of zero
            if (isAddSubZero(current)) {
                logOptimization("Removed add/sub zero: " + current);
                continue;
            }

            // Pattern 3: imul reg, 1 - Remove multiplication by 1
            if (isMultiplyByOne(current)) {
                logOptimization("Removed multiply by one: " + current);
                continue;
            }

            // Pattern 4: mov followed by mov to same destination
            if (i < code.size() - 1) {
                String next = code.get(i + 1).trim();
                if (isRedundantMove(current, next)) {
                    logOptimization("Removed redundant move sequence: " + current + " -> " + next);
                    optimized.add(code.get(i + 1)); // Keep only the second move
                    i++; // Skip next instruction
                    continue;
                }
            }

            optimized.add(code.get(i));
        }

        return optimized;
    }

    /**
     * Remove redundant MOV instructions
     */
    private List<String> removeRedundantMoves(List<String> code) {
        List<String> optimized = new ArrayList<>();
        String lastMove = null;

        for (String line : code) {
            String trimmed = line.trim();
            
            if (trimmed.startsWith("mov ")) {
                // Check if this is the same as the last move
                if (!trimmed.equals(lastMove)) {
                    optimized.add(line);
                    lastMove = trimmed;
                } else {
                    logOptimization("Removed duplicate move: " + trimmed);
                }
            } else {
                optimized.add(line);
                if (!trimmed.isEmpty() && !trimmed.startsWith(";")) {
                    lastMove = null; // Reset on non-move instruction
                }
            }
        }

        return optimized;
    }

    /**
     * Constant Folding: Replace operations with constants with their result
     */
    private List<String> constantFolding(List<String> code) {
        List<String> optimized = new ArrayList<>();

        for (int i = 0; i < code.size(); i++) {
            String line = code.get(i);
            String trimmed = line.trim();

            // Look for add/sub with two constants
            if (trimmed.startsWith("add ") || trimmed.startsWith("sub ")) {
                String[] parts = trimmed.split("\\s+|,\\s*");
                if (parts.length >= 3) {
                    try {
                        String dest = parts[1].replace(",", "");
                        String src = parts[2];
                        
                        // Check if we can fold constants in previous instruction
                        if (i > 0 && code.get(i - 1).trim().startsWith("mov " + dest)) {
                            String prevLine = code.get(i - 1).trim();
                            String[] prevParts = prevLine.split("\\s+|,\\s*");
                            if (prevParts.length >= 3) {
                                int value1 = Integer.parseInt(prevParts[2]);
                                int value2 = Integer.parseInt(src);
                                int result = trimmed.startsWith("add") ? value1 + value2 : value1 - value2;
                                
                                optimized.remove(optimized.size() - 1); // Remove previous mov
                                optimized.add(line.replace(trimmed, "mov " + dest + ", " + result));
                                logOptimization("Constant folding: " + prevLine + " + " + trimmed + " -> mov " + dest + ", " + result);
                                continue;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Not constants, skip
                    }
                }
            }

            optimized.add(line);
        }

        return optimized;
    }

    /**
     * Remove unreachable code after unconditional jumps or returns
     */
    private List<String> removeDeadCode(List<String> code) {
        List<String> optimized = new ArrayList<>();
        boolean deadCode = false;

        for (String line : code) {
            String trimmed = line.trim();

            // Reset dead code flag at labels
            if (trimmed.endsWith(":")) {
                deadCode = false;
            }

            // Mark code as dead after unconditional jump or return
            if (trimmed.equals("ret") || trimmed.startsWith("jmp ")) {
                optimized.add(line);
                deadCode = true;
                continue;
            }

            // Skip dead code (except labels and directives)
            if (deadCode && !trimmed.endsWith(":") && !trimmed.startsWith(".") && !trimmed.isEmpty()) {
                if (!trimmed.startsWith(";")) {
                    logOptimization("Removed dead code: " + trimmed);
                }
                continue;
            }

            optimized.add(line);
        }

        return optimized;
    }

    /**
     * Strength Reduction: Replace expensive operations with cheaper ones
     */
    private List<String> strengthReduction(List<String> code) {
        List<String> optimized = new ArrayList<>();

        for (String line : code) {
            String trimmed = line.trim();
            String optimizedLine = line;

            // Pattern: imul reg, 2 -> shl reg, 1 (shift is faster than multiply)
            if (trimmed.matches("imul\\s+\\w+,\\s*2\\s*")) {
                String[] parts = trimmed.split("\\s+|,\\s*");
                String reg = parts[1].replace(",", "");
                optimizedLine = line.replace(trimmed, "shl " + reg + ", 1");
                logOptimization("Strength reduction: multiply by 2 -> shift left: " + trimmed);
            }
            // Pattern: imul reg, 4 -> shl reg, 2
            else if (trimmed.matches("imul\\s+\\w+,\\s*4\\s*")) {
                String[] parts = trimmed.split("\\s+|,\\s*");
                String reg = parts[1].replace(",", "");
                optimizedLine = line.replace(trimmed, "shl " + reg + ", 2");
                logOptimization("Strength reduction: multiply by 4 -> shift left: " + trimmed);
            }
            // Pattern: imul reg, 8 -> shl reg, 3
            else if (trimmed.matches("imul\\s+\\w+,\\s*8\\s*")) {
                String[] parts = trimmed.split("\\s+|,\\s*");
                String reg = parts[1].replace(",", "");
                optimizedLine = line.replace(trimmed, "shl " + reg + ", 3");
                logOptimization("Strength reduction: multiply by 8 -> shift left: " + trimmed);
            }
            // Pattern: idiv with power of 2 -> sar (arithmetic shift right)
            else if (trimmed.matches("idiv\\s+2\\s*")) {
                optimizedLine = line.replace("idiv 2", "sar eax, 1");
                logOptimization("Strength reduction: divide by 2 -> shift right: " + trimmed);
            }

            optimized.add(optimizedLine);
        }

        return optimized;
    }

    // Helper methods for pattern matching
    private boolean isSelfMove(String instruction) {
        if (!instruction.startsWith("mov ")) return false;
        String[] parts = instruction.split("\\s+|,\\s*");
        if (parts.length >= 3) {
            String dest = parts[1].replace(",", "");
            String src = parts[2];
            return dest.equals(src);
        }
        return false;
    }

    private boolean isAddSubZero(String instruction) {
        if (!instruction.startsWith("add ") && !instruction.startsWith("sub ")) return false;
        return instruction.endsWith(", 0") || instruction.endsWith(" 0");
    }

    private boolean isMultiplyByOne(String instruction) {
        if (!instruction.startsWith("imul ")) return false;
        return instruction.endsWith(", 1") || instruction.endsWith(" 1");
    }

    private boolean isRedundantMove(String current, String next) {
        if (!current.startsWith("mov ") || !next.startsWith("mov ")) return false;
        String[] currentParts = current.split("\\s+|,\\s*");
        String[] nextParts = next.split("\\s+|,\\s*");
        
        if (currentParts.length >= 3 && nextParts.length >= 3) {
            String currentDest = currentParts[1].replace(",", "");
            String nextDest = nextParts[1].replace(",", "");
            return currentDest.equals(nextDest);
        }
        return false;
    }

    private void logOptimization(String message) {
        optimizationLogs.add(message);
        optimizationsApplied++;
    }

    private double calculateImprovement(int originalSize, int optimizedSize) {
        if (originalSize == 0) return 0.0;
        int actualOriginal = countInstructions(originalSize);
        int actualOptimized = countInstructions(optimizedSize);
        if (actualOriginal == 0) return 0.0;
        return ((actualOriginal - actualOptimized) / (double) actualOriginal) * 100.0;
    }

    private int countInstructions(int totalLines) {
        // Rough estimate: about 60% of lines are actual instructions
        return (int) (totalLines * 0.6);
    }

    public void reset() {
        optimizationLogs.clear();
        optimizationsApplied = 0;
    }
}
