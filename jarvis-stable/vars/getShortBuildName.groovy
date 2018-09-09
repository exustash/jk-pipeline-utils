#!/usr/bin/env groovy

/**
 * Create a jenkins based build tag
 * @param numberOfDirectories
 */
String call(Integer numberOfDirectories = 2) {
    try {
        parts = env.JOB_URL.replaceAll("/", "").split('job')

        if (parts.size() > 1) {
            numberOfDirs = Math.min(numberOfDirectories, parts.size())
            parts = parts.takeRight(numberOfDirs)
        }
        return parts.join('-')
    } catch (err) {
        error("[ERR!] Pipeline execution error: ${err.message}")
    }
}
