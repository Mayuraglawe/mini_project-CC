// Configuration for your Spring Boot backend
const CONFIG = {
    API_BASE: 'http://localhost:8080/api/compile',
    ENDPOINTS: {
        VALIDATE: '/validate',
        COMPILE: '/tac',
        HEALTH: '/health'
    },
    TIMEOUTS: {
        DEFAULT: 10000, // 10 seconds
        HEALTH: 5000    // 5 seconds
    }
};

// Application State Management
const AppState = {
    isLoading: false,
    currentRequest: null,
    lastHealthCheck: null,
    compilationHistory: []
};

// DOM Elements Cache
const Elements = {
    tacCode: null,
    validateBtn: null,
    compileBtn: null,
    clearBtn: null,
    healthBtn: null,
    validationResult: null,
    compileResult: null,
    healthResult: null
};

// Initialize Application when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('TAC to Assembly Converter - Frontend Loading...');
    initializeElements();
    attachEventListeners();
    setupKeyboardShortcuts();
    checkApiHealthOnLoad();
});

// Initialize DOM element references
function initializeElements() {
    Elements.tacCode = document.getElementById('tacCode');
    Elements.validateBtn = document.getElementById('validateBtn');
    Elements.compileBtn = document.getElementById('compileBtn');
    Elements.clearBtn = document.getElementById('clearBtn');
    Elements.healthBtn = document.getElementById('healthBtn');
    Elements.validationResult = document.getElementById('validationResult');
    Elements.compileResult = document.getElementById('compileResult');
    Elements.healthResult = document.getElementById('healthResult');

    // Validate that all required elements exist
    const missingElements = Object.entries(Elements)
        .filter(([key, element]) => !element)
        .map(([key]) => key);

    if (missingElements.length > 0) {
        console.error('Missing DOM elements:', missingElements);
        showNotification('Some UI elements are missing. Please refresh the page.', 'error');
    }
}

// Attach event listeners to buttons
function attachEventListeners() {
    if (Elements.validateBtn) {
        Elements.validateBtn.onclick = handleValidation;
    }

    if (Elements.compileBtn) {
        Elements.compileBtn.onclick = handleCompilation;
    }

    if (Elements.clearBtn) {
        Elements.clearBtn.onclick = handleClear;
    }

    if (Elements.healthBtn) {
        Elements.healthBtn.onclick = handleHealthCheck;
    }

    // Add input validation
    if (Elements.tacCode) {
        Elements.tacCode.addEventListener('input', handleTacCodeInput);
        Elements.tacCode.addEventListener('paste', handleTacCodePaste);
    }
}

// Setup keyboard shortcuts
function setupKeyboardShortcuts() {
    document.addEventListener('keydown', function(e) {
        // Ctrl+Enter: Compile
        if (e.ctrlKey && e.key === 'Enter' && !AppState.isLoading) {
            e.preventDefault();
            handleCompilation();
        }

        // Ctrl+Shift+V: Validate
        if (e.ctrlKey && e.shiftKey && e.key === 'V' && !AppState.isLoading) {
            e.preventDefault();
            handleValidation();
        }

        // Escape: Clear results
        if (e.key === 'Escape') {
            hideAllResults();
        }
    });
}

// API Request Handler with timeout and error handling
async function makeApiRequest(endpoint, method = 'GET', data = null, timeout = CONFIG.TIMEOUTS.DEFAULT) {
    const url = CONFIG.API_BASE + endpoint;
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), timeout);

    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        signal: controller.signal
    };

    if (data) {
        options.body = JSON.stringify(data);
    }

    try {
        console.log(`Making ${method} request to: ${url}`);
        const startTime = Date.now();

        const response = await fetch(url, options);
        clearTimeout(timeoutId);

        const responseTime = Date.now() - startTime;
        console.log(`Request completed in ${responseTime}ms`);

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }

        const result = await response.json();
        console.log('API Response:', result);
        return result;

    } catch (error) {
        clearTimeout(timeoutId);

        if (error.name === 'AbortError') {
            throw new Error(`Request timed out after ${timeout}ms`);
        }

        console.error('API Request failed:', error);
        throw error;
    }
}

