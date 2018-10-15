#!/usr/bin/env groovy

Void call() {
    reportPipelineStatus('release/pipeline/quality.report', 'Task quality.report is running', 'PENDING')
    try {
        String scannerHome = tool "ScanerHome"; //change the name of scanerhome to the your liking
        withSonarQubeEnv {
            sh returnStdout: false, script: "${scannerHome}/bin/sonar-scanner"
        }
        reportPipelineStatus('release/pipeline/quality.report', 'Task quality.report has suceeded', 'SUCCESS')
    } catch (err) {
        reportPipelineStatus('release/pipeline/quality.report', 'Task quality.report has failed', 'FAILURE')
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
