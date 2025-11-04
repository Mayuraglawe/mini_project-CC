// ========== VERSION 2 Enhanced JavaScript ==========

const API_BASE = 'http://localhost:8080/api/compile';

// ========== VERSION SWITCHING ==========
function switchVersion(version) {
    const v1Content = document.getElementById('version1');
    const v2Content = document.getElementById('version2');
    const tabs = document.querySelectorAll('.tab-button');
    
    tabs.forEach(tab => tab.classList.remove('active'));
    
    if (version === 'v1') {
        v1Content.classList.add('active');
        v2Content.classList.remove('active');
        tabs[0].classList.add('active');
    } else {
        v1Content.classList.remove('active');
        v2Content.classList.add('active');
        tabs[1].classList.add('active');
    }
}

// ========== VERSION 1 FUNCTIONS ==========
async function validateV1() {
    const tacCode = document.getElementById('tacCodeV1').value.trim();
    
    if (!tacCode) {
        showResultV1(false, 'Please enter TAC code to validate.');
        return;
    }
    
    showResultV1(null, 'Validating TAC syntax...');
    
    try {
        const response = await fetch(`${API_BASE}/validate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tacCode })
        });
        
        const result = await response.json();
        showResultV1(result.valid, result.message);
    } catch (error) {
        showResultV1(false, 'Error: ' + error.message);
    }
}

async function compileV1() {
    const tacCode = document.getElementById('tacCodeV1').value.trim();
    
    if (!tacCode) {
        showResultV1(false, 'Please enter TAC code to compile.');
        return;
    }
    
    showResultV1(null, 'Compiling TAC to Assembly...');
    
    try {
        const response = await fetch(`${API_BASE}/tac`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tacCode })
        });
        
        const result = await response.json();
        
        if (result.success) {
            let html = `
                <div class="success-banner">✅ Compilation Successful!</div>
                <div class="assembly-output">
                    <h4>Generated Assembly Code:</h4>
                    <pre class="code-block">${escapeHtml(result.assemblyCode)}</pre>
                </div>
                <button class="btn btn-secondary" onclick="copyToClipboard('${escapeForAttribute(result.assemblyCode)}')">
                    📋 Copy Assembly Code
                </button>
            `;
            showResultV1(true, html);
        } else {
            showResultV1(false, result.message);
        }
    } catch (error) {
        showResultV1(false, 'Error: ' + error.message);
    }
}

function clearV1() {
    document.getElementById('tacCodeV1').value = '';
    document.getElementById('resultV1').style.display = 'none';
}

function showResultV1(success, message) {
    const resultDiv = document.getElementById('resultV1');
    const contentDiv = resultDiv.querySelector('.result-content');
    
    resultDiv.style.display = 'block';
    
    if (success === null) {
        contentDiv.innerHTML = `<div class="info-banner">${message}</div>`;
    } else if (success) {
        contentDiv.innerHTML = message;
    } else {
        contentDiv.innerHTML = `<div class="error-banner">❌ ${message}</div>`;
    }
    
    resultDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

// ========== VERSION 2 FUNCTIONS ==========

async function runV2Complete() {
    const tacCode = document.getElementById('tacCodeV2').value.trim();
    
    if (!tacCode) {
        alert('Please enter TAC code');
        return;
    }
    
    showLoading(true);
    
    try {
        const response = await fetch(`${API_BASE}/v2/complete`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tacCode })
        });
        
        const result = await response.json();
        
        if (result.success) {
            displayV2Assembly(result);
            displayV2Resources(result.resourceAnalysis);
            displayV2Optimization(result.optimization);
            document.getElementById('v2Results').style.display = 'block';
        } else {
            alert('Error: ' + result.message);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    } finally {
        showLoading(false);
    }
}

async function runV2Assembly() {
    const tacCode = document.getElementById('tacCodeV2').value.trim();
    
    if (!tacCode) {
        alert('Please enter TAC code');
        return;
    }
    
    showLoading(true);
    
    try {
        const response = await fetch(`${API_BASE}/v2/assembly`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tacCode })
        });
        
        const result = await response.json();
        
        if (result.success) {
            displayV2Assembly(result);
            hideV2Section('v2Resources');
            hideV2Section('v2Optimization');
            document.getElementById('v2Results').style.display = 'block';
        } else {
            alert('Error: ' + result.message);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    } finally {
        showLoading(false);
    }
}

async function runV2Resources() {
    const tacCode = document.getElementById('tacCodeV2').value.trim();
    
    if (!tacCode) {
        alert('Please enter TAC code');
        return;
    }
    
    showLoading(true);
    
    try {
        const response = await fetch(`${API_BASE}/v2/resources`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tacCode })
        });
        
        const result = await response.json();
        
        if (result.success) {
            displayV2Assembly(result);
            displayV2Resources(result.resourceAnalysis);
            hideV2Section('v2Optimization');
            document.getElementById('v2Results').style.display = 'block';
        } else {
            alert('Error: ' + result.message);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    } finally {
        showLoading(false);
    }
}

async function runV2Optimize() {
    const tacCode = document.getElementById('tacCodeV2').value.trim();
    
    if (!tacCode) {
        alert('Please enter TAC code');
        return;
    }
    
    showLoading(true);
    
    try {
        const response = await fetch(`${API_BASE}/v2/optimize`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tacCode })
        });
        
        const result = await response.json();
        
        if (result.success) {
            displayV2Optimization(result.optimization);
            hideV2Section('v2Assembly');
            hideV2Section('v2Resources');
            document.getElementById('v2Results').style.display = 'block';
        } else {
            alert('Error: ' + result.message);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    } finally {
        showLoading(false);
    }
}

function clearV2() {
    document.getElementById('tacCodeV2').value = '';
    document.getElementById('v2Results').style.display = 'none';
    hideV2Section('v2Assembly');
    hideV2Section('v2Resources');
    hideV2Section('v2Optimization');
}

// ========== DISPLAY FUNCTIONS ==========

function displayV2Assembly(result) {
    const section = document.getElementById('v2Assembly');
    const content = section.querySelector('.result-content');
    
    let html = `
        <div class="success-banner">✅ Assembly Generated Successfully</div>
        <div class="assembly-output">
            <pre class="code-block">${escapeHtml(result.assemblyCode)}</pre>
        </div>
        <button class="btn btn-secondary" onclick="copyToClipboard('${escapeForAttribute(result.assemblyCode)}')">
            📋 Copy Assembly
        </button>
    `;
    
    content.innerHTML = html;
    section.style.display = 'block';
}

function displayV2Resources(analysis) {
    if (!analysis) return;
    
    const section = document.getElementById('v2Resources');
    const content = section.querySelector('.result-content');
    
    // Build instruction types table
    let instructionTable = '<table class="resource-table"><tr><th>Instruction</th><th>Count</th></tr>';
    for (const [inst, count] of Object.entries(analysis.instructionTypes)) {
        instructionTable += `<tr><td>${inst}</td><td>${count}</td></tr>`;
    }
    instructionTable += '</table>';
    
    let html = `
        <div class="resource-grid">
            <div class="resource-card">
                <div class="resource-icon">📊</div>
                <div class="resource-value">${analysis.totalInstructions}</div>
                <div class="resource-label">Total Instructions</div>
            </div>
            
            <div class="resource-card">
                <div class="resource-icon">🔢</div>
                <div class="resource-value">${analysis.registerCount}</div>
                <div class="resource-label">Registers Used</div>
            </div>
            
            <div class="resource-card">
                <div class="resource-icon">💾</div>
                <div class="resource-value">${analysis.memoryAccesses}</div>
                <div class="resource-label">Memory Accesses</div>
            </div>
            
            <div class="resource-card">
                <div class="resource-icon">➗</div>
                <div class="resource-value">${analysis.arithmeticOperations}</div>
                <div class="resource-label">Arithmetic Ops</div>
            </div>
            
            <div class="resource-card">
                <div class="resource-icon">↪️</div>
                <div class="resource-value">${analysis.jumpInstructions}</div>
                <div class="resource-label">Jump Instructions</div>
            </div>
            
            <div class="resource-card">
                <div class="resource-icon">🏷️</div>
                <div class="resource-value">${analysis.labelCount}</div>
                <div class="resource-label">Labels</div>
            </div>
        </div>
        
        <div class="analysis-details">
            <h4>📈 Complexity Metrics</h4>
            <div class="metric-row">
                <span>Control Flow Complexity:</span>
                <span class="metric-badge ${getBadgeClass(analysis.controlFlowComplexity)}">${analysis.controlFlowComplexity}</span>
            </div>
            <div class="metric-row">
                <span>Memory Pressure:</span>
                <span class="metric-badge ${getBadgeClass(analysis.memoryPressure)}">${analysis.memoryPressure}</span>
            </div>
            <div class="metric-row">
                <span>Register Pressure:</span>
                <span class="metric-badge ${getBadgeClass(analysis.registerPressure)}">${analysis.registerPressure}</span>
            </div>
        </div>
        
        <div class="analysis-details">
            <h4>📋 Registers Used</h4>
            <div class="register-list">
                ${analysis.registersUsed.map(reg => `<span class="register-badge">${reg}</span>`).join(' ')}
            </div>
        </div>
        
        <div class="analysis-details">
            <h4>🔧 Instruction Breakdown</h4>
            ${instructionTable}
        </div>
    `;
    
    content.innerHTML = html;
    section.style.display = 'block';
}

function displayV2Optimization(optimization) {
    if (!optimization) return;
    
    const section = document.getElementById('v2Optimization');
    const content = section.querySelector('.result-content');
    
    const improvement = optimization.improvementPercentage.toFixed(2);
    const instructionsSaved = optimization.originalInstructionCount - optimization.optimizedInstructionCount;
    
    let logsHtml = '<ul class="optimization-logs">';
    optimization.optimizationLogs.forEach(log => {
        logsHtml += `<li>✨ ${escapeHtml(log)}</li>`;
    });
    logsHtml += '</ul>';
    
    let html = `
        <div class="optimization-summary">
            <div class="opt-stat-card">
                <div class="opt-stat-value">${optimization.optimizationsApplied}</div>
                <div class="opt-stat-label">Optimizations Applied</div>
            </div>
            <div class="opt-stat-card">
                <div class="opt-stat-value">${instructionsSaved}</div>
                <div class="opt-stat-label">Instructions Saved</div>
            </div>
            <div class="opt-stat-card highlight">
                <div class="opt-stat-value">${improvement}%</div>
                <div class="opt-stat-label">Improvement</div>
            </div>
        </div>
        
        <div class="optimization-logs-section">
            <h4>🔍 Optimization Log</h4>
            ${optimization.optimizationLogs.length > 0 ? logsHtml : '<p class="info-text">No optimizations were applied.</p>'}
        </div>
        
        <div class="code-comparison">
            <div class="code-column">
                <h4>📄 Original Code (${optimization.originalInstructionCount} instructions)</h4>
                <pre class="code-block">${escapeHtml(optimization.originalCode)}</pre>
            </div>
            <div class="code-column">
                <h4>⚡ Optimized Code (${optimization.optimizedInstructionCount} instructions)</h4>
                <pre class="code-block optimized">${escapeHtml(optimization.optimizedCode)}</pre>
            </div>
        </div>
        
        <div class="code-actions">
            <button class="btn btn-secondary" onclick="copyToClipboard('${escapeForAttribute(optimization.optimizedCode)}')">
                📋 Copy Optimized Code
            </button>
            <button class="btn btn-outline" id="showDiffBtn">🔎 Show Comparison</button>
        </div>
        <div id="diffComparison" class="diff-section" style="display:none; margin-top:1rem;">
            <!-- Diff table will be inserted here -->
        </div>
    `;
    
    content.innerHTML = html;
    section.style.display = 'block';

    // Insert diff table and wire up toggle button
    try {
        const diffContainer = content.querySelector('#diffComparison');
        const diffHtml = generateDiffTable(optimization.originalCode, optimization.optimizedCode);
        if (diffContainer) diffContainer.innerHTML = diffHtml;

        const btn = content.querySelector('#showDiffBtn');
        if (btn) {
            btn.addEventListener('click', () => {
                if (diffContainer.style.display === 'none') {
                    diffContainer.style.display = 'block';
                    btn.textContent = '🔽 Hide Comparison';
                } else {
                    diffContainer.style.display = 'none';
                    btn.textContent = '🔎 Show Comparison';
                }
            });
        }
    } catch (e) {
        console.warn('Failed to render diff comparison:', e);
    }
}

// Generate a simple side-by-side diff table between original and optimized code
function generateDiffTable(original, optimized) {
    const origLines = (original || '').split('\n');
    const optLines = (optimized || '').split('\n');
    const maxLen = Math.max(origLines.length, optLines.length);

    let tableHtml = '<div class="diff-container"><table class="diff-table"><thead><tr><th>Original</th><th>Optimized</th></tr></thead><tbody>';

    for (let i = 0; i < maxLen; i++) {
        const o = (origLines[i] !== undefined) ? origLines[i] : '';
        const p = (optLines[i] !== undefined) ? optLines[i] : '';

        const oTrim = o.trim();
        const pTrim = p.trim();
        const unchanged = (oTrim === pTrim);

        const leftClass = unchanged ? 'diff-unchanged' : 'diff-changed-left';
        const rightClass = unchanged ? 'diff-unchanged' : 'diff-changed-right';

        tableHtml += `<tr>`;
        tableHtml += `<td class="${leftClass}"><pre>${escapeHtml(o)}</pre></td>`;
        tableHtml += `<td class="${rightClass}"><pre>${escapeHtml(p)}</pre></td>`;
        tableHtml += `</tr>`;
    }

    tableHtml += '</tbody></table></div>';
    return tableHtml;
}

// ========== UTILITY FUNCTIONS ==========

function hideV2Section(sectionId) {
    document.getElementById(sectionId).style.display = 'none';
}

function showLoading(show) {
    const overlay = document.getElementById('loadingOverlay');
    if (show) {
        if (!overlay) {
            const div = document.createElement('div');
            div.id = 'loadingOverlay';
            div.innerHTML = '<div class="spinner"></div><p>Processing...</p>';
            div.className = 'loading-overlay';
            document.body.appendChild(div);
        }
    } else {
        if (overlay) {
            overlay.remove();
        }
    }
}

function getBadgeClass(level) {
    const levelLower = level.toLowerCase();
    if (levelLower === 'low') return 'badge-success';
    if (levelLower === 'medium') return 'badge-warning';
    if (levelLower === 'high') return 'badge-danger';
    return 'badge-info';
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function escapeForAttribute(text) {
    if (!text) return '';
    return text.replace(/'/g, "\\'").replace(/"/g, "&quot;").replace(/\n/g, "\\n");
}

function copyToClipboard(text) {
    // Unescape the text first
    text = text.replace(/\\n/g, '\n').replace(/\\'/g, "'");
    
    navigator.clipboard.writeText(text).then(() => {
        alert('✅ Copied to clipboard!');
    }).catch(err => {
        console.error('Copy failed:', err);
        alert('❌ Failed to copy');
    });
}

// Add loading overlay styles
const loadingStyles = document.createElement('style');
loadingStyles.textContent = `
    .loading-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.7);
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        z-index: 9999;
        color: white;
        font-size: 1.2rem;
    }
    
    .spinner {
        border: 4px solid rgba(255, 255, 255, 0.3);
        border-top: 4px solid white;
        border-radius: 50%;
        width: 50px;
        height: 50px;
        animation: spin 1s linear infinite;
        margin-bottom: 1rem;
    }
    
    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }
`;
document.head.appendChild(loadingStyles);

console.log('🚀 Version 2 Enhanced TAC Converter Loaded!');
