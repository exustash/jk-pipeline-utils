#!/usr/bin/env groovy

/**
* Publishes tests reports to jenkins UI
*/
Void call() {
    try {
        Boolean unitExists = fileExists 'build/test.unit-results.xml'
        if (unitExists) {
            junit allowEmptyResults: true, testResults: 'build/test.unit-results.xml'
        }
        Boolean lintExists = fileExists 'build/lint.style-results.xml'
        if (lintExists) {
            checkstyle canComputeNew: false, defaultEncoding: 'UTF-8', healthy: '', pattern: 'build/lint.style-results.xml', unHealthy: ''
        }
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
