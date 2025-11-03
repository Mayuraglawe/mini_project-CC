package com.CC.LowLevel_Language.compiler.analysis;

import java.util.*;

public class SymbolTable {
    private Map<String, SymbolInfo> symbols;
    private int stackOffset;

    public SymbolTable() {
        this.symbols = new HashMap<>();
        this.stackOffset = 0;
    }

    public class SymbolInfo {
        private String name;
        private boolean isAlive;
        private int startLine;
        private int endLine;
        private int stackOffset;
        private Set<String> interferingVariables;

        public SymbolInfo(String name, int startLine) {
            this.name = name;
            this.isAlive = true;
            this.startLine = startLine;
            this.endLine = -1;
            this.stackOffset = -1;
            this.interferingVariables = new HashSet<>();
        }

        // Getters and setters
        public String getName() { return name; }
        public boolean isAlive() { return isAlive; }
        public void setAlive(boolean alive) { isAlive = alive; }
        public int getStartLine() { return startLine; }
        public int getEndLine() { return endLine; }
        public void setEndLine(int endLine) { this.endLine = endLine; }
        public int getStackOffset() { return stackOffset; }
        public void setStackOffset(int offset) { this.stackOffset = offset; }
        public Set<String> getInterferingVariables() { return interferingVariables; }
    }

    public void addSymbol(String name, int startLine) {
        symbols.putIfAbsent(name, new SymbolInfo(name, startLine));
    }

    public void markSymbolDead(String name, int endLine) {
        SymbolInfo info = symbols.get(name);
        if (info != null) {
            info.setAlive(false);
            info.setEndLine(endLine);
        }
    }

    public void addInterference(String var1, String var2) {
        SymbolInfo info1 = symbols.get(var1);
        SymbolInfo info2 = symbols.get(var2);

        if (info1 != null && info2 != null) {
            info1.getInterferingVariables().add(var2);
            info2.getInterferingVariables().add(var1);
        }
    }

    public Set<String> getInterferingVariables(String name) {
        SymbolInfo info = symbols.get(name);
        return info != null ? info.getInterferingVariables() : new HashSet<>();
    }

    public boolean isSymbolAlive(String name) {
        SymbolInfo info = symbols.get(name);
        return info != null && info.isAlive();
    }

    public int allocateStackSlot() {
        stackOffset += 4; // Assuming 4-byte alignment
        return -stackOffset;
    }

    public void assignStackOffset(String name) {
        SymbolInfo info = symbols.get(name);
        if (info != null && info.getStackOffset() == -1) {
            info.setStackOffset(allocateStackSlot());
        }
    }

    public int getSymbolStackOffset(String name) {
        SymbolInfo info = symbols.get(name);
        return info != null ? info.getStackOffset() : 0;
    }

    public Map<String, Integer> getLiveRanges() {
        Map<String, Integer> ranges = new HashMap<>();
        for (SymbolInfo info : symbols.values()) {
            ranges.put(info.getName(), info.getEndLine() - info.getStartLine());
        }
        return ranges;
    }

    public void clear() {
        symbols.clear();
        stackOffset = 0;
    }

    public Collection<SymbolInfo> getAllSymbols() {
        return symbols.values();
    }
}
