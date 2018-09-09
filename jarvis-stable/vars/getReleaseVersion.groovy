#!/usr/bin/env groovy

/**
 * Create a short commit hash version number
 * @return the version number as a string
 */
String call() {
    try {
        println '---> construct and return a release version'
        String commitDate = git.getCommitDate()
        String commitHash = git.getCommitHash()

        return "${commitDate}.${commitHash}"
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