// Handle TAC Validation
async function handleValidation() {
    const tacCode = getTacCodeInput();

    if (!tacCode) {
        showValidationResult(false, 'TAC code is required for validation.');
        return;
    }

    setLoadingState(Elements.validateBtn, true);
    showValidationResult(null, 'Validating TAC syntax...');

    try {
        const result = await makeApiRequest(CONFIG.ENDPOINTS.VALIDATE, 'POST', { tacCode });
        showValidationResult(result.valid, result.message);

        // Log validation result
        console.log('Validation result:', result);

    } catch (error) {
        const errorMessage = getErrorMessage(error);
        showValidationResult(false, `Validation failed: ${errorMessage}`);

    } finally {
        setLoadingState(Elements.validateBtn, false);
    }
}

// Handle TAC Compilation
async function handleCompilation() {
    const tacCode = getTacCodeInput();

    if (!tacCode) {
        showCompilationResult(false, 'TAC code is required for compilation.');
        return;
    }

    setLoadingState(Elements.compileBtn, true);
    showCompilationResult(null, 'Compiling TAC to assembly...');

    try {
        const result = await makeApiRequest(CONFIG.ENDPOINTS.COMPILE, 'POST', { tacCode });
        showCompilationResult(result.success, result.message, result);

        // Add to compilation history
        if (result.success) {
            AppState.compilationHistory.push({
                timestamp: new Date(),
                tacCode: tacCode,
                result: result
            });
        }

        console.log('Compilation result:', result);

    } catch (error) {
        const errorMessage = getErrorMessage(error);
        showCompilationResult(false, `Compilation failed: ${errorMessage}`);

    } finally {
        setLoadingState(Elements.compileBtn, false);
    }
}

// Handle Clear Action
function handleClear() {
    if (Elements.tacCode) {
        Elements.tacCode.value = '';
        Elements.tacCode.focus();
    }

    hideAllResults();
    showNotification('Interface cleared', 'info');
}

// Handle Health Check
async function handleHealthCheck() {
    setLoadingState(Elements.healthBtn, true);
    showHealthResult('Checking API health status...');

    try {
        const startTime = Date.now();
        const result = await makeApiRequest(CONFIG.ENDPOINTS.HEALTH, 'GET', null, CONFIG.TIMEOUTS.HEALTH);
        const responseTime = Date.now() - startTime;

        AppState.lastHealthCheck = {
            timestamp: Date.now(),
            result: result,
            responseTime: responseTime
        };

        const statusClass = result.status === 'OK' ? 'success' : 'error';
        const statusTime = new Date(result.timestamp).toLocaleString();

        showHealthResult(`
            <div class="health-status">
                <div class="health-item">
                    <strong>Status:</strong> <span class="${statusClass}">${result.status}</span>
                </div>
                <div class="health-item">
                    <strong>Message:</strong> ${escapeHtml(result.message)}
                </div>
                <div class="health-item">
                    <strong>Server Time:</strong> ${statusTime}
                </div>
                <div class="health-item">
                    <strong>Response Time:</strong> ${responseTime}ms
                </div>
                <div class="health-item">
                    <strong>API Base URL:</strong> ${CONFIG.API_BASE}
                </div>
            </div>
        `);

        console.log('Health check result:', result);

    } catch (error) {
        const errorMessage = getErrorMessage(error);
        showHealthResult(`
            <div class="health-status">
                <p class="error">Health check failed: ${escapeHtml(errorMessage)}</p>
                <p class="info">Please ensure the backend server is running and accessible.</p>
            </div>
        `);

    } finally {
        setLoadingState(Elements.healthBtn, false);
    }
}

// Input Handlers
function handleTacCodeInput(event) {
    // You can add real-time validation or formatting here
    const value = event.target.value;

    // Simple line count display (optional)
    const lineCount = value.split('\n').length;
    if (lineCount > 1) {
        event.target.title = `${lineCount} lines of TAC code`;
    }
}

function handleTacCodePaste(event) {
    // Handle pasted content
    setTimeout(() => {
        showNotification('TAC code pasted. You can now validate or compile.', 'info');
    }, 100);
}

