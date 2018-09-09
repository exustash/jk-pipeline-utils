#!/usr/bin/env groovy

/**
 * Cleanup all docker containers on current host
 */
Void call() {
    try {
        println("---> Cleanup Docker Containers")
        sh returnStdout: false, script: 'docker ps --quiet --filter=status=exited | xargs --no-run-if-empty docker  rm --force --volumes  || true'
    } catch (err) {
        error("[ERR!] Job execution error: ${err.message}")
    }
}
