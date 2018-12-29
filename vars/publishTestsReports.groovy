#!/usr/bin/env groovy

/**
* Publishes tests reports to jenkins UI
*/
Void call() {
    try {
        Boolean unitExists = fileExists 'build/unit/**.xml'
        if (unitExists) {
            junit allowEmptyResults: true, testResults: 'build/**.xml'
        }
        Boolean lintExists = fileExists 'build/lint/**.xml'
        if (lintExists) {
            checkstyle canComputeNew: false, defaultEncoding: 'UTF-8', healthy: '', pattern: 'build/**s.xml', unHealthy: ''
        }
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