// Display Functions
function showValidationResult(isValid, message) {
    if (!Elements.validationResult) return;

    Elements.validationResult.style.display = 'block';
    const content = Elements.validationResult.querySelector('.result-content');

    if (isValid === null) {
        content.innerHTML = `<p class="info">${escapeHtml(message)}</p>`;
    } else {
        const statusClass = isValid ? 'success' : 'error';
        const statusText = isValid ? 'Valid' : 'Invalid';
        const icon = isValid ? '✅' : '❌';

        content.innerHTML = `
            <div class="validation-status">
                <p class="${statusClass}">
                    <strong>${icon} Validation Status:</strong> ${statusText}
                </p>
                <div class="message-content">
                    <pre>${escapeHtml(message)}</pre>
                </div>
            </div>
        `;
    }

    scrollToElement(Elements.validationResult);
}

function showCompilationResult(success, message, data = null) {
    if (!Elements.compileResult) return;

    Elements.compileResult.style.display = 'block';
    const content = Elements.compileResult.querySelector('.result-content');

    if (success === null) {
        content.innerHTML = `<p class="info">${escapeHtml(message)}</p>`;
        return;
    }

    if (success && data) {
        content.innerHTML = `
            <div class="compilation-success">
                <p class="success">
                    <strong>✅ Compilation Status:</strong> Successful
                </p>

                <div class="stats">
                    <div class="stat-item">
                        <span class="stat-value">${data.instructionCount || 0}</span>
                        <span class="stat-label">TAC Instructions</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-value">${data.assemblyLineCount || 0}</span>
                        <span class="stat-label">Assembly Lines</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-value">${calculateCompressionRatio(data)}%</span>
                        <span class="stat-label">Compression</span>
                    </div>
                </div>

                <div class="assembly-output">
                    <h4>Generated Assembly Code:</h4>
                    <div class="code-container">
                        <pre id="assemblyCodePre">${escapeHtml(data.assemblyCode || 'No assembly code generated.')}</pre>
                        <div class="code-actions">
                            <button class="copy-btn" onclick="copyAssemblyCode()">
                                📋 Copy Assembly Code
                            </button>
                            <button class="download-btn" onclick="downloadAssemblyCode()">
                                💾 Download .asm File
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    } else {
        const statusClass = success ? 'success' : 'error';
        const statusText = success ? 'Successful' : 'Failed';
        const icon = success ? '✅' : '❌';

        content.innerHTML = `
            <div class="compilation-error">
                <p class="${statusClass}">
                    <strong>${icon} Compilation Status:</strong> ${statusText}
                </p>
                <div class="error-content">
                    <pre>${escapeHtml(message)}</pre>
                </div>
            </div>
        `;
    }

    scrollToElement(Elements.compileResult);
}

function showHealthResult(html) {
    if (!Elements.healthResult) return;

    Elements.healthResult.style.display = 'block';
    const content = Elements.healthResult.querySelector('.result-content');
    content.innerHTML = html;
    scrollToElement(Elements.healthResult);
}

// Utility Functions
function getTacCodeInput() {
    return Elements.tacCode ? Elements.tacCode.value.trim() : '';
}

function hideAllResults() {
    [Elements.validationResult, Elements.compileResult, Elements.healthResult].forEach(el => {
        if (el) el.style.display = 'none';
    });
}

function setLoadingState(button, isLoading) {
    if (!button) return;

    if (isLoading) {
        button.classList.add('loading');
        button.disabled = true;
    } else {
        button.classList.remove('loading');
        button.disabled = false;
    }

    AppState.isLoading = isLoading;
}

function scrollToElement(element) {
    if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
}

function escapeHtml(unsafe) {
    if (typeof unsafe !== 'string') return '';

    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function getErrorMessage(error) {
    if (error.message) {
        return error.message;
    }

    if (typeof error === 'string') {
        return error;
    }

    return 'An unexpected error occurred';
}

function calculateCompressionRatio(data) {
    if (!data.instructionCount || !data.assemblyLineCount) return 0;

    const ratio = ((data.assemblyLineCount - data.instructionCount) / data.instructionCount) * 100;
    return Math.round(Math.abs(ratio));
}

// Feature Functions
function copyAssemblyCode() {
    const assemblyCodeElement = document.getElementById('assemblyCodePre');
    if (!assemblyCodeElement) {
        showNotification('No assembly code to copy', 'error');
        return;
    }

    const assemblyCode = assemblyCodeElement.textContent;

    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(assemblyCode).then(() => {
            showNotification('Assembly code copied to clipboard!', 'success');
        }).catch(err => {
            console.error('Failed to copy text:', err);
            fallbackCopyTextToClipboard(assemblyCode);
        });
    } else {
        fallbackCopyTextToClipboard(assemblyCode);
    }
}

function downloadAssemblyCode() {
    const assemblyCodeElement = document.getElementById('assemblyCodePre');
    if (!assemblyCodeElement) {
        showNotification('No assembly code to download', 'error');
        return;
    }

    const assemblyCode = assemblyCodeElement.textContent;
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const filename = `assembly_${timestamp}.asm`;

    try {
        const blob = new Blob([assemblyCode], { type: 'text/plain' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);

        showNotification(`Assembly code downloaded as ${filename}`, 'success');
    } catch (error) {
        console.error('Download failed:', error);
        showNotification('Failed to download assembly code', 'error');
    }
}

function fallbackCopyTextToClipboard(text) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    textArea.style.top = "0";
    textArea.style.left = "0";
    textArea.style.position = "fixed";
    textArea.style.opacity = "0";

    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();

    try {
        const successful = document.execCommand('copy');
        if (successful) {
            showNotification('Assembly code copied to clipboard!', 'success');
        } else {
            throw new Error('Copy command failed');
        }
    } catch (err) {
        console.error('Fallback copy failed:', err);
        showNotification('Failed to copy assembly code', 'error');
    }

    document.body.removeChild(textArea);
}

function showNotification(message, type = 'info', duration = 3000) {
    // Remove existing notifications
    const existingNotifications = document.querySelectorAll('.notification');
    existingNotifications.forEach(n => n.remove());

    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <span class="notification-message">${escapeHtml(message)}</span>
        <button class="notification-close" onclick="this.parentElement.remove()">×</button>
    `;

    const colors = {
        success: '#27ae60',
        error: '#e74c3c',
        info: '#3498db',
        warning: '#f39c12'
    };

    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${colors[type] || colors.info};
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 6px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 1000;
        display: flex;
        align-items: center;
        gap: 1rem;
        max-width: 400px;
        animation: slideInRight 0.3s ease;
    `;

    document.body.appendChild(notification);

    // Auto remove notification
    setTimeout(() => {
        if (notification.parentElement) {
            notification.style.animation = 'slideOutRight 0.3s ease';
            setTimeout(() => {
                if (notification.parentElement) {
                    notification.remove();
                }
            }, 300);
        }
    }, duration);
}

// Auto-check API health on page load
async function checkApiHealthOnLoad() {
    try {
        console.log('Checking API health on load...');
        const result = await makeApiRequest(CONFIG.ENDPOINTS.HEALTH, 'GET', null, CONFIG.TIMEOUTS.HEALTH);
        console.log('API health check on load: OK', result);

        AppState.lastHealthCheck = {
            timestamp: Date.now(),
            result: result,
            responseTime: 0
        };

    } catch (error) {
        console.warn('API health check failed on load:', error);
        showNotification('Warning: Backend API may not be available', 'warning', 5000);
    }
}

// Add CSS animations for notifications if not already present
function addNotificationStyles() {
    if (!document.getElementById('notification-styles')) {
        const style = document.createElement('style');
        style.id = 'notification-styles';
        style.textContent = `
            @keyframes slideInRight {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            @keyframes slideOutRight {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
            .notification-close {
                background: none;
                border: none;
                color: white;
                font-size: 1.2rem;
                cursor: pointer;
                padding: 0;
                line-height: 1;
            }
            .notification-close:hover {
                opacity: 0.7;
            }
        `;
        document.head.appendChild(style);
    }
}

// Initialize notification styles
addNotificationStyles();

// Export for debugging and external access
window.TACConverter = {
    CONFIG,
    AppState,
    Elements,
    makeApiRequest,
    copyAssemblyCode,
    downloadAssemblyCode,
    showNotification,
    handleValidation,
    handleCompilation,
    handleHealthCheck
};

// Console welcome message
console.log(`
🚀 TAC to Assembly Converter Frontend Loaded
📡 API Base URL: ${CONFIG.API_BASE}
⌨️  Keyboard Shortcuts:
   • Ctrl+Enter: Compile TAC
   • Ctrl+Shift+V: Validate TAC
   • Escape: Clear results
`);
