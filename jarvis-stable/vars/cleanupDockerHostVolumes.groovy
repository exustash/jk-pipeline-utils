#!/usr/bin/env groovy

/**
 * Cleanup all docker volums on current host
 */
Void call() {
    try {
        println("---> Cleanup Docker Volumes #### ")
        sh returnStdout: false, script: 'docker volume ls -q | xargs --no-run-if-empty docker volume rm || true'
    } catch (err) {
        error("[ERR!] Job execution error: ${err.message}")
    }
}
