#!/usr/bin/env groovy

Void call() {
    reportBuildStatus('release/pipeline/quality.report', 'Task quality.report is running', 'PENDING')
    try {
        Boolean defaultExists = fileExists 'sonar-project.properties.default'
        if (defaultExists) {
            prepareSonarProps()
        }
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

Void prepareSonarProps() {
    String commitHash = git.getCommitHash()
    sh returnStdout: false, script: "cp sonar-project.properties.default sonar-project.properties"
    sh returnStdout: false, script: "echo sonar.projectVersion=${commitHash} >> sonar-project.properties"
}
