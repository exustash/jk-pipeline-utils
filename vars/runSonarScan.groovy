#!/usr/bin/env groovy

Void call() {
    reportBuildStatus('release/pipeline/quality.report', 'Task quality.report is running', 'PENDING')
    try {
        String scannerHome = tool "Inspect02Scanner";
        withSonarQubeEnv {
            sh returnStdout: false, script: "${scannerHome}/bin/sonar-scanner"
        }
        reportBuildStatus('release/pipeline/quality.report', 'Task quality.report has suceeded', 'SUCCESS')
    } catch (err) {
        reportBuildStatus('release/pipeline/quality.report', 'Task quality.report has failed', 'FAILURE')
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
